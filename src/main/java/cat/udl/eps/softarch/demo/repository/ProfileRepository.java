package cat.udl.eps.softarch.demo.repository;

import cat.udl.eps.softarch.demo.domain.Profile;
import cat.udl.eps.softarch.demo.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface ProfileRepository extends CrudRepository<Profile, Long>, PagingAndSortingRepository<Profile, Long> {
    Profile findByUser(@Param("user") User user);
    List<Profile> findByFullNameContaining(@Param("name") String text);
}
