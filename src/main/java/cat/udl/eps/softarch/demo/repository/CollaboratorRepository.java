package cat.udl.eps.softarch.demo.repository;

import cat.udl.eps.softarch.demo.domain.Collaborator;
import cat.udl.eps.softarch.demo.domain.CollaboratorAction;
import cat.udl.eps.softarch.demo.domain.Project;
import cat.udl.eps.softarch.demo.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource
public interface CollaboratorRepository
        extends CrudRepository<Collaborator, Long>, PagingAndSortingRepository<Collaborator, Long> {

    /** All collaborators on a given project. */
    List<Collaborator> findByProject(Project project);

    /** All projects a user collaborates on. */
    List<Collaborator> findByUser(User user);

    /** Look up a specific user-project collaboration. */
    Optional<Collaborator> findByUserAndProject(User user, Project project);

    /** Collaborators with a specific permission level on a project. */
    List<Collaborator> findByProjectAndAction(Project project, CollaboratorAction action);
}
