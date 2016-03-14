package com.sportperformancemanagement.common;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MatchJSON {

	public static JSONObject matchToJson(Match match) {
		JSONObject obj = new JSONObject();
		obj.put("name", match.getName());
		obj.put("id", match.getId());
		obj.put("server", match.getServer());
		obj.put("port", match.getPort());
		return obj;
	}
	
	public static Match jsonToMatch(JSONObject match) throws JSONException {
        return new Match(
        		match.getString("name"),
                match.getInt("id"),
                match.getString("server"),
                match.getInt("port"));
	}
	
	public static JSONArray matchesToJson(Match[] matches) {
		JSONArray arr = new JSONArray();
		for (Match match : matches)
			arr.put(matchToJson(match));
		return arr;
	}
	
	public static Match[] jsonToMatches(JSONArray matches) throws JSONException {
		Match[] matchArray = new Match[matches.length()];
        for (int i = 0; i < matches.length(); i++)
        	matchArray[i] = jsonToMatch(matches.getJSONObject(i));
        return matchArray;
	}
	
}
