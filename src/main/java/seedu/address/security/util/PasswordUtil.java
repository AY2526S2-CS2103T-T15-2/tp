package seedu.address.security.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utility methods for password hashing and validation.
 * This class provides static methods to ensure passwords meet application requirements
 * and to securely hash them using the SHA-256 algorithm.
 */
public class PasswordUtil {

    public static final String MESSAGE_EMPTY = "Password cannot be empty!";
    public static final String MESSAGE_NO_SPACES = "Password must not contain spaces!";
    private static final String HASH_ALGORITHM = "SHA-256";

    /**
     * Hashes the given plain text password using the SHA-256 algorithm.
     * The resulting hash is returned as a 64-character hexadecimal string.
     *
     * @param password Plain text password to hash.
     * @return A 64-character hexadecimal string representation of the hash.
     * @throws RuntimeException If the SHA-256 algorithm is not available in the environment.
     * @throws NullPointerException If {@code password} is null.
     */
    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
            byte[] encodedHash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(encodedHash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }

    /**
     * Converts a byte array into a hexadecimal string.
     * Each byte is converted to its two-digit hexadecimal equivalent.
     *
     * @param hash The byte array to convert.
     * @return The hexadecimal string representation of the byte array.
     */
    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

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
