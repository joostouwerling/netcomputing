package eu.sportperformancemanagement.webservice.server;

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

import eu.sportperformancemanagement.common.Match;
import eu.sportperformancemanagement.common.MatchJSON;
import eu.sportperformancemanagement.webservice.dao.AlreadyExistsException;
import eu.sportperformancemanagement.webservice.dao.MatchDAO;

/**
 * This class acts as the REST endpoints for fetching matches and
 * inserting new matches.
 * @author Joost Ouwerling <j.t.ouwerling@student.rug.nl>
 *
 */
@Path("/matches")
public class MatchesResource {
	
	/**
	 * Used for logging
	 */
	private static final Logger logger =
	        Logger.getLogger(MatchesResource.class.getName());
	
	
	/**
	 * The match data access object
	 */
	private MatchDAO matchDao;
	
	/**
	 * Initialize matchDAO
	 */
	public MatchesResource() {
		matchDao = new MatchDAO();
	}
	
	
	/**
	 * Listen for GET requests for all matches. If it can sucessfully be retrieved
	 * from the database, return it as a JSON array.
	 * @return a JSON array with all matches from the database
	 * @throws WebApplicationException if anything fails in the db process (500
	 * Internal Server Error).
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getMatches() throws WebApplicationException {
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
	
	/**
	 * Listen for POST requests to create a new match. 
	 * @param port the port of the match server
	 * @param name the name of the match
	 * @param server the hostname of the match server
	 * @return a CREATED response if everything was sucessfull
	 * @throws WebApplicationException if a) the input was errornous (400 Bad Request)
	 * b) the match already exists in the db with this name (Status.Conflict)
	 * c) an error while processing your request (500 internal server error)
	 */
	@POST
	public Response insertMatch(@FormParam("port") int port,
							@FormParam("name") String name,
							@FormParam("server") String server) throws WebApplicationException {
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
		
		// Try to insert the match in the database
		try {
			matchDao.insert(match);
		// The match already exists
		} catch (AlreadyExistsException ex) {
			throw new WebApplicationException(
					Response.status(Status.CONFLICT).entity(ex.getMessage()).build());	
		// A server error.
		} catch (Exception ex) {
			logger.log(Level.WARNING, "Match could not be inserted.", ex);
			throw new WebApplicationException(
					Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build());	
		}
		
		// Return the created response
		return Response.status(Status.CREATED).entity(MatchJSON.matchToJson(match).toString()).build();
	}
	

		
}
