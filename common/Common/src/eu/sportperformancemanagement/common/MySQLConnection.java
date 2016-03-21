package eu.sportperformancemanagement.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A (sort of singleton) class which can be used to retrieve a connection to
 * a MySQL server. When the class is loaded for the first time,
 * an attempt to make a connection is made. If this succeeds, conn
 * contains the MySQL Connection. This connection can be retrieved by
 * the static method getConnection(). The method throws an exception when
 * no connection is available.
 * 
 * The class gets the connection parameters from the environment variables.
 * This is to make sure that the parameters don't end up in repositories and 
 * to make it easy to change connection parameters without editing the code.
 * The following environment variables are used:
 * MYSQL_SERVER (i.e. localhost:3306)
 * MYSQL_DATABASE (i.e. locations)
 * MYSQL_USERNAME (i.e. user123)
 * MYSQL_PASSWORD (i.e. pass123)
 * 
 * An severe logging message is created when one of the above variables 
 * is not available.
 * 
 * In Eclipse, these variables can be set at Run > Run configurations
 * (select the Environment tab, which may be hidden with a >>)
 * 
 * @author Joost Ouwerling <j.t.ouwerling@student.rug.nl>
 *
 */

public class MySQLConnection {
	
	/**
	 * Used for logging
	 */
	private static final Logger logger =
	        Logger.getLogger(MySQLConnection.class.getName());
	
	/**
	 * A singleton instance of this class
	 */
	private static MySQLConnection singleton = new MySQLConnection();
	
	/**
	 * Holds the connection to the database
	 */
	private Connection conn = null;
	
	/**
	 * Private constructor. Connects to the database.
	 */
	private MySQLConnection() {
		connect();
	}
	
	/**
	 * Returns the connection of the singleton MySQLConnection.
	 * @return a Connection object with a MySQL connection
	 * @throws Exception if there is no connection available
	 */
	public static Connection instance() throws Exception {
		if (!singleton.isConnected())
			throw new Exception("MySQL connection not available.");
		return singleton.getConnection();
	}
	
	/**
	 * @return the MySQL connection
	 */
	private Connection getConnection() {
		return conn;
	}
	
	/**
	 * @return true if connected to the database, false otherwise
	 */
	private boolean isConnected() {
		return conn != null;
	}
	
	/**
	 * Connects to the MySQL database, with the parameters retrieved 
	 * by using getConnectionParameters.
	 */
	private void connect() {
		try {
			Map<String, String> connParams = getConnectionParameters();
			String url = "jdbc:mysql://" + connParams.get("server") + "/"
							+ connParams.get("database") + "?autoReconnect=true";
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(url, connParams.get("username"), connParams.get("password"));
			logger.log(Level.INFO, "MySQL connection made");
		} catch (Exception ex) {
			logger.log(Level.SEVERE, "Could not make MySQL Connection", ex);
		}
	}
	
	/**
	 * Returns a map with the following parameters:
	 * server, database, username and password
	 * These values are fetched from the environment variables.
	 * @return a map containing the 4 connection parameters
	 * @throws Exception if one of the environment keys is not available
	 */
	private Map<String, String> getConnectionParameters() throws Exception {
		Map<String, String> env = System.getenv();
		Map<String, String> params = new HashMap<String, String>();
		params.put("server", getFromEnvironment(env, "MYSQL_SERVER"));
		params.put("database", getFromEnvironment(env, "MYSQL_DATABASE"));
		params.put("username", getFromEnvironment(env, "MYSQL_USERNAME"));
		params.put("password", getFromEnvironment(env, "MYSQL_PASSWORD"));
		return params;
	}
	
	/**
	 * Fetches the environment variable key from the environment. If no key exists
	 * in the env map, throw an exception.
	 * @param env the environment variable map
	 * @param key the environment variable we're looking for
	 * @return the value of the environment variable
	 * @throws Exception if the key is not found in the environment variables
	 */
	private String getFromEnvironment(Map<String, String> env, String key) throws Exception {
		if (!env.containsKey(key))
			throw new Exception(key + " not available in environment variables.");
		return env.get(key);
	}
	
	
}
