package cat.udl.eps.softarch.demo.handler;

import cat.udl.eps.softarch.demo.domain.Creator;
import cat.udl.eps.softarch.demo.repository.ProjectRepository;
import org.springframework.data.rest.core.annotation.HandleBeforeDelete;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

@Component
@RepositoryEventHandler
public class CreatorEventHandler {

    private final ProjectRepository projectRepository;

    public CreatorEventHandler(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @HandleBeforeDelete
    public void handleCreatorPreDelete(Creator creator) {
        projectRepository.deleteAll(projectRepository.findByCreatorId(creator.getId()));
    }
}
