package cat.udl.eps.softarch.demo.repository;

import cat.udl.eps.softarch.demo.domain.Portfolio;
import cat.udl.eps.softarch.demo.domain.Project;
import cat.udl.eps.softarch.demo.domain.User;
import cat.udl.eps.softarch.demo.domain.Visibility;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface ProjectRepository
        extends CrudRepository<Project, Long>, PagingAndSortingRepository<Project, Long> {

    List<Project> findByPortfolio(Portfolio portfolio);

    List<Project> findByCreator(User creator);

    List<Project> findByVisibility(Visibility visibility);

    List<Project> findByFlagged(boolean flagged);

    List<Project> findByNameContainingIgnoreCase(String name);
}
