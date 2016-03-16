package eu.sportperformancemanagement.dataserver;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Two static functions which can transfer Location and
 * Location arrays to JSON objects resp JSON arrays.
 * @author joostouwerling
 *
 */
public class LocationJSON {

	/**
	 * Make a JSON array from the Location objects in locs.
	 * @param locs
	 * @return JSONArray with JSONObjects, where each JSONObject is made by locationToJson.
	 */
	public static JSONArray locationsToJson(Location[] locs) {
		JSONArray arr = new JSONArray();
		for (Location loc : locs)
			arr.put(locationToJson(loc));
		return arr;
	}
	
	/**
	 * Make a JSON object from the Location in loc
	 * @param loc
	 * @return a JSONObject with fields lat and lng
	 */
	public static JSONObject locationToJson(Location loc) {
		JSONObject obj = new JSONObject();
		obj.put("lat", loc.getLatitude());
		obj.put("lng", loc.getLongitude());
		return obj;
	}
	
}
