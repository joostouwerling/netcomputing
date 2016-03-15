package com.sportperformancemanagement.webservice.server;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.JSONArray;
import org.json.JSONObject;

import com.sportperformancemanagement.common.Match;
import com.sportperformancemanagement.common.MatchJSON;

@Path("/")
public class MatchesResource {
	
	public static Match[] matches;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/matches")
	public String getMatches() {
		MatchesResource.fillMatches();
		return MatchJSON.matchesToJson(matches).toString();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/locations")
	public String getLocations() {
		double lat = 53.2403851;
		double lng = 6.5365328;
		JSONArray arr = new JSONArray();
		for (double i = -50; i < 50; i++) {
			JSONObject obj = new JSONObject();
			obj.put("lat", lat + (i / 1000));
			obj.put("lng", lng + (i / 1000));
			arr.put(obj);
		}
		return arr.toString();
	}
	
	public static void fillMatches() {
		if (matches != null)
			return;
		int size = 100;
		matches = new Match[size];
		for (int i = 0; i < size; i++)
			matches[i] = new Match("Match " + i, i, "webserver.com", i + 100);
	}
	
}
