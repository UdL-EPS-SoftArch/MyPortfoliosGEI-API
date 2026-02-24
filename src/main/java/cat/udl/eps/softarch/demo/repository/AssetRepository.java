package cat.udl.eps.softarch.demo.repository;

import cat.udl.eps.softarch.demo.domain.Asset;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource
public interface AssetRepository extends CrudRepository<Asset, String>, PagingAndSortingRepository<Asset, String> {
    Optional<Asset> findByName(String name);
}

