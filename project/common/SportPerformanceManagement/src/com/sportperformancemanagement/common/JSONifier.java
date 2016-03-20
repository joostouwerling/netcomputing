package com.sportperformancemanagement.common;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.json.JSONObject;

public class JSONifier {
	
	public static <T> JSONObject toJson(T in) throws Exception {
		JSONObject obj = new JSONObject();
		for (Method method : in.getClass().getDeclaredMethods()) {
		    if (Modifier.isPublic(method.getModifiers())
		        && method.getParameterTypes().length == 0
		        && method.getReturnType() != void.class
		        && (method.getName().startsWith("get") || method.getName().startsWith("is"))
		    )
		    	obj.put(method.getName().substring(3, 4).toLowerCase() + method.getName().substring(4), method.invoke(in));
		}
		return obj;
	}
	
	/*
	public static void main(String[] args) throws Exception {
		Match m = new Match("test", 12, "test", 12);
		System.out.println(JSONifier.toJson(m).toString());
	}*/
	/*
	public static Player jsonToPlayer(JSONObject player) throws JSONException {
        return new Player(match.getInt("id"), match.getInt("name"));
	}
	
	public static JSONArray playersToJson([] matches) {
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
	}*/
	
}
