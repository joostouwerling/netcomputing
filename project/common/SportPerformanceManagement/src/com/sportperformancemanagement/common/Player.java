package com.sportperformancemanagement.common;

/**
 * Player class
 * @author joostouwerling
 *
 */

public class Player {
	
	private int id;
	private String name;
	
	public Player(int id, String name) {
		setId(id);
		setName(name);
	}
	
	public Player(String name) throws Exception {
		this(0, name);
		if (name == null || name == "")
			throw new Exception("A player should have a name.");
	}
	
	
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
}
