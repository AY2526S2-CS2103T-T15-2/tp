package seedu.address.security.util;

/**
 * Utility methods for password hashing and validation.
 * This class provides static methods to ensure passwords meet application requirements.
 */
public class PasswordUtil {

    public static final String MESSAGE_EMPTY = "Password cannot be empty!";
    public static final String MESSAGE_NO_SPACES = "Password must not contain spaces!";

    /**
     * Checks if the given password is valid for application use.
     * @param password The plain text password to validate.
     * @return True if the password is valid (no error found), false otherwise.
     */
    public static boolean isValidPassword(String password) {
        return getValidationError(password) == null;
    }

    /**
     * Returns a specific error message if the password is invalid.
     * The input is trimmed before determining the error type.
     *
     * @param password The password to check.
     * @return The error message constant, or null if the password is valid.
     */
    public static String getValidationError(String password) {
        if (password == null) {
            return MESSAGE_EMPTY;
        }

        String trimmedPassword = password.trim();

        if (trimmedPassword.isEmpty()) {
            return MESSAGE_EMPTY;
        }
        if (trimmedPassword.contains(" ")) {
            return MESSAGE_NO_SPACES;
        }
        return null;
    }
}
