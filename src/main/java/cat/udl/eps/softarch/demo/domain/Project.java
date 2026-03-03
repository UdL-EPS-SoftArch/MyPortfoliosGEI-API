package cat.udl.eps.softarch.demo.domain;

import org.springframework.format.annotation.DateTimeFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

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
    private boolean flagged;
    private Visibility visibility;

    @DateTimeFormat
    private ZonedDateTime created;

    @DateTimeFormat
    private ZonedDateTime lastModified;

    // --- Constructors ---

    /**
     * Constructs a new Project with the specified name, description, and visibility.
     * @param name the name of the project
     * @param description a brief description of the project
     * @param visibility the visibility level of the project
     */
    public Project(String name, String description, Visibility visibility) {
        this.flagged = false;
        this.name = name;
        this.description = description;
        this.visibility = visibility;
        this.created = ZonedDateTime.now();
        this.lastModified = ZonedDateTime.now();
    }

    //getters i setters es generen amb el @data automaticament

}
