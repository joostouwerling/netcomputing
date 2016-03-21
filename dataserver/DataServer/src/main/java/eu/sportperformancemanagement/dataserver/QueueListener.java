package eu.sportperformancemanagement.dataserver;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import eu.sportperformancemanagement.common.SpmConstants;
import eu.sportperformancemanagement.common.LocationRequest;

/**
 * 
 * This class listens on the Rabbit MQ channel for location
 * requests. A location request is identified by the routing key
 * SpmConstants.KEY_LOCATIONS_MATCH_PLAYER. Furthermore, SpmConstants
 * also defines the server and the exchange name.
 * 
 * When a Location Request is received, this is parsed and send to
 * Location Fetcher instance, which handles it further.
 * 
 * The class should run in its own thread - hence it implements Runnable.
 * 
 * @author Joost Ouwerling <j.t.ouwerling@student.rug.nl>
 *
 */
public class QueueListener implements Runnable {
	
	/**
	 * Used for logging
	 */
	private static final Logger logger =
	        Logger.getLogger(QueueListener.class.getName());
	
	/**
	 * This class fetches a location when a request is coming in.
	 */
	private LocationFetcher location_fetcher;
	
	/**
	 * The main thread implementation. Makes the channel and listens for requests.
	 */
	public void run() {
		
		// Create the location fetcher
		location_fetcher = new LocationFetcher();
		
		/**
		 * Initialize the connection with the channel, using a ConnectionFactory
		 * object. Then declare the exchange and bind the queue. Exchange name and 
		 * the server address are defined in SpmConstants. 
		 * 
		 * When connecting succeeds, start listening on the queue. In case
		 * of an exception, log it and return the thread - not much we can do
		 * in that case.
		 */
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(SpmConstants.QUEUE_SERVER);
		Connection connection;
		Channel channel;
		String queueName;
		// Try to make a connection with the channel
		try {
			connection = factory.newConnection();
			channel = connection.createChannel();
			channel.exchangeDeclare(SpmConstants.EXCHANGE_NAME, "fanout");
		    queueName = channel.queueDeclare().getQueue();
		    // Listen for all routing keys on this exchange
		    channel.queueBind(queueName, SpmConstants.EXCHANGE_NAME, "");
			// We are connected!
			logger.log(Level.INFO, "Listening for messages on Rabbit MQ server " + SpmConstants.QUEUE_SERVER +
									" at exchange " + SpmConstants.EXCHANGE_NAME + " and queue name " + queueName);
		// We could not connect. Since no listening is possible now, return the thread.
		} catch (Exception ex) {
			logger.log(Level.SEVERE, "Can not create Rabbit MQ channel. Exiting QueueListener.", ex);
			return;
		}
	    
		/**
		 * Build a consumer object. This consumer handles the incoming
		 * deliverables. When the incoming packet has the routing key
		 * SpmConstants.KEY_LOCATIONS_MATCH_PLAYER, we know it is a 
		 * location request. Send the incoming string message to handleLocationFetch,
		 * which further handles the case.
		 */
	    Consumer consumer = new DefaultConsumer(channel) {
	    	@Override
	        public void handleDelivery(String consumerTag, Envelope envelope,
	                                   AMQP.BasicProperties properties, byte[] body) throws IOException {
	    		// Check if the routing key is equal to what we expect
	    		logger.log(Level.INFO, "Received message with routingkey " + envelope.getRoutingKey());
	        	String message = new String(body, "UTF-8");
	        	if (envelope.getRoutingKey().equals(SpmConstants.KEY_LOCATIONS_MATCH_PLAYER))
	        		handleLocationFetch(message);
	        	else
	        		logger.log(Level.INFO, "No handler for routing key " + envelope.getRoutingKey() + " found");
	        }
	    };
	    
	    /**
	     * Final step. Start listening on the queue with the consumer we created
	     */
	    try {
	    	channel.basicConsume(queueName, true, consumer);
	    } catch (Exception ex) {
	    	logger.log(Level.SEVERE, "Error in consuming from queue", ex);
	    }
	}
	
	/**
	 * Handle an incoming location request message. If it
	 * is parsed correctly, pass it on to location fetcher.
	 * Otherwise, print the error to the log and return.
	 * @param message the string representation of a LocationRequest
	 */
	private void handleLocationFetch(String message) {
		LocationRequest locRequest;
		try {
			locRequest = LocationRequest.parseRequest(message);
		} catch (Exception ex) {
			logger.log(Level.SEVERE, "Could not parse location request from " + message, ex);
			return;
		}
		// We have the Location Request now. Ask the Location Fetcher to handle the request.
		location_fetcher.fetchLocations(locRequest);
	}
	
}
