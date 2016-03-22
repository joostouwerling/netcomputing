package eu.sportperformancemanagement.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A (sort of singleton) class which can be used to create a connection to
 * a MySQL server. When the class is loaded for the first time, it is initalized
 * in setup(). This means, the parameters are loaded from the environment (see below)
 * and the driver  is loaded. Then, when the static method create() is called,
 * a Connection object is returned with the parameters retrieved in the setup step.
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
	private Map<String, String> connParams;
	
	/**
	 * True if the class is properly initialized, false otherwise.
	 */
	private boolean initialized;
	
	/**
	 * Private constructor. Sets initialized to false,
	 * loads the connection parameters and loads
	 * the driver class.
	 */
	private MySQLConnection() {
		initialized = false;
		setup();
	}
	
	/**
	 * Returns a Connection object, based on the parameters in the environment
	 * @return a Connection object with a MySQL connection
	 * @throws Exception if there is no connection available
	 */
	public static Connection create() throws Exception {
		if (!singleton.isInitialized())
			throw new Exception("MySQL connection not available.");
		return singleton.createConnection();
	}
	
	/**
	 * @return the MySQL connection, based on the parameters in connParams
	 */
	private Connection createConnection() throws SQLException {
		String url = "jdbc:mysql://" + connParams.get("server") + "/"
				+ connParams.get("database");
		return DriverManager.getConnection(url, connParams.get("username"), connParams.get("password"));
	}
	
	/**
	 * @return true if the class is initialized, false otherwise.
	 */
	private boolean isInitialized() {
		return initialized;
	}
	
	/**
	 * Retrieve the MySQL connection parameters and load the driver class.
	 */
	private void setup() {
		try {
			connParams = getConnectionParameters();
			Class.forName("com.mysql.jdbc.Driver");
			initialized = true;
			logger.log(Level.INFO, "MySQL parameters read from environment & driver loaded.");
		} catch (Exception ex) {
			logger.log(Level.SEVERE, "Could not load MySQL params from environment or driver not found.", ex);
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
