package com.sportperformancemanagement.webservice.server;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.sportperformancemanagement.common.Match;
import com.sportperformancemanagement.common.MatchJSON;
import com.sportperformancemanagement.webservice.dao.AlreadyExistsException;
import com.sportperformancemanagement.webservice.dao.MatchDAO;

@Path("/matches")
public class MatchesResource {
	
	/**
	 * Used for logging
	 */
	private static final Logger logger =
	        Logger.getLogger(MatchesResource.class.getName());
	
	private MatchDAO matchDao;
	
	public MatchesResource() {
		matchDao = new MatchDAO();
	}
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getMatches() {
		try {
			Match[] matches = matchDao.getAll();
			logger.log(Level.INFO, "Received " + matches.length + " matches from matchDAO");
			return MatchJSON.matchesToJson(matches).toString();
		} catch (Exception ex) {
			logger.log(Level.SEVERE, "Error in loading matches from database", ex);
			throw new WebApplicationException(
					Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build());
		}
	}
	
	@POST
	public Response insertMatch(@FormParam("port") int port,
							@FormParam("name") String name,
							@FormParam("server") String server) {
		Match match;
		
		/**
		 * Try to construct a match without an ID.
		 * This will check the values if they are correct
		 */
		try {
			match = new Match(name, server, port);
		} catch (Exception ex) {
			logger.log(Level.WARNING, "Match could not be created from input.", ex);
			throw new WebApplicationException(
					Response.status(Status.BAD_REQUEST).entity(ex.getMessage()).build());	
		}
		
		try {
			matchDao.insert(match);
		} catch (AlreadyExistsException ex) {
			throw new WebApplicationException(
					Response.status(Status.CONFLICT).entity(ex.getMessage()).build());	
		} catch (Exception ex) {
			logger.log(Level.WARNING, "Match could not be inserted.", ex);
			throw new WebApplicationException(
					Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build());	
		}
		return Response.status(Status.CREATED).entity(MatchJSON.matchToJson(match).toString()).build();
	}
	

		
}