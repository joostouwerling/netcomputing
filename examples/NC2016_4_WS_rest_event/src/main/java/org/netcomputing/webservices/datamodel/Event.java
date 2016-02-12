package org.netcomputing.webservices.datamodel;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Event {
	private String id;
	private String message;
	private Double value;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Double getValue() {
		return value;
	}
	public void setValue(Double value) {
		this.value = value;
	}


}