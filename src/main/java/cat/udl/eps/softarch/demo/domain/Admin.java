package cat.udl.eps.softarch.demo.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "Admin")
@Data
@EqualsAndHashCode(callSuper = true)
public class Admin extends User {

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "admin_creates_tag",
        joinColumns = @JoinColumn(name = "admin_username"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> createdTags = new ArrayList<>();


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER,ROLE_ADMIN");
    }
}
