package cat.udl.eps.softarch.demo.domain;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a Project entity within the system.
 */

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Project extends UriEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    private String description;

    private boolean flagged;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Visibility visibility = Visibility.PUBLIC;

    @DateTimeFormat
    private ZonedDateTime created;

    @DateTimeFormat
    private ZonedDateTime lastModified;

    // --- Relations ---

    /** Portfolio this project belongs to (optional – projects can also exist standalone). */
    @ManyToOne
    @JsonIdentityReference(alwaysAsId = true)
    private Portfolio portfolio;

    @ManyToOne
    @JsonIdentityReference(alwaysAsId = true)
    private User creator;

    @ManyToOne
    @JsonIdentityReference(alwaysAsId = true)
    private User moderator;

    /** Optional parent project for nested/sub-projects. */
    @ManyToOne
    @JsonIdentityReference(alwaysAsId = true)
    private Project project;

    /** Tags associated with this project. */
    @ManyToMany
    @JoinTable(
        name = "project_tags",
        joinColumns = @JoinColumn(name = "project_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();

    // --- Constructors ---

    public Project() {}

    /**
     * Constructs a new Project with the specified name, description, and visibility.
     *
     * @param name        the name of the project
     * @param description a brief description of the project
     * @param visibility  the visibility level of the project
     */
    public Project(String name, String description, Visibility visibility) {
        this.flagged = false;
        this.name = name;
        this.description = description;
        this.visibility = visibility != null ? visibility : Visibility.PUBLIC;
        this.created = ZonedDateTime.now();
        this.lastModified = ZonedDateTime.now();
    }
}
