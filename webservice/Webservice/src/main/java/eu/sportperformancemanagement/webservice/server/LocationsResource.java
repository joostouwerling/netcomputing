package eu.sportperformancemanagement.webservice.server;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import eu.sportperformancemanagement.common.LocationRequest;
import eu.sportperformancemanagement.common.SpmConstants;

/**
 * This class listens for location requests. If one comes in,
 * it asks all data servers what data they have. It returns everything
 * it received from the data servers. More information on
 * this process can be found at the method comment for
 * getLocationsforMatchAndPlayer();
 * 
 * For this class to work, async must be activated in tomcat:
 * <async-supported>true</async-supported>
 * 
 * @author Joost Ouwerling <j.t.ouwerling@student.rug.nl>
 *
 */

@Path("/locations")
public class LocationsResource {
	
	/**
	 * Used for logging
	 */
	private static final Logger logger =
	        Logger.getLogger(LocationsResource.class.getName());
	
	/**
	 * This map contains a count of the incoming location packets.
	 * It is now used for logging / information purposes, but could
	 * have a purpose if the application knows how many requests
	 * it will get
	 */
	private static HashMap<Integer, Integer> countIncomingLocations = new HashMap<Integer, Integer>();
	
	/**
	 * A map that maps "keys" to JSON strings. When a new location comes
	 * in, the JSON is appended to the current JSON.
	 */
	private static HashMap<Integer, String> locationsJson = new HashMap<Integer, String>();
	
	/**
	 * The current key used to map location results to a certain request.
	 * Starts at 0, and then increases for each new request.
	 */
	private static int key = 0;
	
	/**
	 * This function receives a request for locations for a given match id 
	 * and player id. It asks all the data servers listening on the queue
	 * to return what they know about this particular match/player combination.
	 * 
	 * This works by sending a LocationRequest to the queue using RequestEmitter. 
	 * The dataservers which receive this request, look for their locations in their
	 * databases and send them back in JSONArray formats to a callback with a unique key.
	 * This unique key connects every location that comes in to the correct request.
	 * After SpmConstants.WAIT_TIME_LOCATIONS milliseconds, the function returns
	 * all locations that came in. The callback that handles the incoming locations
	 * can be found at the method handleIncomingLocations();
	 * 
	 * This is all done in an asynchronous manner, so other request keep being answered
	 * while this requests wait for incoming locations. This is achieved by accepting
	 * an AsyncResponse as the first parameter and running a thread in the method,
	 * which only returns after the thread has ended.
	 * 
	 * @param asyncResponse necessary for async process
	 * @param matchId the match ID for which the locations are requested
	 * @param playerId the player id for which the locations are requested
	 */
	@GET
	@Path("/{match}/{player}")
	@Produces(MediaType.APPLICATION_JSON)
	public void getLocationsForMatchAndPlayer(@Suspended final AsyncResponse asyncResponse,
									@PathParam("match") final int matchId,
									@PathParam("player") final int playerId) {
	    /**
	     * This thread runs asynchronously on the server
	     */
		new Thread(new Runnable() {
	 
			/**
			 * Request the locations from data servers. If it returns,
			 * resume the response with the given result.
			 */
	        @Override
	        public void run() {
	            String result = requestLocationsFromDataServers();
	            asyncResponse.resume(result);
	        }
	        
	        /**
	         * Request the locations from the data servers
	         * @return a JSON array string
	         */
	        private String requestLocationsFromDataServers() {
	        	try {
	        		// Get the key for this request, and increase it so new requests have a different key
	        		int current_key = key;
	        		key++;
	        		
	        		// Initialize the counter and the JSON array
	        		countIncomingLocations.put(current_key, 0);
	        		locationsJson.put(current_key, "[]");
	        		logger.log(Level.INFO, "Started listening for locations on key " + current_key);
	        		
	        		// notify dataservers via RequestEmitter. If this fails, it throws an Exception
	        		RequestEmitter.emit(new LocationRequest(matchId, 	
	        												playerId,
	        												SpmConstants.WEBSERVICE_BASE_URL + "locations/" + current_key));
	        		
	        		// Wait for a bit, so requests come in.
	        		Thread.sleep(SpmConstants.WAIT_TIME_LOCATIONS);
	        		
	        		// Log some information
	        		String output = locationsJson.get(current_key);
	        		logger.log(Level.INFO, "Received locations from " + countIncomingLocations.get(current_key) + " dataservers.");
	        		logger.log(Level.INFO, "Data received: " + output);
	        		
	        		// Remove the request data and return!
	        		countIncomingLocations.remove(current_key);
	        		locationsJson.remove(current_key);
	        		return output;
	        	
	        	// An error occured. Log it.
	        	// Also, return a 500 internal server error.
	        	} catch (Exception ex) {
	        		logger.log(Level.SEVERE, "Error executing request for locations", ex);
	        		throw new WebApplicationException(
	    					Response.status(Status.INTERNAL_SERVER_ERROR).entity("Could not load the locations.").build());
	        	}
	        }
	    }).start();
	}
	
	
	/**
	 * This methods listens for post requests with locations
	 * and appends them, if the key exists, to the map item
	 * with this key.
	 * @param key a key that identifiers a request for locations
	 * @param locations a JSON array with locations. Only string manipulation is used,
	 * 					no (de) parsing.
	 */
	@POST
	@Path("/{key}")
	public void handleIncomingLocations(@PathParam("key") int key, @FormParam("locations") String locations) {
		
		// The key does not exists. Log it and return 404 not found.
		if (!countIncomingLocations.containsKey(key)) {
			logger.log(Level.INFO, "Key " + key + "received but not found in the Hash Map.");
			throw new WebApplicationException(
					Response.status(Status.NOT_FOUND).entity("Key " + key + " not found in the system.").build());
		}
		
		// No locations (different than 0 locations) in the request? Log it, and throw BAD REQUEST
		if (locations == null) {
			logger.log(Level.INFO, "Received a post request for " + key + " but no locations were submitted.");
			throw new WebApplicationException(
					Response.status(Status.BAD_REQUEST).entity("No locations were submitted in the post request").build());
		}
		
		// Log the info about the incoming data
		logger.log(Level.INFO, "Received the following JSON for key " + key + ": " + locations);
		
		// Increase the incoming count and append the JSON to what we already had.
		countIncomingLocations.put(key, countIncomingLocations.get(key) + 1);
		String jsonNow = locationsJson.get(key);
		String newJson;
		if (jsonNow == "[]")
			newJson = locations;
		else
			newJson = jsonNow.substring(0, jsonNow.length() - 1) + "," + locations.substring(1);
		locationsJson.put(key, newJson);
	}
	

	
}
