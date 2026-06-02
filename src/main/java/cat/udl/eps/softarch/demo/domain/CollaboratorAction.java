package cat.udl.eps.softarch.demo.domain;

/**
 * Actions that a collaborator is permitted to perform on a project.
 */
public enum CollaboratorAction {
    /** Can view project content. */
    View,
    /** Can view and edit project content. */
    Edit,
    /** Can view, edit and remove assets from the project. */
    Remove
}
