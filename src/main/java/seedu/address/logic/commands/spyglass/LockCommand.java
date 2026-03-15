package seedu.address.logic.commands.spyglass;

import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.model.AppMode;
import seedu.address.model.Model;

/**
 * Switches the app to the Locked interface.
 */
public class LockCommand extends Command {

    public static final String COMMAND_WORD = "lock";
    public static final String MESSAGE_SUCCESS = "Switched to Locked Interface.";

    @Override
    public CommandResult execute(Model model) {
        return new CommandResult(MESSAGE_SUCCESS, false, false, AppMode.LOCKED);
    }
}
