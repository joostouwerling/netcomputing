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

import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import com.sportperformancemanagement.common.MySQLConnection;

public class PlayerDAO {
	

	/**
	 * Used for logging
	 */
	private static final Logger logger =
	        Logger.getLogger(PlayerDAO.class.getName());
	
	/**
	 * fetch a list of all players from the database.
	 * @return an array of players
	 * @throws Exception if there is an error fetching the players 
	 * from the database.
	 */
	public Player[] getAll() throws Exception {
		try {
			Connection conn = MySQLConnection.instance();
			Statement stmt = conn.createStatement();
			String query = "SELECT id, name FROM players";
			ResultSet rs = stmt.executeQuery(query);
			List<Player> players = new ArrayList<Player>();
			while (rs.next())
				players.add(new Player(rs.getInt("id"), rs.getString("name")));
			logger.log(Level.INFO, players.size() + " players have been loaded from the database");
			Player[] playersArr = new Player[players.size()];
			playersArr = players.toArray(playersArr);
			return playersArr;
		} catch (SQLException ex) {
			logger.log(Level.SEVERE, "SQL exception while loading players from the database", ex);
			throw new Exception("Could not load players from the database.");
		}
	}
	
	public void insert(Player player) throws Exception {
		Connection conn = MySQLConnection.instance();
		if (conn == null)
			throw new Exception("MySQL Connection not available.");
		
		try {
			String query = "INSERT INTO players(name) VALUES (?)";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, player.getName());
			stmt.execute();
			logger.log(Level.INFO, "Player " + player.getName() + " inserted into the database");
		} catch (MySQLIntegrityConstraintViolationException ex) {
			logger.log(Level.WARNING, "Player probably already exists in database", ex);
			throw new Exception("The player with name " + player.getName() + " already exists!");
		} catch (Exception ex) {
			logger.log(Level.SEVERE, "Exception while insertint player into database.", ex);
			throw new Exception("Could not insert the player into the database.");
		}
	}
	
}
