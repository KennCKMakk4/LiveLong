package data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.StopWatch;

import classes.*;
import util.QuickSort;
import util.OpenStreetMapUtils;
import util.SeparateChainingHashST;

/**
 * @brief Database class is where the data is read from the CSV files and then placed into
 * the correct data structure. It also stores the user's current location, which is then used for
 * calculating the distances from NursingHomes in the database.
 * 
 * Includes functions to return a list of results based on a search filter, and a setter method
 * to change the location to some place else.
 * @author Kenneth Mak - makk4
 *
 */
public class Database {

	// Initial starting location
	private String address = "McMaster University, Hamilton, Ontario";
	private double latitude = 43.260888;
	private double longitude = -79.919276;

	//Hash ST with separate chaining to efficiently store and get nursing homes
	private final SeparateChainingHashST<String, NursingHome> stNH;
	// ArrayList read from CSV, using QuickSort to sort by distance
	private List<NHGeoInfo> nhGeo;

	/**
	 * @brief Reads and initializes data from the CSV files. Assumes the address is McMaster University
	 */
	public Database() {
		this("McMaster University, Hamilton, Ontario");
	}
	
	/**
	 * @brief Reads and initializes data from the CSV files based on the given address
	 * @param address The user's location
	 */
	public Database(String address) {
		stNH = new SeparateChainingHashST<String, NursingHome>(15569);
		// Reading data
		
		// Imported from external library commons-lang3-3.9.jar
		StopWatch sw = new StopWatch();
		sw.start();
		System.out.println("Starting - Initializing Database from Files: " + sw);
		
		
		for (NursingHome nh : DataReader.getNursingHomeInfo())
			stNH.put(nh.getId(), nh);
		System.out.println("Finished reading NursingHome info: " + sw);

		nhGeo = DataReader.getNHGeoInfo();
		if (!this.address.equals(address)) ChangeLocation(address);
		else updateDistances();
		System.out.println("Finished reading NursingHome Geo info: " + sw);
		
		for (Deficiency def : DataReader.getHealthInfo())
			stNH.get(def.getId()).addDef(def);
		System.out.println("Finished reading Health Deficiency: " + sw);

		for (Deficiency def : DataReader.getFireSafetyInfo())
			stNH.get(def.getId()).addDef(def);
		System.out.println("Finished reading Fire Safety Deficiency: " + sw);


		// If this is the same address, don't send a geocode request

		System.out.print("Finished initialization: " + sw + "\n"
						+ "Database is ready. Starting in " + address);
	}

	/**
	 * @brief Changes the user's location to the given address. Affects the results given
	 * as they are based off of distance between the user and a Nursing Home.
	 * @param address The address to change to
	 */
	public void ChangeLocation(String address) {
		System.out.println("Changing locations to: " + address);
		OpenStreetMapUtils geo = OpenStreetMapUtils.getInstance();
		Map<String, Double> map = geo.getCoordinates(address);

		if (map == null) {
			System.out.println("Failed to change locations.");
			return;
		}
		this.address = address;
		this.latitude = map.get("lat");
		this.longitude = map.get("lon");
		System.out.println(latitude + ", " + longitude);
		updateDistances();
	}

	/**
	 * @brief Prompts all NHGeoInfo objects to recalculate their distance values based on the given 
	 * latitude and longitude. Then resorts 
	 */
	private void updateDistances() {
		for (NHGeoInfo nhg : nhGeo)
			nhg.calculateDistance(this.latitude, this.longitude);
		QuickSort.sort(nhGeo);
	}

	/**
	 * @brief Given a search settings, returns a list of NHPair consisting of a NursingHome and
	 * its corresponding NHGeoInfo. Ordered by closest distance to user first. If 
	 * sortByRank parameter is true, it will first order by ratings, then by distance.
	 * @param number The number of results to return
	 * @param minRating The minimum rating to select. Disregarded if the Nursing Home is a SFF
	 * @param isSFF True to select Nursing Homes that are Special Focus or are a SFF Candidate
	 * @param flagAbuse True to filter out Nursing Homes with cases of Abuse
	 * @param flagOldSurvey True to filter out Nursing Homes that have not been inspected in over two years
	 * @param sortByRank True if list is ordered by Nursing Home overall rating
	 * @return
	 */
	public List<NHPair> search(int number,
			double minRating, boolean isSFF, boolean flagAbuse,
			boolean flagOldSurvey, boolean sortByRank) {
		// Sorts GeoInformation from lowest distance to highest distance

		List<NHPair> result = new ArrayList<NHPair>();
		
		// Starting from min distance (i=0)
		for (NHGeoInfo nhg : nhGeo) {
			// if we reached number of results already
			if (result.size() >= number) break;
			
			// access nh corresponding to this geo information
			NursingHome nh = stNH.get(nhg.getId());
			// filter 
			if (minRating > nh.getOverallRating() && !nh.getSFF().equals("SFF"))
				continue;
			if (isSFF && nh.getSFF().isEmpty() 
					|| flagAbuse && nh.getAbuse()
					|| flagOldSurvey && nh.getOldSurvey())
				continue;
			
			// Create a pair of the two corresponding information
			NHPair pair = new NHPair(nh, nhg);
			result.add(pair);
		}
		System.out.println("Returning : " + result.size());
		if (sortByRank) QuickSort.sort(result);
		
		return result;
	}

	/**
	 * @brief Accessor to the current address corresponding to the latitude and longitude
	 * @return Address of current location
	 */
	public String getAddress() {
		return this.address;
	}
	
	/**
	 * @brief Accessor method to latitude being used
	 * @return Latitude of current location
	 */
	public double getLatitude() {
		return this.latitude;
	}

	/**
	 * @brief Accessor method to longitude being used
	 * @return Longitude of current location
	 */
	public double getLongitude() {
		return this.longitude;
	}

}
