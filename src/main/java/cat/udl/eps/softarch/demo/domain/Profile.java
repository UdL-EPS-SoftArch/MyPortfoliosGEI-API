package cat.udl.eps.softarch.demo.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Profile extends UriEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * A Profile belongs to exactly one User
     * (User owns Profile in the diagram)
     */
    @OneToOne(optional = false)
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    /*
     * A Profile creates many Projects
     * (1 -> *)
     *
     * We assume Project has:
     *     @ManyToOne Profile profile;
     */
    @OneToMany(mappedBy = "profile",
        cascade = CascadeType.ALL,
        orphanRemoval = true)
    private Set<Project> projects = new HashSet<>();


    /* ---------- Helper methods ---------- */

    public void addProject(Project project) {
        projects.add(project);
        project.setProfile(this);
    }

    public void removeProject(Project project) {
        projects.remove(project);
        project.setProfile(null);
    }
}

