package org.netcomputing.sockets.hello;
import java.io.*;
import java.net.*;

public class TCPServer {
	static int SERVER_PORT = 7374;

	public static void main(String[] args) {
		TCPServer server = new TCPServer();
		server.listen();
	}

	public void listen() {
		try {
			ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
			System.out.println("Server is listening...");
			while (true) {
				Socket clientSocket = serverSocket.accept();
				//start a thread that waits for a client message coming in
				new ConnectionThread(clientSocket);
			}
		}
		catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}

	class ConnectionThread extends Thread {
		DataInputStream in;
		DataOutputStream out;
		Socket clientSocket;

		public ConnectionThread(Socket aSocket) {
			try {
				clientSocket = aSocket;
				in = new DataInputStream(clientSocket.getInputStream());
				out = new DataOutputStream(clientSocket.getOutputStream());
				this.start();
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}

		public void run() {
			try {
				String data = in.readUTF();
				System.out.println("client>" + data);
				String reversed = new StringBuffer(data).reverse().toString();
				out.writeUTF(reversed);
			} catch (IOException ioException) {
				ioException.printStackTrace();
			} finally {
				try {
					clientSocket.close();
				} catch (IOException ioException) {
					ioException.printStackTrace();
				}
			}
		}
	}
}