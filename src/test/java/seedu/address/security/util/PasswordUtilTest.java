package seedu.address.security.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Test class for {@code PasswordUtil}.
 * Focuses on hashing consistency and validation rules for application setup.
 */
public class PasswordUtilTest {

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
        // Java's trim function treats \n\t as escape characters
        assertFalse(PasswordUtil.isValidPassword("\n\t"));
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

    @Test
    public void isValidPassword_containsInternalSpace_returnsFalse() {
        assertFalse(PasswordUtil.isValidPassword("k l"));
    }

    @Test
    public void isValidPassword_containsInternalTab_returnsTrue() {
        assertTrue(PasswordUtil.isValidPassword("k\tl"));
    }

    @Test
    public void isValidPassword_literalBackslash_returnsTrue() {
        assertTrue(PasswordUtil.isValidPassword("k\\l"));
    }

    // Validation Error Message Tests

    @Test
    public void getValidationError_nullOrEmpty_returnsEmptyMessage() {
        assertEquals(PasswordUtil.MESSAGE_EMPTY, PasswordUtil.getValidationError(null));
        assertEquals(PasswordUtil.MESSAGE_EMPTY, PasswordUtil.getValidationError(""));
        assertEquals(PasswordUtil.MESSAGE_EMPTY, PasswordUtil.getValidationError("   "));
        assertEquals(PasswordUtil.MESSAGE_EMPTY, PasswordUtil.getValidationError("\n\t"));
    }

    @Test
    public void getValidationError_containsSpaces_returnsNoSpacesMessage() {
        assertEquals(PasswordUtil.MESSAGE_NO_SPACES, PasswordUtil.getValidationError("k l"));
        assertEquals(PasswordUtil.MESSAGE_EMPTY, PasswordUtil.getValidationError("   "));
    }

    @Test
    public void getValidationError_validInput_returnsNull() {
        assertNull(PasswordUtil.getValidationError("p"));
        assertNull(PasswordUtil.getValidationError("password123"));
        assertNull(PasswordUtil.getValidationError("  actualPassword  "));

        assertNull(PasswordUtil.getValidationError("k\\l"));
        assertNull(PasswordUtil.getValidationError("\\n\\t"));
    }
}
