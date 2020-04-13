package data;

import classes.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.opencsv.CSVWriter;

/**
 * @brief Used for Writing the objects into CSV format with only the essential values
 * Allows for smaller csv files for faster loading
 * @author Kenneth Mak - makk4
 *
 */
public class DataWriter {

	/**
	 * @brief Writes a list of NursingHome objects into a csv file
	 * @param lst The list of NursingHome objects to save
	 */
	public static void SaveNursingHomeInfo(List<NursingHome> lst) {
		if (lst == null) return;
		try {
			CSVWriter writer = new CSVWriter(new FileWriter(
					DataReader.fileNursingHomes));
			writer.writeNext(NursingHome.getHeader());
			for (NursingHome obj : lst) {
				String[] line = obj.toCSV();
				writer.writeNext(line);
			}
			writer.close();
			System.out.println("Saved NursingHome info to " + DataReader.fileNursingHomes);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @brief Writes a list of NHGeoInfo objects into a csv file
	 * @param lst The list of NHGeoInfo objects to save
	 */
	public static void SaveNHGeoInfo(List<NHGeoInfo> lst) {
		if (lst == null) return;
		try {
			CSVWriter writer = new CSVWriter(new FileWriter(
					DataReader.fileNHGeo));
			writer.writeNext(NHGeoInfo.getHeader());
			for (NHGeoInfo obj : lst) {
				String[] line = obj.toCSV();
				writer.writeNext(line);
			}
			writer.close();
			System.out.println("Saved NHGeoInfo to " + DataReader.fileNHGeo);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * @brief Writes a list of HealthDeficiency objects into a csv file
	 * @param lst The list of HealthDeficiency objects to save
	 */
	public static void SaveHDefInfo(List<HealthDeficiency> lst) {
		if (lst == null) return;
		try {
			CSVWriter writer = new CSVWriter(new FileWriter(
					DataReader.fileHDef));
			writer.writeNext(HealthDeficiency.getHeader());
			for (HealthDeficiency obj : lst) {
				String[] line = obj.toCSV();
				writer.writeNext(line);
			}
			writer.close();
			System.out.println("Saved HealthDeficiencies to " + DataReader.fileHDef);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * @brief Writes a list of FireSafetyDeficiency objects into a csv file
	 * @param lst The list of FireSafetyDeficiency objects to save
	 */
	public static void SaveFSDefInfo(List<FireSafetyDeficiency> lst) {
		if (lst == null) return;
		try {
			CSVWriter writer = new CSVWriter(new FileWriter(
					DataReader.fileFSDef));
			writer.writeNext(FireSafetyDeficiency.getHeader());
			for (FireSafetyDeficiency obj : lst) {
				String[] line = obj.toCSV();
				writer.writeNext(line);
			}
			writer.close();
			System.out.println("Saved FireSafetyDeficiencies to " + DataReader.fileFSDef);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
