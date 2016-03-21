package eu.sportperformancemanagement.dataserver;

/**
 * Storage class for Locations, i.e. a holder for a latitude and longitude.
 * @author Joost Ouwerling <j.t.ouwerling@student.rug.nl>
 *
 */
public class Location {

	private double latitude;
	private double longitude;
	
	/**
	 * Constructor which takes a latitude and longitude
	 * @param lat the latitude
	 * @param lng the longitude
	 */
	public Location(double lat, double lng) {
		setLatitude(lat);
		setLongitude(lng);
	}
	
	/**
	 * @return the longitude
	 */
	public double getLongitude() {
		return longitude;
	}
	
	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	/**
	 * @return the latitude
	 */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
	
}
