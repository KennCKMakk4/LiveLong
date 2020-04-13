package util;
import java.awt.Desktop;
import java.net.URL;

/**
 * @brief Currently only used to prompt opening up GoogleMaps in the browser
 * @author Kenneth Mak
 *
 */
public class GoogleMapsUtils {

    private static GoogleMapsUtils instance = null;
    public GoogleMapsUtils() {
    }

    public static GoogleMapsUtils getInstance() {
        if (instance == null) {
            instance = new GoogleMapsUtils();
        }
        return instance;
    }

    /**
     * @brief Opens up GoogleMaps in the default browser with the origin and destination already set.
     * @param origin Start position of the path
     * @param destination Destination of the path
     * @throws Exception thrown if Desktop was unable to open the browser
     */
    public void openMaps(String origin, String destination) throws Exception {
    	String start = urlEscape(origin);
    	String dest = urlEscape(destination);
    	String query = "https://www.google.com/maps/dir/?api=1";
    	query += "&origin=" + start;
    	query += "&destination=" + dest;
    	
    	URL url = new URL(query);
    	// https://stackoverflow.com/questions/10967451/open-a-link-in-browser-with-java-button
    	Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
    	if (desktop == null || !desktop.isSupported(Desktop.Action.BROWSE)) return;
    	
    	try {
    		desktop.browse(url.toURI());
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
    /**
     * @brief Converts a String into a readable format for the browser url
     * @param s String to convert
     * @return URL-Escaped version of the string
     */
    private String urlEscape(String s) {
    	String url = s;
    	url = url.replace(" ", "%20");
    	url = url.replace(",", "%2C");
    	
    	return url;
    }
}
