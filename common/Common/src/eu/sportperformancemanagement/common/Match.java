package eu.sportperformancemanagement.common;

/**
 * Created by joostouwerling on 24/02/16.
 */
public class Match {

    private String name;
    private int id;
    private String server;
    private int port;

    public Match(int id, String name, String server, int port) {
        setId(id);
        setName(name);
        setServer(server);
        setPort(port);
    }
    
    public Match(String name, String server, int port) throws Exception {
    	this(0, name, server, port);
		if (name == null || name == "")
    		throw new Exception("A match should have a name.");
    	if (server == null || server == "")
    		throw new Exception("A match should have a name.");
    	if (port <= 0)
        	throw new Exception("The port can not be zero or negative.");
    }
    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

}
