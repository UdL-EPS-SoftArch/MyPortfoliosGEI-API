package cat.udl.eps.softarch.demo.repository;

import cat.udl.eps.softarch.demo.domain.Tag;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource
public interface TagRepository
        extends CrudRepository<Tag, Long>, PagingAndSortingRepository<Tag, Long> {

    boolean existsByName(String name);

    Optional<Tag> findByName(String name);
}
