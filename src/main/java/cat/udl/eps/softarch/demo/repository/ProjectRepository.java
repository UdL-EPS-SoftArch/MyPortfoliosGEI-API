package cat.udl.eps.softarch.demo.repository;

import cat.udl.eps.softarch.demo.domain.Project;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import org.springframework.data.repository.query.Param;
import java.util.List;

@RepositoryRestResource
public interface ProjectRepository extends CrudRepository<Project, Long>, PagingAndSortingRepository<Project, Long> {
    List<Project> findByCreatorId(@Param("id") String id);
}
