package eu.sportperformancemanagement.webservice.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;

import eu.sportperformancemanagement.common.MySQLConnection;
import eu.sportperformancemanagement.common.Player;

/**
 * Player Data Access Object. It is used to get all players
 * and insert new players in the database. Depends on the
 * MySQLConnection in common to make connections.
 * 
 * @author Joost Ouwerling <j.t.ouwerling@student.rug.nl>
 *
 */
public class PlayerDAO {
	

	/**
	 * Used for logging
	 */
	private static final Logger logger =
	        Logger.getLogger(PlayerDAO.class.getName());
	
	/**
	 * fetch a list of all players from the database. On failure, it returns 
	 * an exception with a user-friendly message.
	 * @return an array of players
	 * @throws Exception if there is an error fetching the players 
	 * from the database.
	 */
	public Player[] getAll() throws Exception {
		try {
			// Make a connection and prepare the query
			Connection conn = MySQLConnection.create();
			Statement stmt = conn.createStatement();
			String query = "SELECT id, name FROM players";
			ResultSet rs = stmt.executeQuery(query);
			
			// Fetch the resultset into an Array List
			List<Player> players = new ArrayList<Player>();
			while (rs.next())
				players.add(new Player(rs.getInt("id"), rs.getString("name")));
			logger.log(Level.INFO, players.size() + " players have been loaded from the database");
			
			// Close the resources
			rs.close();
			stmt.close();
			conn.close();
			
			// Convert the array list to an array
			Player[] playersArr = new Player[players.size()];
			playersArr = players.toArray(playersArr);
			return playersArr;
		
		// An error when doing the SQL operations. Log and rethrow a user friendly exception
		} catch (SQLException ex) {
			logger.log(Level.SEVERE, "SQL exception while loading players from the database", ex);
			throw new Exception("Could not load players from the database.");
		}
	}
	
	/**
	 * Insert player into the database. On failure, it returns 
	 * an exception with a user-friendly message.
	 * @param player
	 * @throws AlreadyExistsException if the player with this name already exists in the db
	 * @throws Exception if the query fails
	 */
	public void insert(Player player) throws AlreadyExistsException, Exception {
		try {
			// Set up the connection and query
			Connection conn = MySQLConnection.create();
			String query = "INSERT INTO players(name) VALUES (?)";
			// Execute the prepared statement
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, player.getName());
			stmt.execute();
			// Close the database resources
			stmt.close();
			conn.close();
			logger.log(Level.INFO, "Player " + player.getName() + " inserted into the database");
			
		// In most cases, this exception will mean that the player already exists.
		// Log the exception and return a user friendly message
		} catch (MySQLIntegrityConstraintViolationException ex) {
			logger.log(Level.WARNING, "Player probably already exists in database", ex);
			throw new AlreadyExistsException("The player with name " + player.getName() + " already exists!");
		
		// Any other exception. This is probably server related, so return a normal error
		} catch (Exception ex) {
			logger.log(Level.SEVERE, "Exception while insertint player into database.", ex);
			throw new Exception("Could not insert the player into the database.");
		}
	}
	
}
