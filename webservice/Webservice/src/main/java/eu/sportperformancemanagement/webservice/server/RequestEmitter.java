package eu.sportperformancemanagement.webservice.server;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import eu.sportperformancemanagement.common.LocationRequest;
import eu.sportperformancemanagement.common.SpmConstants;

/**
 * This class emits requests for locations on the Rabbit MQ
 * defined in SpmConstants.
 * 
 * @author Joost Ouwerling <j.t.ouwerling@student.rug.nl>
 *
 */

public class RequestEmitter {
	
	/**
	 * Used for logging
	 */
	private static final Logger logger =
	        Logger.getLogger(RequestEmitter.class.getName());
	
	
	/**
	 * Emit LocationRequest locreq to the queue with routing
	 * key SpmCOnstants.KEY_LOCATIONS_MATCH_PLAYER. It first 
	 * creates a connection to the channel. The exchange is set to
	 * fanout, so all listeners receive the request.
	 * 
	 * If the connection succeeds, it tries to send the message.
	 * Close the connection afterwards.
	 * 
	 * @param locreq the LocationRequest to emit
	 * @throws Exception if any step of the emitting fails
	 */
	public static void emit(LocationRequest locreq) throws Exception {
		try {
			logger.log(Level.INFO, "Received location request:\n" + locreq.toString() + "\nTrying to connect to the queue.");
			// Make a connection using the factory
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost(SpmConstants.QUEUE_SERVER);
			factory.setPort(SpmConstants.QUEUE_PORT);
			factory.setUsername(SpmConstants.QUEUE_USERNAME);
			factory.setPassword(SpmConstants.QUEUE_PASSWORD);
			Connection connection = factory.newConnection();
			// Create the channel and declare the exchange as fanout
			Channel channel = connection.createChannel();
			channel.exchangeDeclare(SpmConstants.EXCHANGE_NAME, "fanout");
			// Do a basic publish on the exchange
			channel.basicPublish(SpmConstants.EXCHANGE_NAME, SpmConstants.KEY_LOCATIONS_MATCH_PLAYER, null, locreq.toString().getBytes());
			logger.log(Level.INFO, "Succesfully published on the exchange");
			// Close the connection
			channel.close();
			connection.close();
		} catch (Exception ex) {
			logger.log(Level.WARNING, "Could not emit the request", ex);
			throw new Exception("Error while emitting the location request. See logs for more details.");
		}
	}
	
}
