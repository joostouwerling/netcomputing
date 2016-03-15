package com.sportperformancemanagement.webservice.server;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.sportperformancemanagement.common.Match;
import com.sportperformancemanagement.common.MatchJSON;
import com.sportperformancemanagement.webservice.dao.MatchDAO;

@Path("/matches")
public class MatchesResource {
	
	/**
	 * Used for logging
	 */
	private static final Logger logger =
	        Logger.getLogger(MatchesResource.class.getName());
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getMatches() {
		MatchDAO matchDao = new MatchDAO();
		try {
			Match[] matches = matchDao.getMatches();
			logger.log(Level.INFO, "Received " + matches.length + " matches from matchDAO");
			return MatchJSON.matchesToJson(matches).toString();
		} catch (Exception ex) {
			logger.log(Level.SEVERE, "Error in loading matches from database", ex);
			throw new WebApplicationException(
					Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build());
		}
	}
		
}
