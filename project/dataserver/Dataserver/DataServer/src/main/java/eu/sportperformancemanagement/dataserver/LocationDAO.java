package eu.sportperformancemanagement.dataserver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sportperformancemanagement.common.LocationPacket;
import com.sportperformancemanagement.common.MySQLConnection;

public class LocationDAO {

	/**
	 * Used for logging
	 */
	private static final Logger logger =
	        Logger.getLogger(LocationDAO.class.getName());
	
	/**
	 * Hold the connection to the MySQL database.
	 */
	Connection conn = null;
	
	/**
	 * Empty constructor
	 */
	public LocationDAO() {
		try {
			conn = MySQLConnection.instance();
		} catch (Exception ex) {
			logger.log(Level.WARNING, "Error in retrieving the MySQL connection.", ex);
		}
	}
	
	/**
	 * Insert locationPacket into the database. Connection given by
	 * MySQLConnection.getInstance();
	 * @param locationPacket
	 */
	public void insert(LocationPacket locationPacket) throws Exception {
		if (conn == null)
			throw new Exception("MySQL Connection not available.");
		
		String query = "INSERT INTO locations(match_id, player_id, datetime_received, latitude, longitude) VALUES (?, ?, ?, ?, ?)";
		PreparedStatement stmt = conn.prepareStatement(query);
		stmt.setInt(1, locationPacket.getMatchId());
		stmt.setInt(2, locationPacket.getPlayerId());
		stmt.setTimestamp(3, new Timestamp(locationPacket.getDate().getTime()));
		stmt.setDouble(4, locationPacket.getLatitude());
		stmt.setDouble(5, locationPacket.getLongitude());
		stmt.execute();
	}
	
	/**
	 * Returns an array with Location objects, where all
	 * Location obejcts are created from entries in match 
	 * matchId for player playerId.
	 * @param matchId the match id
	 * @param playerId the player id
	 * @return an array of Locations for this player in this match
	 * @throws Exception if there is a problem with SQL.
	 */
	public Location[] findLocations(int matchId, int playerId) throws Exception {
		if (conn == null)
			throw new Exception("MySQL Connection not available.");
		
		// Make the select statement
		String query = "SELECT latitude, longitude FROM locations WHERE match_id = ? and player_id = ?";
		PreparedStatement stmt = conn.prepareStatement(query);
		stmt.setInt(1, matchId);
		stmt.setInt(2, playerId);
		
		// ArrayList for holding the locations
		List<Location> locations = new ArrayList<Location>();
		
		// Try to fetch the results from the database
		try {
			ResultSet rs = stmt.executeQuery();
			while (rs.next())
				locations.add(new Location(rs.getDouble("latitude"), rs.getDouble("longitude")));
			logger.log(Level.INFO, locations.size() + " locations have been loaded from the database");
		} catch (SQLException ex) {
			logger.log(Level.SEVERE, "Could not load Locations from database", ex);
			throw new Exception("Could not load locations from database. See the logs for more details.");
		}
		
		// Convert the array list to an array and return.
		Location[] locationsArr = new Location[locations.size()];
		locationsArr = locations.toArray(locationsArr);
		return locationsArr;
	
	}
	
}
