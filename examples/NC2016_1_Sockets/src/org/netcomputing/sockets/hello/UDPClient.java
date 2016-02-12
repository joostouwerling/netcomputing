package org.netcomputing.sockets.hello;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class UDPClient {
	private static String SERVER_ADDRESS = "localhost";

	public static void main(String[] args) {
		DatagramSocket aSocket = null;
		try {
			aSocket = new DatagramSocket();
			byte[] m = "Hello world".getBytes();
			InetAddress aHost = InetAddress.getByName(SERVER_ADDRESS);
			DatagramPacket request = new DatagramPacket(m, m.length, aHost,
					UDPServer.SERVER_PORT);
			aSocket.send(request);
			// get reply
			byte[] buffer = new byte[1000];
			DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
			aSocket.receive(reply);
			System.out.println("Reply: " + new String(reply.getData()));
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
