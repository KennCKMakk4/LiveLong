package classes;

/**
 * @brief Superclass of FireSafetyDeficiency and HealthDeficiency. Stores information about Deficiency 
 * that was read from the corresponding Deficiency csv.
 * @author Kenneth Mak - makk4
 *
 */
public class Deficiency {
	// id of the nursing home this deficiency corresponds to
	protected final String id;

	protected final String defStartDate;

	protected final String defCategory;
	protected final String defDescription;

	// corrected yet?
	protected final String defStatus;
	protected final String defStatusDate;

	protected final String dateUpdated;

	/**
	 * @brief Constructor when reading from a raw file type with unused columns or empty values
	 * @param s The row from CSV to read from
	 * @param x The character to insert to replace empty values
	 */
	public Deficiency(String[] s, String x) {
		if (s[0].length() < 6) s[0] = "0" + s[0];
		this.id = s[0];

		this.defStartDate = s[6];
		this.defCategory = s[9];
		
		// Accounting for erroneous empty column in data
		int i = (s.length > 19) ? 1 : 0;
		this.defDescription = s[11+i];

		this.defStatus = s[13+i];
		this.defStatusDate = s[14+i];

		this.dateUpdated = s[18+i];
	}

	/**
	 * @brief Constructor when reading from compressed (Saved) data files containing only
	 * the essential information
	 * @param s Row from compressed CSV to read from
	 */
	public Deficiency(String[] s) {
		if (s[0].length() < 6) s[0] = "0" + s[0];
		this.id = s[0];

		this.defStartDate = s[1];
		this.defCategory = s[2];
		this.defDescription = s[3];

		this.defStatus = s[4];
		this.defStatusDate = s[5];

		this.dateUpdated = s[6];
	}

	public String toString() {
		String n = id + " - : " + defCategory + " - " + defDescription + "\n"
				+ "\t\tStarted: " + defStartDate + "\n\t\t" + "Status: " + defStatus;
		if (defStatus.equals("Deficient, Provider has plan of correction")
				|| defStatus
						.equals("Deficient, Provider has date of correction")
				|| defStatus.equals("Waiver has been granted"))
			n += "\t" + defStatusDate;
		return n;
	}

	/**
	 * @brief Checks if this deficiency has been resolved or not
	 * @return Returns true if this deficiency has not been resolved yet
	 */
	public boolean isActive() {
		return !(defStatus.equals("Deficient, Provider has date of correction")
				|| defStatus.equals("Waiver has been granted") || defStatus
					.equals("Fire Safety Evaluation Survey"));
	}

	/**
	 * @brief Returns the name of the variables. Used when writing this object into a CSV file
	 * @return The Header row needed for writing CSV files
	 */
	public static String[] getHeader() {
		return new String[] { "ID", "Start_Date", "Category", "Description",
				"Status", "Status Date", "Date Updated" };
	}

	/**
	 * @brief Returns the values in the variables. Used for writing this object into a CSV file
	 * @return Returns the values stored in each variable
	 */
	public String[] toCSV() {
		return new String[] { id,

		defStartDate, defCategory, defDescription,

		defStatus, defStatusDate,

		dateUpdated

		};
	}
	
	public String getId() {
		return id;
	}

	public String getDefStartDate() {
		return defStartDate;
	}

	public String getDefCategory() {
		return defCategory;
	}

	public String getDefDescription() {
		return defDescription;
	}

	public String getDefStatus() {
		return defStatus;
	}

	public String getDefStatusDate() {
		return defStatusDate;
	}

	public String getDateUpdated() {
		return dateUpdated;
	}
}
