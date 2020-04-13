package classes;
import java.util.LinkedList;

/**
 * @brief NursingHome object stores information pertaining to a row in the CSV file. Also allows additions of
 * a deficiency that correspond with this home.
 * @author Kenneth Mak - makk4
 *
 */
public class NursingHome implements Comparable<NursingHome> {

	private final String id;
	private final String name;

	// Address
	private final String strAddress;
	private final String strCity;
	private final String strState;
	private final String strZIP;
	private final String strCounty;

	private final String strPhone;

	// Date First Approved to Provide Medicare and Medicaid services
	private final String dateStart;

	private final String SFFStatus;
	private final boolean bAbuse;
	// last health inspection > 2 yrs
	private final boolean bOldSurvey;

	private final String ratingOverall;
	private final String ratingHealthInspection;
	private final String ratingQuality;
	// long-stay vs short-stay
	private final String ratingLSQuality;
	private final String ratingSSQuality;
	private final String ratingStaffing;
	private final String ratingRNStaffing;

	private final int pastIncidents;
	private final int pastComplaints;
	private final int totalPenalties;

	private final String dateUpdated;

	private final LinkedList<Deficiency> defList;

	/**
	 * @brief Constructor when reading from a raw file type with unused columns or empty values
	 * @param s The row from CSV to read from
	 * @param x The character to insert to replace empty values
	 */
	public NursingHome(String[] phrase, String x) {
		// id, name
		if (phrase[0].length() < 6) phrase[0] = "0" + phrase[0];
		this.id = phrase[0];
		this.name = phrase[1];

		// address, city, state, zip, phone, county name
		this.strAddress = phrase[2];
		this.strCity = phrase[3];
		this.strState = phrase[4];
		this.strZIP = phrase[5];
		this.strPhone = phrase[6];
		this.strCounty = phrase[8];

		this.dateStart = phrase[15];

		this.SFFStatus = phrase[17];
		this.bAbuse = (phrase[18].equals("Y"));
		this.bOldSurvey = (phrase[19].equals("Y"));

		this.ratingOverall = phrase[23].isEmpty() ? x : phrase[23];
		this.ratingHealthInspection = phrase[25].isEmpty() ? x : phrase[25];
		this.ratingQuality = phrase[27].isEmpty() ? x : phrase[27];
		this.ratingLSQuality = phrase[29].isEmpty() ? x : phrase[29];
		this.ratingSSQuality = phrase[31].isEmpty() ? x : phrase[31];
		this.ratingStaffing = phrase[33].isEmpty() ? x : phrase[33];
		this.ratingRNStaffing = phrase[35].isEmpty() ? x : phrase[35];

		this.pastIncidents = Integer.parseInt(phrase[78]);
		this.pastComplaints = Integer.parseInt(phrase[79]);
		this.totalPenalties = Integer.parseInt(phrase[83]);

		this.dateUpdated = phrase[84];

		this.defList = new LinkedList<Deficiency>();
	}

	/**
	 * @brief Constructor when reading from compressed (Saved) data files containing only
	 * the essential information
	 * @param s Row from compressed CSV to read from
	 */
	public NursingHome(String[] phrase) {
		this.id = phrase[0];
		this.name = phrase[1];

		// address, city, state, zip, phone, county name
		this.strAddress = phrase[2];
		this.strCity = phrase[3];
		this.strState = phrase[4];
		this.strZIP = phrase[5];
		this.strPhone = phrase[6];
		this.strCounty = phrase[7];

		this.dateStart = phrase[8];

		this.SFFStatus = phrase[9];
		this.bAbuse = (phrase[10].equals("Y"));
		this.bOldSurvey = (phrase[11].equals("Y"));

		this.ratingOverall = phrase[12];
		this.ratingHealthInspection = phrase[13];
		this.ratingQuality = phrase[14];
		this.ratingLSQuality = phrase[15];
		this.ratingSSQuality = phrase[16];
		this.ratingStaffing = phrase[17];
		this.ratingRNStaffing = phrase[18];

		this.pastIncidents = Integer.parseInt(phrase[19]);
		this.pastComplaints = Integer.parseInt(phrase[20]);
		this.totalPenalties = Integer.parseInt(phrase[21]);

		this.dateUpdated = phrase[22];

		this.defList = new LinkedList<Deficiency>();
	}

	public String getId() {
		return this.id;
	}

	/**
	 * @brief Returns the address of the Nursing Home, combining the street adresss, city, state, and zip
	 * @return Address of this Nursing Home
	 */
	public String getAddress() {
		return this.strAddress + ", " + this.strCity + ", " + this.strState + ", " + this.strZIP;
	}

	/**
	 * @brief Adds a Deficiency to this NursingHome
	 * @param def The deficiency to add
	 */
	public void addDef(Deficiency def) {
		this.defList.add(def);
	}
	
	/**
	 * @brief Counts the number of active deficiencies on the Nursing Home
	 * @return Number of Active Deficiencies
	 */
	public int getActiveDefCount() {
		int defs = 0;
		for (Deficiency def : defList)
			if (def.isActive()) defs += 1;
		return defs;
	}
	
	/**
	 * @brief Method returning the overall rating in integer format.
	 * @return Overall Rating of Nursing Home
	 */
	public int getOverallRating() {
		try {
			int num = Integer.parseInt(ratingOverall);
			return num;
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	public String getSFF() {
		return this.SFFStatus;
	}
	
	public boolean getAbuse() {
		return this.bAbuse;
	}

	public boolean getOldSurvey() {
		return this.bOldSurvey;
	}

	/**
	 * @brief Used for printing out a simplified version of the NursingHome object
	 */
	public String toString() {
		String n = id + " - " + name + " - ";

		n += strAddress + ", " + strCity + ", " + strState + "\n" 
			+ "Phone #: "+ strPhone + "\n";
		if (!SFFStatus.isEmpty())
			n += "Special Focus Status: '" + SFFStatus + "'\n";

		if (bAbuse)
			n += "!!>> Warning: History of Abuse\n";
		if (bOldSurvey)
			n += "!!>> Warning: Last health inspection was over 2 years\n";

		n += "Overall Rating: " + ratingOverall + "/5\n";
		
		if (getActiveDefCount() > 0)
			n += "Active Deficiencies: " + getActiveDefCount() + "\n";
		
		if (pastIncidents+pastComplaints+totalPenalties > 0)
			n += "Past Incidents/Complaints/Penalties: " + 
					(pastIncidents + pastComplaints + totalPenalties) + "\n";

		n += "Date First Approved: \t" + dateStart;
		return n;
	}

	/**
	 * @brief Used for printing out a more detailed version of the NursingHome object
	 */
	public String detailed() {
		String n = id + " - " + name + "\n";

		n += strAddress + ", " + strCity + ", " + strState + "\n" 
			+ "Phone #: "+ strPhone + "\n";
		if (!SFFStatus.isEmpty())
			n += "Special Focus Status: '" + SFFStatus + "'\n";
	
		if (bAbuse)
			n += "!!>> Warning: History of Abuse\n";
		if (bOldSurvey)
			n += "!!>> Warning: Last health inspection was over 2 years\n";
	
		n += "Overall Rating: " + ratingOverall + "/5\n";
		if(SFFStatus.equals("SFF"))
			n += "\tNote: SFF homes ratings are not relevant to US Datasets\n";
		if (!ratingOverall.equals("?")) {
			n += "\tHealth Inspection Rating: " + ratingHealthInspection + "\n";
			n += "\tOverall Quality Rating: " + ratingQuality + "\n";
			n += "\tLong-Stay Quality Rating: " + ratingLSQuality + "\n";
			n += "\tShort-Stay Quality Rating: " + ratingSSQuality + "\n";
			n += "\tStaffing Rating: " + ratingStaffing + "\n";
			n += "\tRN Staffing Rating: " + ratingRNStaffing + "\n";
		}
	
		if (getActiveDefCount() > 0){
			n += "Active Deficiencies: " + getActiveDefCount() +"\n";
			for (Deficiency def : defList)
				if (def.isActive()) n += "\t" + def + "\n";
		}
		
		if (pastIncidents > 0)
			n += "Past Incidents: " + pastIncidents + "\n";
		if (pastComplaints > 0)
			n += "Past Complaints: " + pastComplaints + "\n";
		if (totalPenalties > 0)
			n += "Total Penalties: " + totalPenalties + "\n";
		n += "Date First Approved: \t" + dateStart + "\n"
				+ "Data Last Updated on: \t" + dateUpdated;
		return n;
	}

	/**
	 * @brief Returns the name of the variables. Used when writing this object into a CSV file
	 * @return The Header row needed for writing CSV files
	 */
	public static String[] getHeader() {
		return new String[] { "ID", "Name", "Address", "City",
				"State", "ZIP", "Phone", "County",
				"Date Started", "Special Focus Status", "Abuse", "Old Survey",
				"Overall Rating", "Health Inspection Rating", "Quality Rating", "Long-Stay Quality Rating",
				"Short-Stay Quality Rating", "Staff Rating", "RN Staff Rating",
				"Past Incidents", "Past Complaints", "Total Penalties", "Date Updated"
				};
	}
	/**
	 * @brief Returns the values in the variables. Used for writing this object into a CSV file
	 * @return Returns the values stored in each variable
	 */
	public String[] toCSV() {
		return new String[] { id, name, strAddress, strCity, strState, strZIP,
				strPhone, strCounty, dateStart, SFFStatus, bAbuse ? "Y" : "N",
				bOldSurvey ? "Y" : "N", ratingOverall, ratingHealthInspection,
				ratingQuality, ratingLSQuality, ratingSSQuality,
				ratingStaffing, ratingRNStaffing,
				Integer.toString(pastIncidents),
				Integer.toString(pastComplaints),
				Integer.toString(totalPenalties), dateUpdated };
	}

	// Sort by overall rating, then by number of active deficiencies
	@Override
	public int compareTo(NursingHome o) {
		int cmp = this.ratingOverall.compareTo(o.ratingOverall);
		
		if (cmp == 0) {
			int numDef1 = this.getActiveDefCount();
			int numDef2 = o.getActiveDefCount();
			
			if (numDef1 < numDef2) return 1;
			if (numDef1 > numDef2) return -1;
			return 0;
		} else {
			return -cmp;
		}
	}
	
	
	
}
