package cat.udl.eps.softarch.demo.repository;

import cat.udl.eps.softarch.demo.domain.Portfolio;
import cat.udl.eps.softarch.demo.domain.User;
import cat.udl.eps.softarch.demo.domain.Visibility;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;


@RepositoryRestResource
public interface PortfolioRepository {
    List<Portfolio> findByCreator(@Param("user") User creator);
    List<Portfolio> findByNameContaining(@Param("name") String text);
    List<Portfolio> findByVisibility(@Param("visibility") Visibility visibility);
}
