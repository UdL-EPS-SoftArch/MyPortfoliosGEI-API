package cat.udl.eps.softarch.demo.domain;

import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Tag {

    @NotBlank
    @EqualsAndHashCode.Include
    private String name;

    private Set<Project> projects = new HashSet<>();

    public Tag(String name) {
        this.name = name;
    }

    public void addProject(Project project) {
        projects.add(project);
    }

    public void removeProject(Project project) {
        projects.remove(project);
    }
}
