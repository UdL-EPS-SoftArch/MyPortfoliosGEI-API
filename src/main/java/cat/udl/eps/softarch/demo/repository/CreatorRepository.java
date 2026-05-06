package cat.udl.eps.softarch.demo.repository;

import cat.udl.eps.softarch.demo.domain.Creator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface CreatorRepository extends JpaRepository<Creator, String> {
}