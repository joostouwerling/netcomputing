package eu.sportperformancemanagement.common;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class provides four static methods:
 * - Convert a single Player object to JSOn representation
 * - Convert an array of Player objects to JSON representation
 * - Convert JSOn representation of a single player to a Player object
 * - Convert JSON of a Player array to an array of Player objects.
 * 
 * @author Joost Ouwerling <j.t.ouwerling@student.rug.nl>
 *
 */
public class PlayerJSON {

	/**
	 * Converts player to JSON
	 * @param player
	 * @return JSON representation of player
	 */
	public static JSONObject playerToJson(Player player) {
		JSONObject obj = new JSONObject();
		obj.put("name", player.getName());
		obj.put("id", player.getId());
		return obj;
	}
	
	/**
	 * Converts player to an actual Player
	 * @param player JSON representation of a player
	 * @return a Player objects
	 * @throws JSONException if parsing fails
	 */
	public static Player jsonToPlayer(JSONObject player) throws JSONException {
        return new Player(player.getInt("id"), player.getString("name"));
	}
	
	/**
	 * converts players to a JSON array
	 * @param players
	 * @return a JSON array representation of players
	 */
	public static JSONArray playersToJson(Player[] players) {
		JSONArray arr = new JSONArray();
		for (Player player : players)
			arr.put(playerToJson(player));
		return arr;
	}
	/**
	 * Converts players into a Player array
	 * @param players
	 * @return an array of Player objects
	 * @throws JSONException when parsing fails
	 */
	public static Player[] jsonToPlayers(JSONArray players) throws JSONException {
		Player[] playerArray = new Player[players.length()];
        for (int i = 0; i < players.length(); i++)
        	playerArray[i] = jsonToPlayer(players.getJSONObject(i));
        return playerArray;
	}

	
}
