package eu.sportperformancemanagement.common;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class provides four static methods:
 * - Convert a single Match object to JSOn representation
 * - Convert an array of Match objects to JSON representation
 * - Convert JSOn representation of a single match to a Match object
 * - Convert JSON of a Match array to an array of Match objects.
 * 
 * @author Joost Ouwerling <j.t.ouwerling@student.rug.nl>
 *
 */
public class MatchJSON {

	/**
	 * Single match to JSON
	 * @param match
	 * @return JSONObject of match
	 */
	public static JSONObject matchToJson(Match match) {
		JSONObject obj = new JSONObject();
		obj.put("id", match.getId());
		obj.put("name", match.getName());
		obj.put("server", match.getServer());
		obj.put("port", match.getPort());
		return obj;
	}
	
	/**
	 * JSON match to Match object
	 * @param match
	 * @return a Match object of (JSONObject) match
	 * @throws JSONException when a get* of JSONObject fails
	 */
	public static Match jsonToMatch(JSONObject match) throws JSONException {
        return new Match(
                match.getInt("id"),
        		match.getString("name"),
                match.getString("server"),
                match.getInt("port"));
	}
	
	/**
	 * Converts an array of Matches to a JSON array
	 * @param matches
	 * @return a JSONArray of matches
	 */
	public static JSONArray matchesToJson(Match[] matches) {
		JSONArray arr = new JSONArray();
		for (Match match : matches)
			arr.put(matchToJson(match));
		return arr;
	}
	
	/**
	 * Converts a JSON Array of matches to a Match array
	 * @param matches
	 * @return an array of Match objects
	 * @throws JSONException if parsing of JSOn fails
	 */
	public static Match[] jsonToMatches(JSONArray matches) throws JSONException {
		Match[] matchArray = new Match[matches.length()];
        for (int i = 0; i < matches.length(); i++)
        	matchArray[i] = jsonToMatch(matches.getJSONObject(i));
        return matchArray;
	}

	
}
