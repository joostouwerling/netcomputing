package com.sportperformancemanagement.webservice.server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sportperformancemanagement.webservice.dao.Player;

public class PlayerJSON {

	public static JSONObject playerToJson(Player player) {
		JSONObject obj = new JSONObject();
		obj.put("name", player.getName());
		obj.put("id", player.getId());
		return obj;
	}
	
	
	public static Player jsonToPlayer(JSONObject player) throws JSONException {
        return new Player(player.getInt("id"), player.getString("name"));
	}
	
	public static JSONArray playersToJson(Player[] players) {
		JSONArray arr = new JSONArray();
		for (Player player : players)
			arr.put(playerToJson(player));
		return arr;
	}
	
	public static Player[] jsonToPlayers(JSONArray players) throws JSONException {
		Player[] playerArray = new Player[players.length()];
        for (int i = 0; i < players.length(); i++)
        	playerArray[i] = jsonToPlayer(players.getJSONObject(i));
        return playerArray;
	}

	
}
