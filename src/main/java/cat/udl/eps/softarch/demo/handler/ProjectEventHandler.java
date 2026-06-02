package cat.udl.eps.softarch.demo.handler;

import cat.udl.eps.softarch.demo.domain.Project;
import cat.udl.eps.softarch.demo.domain.User;
import cat.udl.eps.softarch.demo.domain.Visibility;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Component
@RepositoryEventHandler
public class ProjectEventHandler {

    public ProjectEventHandler() {
    }

    @HandleBeforeCreate
    public void handleProjectPreCreate(Project project) {
        User authenticated = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        project.setCreator(authenticated);

        ZonedDateTime now = ZonedDateTime.now();
        if (project.getCreated() == null) {
            project.setCreated(now);
        }
        project.setLastModified(now);

        if (project.getVisibility() == null) {
            project.setVisibility(Visibility.PUBLIC);
        }
    }

    @HandleBeforeSave
    public void handleProjectPreSave(Project project) {
        project.setLastModified(ZonedDateTime.now());
    }
}
