package com.sportperformancemanagement.common;

import java.util.Date;

public class LocationPacket {

	private int playerId;
	private int matchId;
	private Date date;
	private float latitude;
	private float longitude;
	
	public LocationPacket (int playerId, int matchId, Date date, float latitude, float longitude) {
		setPlayerId(playerId);
		setMatchId(matchId);
		setDate(date);
		setLatitude(latitude);
		setLongitude(longitude);
	}
	
	public String toString() {
		return playerId + "\n" + matchId + "\n" + date.getTime() + "\n" + latitude + "\n" + longitude;
	}
	
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
			ex.printStackTrace();
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
