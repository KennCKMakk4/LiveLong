package util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;


/**
 * @brief Used to request longitude and latitude information from an address via GeoCoding
 * without the requirement of an API key. Response is returned in JSON. 
 * https://nominatim.org/release-docs/develop/api/Search/
 * @author Kenneth Mak - makk4
 *
 */
public class OpenStreetMapUtils {


    private static OpenStreetMapUtils instance = null;
    public OpenStreetMapUtils() {
    }

    public static OpenStreetMapUtils getInstance() {
        if (instance == null) {
            instance = new OpenStreetMapUtils();
        }
        return instance;
    }

    /**
     * @brief Sends the URL to the OSM website to get a GeoCoding response in JSON format
     * @param url The URL to send to OSM website
     * @return Returns response (location information) in JSON format
     * @throws Exception
     */
    private String getRequest(String url) throws Exception {
        final URL obj = new URL(url);
        final HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("GET");
        // 200 = successful connection
        if (con.getResponseCode() != 200) {
            return null;
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        // Storing response
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        // System.out.println("Response: " + response);
        return response.toString();
    }

    /**
     * @brief Returns the Longitude and Latitude values of the given address via Geocoding on the OpenStreetMap website.
     * Builds the URL request with the given address and parses the JSON responses into a Map.
     * @param address Address to Geocode
     * @return Returns map containing Latitude and Longitude values (keys: "lat" and "lon")
     */
    public Map<String, Double> getCoordinates(String address) {
    	// response
        Map<String, Double> res = new HashMap<String, Double>();
        StringBuffer query = new StringBuffer();
        query.append("https://nominatim.openstreetmap.org/search/");
        query.append(urlEscape(address));
        query.append("?format=json&addressdetails=1");

        //Query = http request to nominatim.openstreetmap.org

        String queryResult = null;
        try {
            queryResult = getRequest(query.toString());
        } catch (Exception e) {
        	System.out.println("Error while trying to get data with the following query " + query);
        }

        if (queryResult == null) {
        	System.out.println("No query result");
        	return null;
        }
        

        // json-simple-1.1.1.jar library used here specifically for JSON response
        Object obj = JSONValue.parse(queryResult);
        //System.out.println("obj=" + obj);

        if (obj instanceof JSONArray) {
            JSONArray array = (JSONArray) obj;
            if (array.size() > 0) {
                JSONObject jsonObject = (JSONObject) array.get(0);
                String lon = (String) jsonObject.get("lon");
                String lat = (String) jsonObject.get("lat");
                res.put("lon", Double.parseDouble(lon));
                res.put("lat", Double.parseDouble(lat));
            }
        }

        return res;
    }

	/**
	 * @brief Converts a String into a readable format for the browser url
	 * @param s
	 *            String to convert
	 * @return URL-Escaped version of the string
	 */
	private String urlEscape(String s) {
		String url = s;
		url = url.replace(" ", "%20");
		url = url.replace(",", "%2C");
		url = url.replace(";", "%3B");

		return url;
	}
    
}