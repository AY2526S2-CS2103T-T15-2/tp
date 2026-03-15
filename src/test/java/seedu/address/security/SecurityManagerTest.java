package seedu.address.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.util.FileUtil;
import seedu.address.logic.Logic;
import seedu.address.logic.commands.CommandResult;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Person;
import seedu.address.security.util.PasswordUtil;

public class SecurityManagerTest {

    @TempDir
    public Path temporaryFolder;

    @Test
    public void isAuthenticated_fileExists_returnsTrue() throws Exception {
        Path passwordFile = temporaryFolder.resolve("password.txt");
        FileUtil.writeToFile(passwordFile, "any_hash");

        SecurityManager securityManager = new SecurityManager(new LogicStub(), passwordFile, Optional::empty);

        assertTrue(securityManager.isAuthenticated());
    }

    @Test
    public void isAuthenticated_fileMissing_successfulSetup() throws Exception {
        Path passwordFile = temporaryFolder.resolve("new_password.txt");
        String rawPassword = "nusStudent2026";

        SecurityManager securityManager = new SecurityManager(new LogicStub(),
                passwordFile, () -> Optional.of(rawPassword));

        assertTrue(securityManager.isAuthenticated());
        assertTrue(FileUtil.isFileExists(passwordFile));
        assertEquals(PasswordUtil.hashPassword(rawPassword), FileUtil.readFromFile(passwordFile));
    }

    @Test
    public void isAuthenticated_setupCancelled_returnsFalse() {
        Path passwordFile = temporaryFolder.resolve("cancelled.txt");

        //  Simulate user closes the window
        SecurityManager securityManager = new SecurityManager(
                new LogicStub(),
                passwordFile,
                Optional::empty
        );

        assertFalse(securityManager.isAuthenticated());
        assertFalse(FileUtil.isFileExists(passwordFile));
    }

    @Test
    public void savePassword_pathIsDirectory_returnsFalse() throws Exception {
        Path directoryPath = temporaryFolder.resolve("i_am_a_directory");
        Files.createDirectories(directoryPath);

        SecurityManager securityManager = new SecurityManager(new LogicStub(),
                directoryPath, () -> Optional.of("validPassword"));

        assertFalse(securityManager.isAuthenticated());
    }

    @Test
    public void constructor_production_isNotNull() {
        // smoke test for the production constructor
        assertNotNull(new SecurityManager(new LogicStub()));
    }

    /**
     * A default stub where all methods fail except those needed for SecurityManager.
     */
    private static class LogicStub implements Logic {
        @Override
        public CommandResult execute(String commandText) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Path getAddressBookFilePath() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public GuiSettings getGuiSettings() {
            return new GuiSettings();
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            throw new AssertionError("This method should not be called.");
        }
    }
}
