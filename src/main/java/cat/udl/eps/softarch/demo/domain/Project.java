package cat.udl.eps.softarch.demo.domain;
import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

import java.time.ZonedDateTime;

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

    @NotBlank
    private boolean flagged;

    @NotBlank
    @Enumerated(EnumType.STRING)
    private Visibility visibility;

    @NotBlank
    @Enumerated(EnumType.STRING)
    private Status status;
    private Boolean isPrivate;

    @DateTimeFormat
    private ZonedDateTime created;

    @DateTimeFormat
    private ZonedDateTime lastModified;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<Asset> assets;

    // User Relations

    @Setter
    @ManyToOne
    @JsonIdentityReference(alwaysAsId = true)
    private User creator;

    @Setter
    @ManyToOne
    @JsonIdentityReference(alwaysAsId = true)
    private User moderator;

    @Setter
    @ManyToOne
    @JsonIdentityReference(alwaysAsId = true)
    private Project project;

    @OneToMany
    private java.util.List<User> collaborators;

    // --- Constructors ---

    /**
     * Constructs a new Project with the specified name, description, and visibility.
     * @param name the name of the project
     * @param description a brief description of the project
     * @param isPrivate the visibility status of the project (true for private, false for public)
     */
    public Project(String name, String description, Boolean isPrivate) {
        this.flagged = false;
        this.name = name;
        this.description = description;
        this.isPrivate = isPrivate;
        this.created = ZonedDateTime.now();
        this.lastModified = ZonedDateTime.now();
    }

    //getters i setters es generen amb el @data automaticament

}
