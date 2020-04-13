package classes;

/**
 * @brief FireSafetyDeficiency containing a row of information read from the Fire Safety Deficiency data files
 * @author Kenneth Mak - makk4
 *
 */
public class FireSafetyDeficiency extends Deficiency {

	public FireSafetyDeficiency(String[] s, String x){
		super(s, x);
	}
	
	public FireSafetyDeficiency(String[] s){
		super(s);
	}


	public String toString() {
		String n = "Fire Safety Deficiency: " + defCategory + " - " + defDescription + "\n"
				+ "\t\tStarted: " + defStartDate + "\n\t\t" + "Status: " + defStatus;
		if (defStatus.equals("Deficient, Provider has plan of correction")
				|| defStatus
						.equals("Deficient, Provider has date of correction")
				|| defStatus.equals("Waiver has been granted"))
			n += "\t" + defStatusDate;
		return n;
	}
}
