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

    // Message to be displayed when the password fails validation constraints
    public static final String MESSAGE_CONSTRAINTS = "Password cannot be empty or contain only whitespace!";

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
     * A valid password must not be null, empty, or consist solely of whitespace.
     *
     * @param password The plain text password to validate.
     * @return True if the password is valid, false otherwise.
     */
    public static boolean isValidPassword(String password) {
        return password != null && !password.trim().isEmpty();
    }
}
