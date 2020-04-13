package classes;

/**
 * @brief NHGeoInfo contains the longitude and latitude value corresponding to a Nursing Home.
 * The ID of this NHGeoInfo should correspond with a NursingHome object.
 * Distance value is calculated with its lat/lon values with some other lat/lon value.
 * @author Kenneth Mak - makk4
 *
 */
public class NHGeoInfo implements Comparable<NHGeoInfo> {

	private final String id;
	private final double latitude;
	private final double longitude;
	private double distance;

	/**
	 * @brief Constructor using a NursingHome as a parameter. Calculates the lat/lon
	 * values with the address from the NursingHome using geocoding from OpenStreetMap.com
	 * Note: Currently disabled to prevent batch geocoding
	 * @param nh NursingHome to calculate NHGeoInfo for
	 */
	public NHGeoInfo(NursingHome nh) {
		this.id = nh.getId();

		// Requesting longitude and latitude from OpenStreetMapUtils
		// Commented out to prevent accidental batch geocoding of 15k records
		// Would crash the online api and flag my account
		/*Map<String, Double> vals = OpenStreetMapUtils.getInstance()
				.getCoordinates(nh.getAddress());
		this.latitude = vals.get("lat");
		this.longitude = vals.get("lon");*/
		
		this.latitude = 0;
		this.longitude = 0;
		this.distance = 0;
	}

	/**
	 * @brief Constructor by reading an array of String (I.e. from CSV)
	 * @param s String of values to use
	 */
	public NHGeoInfo(String[] s) {
		if (s[0].length() < 6) s[0] = "0" + s[0];
		this.id = s[0];
		
		this.latitude = Double.parseDouble(s[1]);
		this.longitude = Double.parseDouble(s[2]);
		
		if (s[3].isEmpty()) this.distance = 0;
		else this.distance = Double.parseDouble(s[3]);
	}

	//https://www.movable-type.co.uk/scripts/latlong.html
	/**
	 * @brief Calculates the distance from this Nursing Home to a given location
	 * @param latitude2 Latitude of target location
	 * @param longitude2 Longitude of target location
	 */
	public void calculateDistance(double latitude2, double longitude2) {
		double radius = 6371.00;
		
		double lat1 = Math.toRadians(this.latitude);
		double lat2 = Math.toRadians(latitude2);
		
		double deltaLat = Math.toRadians(latitude2 - this.latitude);
		double deltaLon = Math.toRadians(longitude2 - this.longitude);
		
		
		double a = Math.sin(deltaLat/2) * Math.sin(deltaLat/2) +
					Math.cos(lat1) * Math.cos(lat2) * 
					Math.sin(deltaLon/2) * Math.sin(deltaLon/2);
		
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
		
		this.distance = (radius * c);
	}

	public String getId() {
		return this.id;
	}

	public double getLongitude() {
		return longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getDistance() {
		return distance;
	}

	public String toString() {
		return id + ": <" + latitude + ", " + longitude + ">, dist = "
				+ Double.toString(distance) + "km";
	}

	/**
	 * @brief Returns the name of the variables. Used when writing this object into a CSV file
	 * @return The Header row needed for writing CSV files
	 */
	public static String[] getHeader() {
		return new String[] { "ID", "Latitude", "Longitude", "Distance"};
	}

	/**
	 * @brief Returns the values in the variables. Used for writing this object into a CSV file
	 * @return Returns the values stored in each variable
	 */
	public String[] toCSV() {
		return new String[] { id, Double.toString(latitude),
				Double.toString(longitude), Double.toString(distance) };
	}

	@Override
	public int compareTo(NHGeoInfo o) {
		if (this.distance < o.distance) return -1;
		if (this.distance > o.distance) return 1;
		return 0;
	}

}
