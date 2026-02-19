package cat.udl.eps.softarch.demo.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Profile extends UriEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* ---------- Basic Profile Info ---------- */

    @NotBlank
    @Size(min = 2, max = 60)
    private String fullName;

    @Email
    @NotBlank
    @Column(unique = true)
    private String email;

    @Size(max = 250)
    private String bio;

    private String avatarUrl;

    private String location;

    /* ---------- Social Links ---------- */

    private String github;
    private String twitter;
    private String instagram;
    private String linkedin;

    /* ---------- Timestamps ---------- */

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /* ---------- Relationships ---------- */

    /*
     * A Profile belongs to exactly one User
     */
    @OneToOne(optional = false)
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    /*
     * A Profile creates many Projects
     */
    @OneToMany(mappedBy = "profile",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @Builder.Default
    private Set<Project> projects = new HashSet<>();


    /* ---------- Helper Methods ---------- */

    public void addProject(Project project) {
        projects.add(project);
        project.setProfile(this);
    }

    public void removeProject(Project project) {
        projects.remove(project);
        project.setProfile(null);
    }

    public void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }

    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
