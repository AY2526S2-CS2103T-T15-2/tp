package seedu.address.security;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.util.FileUtil;
import seedu.address.logic.Logic;
import seedu.address.security.util.PasswordUtil;
import seedu.address.ui.PasswordWindow;

/**
 * Manages the security and authentication state of the application.
 * The {@code SecurityManager} handles the lifecycle of application access,
 * including initial password setup and persistent authentication state.
 * It coordinates between the UI (password collection) and the filesystem (persistence).
 */
public class SecurityManager implements Security {

    private static final Logger logger = LogsCenter.getLogger(SecurityManager.class);

    private final Path passwordFilePath;
    private final Logic logic;
    private final Supplier<Optional<String>> passwordSupplier;

    /**
     * Constructs a {@code SecurityManager} for production use.
     * Uses the default data path {@code data/password.txt} and initializes a real
     * {@link PasswordWindow} to collect user input.
     *
     * @param logic The logic component used to retrieve GUI settings for the UI.
     */
    public SecurityManager(Logic logic) {
        this(logic, Paths.get("data", "password.txt"), () -> {
            PasswordWindow passwordWindow = new PasswordWindow(logic.getGuiSettings());
            passwordWindow.show();
            return passwordWindow.getPassword();
        });
    }

    /**
     * Constructs a {@code SecurityManager} with custom dependencies.
     * This constructor is primarily used for testing to inject mock file paths
     * and simulated password input.
     *
     * @param logic The logic component.
     * @param passwordFilePath The path where the password hash is stored.
     * @param passwordSupplier A functional interface providing an Optional password string.
     */
    public SecurityManager(Logic logic, Path passwordFilePath, Supplier<Optional<String>> passwordSupplier) {
        this.logic = logic;
        this.passwordFilePath = passwordFilePath;
        this.passwordSupplier = passwordSupplier;
    }

    /**
     * Checks if the application is currently authenticated.
     * If a password file exists at {@code passwordFilePath}, authentication is considered
     * successful. If not, the first-time password setup dialog is triggered.
     *
     * @return True if authenticated or if setup is completed successfully; false if setup is cancelled.
     */
    @Override
    public boolean isAuthenticated() {
        if (FileUtil.isFileExists(passwordFilePath)) {
            logger.info("Authentication successful: Password file detected at " + passwordFilePath);
            return true;
        }
        logger.info("Authentication required: Starting first-time password setup.");
        return showPasswordSetupDialog();
    }

    /**
     * Orchestrates the password setup process.
     * It retrieves the raw password from the {@code passwordSupplier} and attempts to
     * persist it.
     *
     * @return True if a password was successfully provided and saved; false otherwise.
     */
    private boolean showPasswordSetupDialog() {
        Optional<String> result = passwordSupplier.get();
        if (result.isPresent()) {
            return savePassword(result.get());
        }

        logger.warning("Security setup aborted: User closed the setup window.");
        return false;
    }

    /**
     * Saves the provided raw password to the local filesystem.
     *
     * @param password The plain text password entered by the user.
     * @return True if the password was successfully written; false otherwise.
     */
    private boolean savePassword(String password) {
        try {
            if (!PasswordUtil.isValidPassword(password)) {
                logger.warning("Attempted to save an invalid password.");
                return false;
            }

            FileUtil.createParentDirsOfFile(passwordFilePath);
            FileUtil.writeToFile(passwordFilePath, password);

            logger.info("Security setup complete: Password saved to " + passwordFilePath);
            return true;
        } catch (IOException e) {
            logger.severe("Security setup failed: Could not save password file. " + e.getMessage());
            return false;
        }
    }
}
