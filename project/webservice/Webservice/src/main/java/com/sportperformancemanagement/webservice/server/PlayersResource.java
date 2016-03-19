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

import com.sportperformancemanagement.webservice.dao.Player;
import com.sportperformancemanagement.webservice.dao.PlayerDAO;

@Path("/players")
public class PlayersResource {
	
	/**
	 * Used for logging
	 */
	private static final Logger logger =
	        Logger.getLogger(MatchesResource.class.getName());
	
	private PlayerDAO playerDao;
	
	public PlayersResource() {
		playerDao = new PlayerDAO();
	}
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getPlayers() {
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
	
	@POST
	public void insertMatch(@FormParam("name") String name) {
		Player player = new Player(name); 
		try {
			playerDao.insert(player);
		} catch (Exception ex) {
			logger.log(Level.WARNING, "Player could not be inserted.", ex);
			throw new WebApplicationException(
					Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build());	
		}
	}


}
