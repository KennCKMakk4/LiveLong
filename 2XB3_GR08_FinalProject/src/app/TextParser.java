package app;
import java.text.DecimalFormat;
import java.util.List;

import util.GoogleMapsUtils;
import classes.NHPair;

/**
 * @brief TextParser is used for parsing responses from the user in the command line interface
 * and converting them into the correct command for the application to then run.
 * It is also in charge of providing UI, and changing the Application State via user input.
 * @author Kenneth Mak - makk4
 *
 */
public class TextParser {

	private Controller controller;
	private static DecimalFormat df = new DecimalFormat("#.##");

	/**
	 * @brief Constructor with a reference to controller. Responses are parsed into the correct
	 * command for the controller.
	 * @param controller Controller containing the settings and search request methods.
	 */
	public TextParser(Controller controller) {
		this.controller = controller;
	}

	/**
	 * @brief Upon receiving a response, check which state it is and pass the response
	 * to the appropriate location
	 * @param response The phrase the user typed in
	 * @param appState Current state of the application
	 */
	public void receive(String response, State appState) {
		if (appState == State.Menu)
			handleMenuResponse(response);
		if (appState == State.Settings)
			handleSettingsResponse(response);
		if (appState == State.Finished)
			handleFinishedResponse(response);
	}

	/**
	 * @brief Prints the 'main screen' of each state
	 * @param appState Current state of the application
	 */
	public void printScreen(State appState) {
		if (appState == State.Menu)
			printMenu();
		if (appState == State.Settings)
			printSettings();
		if (appState == State.Finished)
			printFinished();
		if (appState == State.Exiting)
			printExiting();
	}

	/**
	 * @brief Prints the menu to the command line, showing current filter settings and commands.
	 */
	private void printMenu() {
		String msg = "\n\nWelcome to LiveLong, the app for helping you choose the correct Nursing Home!\n";
		msg += "Current location: " + controller.getAddress() + "\n";
		msg += "Current filters: " + "\n" + "\tNumber of Results: "
				+ controller.getNumResults() + "\n" + "\tMinimum Rating: "
				+ controller.getMinRating() + "\n" + "\tIs a SFF: "
				+ (controller.isFilterForSFF() ? "Y" : "-") + "\n"
				+ "\tNo Abuse History: "
				+ (controller.isFilterAbuse() ? "Y" : "-") + "\n"
				+ "\tInspected Recently (<2years): "
				+ (controller.isFilterOldSurvey() ? "Y" : "-") + "\n";
		msg += "Type 'Search' to seach with the above filters\n";
		msg += "Type 'Settings' to adjust the above filters\n";
		msg += "Type 'Exit' to stop the program\n";
		System.out.println(msg);
	}

	/**
	 * @brief Handles the response while the application is in the Menu
	 * @param response The command the player sent
	 */
	private void handleMenuResponse(String response) {
		switch (response.toUpperCase()) {
		case "SEARCH":
			LiveLong.setAppState(State.Searching);
			controller.sendSearch();
			break;
		case "SETTINGS":
			LiveLong.setAppState(State.Settings);
			break;
		case "EXIT":
			LiveLong.setAppState(State.Exiting);
			break;
		default:
			System.out.println("Unknown command received");
		}
	}

	/**
	 * @brief Prints the Settings to the command line, showing current filter settings and commands to change them.
	 */
	private void printSettings() {
		String msg = "\n\nLiveLong - Settings\n";
		msg += "Current location: " + controller.getAddress() + "\n";
		msg += "Sorting by: "
				+ (controller.isSortByRank() ? "Ratings" : "Distance") + "\n";
		msg += "Current filters: " + "\n" + "\tNumber of Results: "
				+ controller.getNumResults() + "\n" + "\tMinimum Rating: "
				+ controller.getMinRating() + "\n" + "\tIs a SFF: "
				+ (controller.isFilterForSFF() ? "Y" : "-") + "\n"
				+ "\tFilter Out: History of Abuse?: "
				+ (controller.isFilterAbuse() ? "Y" : "-") + "\n"
				+ "\tFilter Out: > 2 Years since Inspection?: "
				+ (controller.isFilterOldSurvey() ? "Y" : "-") + "\n";
		msg += "Type 'ChangeLocation:x y z' to change your address to 'x y z' \n";
		msg += "Type 'sort' to change result ordering to be by Ratings or Distance\n";
		msg += "Type 'num:x' to change the number of results to show (1-15) \n";
		msg += "Type 'rate:x' to change the minimum rating to show (1-5) \n";
		msg += "Type 'sff' to filter for homes that have Special Focus status \n";
		msg += "Type 'abuse' to filter out homes that had a previous flag of abuse \n";
		msg += "Type 'old' to filter out homes that have not been inspected in over two years \n";

		msg += "Type 'menu' to return to menu";
		System.out.println(msg);
	}


	/**
	 * @brief Handles the response while the application is in the Settings state
	 * @param response The command the player sent
	 */
	private void handleSettingsResponse(String response) {
		String[] phrase = response.toUpperCase().split(":");
		switch (phrase[0]) {
		case "CHANGELOCATION":
			if (phrase.length > 1)
				controller.requestChangeLocation(phrase[1]);
			else
				System.out.println("Could not find address 'x'");
			LiveLong.setAppState(State.Settings);
			break;
		case "SORT":
			controller.setSortByRank(!controller.isSortByRank());
			LiveLong.setAppState(State.Settings);
			break;

		case "NUM":
			try {
				if (phrase.length > 1) {
					int num = Integer.parseInt(phrase[1]);
					num = Math.max(1, Math.min(15, num));
					controller.setNumResults(num);
				}
			} catch (NumberFormatException e) {
				System.out
						.println("Invalid value found when setting number of results");
			}
			LiveLong.setAppState(State.Settings);
			break;

		case "RATE":
			try {
				if (phrase.length > 1) {
					int num = Integer.parseInt(phrase[1]);
					num = Math.max(0, Math.min(5, num));
					controller.setMinRating(num);
				}
			} catch (NumberFormatException e) {
				System.out
						.println("Invalid value found when setting number of minimum rating");
			}
			LiveLong.setAppState(State.Settings);
			break;

		case "SFF":
			controller.setFilterForSFF(!controller.isFilterForSFF());
			LiveLong.setAppState(State.Settings);
			break;

		case "ABUSE":
			controller.setFilterAbuse(!controller.isFilterAbuse());
			LiveLong.setAppState(State.Settings);
			break;

		case "OLD":
			controller.setFilterOldSurvey(!controller.isFilterOldSurvey());
			LiveLong.setAppState(State.Settings);
			break;

		case "MENU":
			LiveLong.setAppState(State.Menu);
			break;

		default:
			System.out.print(" - Unknown command received\n");
			break;
		}
	}

	/**
	 * @brief Prints the search results, and the available commands.
	 * Player can choose to inspect a nursing home for more details, and then type 'open:x' to
	 * view it on GoogleMaps
	 */
	private void printFinished() {
		printResults(controller.getResults());
		if (controller.getResults().size() > 0) {
			System.out
					.println("Type a number corresponding to an above Nursing Home to get more details");
			System.out
					.println("Type 'open:x', where x is a number referring to a home, to open it up in Google Maps");
		}
		System.out.println("Type 'Menu' to return to menu");
	}

	/**
	 * @brief Handles the response while the application is in the Finished state after searching
	 * @param response The command the player sent
	 */
	private void handleFinishedResponse(String response) {
		// "MENU"
		if (response.toUpperCase().equals("MENU")) {
			LiveLong.setAppState(State.Menu);
			return;
		}

		// Number of results
		int size = controller.getResults().size();
		if (size > 0) {
			// opening google maps
			if (response.split(":")[0].toUpperCase().equals("OPEN")) {
				try {
					int num = Integer.parseInt(response.split(":")[1]);
					num = Math.max(1, Math.min(size, num));
					
					// Prompting opening with origin and destination
					GoogleMapsUtils.getInstance().openMaps(
							controller.getLatLong(),
							controller.getResults().get(num).getLeft()
									.getAddress());
				} catch (Exception e) {
					System.out
							.println("Invalid value found when trying to select a home");
				}

			} else {
				// "Number, view details
				try {
					int num = Integer.parseInt(response);
					num = Math.max(1, Math.min(size, num));
					System.out.println((num)
							+ ". =============================");
					printNHDetailed(controller.getResults().get(num - 1));
				} catch (Exception e) {
					System.out
							.println("Invalid value found when trying to examine a home");
				}

			}

		}
		
		// Commands printing again at the end of handling commands 
		if (controller.getResults().size() > 0) {
			System.out
					.println("Type a number corresponding to an above Nursing Home to get more details");
			System.out
					.println("Type 'open:x', where x is a number referring to a home, to open it up in Google Maps");
		}
		System.out.println("Type 'Menu' to return to menu");
	}


	/**
	 * @brief Prints out the list of results to screen
	 * @param results List of NHPair to print
	 */
	private void printResults(List<NHPair> results) {
		for (int i = 0; i < results.size(); i++) {
			NHPair pair = results.get(i);
			System.out.println((i + 1) + ". ============================");
			printNH(pair);
		}
	}

	/**
	 * @brief Prints the result of a single pair of NursingHome and NHGeoInfo
	 * @param result the NHPair to print out
	 */
	private void printNH(NHPair result) {
		System.out.println(result.getLeft());
		System.out.println("Located "
				+ df.format(result.getRight().getDistance())
				+ "km from your location");
		System.out.println("===============================");
	}

	/**
	 * @brief Prints the result of a single pair of NursingHome and NHGeoInfo with more detailed information
	 * @param result the NHPair to print out
	 */
	private void printNHDetailed(NHPair result) {
		System.out.println(result.getLeft().detailed());
		System.out.println("Located "
				+ df.format(result.getRight().getDistance())
				+ "km from your location");
		System.out.println("===============================");
	}


	/**
	 * @brief Prints the Exiting screen to the command line
	 */
	private void printExiting() {
		System.out.println("Exiting...");
	}

}
