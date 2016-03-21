package eu.sportperformancemanagement.common;

/**
 * This class represents a request from the webservice to 
 * get all locations for a certain match and player. This
 * data needs to be JSOnized and then POSTed to a callback.
 * 
 * @author Joost Ouwerling <j.t.ouwerling@student.rug.nl>
 *
 */

public class LocationRequest {
	
	/**
	 * The match id for this request
	 */
	private Integer matchId = null;
	
	/**
	 * The player id for this request
	 */
	private Integer playerId = null;
	
	/**
	 * the URL for the callback
	 */
	private String callback = null;
 
	// Empty constructor
	public LocationRequest() {}
	
	/**
	 * Constructor which sets the values of this class
	 * @param match
	 * @param player
	 * @param cb
	 */
	public LocationRequest(int match, int player, String cb) {
		setMatchId(match);
		setPlayerId(player);
		setCallback(cb);
	}
	
	/**
	 * Parse a string request into a LocationPacket. This is the inverse
	 * operation of toString() and is used on the receiver side.
	 * @param request the string representation of a location request
	 * @return the parsed Location Request
	 * @throws Exception when something went wrong in the parsing, i.e. conversion to int.
	 */
	public static LocationRequest parseRequest(String request) throws Exception {
		String[] parts = request.split("\n");
		LocationRequest locReq = new LocationRequest();
		locReq.setMatchId(Integer.parseInt(parts[0]));
		locReq.setPlayerId(Integer.parseInt(parts[1]));
		locReq.setCallback(parts[2]);
		return locReq;
	}
	
	/**
	 * Returns a string representation of this object. 
	 * Properties are separated by newlines.
	 */
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
