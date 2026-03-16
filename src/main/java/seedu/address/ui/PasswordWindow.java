package seedu.address.ui;

import java.util.Optional;
import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.security.util.PasswordUtil;

/**
 * Controller for the password setup window.
 * This window is responsible for capturing the initial application password
 * and providing visual feedback for validation errors.
 */
public class PasswordWindow extends UiPart<Stage> {

    private static final String FXML = "PasswordWindow.fxml";
    private final Logger logger = LogsCenter.getLogger(getClass());

    private String passwordEntered = null;

    @FXML
    private TextField passwordInput;

    @FXML
    private Label errorMessage;

    /**
     * Creates a new {@code PasswordWindow} with the default stage and specified settings.
     *
     * @param guiSettings The GUI settings to determine the initial window size and position.
     */
    public PasswordWindow(GuiSettings guiSettings) {
        this(new Stage(), guiSettings);
    }

    /**
     * Creates a new {@code PasswordWindow} using the given {@code root} stage.
     * Sets up listeners to clear error states when the user interacts with the input field.
     *
     * @param root Stage to use as the root of the PasswordWindow.
     * @param guiSettings The GUI settings to determine the initial window size and position.
     */
    public PasswordWindow(Stage root, GuiSettings guiSettings) {
        super(FXML, root);
        setWindowDefaultSize(root, guiSettings);

        root.setMinWidth(900);
        root.setMinHeight(650);

        // Listener to clear error as soon as user starts typing
        passwordInput.textProperty().addListener((observable, oldValue, newValue) -> clearError());
    }

    /**
     * Sets the default size and position of the window based on {@code guiSettings}.
     *
     * @param stage The stage to configure.
     * @param guiSettings The settings to apply.
     */
    private void setWindowDefaultSize(Stage stage, GuiSettings guiSettings) {
        stage.setHeight(guiSettings.getWindowHeight());
        stage.setWidth(guiSettings.getWindowWidth());
        if (guiSettings.getWindowCoordinates() != null) {
            stage.setX(guiSettings.getWindowCoordinates().getX());
            stage.setY(guiSettings.getWindowCoordinates().getY());
        }
    }

    /**
     * Shows the password window and waits for the user to close it.
     */
    public void show() {
        logger.fine("Showing password setup window.");
        getRoot().showAndWait();
    }

    /**
     * Returns the password entered by the user, wrapped in an {@code Optional}.
     * Returns an empty {@code Optional} if the user closed the window without entering a password.
     *
     * @return An Optional containing the entered password.
     */
    public Optional<String> getPassword() {
        return Optional.ofNullable(passwordEntered);
    }

    /**
     * Resets the error message and clears error styling from UI components.
     */
    private void clearError() {
        errorMessage.getStyleClass().remove("error");
        passwordInput.getStyleClass().remove("error");
        errorMessage.setText("The application requires a password to initialise.");
    }

    /**
     * Handles the event when the user attempts to submit a password.
     * Validates that the input satisfies the security constraints before closing the window.
     */
    @FXML
    private void handlePasswordSetup() {
        String input = passwordInput.getText();
        clearError();

        if (!PasswordUtil.isValidPassword(input)) {
            showError(PasswordUtil.getValidationError(input));
        } else {
            passwordEntered = input;
            getRoot().close();
        }
    }

    /**
     * Displays an error message and applies error styling to the input field and label.
     *
     * @param message The error message to display.
     */
    private void showError(String message) {
        errorMessage.setText(message);
        errorMessage.getStyleClass().add("error");
        passwordInput.getStyleClass().add("error");
    }
}
