package seedu.address.model;

import static java.util.Objects.requireNonNull;

/**
 * Single source of truth for the current lock state.
 */
public class AppModeManager {

    private AppMode appMode;

    public AppModeManager(AppMode initialState) {
        appMode = requireNonNull(initialState);
    }

    public AppMode getMode() {
        return appMode;
    }

    public void lock() {
        appMode = AppMode.LOCKED;   
    }

    public void unlock() {
        appMode = AppMode.UNLOCKED;
    }
}
