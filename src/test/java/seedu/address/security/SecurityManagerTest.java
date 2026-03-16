package seedu.address.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

public class SecurityManagerTest {

    @TempDir
    public Path temporaryFolder;

    @Test
    public void isAuthenticated_fileExists_returnsTrue() throws Exception {
        Path passwordFile = temporaryFolder.resolve("password.txt");
        FileUtil.writeToFile(passwordFile, "any_pw");

        SecurityManager securityManager = new SecurityManager(new LogicStub(), passwordFile, Optional::empty);

        assertTrue(securityManager.isAuthenticated());
    }

    @Test
    public void isAuthenticated_fileMissing_successfulSetup() throws Exception {
        Path passwordFile = temporaryFolder.resolve("new_password.txt");
        String password = "nusStudent2026";

        SecurityManager securityManager = new SecurityManager(new LogicStub(),
                passwordFile, () -> Optional.of(password));

        assertTrue(securityManager.isAuthenticated());
        assertTrue(FileUtil.isFileExists(passwordFile));
        assertEquals(password, FileUtil.readFromFile(passwordFile));
    }

    @Test
    public void isAuthenticated_setupCancelled_returnsFalse() {
        Path passwordFile = temporaryFolder.resolve("cancelled.txt");

        SecurityManager securityManager = new SecurityManager(
                new LogicStub(),
                passwordFile,
                Optional::empty
        );

        assertFalse(securityManager.isAuthenticated());
        assertFalse(FileUtil.isFileExists(passwordFile));
    }

    @Test
    public void constructor_production_isNotNull() {
        Path passwordFile = temporaryFolder.resolve("prod_password.txt");
        assertNotNull(new SecurityManager(new LogicStub(passwordFile)));
    }

    /**
     * A default stub where all methods fail except those needed for SecurityManager.
     */
    private static class LogicStub implements Logic {
        private final Path passwordPath;

        LogicStub() {
            this.passwordPath = null;
        }

        LogicStub(Path passwordPath) {
            this.passwordPath = passwordPath;
        }

        @Override
        public Path getAddressBookPasswordPath() {
            return passwordPath;
        }

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
