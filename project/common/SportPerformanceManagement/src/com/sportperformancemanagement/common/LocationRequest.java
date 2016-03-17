package com.sportperformancemanagement.common;

public class LocationRequest {

	private Integer matchId = null;
	private Integer playerId = null;
	private String callback = null;
 
	public LocationRequest() {}
	
	public LocationRequest(int match, int player, String cb) {
		setMatchId(match);
		setPlayerId(player);
		setCallback(cb);
	}
	
	public static LocationRequest parseRequest(String request) throws Exception {
		String[] parts = request.split("\n");
		LocationRequest locReq = new LocationRequest();
		locReq.setMatchId(Integer.parseInt(parts[0]));
		locReq.setPlayerId(Integer.parseInt(parts[1]));
		locReq.setCallback(parts[2]);
		return locReq;
	}
	
	
	public String toString() {
		return matchId + "\n" + playerId + "\n" + callback;
	}
	
	/**
	 * @return the matchId
	 */
	public Integer getMatchId() {
		return matchId;
	}
	/**
	 * @param matchId the matchId to set
	 */
	public void setMatchId(Integer matchId) {
		this.matchId = matchId;
	}
	/**
	 * @return the playerId
	 */
	public Integer getPlayerId() {
		return playerId;
	}
	/**
	 * @param playerId the playerId to set
	 */
	public void setPlayerId(Integer playerId) {
		this.playerId = playerId;
	}
	/**
	 * @return the callback
	 */
	public String getCallback() {
		return callback;
	}
	/**
	 * @param callback the callback to set
	 */
	public void setCallback(String callback) {
		this.callback = callback;
	}
	
}
