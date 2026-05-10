package cat.udl.eps.softarch.demo.repository;

import cat.udl.eps.softarch.demo.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import java.util.List;
import java.util.Optional;

@RepositoryRestResource
public interface TagRepository extends JpaRepository<Tag, Long> {

    boolean existsByName(String name);

    Optional<Tag> findByName(String name);

    List<Tag> findByNameContainingIgnoreCase(@Param("name") String name);
}