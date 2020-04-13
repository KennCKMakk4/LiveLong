package classes;

/**
 * @brief Used to combine a NursingHome object with a corresponding NHGeoInfo object.
 * Allows for comparing NursingHomes by ratings but also keeping its corresponding NHGeoInfo object with it
 * @author Kenneth Mak - makk4
 *
 */
public class NHPair implements Comparable<NHPair> {

	private final NursingHome nh;
	private final NHGeoInfo nhg;
	
	public NHPair(NursingHome nh, NHGeoInfo nhg) {
		this.nh = nh;
		this.nhg = nhg;
	}
	
	public NursingHome getLeft() { return nh; }
	
	public NHGeoInfo getRight() { return nhg; }

	// Sort by NursingHome rank, while keeping corresponding NHGeoInfo with it
	@Override
	public int compareTo(NHPair o) {
		return (this.nh.compareTo(o.nh));
	}
	
	
	
}
