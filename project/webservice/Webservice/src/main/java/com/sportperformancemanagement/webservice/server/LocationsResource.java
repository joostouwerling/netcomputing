package com.sportperformancemanagement.webservice.server;

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

@Path("/locations")
public class LocationsResource {
	
	/**
	 * Used for logging
	 */
	private static final Logger logger =
	        Logger.getLogger(LocationsResource.class.getName());
	
	private static HashMap<Integer, Integer> countIncomingLocations = new HashMap<Integer, Integer>();
	private static HashMap<Integer, String> locationsJson = new HashMap<Integer, String>();
	private static int key = 0;
	
	@GET
	@Path("/{match}/{player}")
	@Produces(MediaType.APPLICATION_JSON)
	public void getLocationsForMatchAndPlayer(@Suspended final AsyncResponse asyncResponse,
									@PathParam("match") final int matchId,
									@PathParam("player") final int playerId) {
	    /*asyncResponse.setTimeoutHandler(new TimeoutHandler() {
	 
	        @Override
	        public void handleTimeout(AsyncResponse asyncResponse) {
	            asyncResponse.resume(Response.status(Response.Status.SERVICE_UNAVAILABLE)
	                    .entity("Operation time out.").build());
	        }
	    });
	    asyncResponse.setTimeout(10, TimeUnit.SECONDS);*/
	 
	    new Thread(new Runnable() {
	 
	        @Override
	        public void run() {
	            String result = requestLocationsFromDataServers();
	            asyncResponse.resume(result);
	        }
	 
	        private String requestLocationsFromDataServers() {
	        	try {
	        		int current_key = key;
	        		key++;
	        		countIncomingLocations.put(current_key, 0);
	        		locationsJson.put(current_key, "[]");
	        		logger.log(Level.INFO, "Started listening for locations on key " + current_key);
	        		// notify dataservers via rabbit mq
	        		Thread.sleep(15000);
	        		String output = locationsJson.get(current_key);
	        		logger.log(Level.INFO, "Received locations from " + countIncomingLocations.get(current_key) + " dataservers.");
	        		logger.log(Level.INFO, "Data received: " + output);
	        		countIncomingLocations.remove(current_key);
	        		locationsJson.remove(current_key);
	        		return output;
	        	} catch (Exception ex) {
	        		logger.log(Level.SEVERE, "Error executing request for locations", ex);
	        		throw new WebApplicationException(
	    					Response.status(Status.INTERNAL_SERVER_ERROR).entity("Could not load the locations.").build());
	        	}
	        }
	    }).start();
	}
	
	@POST
	@Path("/{key}")
	public void handleIncomingLocations(@PathParam("key") int key, @FormParam("locations") String locations) {
		if (!countIncomingLocations.containsKey(key)) {
			logger.log(Level.INFO, "Key " + key + "received but not found in the Hash Map.");
			throw new WebApplicationException(
					Response.status(Status.NOT_FOUND).entity("Key " + key + " not found in the system.").build());
		}
		if (locations == null) {
			logger.log(Level.INFO, "Received a post request for " + key + " but no locations were submitted.");
			throw new WebApplicationException(
					Response.status(Status.BAD_REQUEST).entity("No locations were submitted in the post request").build());
		}
		logger.log(Level.INFO, "Received the following JSON for key " + key + ": " + locations);
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
