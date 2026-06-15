package cat.udl.eps.softarch.demo.domain;

/**
 * Visibility level for portfolios and projects.
 * <ul>
 *   <li>PUBLIC    – visible to everyone, including unauthenticated visitors</li>
 *   <li>UNLISTED  – accessible via direct link but not listed publicly</li>
 *   <li>PRIVATE   – visible only to the owner and collaborators</li>
 *   <li>RESTRICTED – visible only to users explicitly granted access</li>
 * </ul>
 */
public enum Visibility {
    PUBLIC,
    UNLISTED,
    PRIVATE,
    RESTRICTED
}
