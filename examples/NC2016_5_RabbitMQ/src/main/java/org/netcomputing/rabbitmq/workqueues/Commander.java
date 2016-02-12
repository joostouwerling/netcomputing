package org.netcomputing.rabbitmq.workqueues;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

//If you put a . in the command the worker that picksup the message will sleep for a while,
//this shows the round-robbin but only amongst the 'free' workers

public class Commander extends JFrame implements ActionListener {
	private static final String TASK_QUEUE_NAME = "task_queue";
	JTextField command;
	JButton send;

	public Commander(String title, int x, int y) {
		super(title);
		command = new JTextField(10);
		send = new JButton("send");
		this.getContentPane().add(command, BorderLayout.WEST);
		this.getContentPane().add(send, BorderLayout.EAST);
		send.addActionListener(this);
		setSize(200, 100);
		setLocation(x, y);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	public void actionPerformed(ActionEvent arg0) {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection;
		try {
			connection = factory.newConnection();
			Channel channel = connection.createChannel();
			channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
			String message = new Date() + ":" + command.getText();
			channel.basicPublish("", TASK_QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
			System.out.println(" [x] Sent '" + message + "'");
			channel.close();
			connection.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}