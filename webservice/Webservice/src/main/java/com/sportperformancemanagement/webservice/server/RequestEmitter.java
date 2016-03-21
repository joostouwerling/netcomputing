package com.sportperformancemanagement.webservice.server;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.sportperformancemanagement.common.LocationRequest;
import com.sportperformancemanagement.common.QueueConstants;

public class RequestEmitter {
	
	public static void emitRequest(LocationRequest locreq, String routingKey) {
		try {
			System.out.println("Received location request" + locreq.toString());
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost(QueueConstants.QUEUE_SERVER);
			Connection connection = factory.newConnection();
			Channel channel = connection.createChannel();
			channel.exchangeDeclare(QueueConstants.EXCHANGE_NAME, "fanout");
			channel.basicPublish(QueueConstants.EXCHANGE_NAME, QueueConstants.KEY_LOCATIONS_MATCH_PLAYER, null, locreq.toString().getBytes());
			System.out.println("Sent message to queue!");
			System.out.println(channel.getChannelNumber());
			channel.close();
			connection.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
}
