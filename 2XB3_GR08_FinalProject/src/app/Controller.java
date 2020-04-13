package app;
import java.util.List;

import classes.NHPair;
import data.Database;

/**
 * @brief Controller class is used to request results from the Database class. 
 * It contains the Search settings, as well as methods to request changes or results from the Database
 * @author kennm
 *
 */
public class Controller {

	private final Database db;
	private List<NHPair> results;

	private int numResults = 5;
	private int minRating = 0;
	private boolean filterForSFF = false;
	private boolean filterAbuse = true;
	private boolean filterOldSurvey = true;
	private boolean sortByRank = false;

	/**
	 * @brief Constructor with reference to the Database to retrieve information from
	 * @param db The Database object with information
	 */
	public Controller(Database db) {
		this.db = db;
	}
	
	/**
	 * @brief Requests a List of NHPairs fulfilling the search criteria from the Database
	 */
	public void sendSearch() {
		results = db.search(numResults, minRating, filterForSFF, filterAbuse, filterOldSurvey, sortByRank);
		if (LiveLong.getAppState() == State.Searching)
			LiveLong.setAppState(State.Finished);
	}
	
	/**
	 * @brief Accessor method to the results found
	 * @return List of NHPair corresponding to search results
	 */
	public List<NHPair> getResults() {
		return results;
	}
	
	
	/**
	 * @brief Requests the database to change to the new address
	 * @param address The location to change to
	 */
	public void requestChangeLocation(String address) {
		db.ChangeLocation(address);
	}

	/**
	 * @brief Accessor method to the address stored in the database
	 * @return Current location of the user
	 */
	public String getAddress() { return db.getAddress(); }
	public String getLatLong() { return db.getLatitude() + ", " + db.getLongitude(); }
	
	public void setNumResults(int numResults) { this.numResults = numResults; }
	public int getNumResults() { return this.numResults; }

	
	public void setMinRating(int minRating) { this.minRating = minRating; }
	public int getMinRating() { return this.minRating; }


	public void setFilterForSFF(boolean b) { this.filterForSFF = b; }
	public boolean isFilterForSFF() { return this.filterForSFF; }

	public void setFilterAbuse(boolean b) { this.filterAbuse = b; }
	public boolean isFilterAbuse() { return this.filterAbuse; }


	public void setFilterOldSurvey(boolean b) { this.filterOldSurvey = b; }
	public boolean isFilterOldSurvey() { return this.filterOldSurvey; }

	public void setSortByRank(boolean b) { this.sortByRank = b; }
	public boolean isSortByRank() { return this.sortByRank; }
	

}
