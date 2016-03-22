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

import eu.sportperformancemanagement.common.Player;
import eu.sportperformancemanagement.common.PlayerJSON;
import eu.sportperformancemanagement.webservice.dao.AlreadyExistsException;
import eu.sportperformancemanagement.webservice.dao.PlayerDAO;

/**
 * This class acts as REST endpoint for fetching all players
 * and inserting new players.
 * 
 * @author Joost Ouwerling <j.t.ouwerling@student.rug.nl>
 *
 */
@Path("/players")
public class PlayersResource {
	
	/**
	 * Used for logging
	 */
	private static final Logger logger =
	        Logger.getLogger(MatchesResource.class.getName());
	
	/**
	 * The database access object for players.
	 */
	private PlayerDAO playerDao;
	
	/**
	 * Initialize the database access object
	 */
	public PlayersResource() {
		playerDao = new PlayerDAO();
	}
	
	
	/**
	 * GET a JSON list of all players. When the database fails to return
	 * a list of players, a 500 internal server error is thrown.
	 * @return a list of all players in JSON format
	 * @throws WebApplicationException if something went wrong in the db
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getPlayers() throws WebApplicationException {
		try {
			Player[] players = playerDao.getAll();
			logger.log(Level.INFO, "Received " + players.length + " players from playerDAO");
			return PlayerJSON.playersToJson(players).toString();
		} catch (Exception ex) {
			logger.log(Level.SEVERE, "Error in loading players from database", ex);
			throw new WebApplicationException(
					Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build());
		}
	}
	
	/**
	 * This method listens for POST requests, which want to insert a new
	 * player in the database. It first tries to create a player object,
	 * which might throw if name is invalid. Then it tries to insert the 
	 * new player in the database
	 * 
	 * @param name the name of the new player
	 * @return a CREATED response if everything was succesfull
	 * @throws WebApplicationException if a) the input was invalid (400 bad request)
	 * or b) if there was an error while inserting the player in the db
	 * (500 internal server error) or c) when a player with this name already exists
	 * (Status.CONFLICT).
	 */
	@POST
	public Response insertPlayer(@FormParam("name") String name) throws WebApplicationException {
		
		// Try to create the player object without id, so error checking is done
		Player player;
		try {
			player = new Player(name); 
		} catch (Exception ex) {
			logger.log(Level.WARNING, "Player could not be created from input.", ex);
			throw new WebApplicationException(
					Response.status(Status.BAD_REQUEST).entity(ex.getMessage()).build());	
		}
		
		// Insert it in the database
		try {
			playerDao.insert(player);
		// The player already exists!
		} catch (AlreadyExistsException ex) {
			throw new WebApplicationException(
					Response.status(Status.CONFLICT).entity(ex.getMessage()).build());	
		// Another error while inserting it in the database
		} catch (Exception ex) {
			logger.log(Level.WARNING, "Player could not be inserted.", ex);
			throw new WebApplicationException(
					Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build());	
		}
		
		// Return the created resonse.
		return Response.status(Status.CREATED).entity(PlayerJSON.playerToJson(player).toString()).build();
	}


}
