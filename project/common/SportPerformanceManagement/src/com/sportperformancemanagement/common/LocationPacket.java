package com.sportperformancemanagement.common;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is used to transfer location packets between 
 * multiple network nodes. The sender can set all the data of
 * the LocationPacket and send it over a socket using toString(),
 * which generates a line-by-line representation of the object.
 * All ints and floats are send in a raw format. The date is converted
 * to a unix timestamp.
 * 
 * The receiver can use parsePacket to retrieve a LocationPacket with
 * all the content set correctly.
 * 
 * @author Joost Ouwerling <j.t.ouwerling@student.rug.nl>
 *
 */

public class LocationPacket {
	
	/**
	 * Used for logging
	 */
	private static final Logger logger =
	        Logger.getLogger(MySQLConnection.class.getName());
	
	/**
	 * The player id
	 */
	private int playerId;
	
	/**
	 * The match id
	 */
	private int matchId;
	
	/**
	 * the date of this packet
	 */
	private Date date;
	
	/**
	 * The latitude of the location
	 */
	private float latitude;
	
	/**
	 * The longitude of the location
	 */
	private float longitude;
	
	/**
	 * Constructor, which sets all of the above fields.
	 * @param playerId
	 * @param matchId
	 * @param date
	 * @param latitude
	 * @param longitude
	 */
	public LocationPacket (int playerId, int matchId, Date date, float latitude, float longitude) {
		setPlayerId(playerId);
		setMatchId(matchId);
		setDate(date);
		setLatitude(latitude);
		setLongitude(longitude);
	}
	
	/**
	 * Makes a string representation of this class. Can be used to
	 * send it to another server, which can construct a location packet
	 * from it with parsePacket.
	 */
	public String toString() {
		return playerId + "\n" + matchId + "\n" + date.getTime() + "\n" + latitude + "\n" + longitude;
	}
	
	/**
	 * Parse a string and return a LocationPacket
	 * @param packet the string you want to parse
	 * @return a LocationPacket
	 */
	public static LocationPacket parsePacket(String packet) {
		String[] lines = packet.split(" ");
		try {
			int playerId = Integer.parseInt(lines[0]);
			int matchId = Integer.parseInt(lines[1]);
			int timestamp = Integer.parseInt(lines[2]);
			float latitude = Float.parseFloat(lines[3]);
			float longitude = Float.parseFloat(lines[4]);
			// Convert timestamp to a date.
			Date date = new Date();
			date.setTime(timestamp);
			return new LocationPacket (playerId, matchId, date, latitude, longitude);
		} catch (Exception ex) {
			logger.log(Level.WARNING, "Could not parse input packet:\n" + packet, ex);
			return null;
		}
	}

	/**
	 * @return the latitude
	 */
	public float getLatitude() {
		return latitude;
	}
	
	/**
	 * @param latitude the latitude to set
	 */
	private void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return the longitude
	 */
	public float getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}

	/**
	 * @return the matchId
	 */
	public int getMatchId() {
		return matchId;
	}

	/**
	 * @param matchId the matchId to set
	 */
	public void setMatchId(int matchId) {
		this.matchId = matchId;
	}

	/**
	 * @return the playerId
	 */
	public int getPlayerId() {
		return playerId;
	}

	/**
	 * @param playerId the playerId to set
	 */
	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}
	
}
