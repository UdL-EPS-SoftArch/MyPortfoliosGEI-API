package cat.udl.eps.softarch.demo.handler;

import cat.udl.eps.softarch.demo.domain.Collaborator;
import cat.udl.eps.softarch.demo.domain.User;
import cat.udl.eps.softarch.demo.repository.UserRepository;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeDelete;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Enforces ownership rules for {@link Collaborator} records.
 *
 * <ul>
 *   <li>Only the project creator (or an admin) may add, modify, or remove collaborators.</li>
 *   <li>A user cannot add themselves as a collaborator on their own project.</li>
 * </ul>
 */
@Component
@RepositoryEventHandler
public class CollaboratorEventHandler {

    private final UserRepository userRepository;

    public CollaboratorEventHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @HandleBeforeCreate
    public void handleCollaboratorPreCreate(Collaborator collaborator) {
        User current = currentUser();

        if (collaborator.getProject() == null) {
            throw new IllegalArgumentException("A collaborator must be linked to a project.");
        }
        if (collaborator.getUser() == null) {
            throw new IllegalArgumentException("A collaborator must reference a user.");
        }

        User projectCreator = collaborator.getProject().getCreator();

        if (!isAdmin(current) && (projectCreator == null || !projectCreator.getId().equals(current.getId()))) {
            throw new AccessDeniedException("Only the project owner or an admin can add collaborators.");
        }

        if (projectCreator != null && projectCreator.getId().equals(collaborator.getUser().getId())) {
            throw new IllegalArgumentException("The project owner cannot be added as a collaborator.");
        }
    }

    @HandleBeforeSave
    public void handleCollaboratorPreSave(Collaborator collaborator) {
        User current = currentUser();
        User projectCreator = collaborator.getProject() != null ? collaborator.getProject().getCreator() : null;

        if (!isAdmin(current) && (projectCreator == null || !projectCreator.getId().equals(current.getId()))) {
            throw new AccessDeniedException("Only the project owner or an admin can modify collaborators.");
        }
    }

    @HandleBeforeDelete
    public void handleCollaboratorPreDelete(Collaborator collaborator) {
        User current = currentUser();
        User projectCreator = collaborator.getProject() != null ? collaborator.getProject().getCreator() : null;

        // Project owner or the collaborator themselves may remove the record
        boolean isOwner = projectCreator != null && projectCreator.getId().equals(current.getId());
        boolean isSelf  = collaborator.getUser() != null && collaborator.getUser().getId().equals(current.getId());

        if (!isAdmin(current) && !isOwner && !isSelf) {
            throw new AccessDeniedException("Only the project owner, the collaborator themselves, or an admin can remove a collaborator.");
        }
    }

    // --- helpers ---

    private User currentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findById(auth.getName())
                .orElseThrow(() -> new AccessDeniedException("Authenticated user not found"));
    }

    private boolean isAdmin(User user) {
        return user.getAuthorities().stream()
                .anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()));
    }
}
