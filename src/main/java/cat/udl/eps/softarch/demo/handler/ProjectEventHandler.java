package cat.udl.eps.softarch.demo.handler;

import cat.udl.eps.softarch.demo.domain.Project;
import cat.udl.eps.softarch.demo.domain.User;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RepositoryEventHandler
public class ProjectEventHandler {

    public ProjectEventHandler() {
    }

    @HandleBeforeCreate
    public void handlePortfolioPreCreate(Project project) {
        User authenticated = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        project.setCreator(authenticated);
    }
}
