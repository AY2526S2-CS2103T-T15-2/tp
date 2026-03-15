package seedu.address.security;

/**
 * API of the Security component.
 */
public interface Security {
    /**
     * Handles the password setup/check.
     * @return true if access is granted, false otherwise.
     */
    boolean isAuthenticated();
}
