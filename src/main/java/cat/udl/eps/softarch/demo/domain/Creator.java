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
@Table(name = "Creator")
@Data
@EqualsAndHashCode(callSuper = true)
public class Creator extends User {

    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Portfolio> portfolios = new ArrayList<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "creator_creates_tag",
        joinColumns = @JoinColumn(name = "creator_username"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )


    // private List<Tag> createdTags = new ArrayList<>();

    //@OneToMany(mappedBy = "creator", cascade = CascadeType.ALL, orphanRemoval = true)
    //private List<Collaborator> collaborators = new ArrayList<>();


    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Asset> assets = new ArrayList<>();


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER,ROLE_CREATOR");
    }
}