package app;
/**
 * @brief State enum pertaining to the current status of the application
 * @author kennm
 *
 */
public enum State {
	Menu,  			// In Main menu
	Settings,		// In Settings screen
	Searching,		// Currently running a search
	Finished,		// Finished finding results, displaying
	Exiting			// Stop application run loop
}
