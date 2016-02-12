package org.netcomputing.rabbitmq.helloworld;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;

public class Sender {
	private final static String QUEUE_NAME = "hello";
	public static void main(String[] argv) throws java.io.IOException {
		// create a connection to the server
		// The connection abstracts the socket connection, and takes care of
		// protocol version negotiation and authentication and so on for us.
		// Here we connect to a broker on the local machine - hence the
		// localhost. If we wanted to connect to a broker on a different machine
		// we'd simply specify its name or IP address here.
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		// Next we create a channel, which is where most of the API for getting
		// things done resides.
		Channel channel = connection.createChannel();
		// To send, we must declare a queue for us to send to; then we can
		// publish a message to the queue:
		// Declaring a queue is idempotent - it will only be created if it
		// doesn't exist already. The message content is a byte array, so you
		// can encode whatever you like there.
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		String message = "Hello World!";
		channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
		System.out.println(" [x] Sent '" + message + "'");
		//Lastly, we close the channel and the connection
		channel.close();
		connection.close();
	}
}
