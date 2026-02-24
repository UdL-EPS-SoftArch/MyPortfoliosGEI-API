package cat.udl.eps.softarch.demo.domain;

import org.springframework.lang.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import java.time.ZonedDateTime;

/**
 * Represents a Project entity within the system.
 */
@Entity
public class Project extends UriEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private boolean flagged;
    private Visibility visibility;

    private ZonedDateTime created;
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

    // --- Getters ---

    /**
     * Returns the unique identifier of the project.
     * @return the project ID
     */
    @Nullable
    @Override
    public Long getId() {
        return id;
    }

    /**
     * Returns the name of the project.
     * @return the project name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the description of the project.
     * @return the project description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns whether the project is flagged.
     * @return true if flagged, false otherwise
     */
    public boolean getFlagged() {
        return flagged;
    }

    /**
     * Returns the visibility level of the project.
     * @return the project visibility
     */
    @Nullable
    public Visibility getVisibility() {
        return this.visibility;
    }

    /**
     * Returns the date and time when the project was created.
     * @return the creation timestamp
     */
    @Nullable
    public ZonedDateTime getCreated() {
        return created;
    }

    /**
     * Returns the date and time when the project was last modified.
     * @return the last modification timestamp
     */
    public ZonedDateTime getLastModified() {
        return lastModified;
    }

    // --- Setters ---

    /**
     * Sets the name of the project.
     * @param name the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the description of the project.
     * @param description the new description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Sets the flagged status of the project.
     * @param flagged the new flagged status
     */
    public void setFlagged(boolean flagged) {
        this.flagged = flagged;
    }

    /**
     * Sets the visibility level of the project.
     * @param visibility the new visibility level
     */
    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    /**
     * Sets the creation date and time of the project.
     * @param created the creation timestamp
     */
    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }

    /**
     * Sets the last modification date and time of the project.
     * @param lastModified the last modification timestamp
     */
    public void setLastModified(ZonedDateTime lastModified) {
        this.lastModified = lastModified;
    }

}
