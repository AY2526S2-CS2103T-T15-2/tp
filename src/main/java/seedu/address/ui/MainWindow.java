package seedu.address.ui;

import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.Logic;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.MainApp;
import seedu.address.model.AppMode;
import seedu.address.ui.dummy.DummyMainPanel;
import seedu.address.ui.spyglass.SpyglassMainPanel;

/**
 * The Main Window. Provides the basic application layout containing
 * a menu bar and space where other JavaFX elements can be placed.
 */
public class MainWindow extends UiPart<Stage> {

    private static final String FXML = "MainWindow.fxml";

    private final Logger logger = LogsCenter.getLogger(getClass());

    private Stage primaryStage;
    private Logic logic;

    private DummyMainPanel dummyMainPanel;
    private SpyglassMainPanel spyglassMainPanel;
    private AppMode currentMode = AppMode.LOCKED;

    @FXML
    private StackPane modePanelPlaceholder;

    /**
     * Creates a {@code MainWindow} with the given {@code Stage} and {@code Logic}.
     */
    public MainWindow(Stage primaryStage, Logic logic) {
        super(FXML, primaryStage);

        // Set dependencies
        this.primaryStage = primaryStage;
        this.logic = logic;

        // Configure the UI
        setWindowDefaultSize(logic.getGuiSettings());
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * Fills up all the placeholders of this window.
     */
    void fillInnerParts() {
        dummyMainPanel = new DummyMainPanel(
            logic.getFilteredPersonList(), logic.getAddressBookFilePath(), this::executeCommand, this::handleExit);
        spyglassMainPanel = new SpyglassMainPanel(
            logic.getFilteredPersonList(), logic.getAddressBookFilePath(), this::executeCommand, this::handleExit);
        setMode(AppMode.LOCKED);
    }

    /**
     * Sets the default size based on {@code guiSettings}.
     */
    private void setWindowDefaultSize(GuiSettings guiSettings) {
        primaryStage.setHeight(guiSettings.getWindowHeight());
        primaryStage.setWidth(guiSettings.getWindowWidth());
        if (guiSettings.getWindowCoordinates() != null) {
            primaryStage.setX(guiSettings.getWindowCoordinates().getX());
            primaryStage.setY(guiSettings.getWindowCoordinates().getY());
        }
    }

    void show() {
        primaryStage.show();
    }

    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        GuiSettings guiSettings = new GuiSettings(primaryStage.getWidth(), primaryStage.getHeight(),
                (int) primaryStage.getX(), (int) primaryStage.getY());
        logic.setGuiSettings(guiSettings);
        if (dummyMainPanel != null) {
            dummyMainPanel.hideHelpWindow();
        }
        if (spyglassMainPanel != null) {
            spyglassMainPanel.hideHelpWindow();
        }
        primaryStage.hide();
    }

    private void setMode(AppMode mode) {
        currentMode = mode;
        boolean isLocked = mode == AppMode.LOCKED;
        modePanelPlaceholder.getChildren().setAll(
                (isLocked ? dummyMainPanel : spyglassMainPanel).getRoot());
        String themeFolder = isLocked ? "dummy" : "spyglass";
        primaryStage.getScene().getStylesheets().setAll(
                MainApp.class.getResource("/view/" + themeFolder + "/DarkTheme.css").toExternalForm(),
                MainApp.class.getResource("/view/" + themeFolder + "/Extensions.css").toExternalForm());
        primaryStage.setTitle(isLocked ? "AddressBook" : "Spyglass");
    }

    private void setFeedbackToActiveResultDisplay(String feedbackToUser) {
        if (currentMode == AppMode.LOCKED) {
            dummyMainPanel.setFeedbackToUser(feedbackToUser);
        } else {
            spyglassMainPanel.setFeedbackToUser(feedbackToUser);
        }
    }

    /**
     * Executes the command and returns the result.
     *
     * @see seedu.address.logic.Logic#execute(String)
     */
    private CommandResult executeCommand(String commandText) throws CommandException, ParseException {
        try {
            CommandResult commandResult = logic.execute(commandText);
            logger.info("Result: " + commandResult.getFeedbackToUser());

            commandResult.getRequestedMode().ifPresent(this::setMode);
            setFeedbackToActiveResultDisplay(commandResult.getFeedbackToUser());

            if (commandResult.isShowHelp()) {
                if (currentMode == AppMode.LOCKED) {
                    dummyMainPanel.handleHelp();
                } else {
                    spyglassMainPanel.handleHelp();
                }
            }

            if (commandResult.isExit()) {
                handleExit();
            }

            return commandResult;
        } catch (CommandException | ParseException e) {
            logger.info("An error occurred while executing command: " + commandText);
            setFeedbackToActiveResultDisplay(e.getMessage());
            throw e;
        }
    }
}
