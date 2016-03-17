package com.sportperformancemanagement.webservice.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sportperformancemanagement.common.Match;
import com.sportperformancemanagement.common.MySQLConnection;

public class MatchDAO {
	
	/**
	 * Used for logging
	 */
	private static final Logger logger =
	        Logger.getLogger(MatchDAO.class.getName());
	
	/**
	 * Get a list of all the matches from the database.
	 * @return an array of Match objects
	 * @throws Exception if there was an error loading the matches from the database.
	 */
	public Match[] getMatches() throws Exception {
		try {
			Connection conn = MySQLConnection.instance();
			Statement stmt = conn.createStatement();
			String query = "SELECT id, name, server, port FROM matches";
			ResultSet rs = stmt.executeQuery(query);
			List<Match> matches = new ArrayList<Match>();
			while (rs.next()) {
				matches.add(new Match(
						rs.getString("name"),
						rs.getInt("id"),
						rs.getString("server"),
						rs.getInt("port")));
			}
			logger.log(Level.INFO, matches.size() + " matches have been loaded from the database");
			Match[] matchesArr = new Match[matches.size()];
			matchesArr = matches.toArray(matchesArr);
			return matchesArr;
		} catch (SQLException ex) {
			logger.log(Level.SEVERE, "Could not load Matches from database", ex);
			throw new Exception("Could not load matches from database. See the logs for more details.");
		}
	}
	
	
	public void insert(Match match) throws Exception {
		Connection conn = MySQLConnection.instance();
		if (conn == null)
			throw new Exception("MySQL Connection not available.");

		String query = "INSERT INTO matches(name, server, port) VALUES (?, ?, ?)";
		PreparedStatement stmt = conn.prepareStatement(query);
		stmt.setString(1, match.getName());
		stmt.setString(2, match.getServer());
		stmt.setInt(3, match.getPort());
		stmt.execute();
	}
	
}
