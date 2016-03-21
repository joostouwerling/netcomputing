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

import eu.sportperformancemanagement.common.Match;
import eu.sportperformancemanagement.common.MySQLConnection;

/**
 * Match Data Access Object. Used to fetch all matches and
 * insert new Matches in the database. Relies on 
 * MySQLConnection of the common package to create database connection.
 * 
 * @author Joost Ouwerling <j.t.ouwerling@student.rug.nl>
 *
 */
public class MatchDAO {
	
	/**
	 * Used for logging
	 */
	private static final Logger logger =
	        Logger.getLogger(MatchDAO.class.getName());
	
	/**
	 * Get a list of all the matches from the database. On failure,
	 * it returns an exception with a user-friendly message.
	 * @return an array of Match objects
	 * @throws Exception if there was an error loading the matches from the database.
	 */
	public Match[] getAll() throws Exception {
		try {
			// Make connection and prepare statement
			Connection conn = MySQLConnection.create();
			Statement stmt = conn.createStatement();
			String query = "SELECT id, name, server, port FROM matches";
			ResultSet rs = stmt.executeQuery(query);
			
			// Store the ResultSet in an ArrayList
			List<Match> matches = new ArrayList<Match>();
			while (rs.next()) {
				matches.add(new Match(
						rs.getInt("id"),
						rs.getString("name"),
						rs.getString("server"),
						rs.getInt("port")));
			}
			logger.log(Level.INFO, matches.size() + " matches have been loaded from the database");
			
			// Close the database resources
			rs.close();
			stmt.close();
			conn.close();
			
			// Convert the arraylist into an array
			Match[] matchesArr = new Match[matches.size()];
			matchesArr = matches.toArray(matchesArr);
			return matchesArr;
			
		// An error occured anywhere in the above process. Log the
		// exception and return a user friendly message.
		} catch (SQLException ex) {
			logger.log(Level.SEVERE, "Could not load Matches from database", ex);
			throw new Exception("Could not load matches from database.");
		}
	}
	
	/**
	 * Inserts Match into the database. On failure, it returns an exception
	 * with a user-friendly message.
	 * @param match
	 * @throws AlreadyExistsException if a match with this name already exists in the db
	 * @throws Exception if any other error occurs
	 */
	public void insert(Match match) throws AlreadyExistsException, Exception {
		try {
			// Create the connection and execute the statement.
			Connection conn = MySQLConnection.create();
			String query = "INSERT INTO matches(name, server, port) VALUES (?, ?, ?)";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, match.getName());
			stmt.setString(2, match.getServer());
			stmt.setInt(3, match.getPort());
			stmt.execute();
			logger.log(Level.INFO, "Match " + match.getName() + " inserted into the database");
			// Free the resources
			stmt.close();
			conn.close();

		// In most cases, this exception will mean that the match already exists.
		// Log the exception and return a user friendly message
		} catch (MySQLIntegrityConstraintViolationException ex) {
			logger.log(Level.WARNING, "Match probably already exists in database", ex);
			throw new AlreadyExistsException("The match with name " + match.getName() + " already exists!");
		
		// Any other exception (i.e. probably SQL) occured
		// Log and return a user friendly message
		} catch (Exception ex) {
			logger.log(Level.SEVERE, "Exception while inserting match into database.", ex);
			throw new Exception("Could not insert the match into the database.");
		}
	}
		
}
