package seedu.address.ui.spyglass;

import java.nio.file.Path;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import seedu.address.model.person.Person;
import seedu.address.ui.UiPart;

/**
 * The main panel for spyglass mode, containing all inner UI components.
 */
public class SpyglassMainPanel extends UiPart<Region> {

    private static final String FXML = "spyglass/MainPanel.fxml";

    // Independent Ui parts residing in this Ui container
    private final CommandBox commandBox;
    private final ResultDisplay resultDisplay;
    private final PersonListPanel personListPanel;
    private final SpyglassHelpWindow helpWindow;
    private final Runnable exitAction;

    @FXML
    private MenuItem helpMenuItem;

    @FXML
    private StackPane commandBoxPlaceholder;
    @FXML
    private StackPane resultDisplayPlaceholder;
    @FXML
    private StackPane personListPanelPlaceholder;
    @FXML
    private StackPane summaryPlaceholder; // TODO: Currently unused, reserved for future use

    /**
     * Creates a {@code MainPanel} for spyglass mode.
     */
    public SpyglassMainPanel(ObservableList<Person> personList, Path filePath,
                             CommandBox.CommandExecutor executor, Runnable exitAction) {
        super(FXML);
        this.exitAction = exitAction;
        commandBox = new CommandBox(executor);
        resultDisplay = new ResultDisplay();
        personListPanel = new PersonListPanel(personList);
        helpWindow = new SpyglassHelpWindow();

        commandBoxPlaceholder.getChildren().add(commandBox.getRoot());
        resultDisplayPlaceholder.getChildren().add(resultDisplay.getRoot());
        personListPanelPlaceholder.getChildren().add(personListPanel.getRoot());

        setAccelerators();
    }

    public void setFeedbackToUser(String feedbackToUser) {
        resultDisplay.setFeedbackToUser(feedbackToUser);
    }

    public void hideHelpWindow() {
        helpWindow.hide();
    }

    @FXML
    public void handleHelp() {
        if (!helpWindow.isShowing()) {
            helpWindow.show();
        } else {
            helpWindow.focus();
        }
    }

    @FXML
    private void handleExit() {
        exitAction.run();
    }

    private void setAccelerators() {
        setAccelerator(helpMenuItem, KeyCombination.valueOf("F1"));
    }

    private void setAccelerator(MenuItem menuItem, KeyCombination keyCombination) {
        menuItem.setAccelerator(keyCombination);

        /*
         * TODO: the code below can be removed once the bug reported here
         * https://bugs.openjdk.java.net/browse/JDK-8131666
         * is fixed in later version of SDK.
         *
         * According to the bug report, TextInputControl (TextField, TextArea) will
         * consume function-key events. Because CommandBox contains a TextField, and
         * ResultDisplay contains a TextArea, thus some accelerators (e.g F1) will
         * not work when the focus is in them because the key event is consumed by
         * the TextInputControl(s).
         *
         * For now, we add following event filter to capture such key events and open
         * help window purposely so to support accelerators even when focus is
         * in CommandBox or ResultDisplay.
         */
        getRoot().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getTarget() instanceof TextInputControl && keyCombination.match(event)) {
                menuItem.getOnAction().handle(new ActionEvent());
                event.consume();
            }
        });
    }
}
