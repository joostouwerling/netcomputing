package UDP;

import java.io.Serializable;

public class Package implements Serializable {

	private static final long serialVersionUID = 1541764198049252424L;
	
	//package data
	private String name; 
	private String date; 
	private float magnitude; 
	private float longitude; 

	public Package(String name, String date, float magnitude, float longitude) {
		this.setName(name);
		this.setDate(date);
		this.setMagnitude(magnitude);
		this.setLongitude(longitude);
		this.toString(); 
	}

	public float getMagnitude() {
		return magnitude;
	}

	public void setMagnitude(float magnitude) {
		this.magnitude = magnitude;
	}

	public float getLongitude() {
		return longitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
	public String toString() {
		return this.name +", "+  this.date + ", " + this.magnitude + ", " + this.longitude;
	}
}