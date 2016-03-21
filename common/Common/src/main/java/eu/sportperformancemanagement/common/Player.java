package eu.sportperformancemanagement.common;

/**
 * Player class. Holds an id and a name.
 * 
 * @author Joost Ouwerling <j.t.ouwerling@student.rug.nl>
 */

public class Player {
	
	/**
	 * The player id
	 */
	private int id;
	
	/**
	 * The player name
	 */
	private String name;
	
	/**
	 * Construct an object with all parameters
	 * @param id
	 * @param name
	 */
	public Player(int id, String name) {
		setId(id);
		setName(name);
	}
	
	/**
     * Construct a player with no ID. This is only used when
     * a player is constructed from user input, for e.g. inserting
     * it in the database. Therefore, some basic checks are done
     * to check if the input is correct
     * @param name must be not-null and unequal to ""
     * @throws Exception if the above constraints is not satisfied
     */
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
