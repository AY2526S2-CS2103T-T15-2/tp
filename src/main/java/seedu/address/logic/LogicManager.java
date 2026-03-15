package seedu.address.logic;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Path;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.Parser;
import seedu.address.logic.parser.ParserManager;
import seedu.address.logic.parser.dummy.DummyCommandParser;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.logic.parser.spyglass.SpyglassParser;
import seedu.address.model.AppMode;
import seedu.address.model.AppModeManager;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Person;
import seedu.address.storage.Storage;

/**
 * The main LogicManager of the app.
 */
public class LogicManager implements Logic {
    public static final String FILE_OPS_ERROR_FORMAT = "Could not save data due to the following error: %s";

    public static final String FILE_OPS_PERMISSION_ERROR_FORMAT =
            "Could not save data to file %s due to insufficient permissions to write to the file or the folder.";

    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    private final Model model;
    private final Storage storage;
    private final AppModeManager modeManager;
    private final Parser parserManager;

    /**
     * Constructs a {@code LogicManager} with the given {@code Model} and {@code Storage}.
     * The {@code ModeManager} will be initialized to the default locked mode.
     */
    public LogicManager(Model model, Storage storage) {
        this(model, storage, new AppModeManager(AppMode.LOCKED));
    }

    /**
     * Constructs a {@code LogicManager} with an explicit runtime mode manager.
     */
    public LogicManager(Model model, Storage storage, AppModeManager modeManager) {
        this.model = model;
        this.storage = storage;
        this.modeManager = modeManager;
        this.parserManager = new ParserManager(modeManager, new SpyglassParser(), new DummyCommandParser());
    }

    @Override
    public CommandResult execute(String commandText) throws CommandException, ParseException {
        logger.info("----------------[USER COMMAND][" + commandText + "]");

        CommandResult commandResult;
        Command command = parserManager.parseCommand(commandText);
        commandResult = command.execute(model);

        commandResult.getRequestedMode().ifPresent(requestedMode -> {
            if (requestedMode == AppMode.LOCKED) {
                modeManager.lock();
            } else {
                modeManager.unlock();
            }
        });

        try {
            storage.saveAddressBook(model.getAddressBook());
        } catch (AccessDeniedException e) {
            throw new CommandException(String.format(FILE_OPS_PERMISSION_ERROR_FORMAT, e.getMessage()), e);
        } catch (IOException ioe) {
            throw new CommandException(String.format(FILE_OPS_ERROR_FORMAT, ioe.getMessage()), ioe);
        }

        return commandResult;
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return model.getAddressBook();
    }

    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return model.getFilteredPersonList();
    }

    @Override
    public Path getAddressBookFilePath() {
        return model.getAddressBookFilePath();
    }

    @Override
    public GuiSettings getGuiSettings() {
        return model.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        model.setGuiSettings(guiSettings);
    }
}
