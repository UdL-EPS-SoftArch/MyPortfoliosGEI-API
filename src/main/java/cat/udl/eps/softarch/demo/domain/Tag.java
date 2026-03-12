package cat.udl.eps.softarch.demo.domain;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Tag extends UriEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, unique = true)
    @EqualsAndHashCode.Include
    private String name;

    // --- Relationships ---

    @ManyToMany(mappedBy = "tags")
    @JsonIdentityReference(alwaysAsId = true)
    private Set<Project> projects = new HashSet<>();

    public Tag() {}

    public Tag(String name) {
        this.name = name;
    }

    // --- Helper methods ---

    public void addProject(Project project) {
        projects.add(project);
        project.getTags().add(this);
    }

    public void removeProject(Project project) {
        projects.remove(project);
        project.getTags().remove(this);
    }

    @Override
    public Long getId() {
        return id;
    }
}