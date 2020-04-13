package app;
import data.Database;

import java.util.Scanner;

/**
 * @brief LiveLong class containing Application Loop and initialization.
 * User input is received here
 * @author Kenneth Mak - makk4
 *
 */
public class LiveLong {

	private static boolean bIsRunning;
	private static State appState;
	
	
	// parses user response and parses them into commands for controller
	// also in charge of UI
	private static TextParser parser;
	
	public static void main(String args[]) {
		
		Scanner scanner = new Scanner(System.in);
		
		parser = new TextParser(new Controller(new Database()));
		
		
		String response;
		bIsRunning = true;
		
		setAppState(State.Menu);
		// app loop. Stops when state is set to Exiting by TextParser
		while(bIsRunning) {
			response = scanner.nextLine();
			
			// send response to TextParser to handle
			parser.receive(response, appState);
		}
		
		// app exiting
		scanner.close();
	}
	
	/**
	 * @brief Accessor method to current state of the application
	 * @return State of the application
	 */
	public static State getAppState() { return appState; }
	
	/**
	 * @brief Setter method to change the state of an application. 
	 * Prompts TextParser to print the new screen upon change.
	 * @param newState The State to change to
	 */
	public static void setAppState(State newState) { 
		appState = newState; 
		parser.printScreen(newState);
		
		if (newState == State.Exiting)
			bIsRunning = false;
	}

}
