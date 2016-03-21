package eu.sportperformancemanagement.dataserver;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;

import eu.sportperformancemanagement.common.LocationRequest;

/**
 * 
 * This class fetches Location objects from the database
 * using LocationDAO and then sends them to the callback,
 * in JSON format, for which the URL is given in the 
 * LocationRequest object.
 * 
 * @author Joost Ouwerling <j.t.ouwerling@student.rug.nl>
 *
 */
public class LocationFetcher {
	
	/**
	 * Used for logging
	 */
	private static final Logger logger =
	        Logger.getLogger(LocationFetcher.class.getName());
	
	/**
	 * A reference to a LocationDAO object,
	 */
	private LocationDAO locationDao;
	
	/**
	 * Initialize the LocationDAO object.
	 */
	public LocationFetcher() {
		locationDao = new LocationDAO();
	}
	
	/**
	 * Fetch the locations for a certain match and player, as given in
	 * locReq, from the LocationsDAO. Then send them to the callback using
	 * postLocationsToCallback().
	 * @param locReq The location request object, which holds the info
	 * 				 necessary to handle this request.
	 */
	public void fetchLocations(LocationRequest locReq) {
		Location[] locs;
		// Try to find the locations in the database
		// If this fails, there's not much we can do, so we log the error
		// and return. This is OK, since the requesting server will wait
		// anyway, if we send something or not. (See Webservice.LocationResource)
		try {
			locs = locationDao.findLocations(locReq.getMatchId(), locReq.getPlayerId());
		} catch (Exception ex) {
			logger.log(Level.SEVERE, "Could not load locations from the database.", ex);
			return;
		}
		
		// We received the locations! Log some info and then post them to the callback
		logger.log(Level.INFO, "Posting " + locs.length + " locations to callback " + locReq.getCallback());
		postLocationsToCallback(locs, locReq.getCallback());
	}
	
	/**
	 * Send the Location objects in locs to 
	 * @param locs an array of Location objects
	 * @param callback the URL where a post request with JSON representation of 
	 * 				   locs needs to be send to.
	 */
	private void postLocationsToCallback(Location[] locs, String callback) {
		// Make a JSON array of the locations.
		JSONArray arr = LocationJSON.locationsToJson(locs);
		
		// Try to send them to the callback using POST. 
		HttpClient http = HttpClientBuilder.create().build();
		try {
			HttpPost request = new HttpPost(callback);
	        StringEntity params = new StringEntity("locations=" + arr.toString());
	        request.addHeader("content-type", "application/x-www-form-urlencoded");
	        request.setEntity(params);
	        logger.log(Level.INFO, "Sending post request to " + callback + "\n" + arr.toString());
	        http.execute(request);
	    // If it failed, not much we need to do.
	    // Same argument holds as for the SQL exception in fetchLocations()
	    } catch (Exception ex) {
	    	logger.log(Level.WARNING, "Could not send post request", ex);
	    }
	}
	
	
}
