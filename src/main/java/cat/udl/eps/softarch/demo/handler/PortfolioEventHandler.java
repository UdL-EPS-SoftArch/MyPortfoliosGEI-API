package cat.udl.eps.softarch.demo.handler;

import cat.udl.eps.softarch.demo.domain.Portfolio;
import cat.udl.eps.softarch.demo.domain.User;
import cat.udl.eps.softarch.demo.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeDelete;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RepositoryEventHandler
public class PortfolioEventHandler {

    final Logger logger = LoggerFactory.getLogger(PortfolioEventHandler.class);
    private final UserRepository userRepository;

    public PortfolioEventHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

    private boolean isAdmin(String username) {
        return "admin".equals(username);
    }

    private User getCurrentUser(String username) {
        return userRepository.findById(username)
            .orElseThrow(() -> new AccessDeniedException("Authenticated user not found"));
    }

    @HandleBeforeCreate
    public void handlePortfolioPreCreate(Portfolio portfolio) {
        String currentUsername = getCurrentUsername();
        boolean isAdmin = isAdmin(currentUsername);

        if (portfolio.getCreator() == null) {
            portfolio.setCreator(getCurrentUser(currentUsername));
            return;
        }

        if (!isAdmin && !portfolio.getCreator().getUsername().equals(currentUsername)) {
            throw new AccessDeniedException("As a regular user, you cannot create a portfolio for another user.");
        }
    }

    @HandleBeforeSave
    public void handlePortfolioPreSave(Portfolio portfolio) {
        String currentUsername = getCurrentUsername();
        boolean isAdmin = isAdmin(currentUsername);

        if (portfolio.getCreator() == null) {
            portfolio.setCreator(getCurrentUser(currentUsername));
            return;
        }

        if (!isAdmin && !portfolio.getCreator().getUsername().equals(currentUsername)) {
            throw new AccessDeniedException("As a regular user, you cannot modify another user's portfolio.");
        }
    }

    @HandleBeforeDelete
    public void handlePortfolioPreDelete(Portfolio portfolio) {
        String currentUsername = getCurrentUsername();
        boolean isAdmin = isAdmin(currentUsername);

        if (!isAdmin) {
            if (portfolio.getCreator() == null || !portfolio.getCreator().getUsername().equals(currentUsername)) {
                throw new AccessDeniedException("As a regular user, you cannot delete a portfolio belonging to another user.");
            }
        }
    }
}