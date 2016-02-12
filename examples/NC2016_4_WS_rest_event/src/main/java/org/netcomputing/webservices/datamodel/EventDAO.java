package org.netcomputing.webservices.datamodel;

import java.util.HashMap;
import java.util.Map;

/**
 * Singleton designpattern The possible implementation of Java depends on the
 * version of Java you are using. As of Java 6 you can define singletons with a
 * single-element enum type. This way is currently the best way to implement a
 * singleton in Java 1.6 or later according to the book ""Effective Java from
 * Joshua Bloch.
 */

public enum EventDAO {
	instance;

	private Map<String, Event> contentProvider = new HashMap<String, Event>();

	private EventDAO() {
		Event p = new Event();
		p.setId("01");
		p.setMessage("initial message");
		p.setValue(12.0);
		contentProvider.put("1", p);
	}

	public Map<String, Event> getModel() {
		return contentProvider;
	}

}