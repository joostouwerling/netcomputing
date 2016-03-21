package eu.sportperformancemanagement.common;

/**
 * A set of constants used across different application parts.
 * @author Joost Ouwerling <j.t.ouwerling@student.rug.nl>
 *
 */
public class SpmConstants {

	/**
	 * The base url of the webservice
	 */
	public static final String WEBSERVICE_BASE_URL = "http://webservice.sportperformancemanagement.eu:8080/Webservice/rest/";
	
	/**
	 * The server where the queue can be found.
	 */
	public static final String QUEUE_SERVER = "rabbitmq.sportperformancemanagement.eu";
	

	/**
	 * The exchange name of the rabbit mq exchange.
	 */
	public static final String EXCHANGE_NAME = "communication";
	

	/**
	 * The routing key for fetching the locations for a certain match and player
	 */
	public static final String KEY_LOCATIONS_MATCH_PLAYER = "locations_match_player";
	
}
