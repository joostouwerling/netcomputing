package eu.sportperformancemanagement.common;

/**
 * 
 * A class that holds the data for a Match object. It 
 * stores a name, an id, a server and a port. The last two
 * are used to send the LocationPackets to.
 * 
 * @author Joost Ouwerling <j.t.ouwerling@student.rug.nl>
 */
public class Match {

	/**
	 * The name of the match
	 */
    private String name;
    
    /**
     * The id of the match
     */
    private int id;
    
    /**
     * The server where the location packets should be send to
     */
    private String server;
    
    /**
     * The port of the server
     */
    private int port;

    /**
     * Initialize all fields
     * @param id
     * @param name
     * @param server
     * @param port
     */
    public Match(int id, String name, String server, int port) {
        setId(id);
        setName(name);
        setServer(server);
        setPort(port);
    }
    
    /**
     * Construct a match with no ID. This is only used when
     * a match is constructed from user input, for e.g. inserting
     * it in the database. Therefore, some basic checks are done
     * to check if the input is correct
     * @param name must be not-null and unequal to ""
     * @param server must be not-null and unequal to ""
     * @param port must be strictly positive
     * @throws Exception if one of the above constraints is not satisfied
     */
    public Match(String name, String server, int port) throws Exception {
    	this(0, name, server, port);
		if (name == null || name == "")
    		throw new Exception("A match should have a name.");
    	if (server == null || server == "")
    		throw new Exception("A match should have a name.");
    	if (port <= 0)
        	throw new Exception("The port can not be zero or negative.");
    }
    
  
    /**
     * @return name of the match
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of the match
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * @return id of the match
     */
    public int getId() {
        return id;
    }

    /**
     * Set the id of the match
     * @param name
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return server of the match
     */
    public String getServer() {
        return server;
    }


    /**
     * Set the server of the match
     * @param name
     */
    public void setServer(String server) {
        this.server = server;
    }

    /**
     * @return port of the server
     */
    public int getPort() {
        return port;
    }

    /**
     * Set the port of the match
     * @param name
     */
    public void setPort(int port) {
        this.port = port;
    }

}
