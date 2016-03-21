package eu.sportperformancemanagement.common;

public class QueueConstants {

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
