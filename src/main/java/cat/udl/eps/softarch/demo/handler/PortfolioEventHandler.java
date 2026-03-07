package cat.udl.eps.softarch.demo.handler;

import cat.udl.eps.softarch.demo.domain.Portfolio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeDelete;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RepositoryEventHandler
public class PortfolioEventHandler {

    final Logger logger = LoggerFactory.getLogger(PortfolioEventHandler.class);

    @HandleBeforeCreate
    public void handlePortfolioPreCreate(Portfolio portfolio) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = auth.getName();

        // TRUCO: Si el usuari es diu "admin", el considerarem Administrador
        boolean isAdmin = "admin".equals(currentUsername);

        if (!isAdmin) {
            // Si no es admin i està intentant asignar un creador diferent a ell mateix
            if (portfolio.getCreator() != null && !portfolio.getCreator().getUsername().equals(currentUsername)) {
                throw new AccessDeniedException("As a regular user, you cannot create a portfolio for another user.");
            }
        }
    }

    @HandleBeforeDelete
    public void handlePortfolioPreDelete(Portfolio portfolio) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = auth.getName();

        // TRUCO: Comprobarem el nom d'usuari de nou
        boolean isAdmin = "admin".equals(currentUsername);

        if (!isAdmin) {
            // Si no es admin i el portfolio no es seu, no pot borrarlo
            if (portfolio.getCreator() == null || !portfolio.getCreator().getUsername().equals(currentUsername)) {
                throw new AccessDeniedException("As a regular user, you cannot delete a portfolio belonging to another user.");
            }
        }
    }
}