package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;

import seedu.address.logic.commands.Command;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.AppMode;
import seedu.address.model.AppModeManager;

/**
 * Routes parsing to the appropriate parser path based on lock state.
 */
public class ParserManager implements Parser {

    private final AppModeManager modeManager;
    private final Parser spyglassParser;
    private final Parser dummyParser;

    public ParserManager(AppModeManager modeManager,
                           Parser spyglassParser,
                           Parser dummyParser) {
        this.modeManager = requireNonNull(modeManager);
        this.spyglassParser = requireNonNull(spyglassParser);
        this.dummyParser = requireNonNull(dummyParser);
    }

    @Override
    public Command parseCommand(String userInput) throws ParseException {
        if (modeManager.getMode() == AppMode.UNLOCKED) {
            return spyglassParser.parseCommand(userInput);
        }
        return dummyParser.parseCommand(userInput);
    }
}
