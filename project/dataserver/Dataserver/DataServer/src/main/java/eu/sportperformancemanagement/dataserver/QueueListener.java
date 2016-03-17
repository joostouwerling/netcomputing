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
import com.sportperformancemanagement.common.LocationRequest;
import com.sportperformancemanagement.common.QueueConstants;

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
	
	public void run() {
		
		location_fetcher = new LocationFetcher();
		
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(QueueConstants.QUEUE_SERVER);
		Connection connection;
		Channel channel;
		String queueName;
		// Try to make a connection with the channel
		try {
			connection = factory.newConnection();
			channel = connection.createChannel();
			channel.exchangeDeclare(QueueConstants.EXCHANGE_NAME, "fanout");
		    queueName = channel.queueDeclare().getQueue();
		    // Listen for all routing keys on this exchange
		    channel.queueBind(queueName, QueueConstants.EXCHANGE_NAME, "");
		} catch (Exception ex) {
			logger.log(Level.SEVERE, "Can not create Rabbit MQ channel", ex);
			return;
		}
		
		// We are connected!
		logger.log(Level.INFO, "Listening for messages on Rabbit MQ server " + QueueConstants.QUEUE_SERVER +
								" at exchange " + QueueConstants.EXCHANGE_NAME + " and queue name " + queueName);
	    
		// Build the consumer. Messages are handled based on the routing key
	    Consumer consumer = new DefaultConsumer(channel) {
	    	@Override
	        public void handleDelivery(String consumerTag, Envelope envelope,
	                                   AMQP.BasicProperties properties, byte[] body) throws IOException {
	    		logger.log(Level.INFO, "Received message with routingkey " + envelope.getRoutingKey());
	        	String message = new String(body, "UTF-8");
	        	if (envelope.getRoutingKey().equals(QueueConstants.KEY_LOCATIONS_MATCH_PLAYER))
	        		handleLocationFetch(message);
	        	else
	        		logger.log(Level.INFO, "No handler for routing key " + envelope.getRoutingKey() + " found");
	        }
	    };
	    
	    // Start listening on the queue with the given consumer.
	    try {
	    	channel.basicConsume(queueName, true, consumer);
	    } catch (Exception ex) {
	    	logger.log(Level.SEVERE, "Error in consuming from queue", ex);
	    }
	}
	
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
