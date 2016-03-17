package eu.sportperformancemanagement.dataserver;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;

import com.sportperformancemanagement.common.LocationRequest;

public class LocationFetcher {
	
	/**
	 * Used for logging
	 */
	private static final Logger logger =
	        Logger.getLogger(LocationFetcher.class.getName());
	
	private LocationDAO locationDao;
	
	public LocationFetcher() {
		locationDao = new LocationDAO();
	}
	
	public void fetchLocations(LocationRequest locReq) {
		Location[] locs;
		try {
			locs = locationDao.findLocations(locReq.getMatchId(), locReq.getPlayerId());
		} catch (Exception ex) {
			logger.log(Level.SEVERE, "Could not load locations from the database.", ex);
			return;
		}
		logger.log(Level.INFO, "Posting " + locs.length + " locations to callback " + locReq.getCallback());
		postLocationsToCallback(locs, locReq.getCallback());
	}
	
	private void postLocationsToCallback(Location[] locs, String callback) {
		JSONArray arr = LocationJSON.locationsToJson(locs);
		HttpClient http = HttpClientBuilder.create().build();
		try {
			HttpPost request = new HttpPost(callback);
	        StringEntity params = new StringEntity("locations=" + arr.toString());
	        request.addHeader("content-type", "application/x-www-form-urlencoded");
	        request.setEntity(params);
	        logger.log(Level.INFO, "Sending post request: \n" + request.toString() + "\n" + params.toString() + "\n" + arr.toString());
	        HttpResponse response = http.execute(request);
	        logger.log(Level.INFO, "Sent request and received " + response.toString());
	    } catch (Exception ex) {
	        // handle exception here
	    	logger.log(Level.WARNING, "Could not send post request", ex);
	    }
	}
	
	
}
