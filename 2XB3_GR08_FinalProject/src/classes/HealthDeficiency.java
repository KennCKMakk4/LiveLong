package classes;


/**
 * @brief HealthDeficiency containing a row of information read from the Health Deficiency data files
 * @author Kenneth Mak - makk4
 *
 */
public class HealthDeficiency extends Deficiency {

	
	public HealthDeficiency(String[] s, String x){
		super(s, x);
	}

	
	public HealthDeficiency(String[] s){
		super(s);
	}

	public String toString() {
		String n = "Health Deficiency: " + defCategory + " - " + defDescription + "\n"
				+ "\t\tStarted: " + defStartDate + "\n\t\t" + "Status: " + defStatus;
		if (defStatus.equals("Deficient, Provider has plan of correction")
				|| defStatus
						.equals("Deficient, Provider has date of correction")
				|| defStatus.equals("Waiver has been granted"))
			n += "\t" + defStatusDate;
		return n;
	}

}
