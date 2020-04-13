package data;

import classes.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.opencsv.CSVReaderHeaderAware;
import com.opencsv.exceptions.CsvException;


/**
 * @brief Used for reading CSV files, constructing the relevant object from the information, and returning
 * a list of objects corresponding to the information given.
 * @author Kenneth Mak - makk4
 *
 */
public class DataReader {

	// Raw files (Uncompressed files with unnecessary information)
	public static final String fileProviderInfo = "Data/Raw/ProviderInfo_Download.csv";
	public static final String fileProviderGeoInfo = "Data/Raw/ProviderGeoInfo.csv";
	public static final String fileHealthDeficiencies = "Data/Raw/HealthDeficiencies_Download.csv";
	public static final String fileFireSafetyDeficiencies = "Data/Raw/FireSafetyDeficiencies_Download.csv";

	// Compressed CSV versions containing only values used for construction
	public static final String fileNursingHomes = "Data/Saved/NursingHomeInfo.csv";
	public static final String fileNHGeo = "Data/Saved/NHGeoInfo.csv";
	public static final String fileHDef = "Data/Saved/HDefInfo.csv";
	public static final String fileFSDef = "Data/Saved/FSDefInfo.csv";

	
	/**
	 * @brief Reads from a file and returns a List of String arrays. Assumes CSV has a header row
	 * @param file File to read
	 * @return Returns a List of String[]. Each String[] is a row, each String is a cell
	 */
	private static List<String[]> readInfo(String file) {
		if (!fileExists(file)) return null;
		List<String[]> info = null;
		try {
			//opencsv-5.1.jar used here for reading a CSV file
			CSVReaderHeaderAware reader = new CSVReaderHeaderAware(new FileReader(file));
			info = reader.readAll();
			reader.close();
		} catch (IOException | CsvException e) {
			e.printStackTrace();
		}
		return info;
	}
	
	
	/**
	 * @brief Constructs and returns a List of NursingHome objects from the CSV files
	 * If the compressed version is unavailable, it will attempt to access the raw version instead.
	 * @return Returns a List of NursingHome objects
	 */
	public static LinkedList<NursingHome> getNursingHomeInfo() {
		// If compressed version not available, access raw data file
		if(!fileExists(fileNursingHomes)) {
			System.out.println(fileNursingHomes + " not found. Accessing raw file: " + fileProviderInfo);
			return getNursingHomeRawInfo();
		}
		
		LinkedList<NursingHome> lstNH = new LinkedList<NursingHome>();
		for (String[] s : readInfo(fileNursingHomes))
			if (s.length > 1) lstNH.add(new NursingHome(s));
		
		return lstNH;
	}
	
	
	/**
	 * @brief Constructs and returns a List of NHGeoInfo objects from the CSV files
	 * If the compressed version is unavailable, it will attempt to access the raw version instead.
	 * @return Returns a List of NHGeoInfo objects
	 */
	public static ArrayList<NHGeoInfo> getNHGeoInfo() {
		// If compressed version not available, access raw data file
		if(!fileExists(fileNHGeo)) {
			System.out.println(fileNHGeo + " not found. Accessing raw file: " + fileProviderGeoInfo);
			return getNHGeoRawInfo();
		}
		
		ArrayList<NHGeoInfo> lstGeo = new ArrayList<NHGeoInfo>();
		for (String[] s : readInfo(fileNHGeo)) 
			if (s.length > 1) lstGeo.add(new NHGeoInfo(s));
		return lstGeo;
	}
	
	
	/**
	 * @brief Constructs and returns a List of HealthDeficiency objects from the CSV files
	 * If the compressed version is unavailable, it will attempt to access the raw version instead.
	 * @return Returns a List of HealthDeficiency objects
	 */
	public static LinkedList<HealthDeficiency> getHealthInfo() {
		// If compressed version not available, access raw data file
		if(!fileExists(fileHDef)) {
			System.out.println(fileHDef + " not found. Accessing raw file: " + fileHealthDeficiencies);
			return getHealthRawInfo();
		}
		
		LinkedList<HealthDeficiency> lstHDef = new LinkedList<HealthDeficiency>();
		for (String[] s : readInfo(fileHDef)) 
			if (s.length > 1) lstHDef.add(new HealthDeficiency(s));
		
		return lstHDef;
	}

	
	
	/**
	 * @brief Constructs and returns a List of FireSafetyDeficiency objects from the CSV files
	 * If the compressed version is unavailable, it will attempt to access the raw version instead.
	 * @return Returns a List of FireSafetyDeficiency objects
	 */
	public static LinkedList<FireSafetyDeficiency> getFireSafetyInfo() {
		// If compressed version not available, access raw data file
		if(!fileExists(fileFSDef)) {
			System.out.println(fileFSDef + " not found. Accessing raw file: " + fileFireSafetyDeficiencies);
			return getFireSafetyRawInfo();
		}
		
		LinkedList<FireSafetyDeficiency> lstFSDef = new LinkedList<FireSafetyDeficiency>();
		for (String[] s : readInfo(fileFSDef)) 
			if (s.length > 1) lstFSDef.add(new FireSafetyDeficiency(s));
		
		return lstFSDef;
	}
	

	/**
	 * @brief Constructs and returns a List of NursingHome objects from the Raw CSV files
	 * Then writes a compressed version of the list for easier usage in the future
	 * @return Returns a List of NursingHome objects
	 */
	public static LinkedList<NursingHome> getNursingHomeRawInfo() {
		LinkedList<NursingHome> lstNH = new LinkedList<NursingHome>();
		for (String[] s : readInfo(fileProviderInfo)) {
			if (s.length > 1) lstNH.add(new NursingHome(s, "?"));
		}
		DataWriter.SaveNursingHomeInfo(lstNH);
		return lstNH;
	}
	


	/**
	 * @brief Constructs and returns a List of NHGeoInfo objects from the Raw CSV files
	 * Then writes a compressed version of the list for easier usage in the future
	 * @return Returns a List of NHGeoInfo objects
	 */
	public static ArrayList<NHGeoInfo> getNHGeoRawInfo() {
		ArrayList<NHGeoInfo> lstGeo = new ArrayList<NHGeoInfo>();
		for (String[] s : readInfo(fileProviderGeoInfo)) 
			if (s.length > 1) lstGeo.add(new NHGeoInfo(s));
		DataWriter.SaveNHGeoInfo(lstGeo);
		return lstGeo;
	}

	/**
	 * @brief Constructs and returns a List of HealthDeficiency objects from the Raw CSV files
	 * Then writes a compressed version of the list for easier usage in the future
	 * @return Returns a List of HealthDeficiency objects
	 */
	private static LinkedList<HealthDeficiency> getHealthRawInfo() {
		LinkedList<HealthDeficiency> lstHDef = new LinkedList<HealthDeficiency>();
		for (String[] s: readInfo(fileHealthDeficiencies)) {
			if (s.length > 1) lstHDef.add(new HealthDeficiency(s, ""));
		}
		DataWriter.SaveHDefInfo(lstHDef);
		return lstHDef;
	}

	/**
	 * @brief Constructs and returns a List of FireSafetyDeficiency objects from the Raw CSV files
	 * Then writes a compressed version of the list for easier usage in the future
	 * @return Returns a List of FireSafetyDeficiency objects
	 */
	private static LinkedList<FireSafetyDeficiency> getFireSafetyRawInfo() {
		LinkedList<FireSafetyDeficiency> lstFSDef = new LinkedList<FireSafetyDeficiency>();
		for (String[] s: readInfo(fileFireSafetyDeficiencies)) {
			if (s.length > 1) lstFSDef.add(new FireSafetyDeficiency(s, ""));
		}
		DataWriter.SaveFSDefInfo(lstFSDef);
		return lstFSDef;
	}

	/**
	 * @brief Checks if a file exists
	 * @param s String path to the file (relative to project location)
	 * @return Returns true if file exists
	 */
	private static boolean fileExists(String s) {
		return new File(s).exists();
	}
}
