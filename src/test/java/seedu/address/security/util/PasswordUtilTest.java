package seedu.address.security.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * Test class for {@code PasswordUtil}.
 * Focuses on hashing consistency and validation rules for application setup.
 */
public class PasswordUtilTest {

    // Hashing Logic Tests

    @Test
    public void hashPassword_validInput_returnsCorrectLength() {
        String hash = PasswordUtil.hashPassword("myPassword123");
        // SHA-256 hex string is always 64 characters
        assertEquals(64, hash.length());
    }

    @Test
    public void hashPassword_emptyString_returnsCorrectLength() {
        String hash = PasswordUtil.hashPassword("");
        assertEquals(64, hash.length());
    }

    @Test
    public void hashPassword_nullInput_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> PasswordUtil.hashPassword(null));
    }

    @Test
    public void hashPassword_differentInputs_produceDifferentHashes() {
        String hash1 = PasswordUtil.hashPassword("abc");
        String hash2 = PasswordUtil.hashPassword("abd");
        assertNotEquals(hash1, hash2);
    }

    // Validation Logic Tests

    @Test
    public void isValidPassword_nullInput_returnsFalse() {
        assertFalse(PasswordUtil.isValidPassword(null));
    }

    @Test
    public void isValidPassword_emptyString_returnsFalse() {
        assertFalse(PasswordUtil.isValidPassword(""));
    }

    @Test
    public void isValidPassword_onlyWhitespace_returnsFalse() {
        assertFalse(PasswordUtil.isValidPassword("   "));
        assertFalse(PasswordUtil.isValidPassword("\n\t "));
    }

    @Test
    public void isValidPassword_singleCharacter_returnsTrue() {
        assertTrue(PasswordUtil.isValidPassword("p"));
    }

    @Test
    public void isValidPassword_alphanumeric_returnsTrue() {
        assertTrue(PasswordUtil.isValidPassword("password123"));
    }

    @Test
    public void isValidPassword_specialCharacters_returnsTrue() {
        assertTrue(PasswordUtil.isValidPassword("!@#$%^&*()"));
    }

    @Test
    public void isValidPassword_withLeadingTrailingWhitespace_returnsTrue() {
        assertTrue(PasswordUtil.isValidPassword("  actualPassword  "));
    }
}
