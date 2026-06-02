package cat.udl.eps.softarch.demo.domain;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Links a {@link User} to a {@link Project} and defines the actions that user is permitted to perform.
 *
 * <p>A project owner can invite other users as collaborators by creating a Collaborator record.
 * The {@code action} field controls the maximum permission level granted.</p>
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Collaborator extends UriEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** The user who has been granted collaborator access. */
    @ManyToOne(optional = false)
    @JsonIdentityReference(alwaysAsId = true)
    private User user;

    /** The project this collaborator has access to. */
    @ManyToOne(optional = false)
    @JsonIdentityReference(alwaysAsId = true)
    private Project project;

    /** The permission level granted to this collaborator. */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CollaboratorAction action;

    public Collaborator() {}

    public Collaborator(User user, Project project, CollaboratorAction action) {
        this.user = user;
        this.project = project;
        this.action = action;
    }
}
