package cat.udl.eps.softarch.demo.repository;

import cat.udl.eps.softarch.demo.domain.Asset;
import cat.udl.eps.softarch.demo.domain.Project;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource
public interface AssetRepository extends CrudRepository<Asset, String>, PagingAndSortingRepository<Asset, String> {
    Optional<Asset> findByName(String name);
    List<Asset> findByBelongsTo(@Param("project") Project project);
}

