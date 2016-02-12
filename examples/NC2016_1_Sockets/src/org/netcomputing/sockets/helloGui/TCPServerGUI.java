package org.netcomputing.sockets.helloGui;

import java.awt.Color;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.border.Border;

/**
 * Basic TCP socket example, server side.
 */
public class TCPServerGUI extends JFrame {
	static int SERVER_PORT = 7375;

	private TextPanel textpanel;

	public TCPServerGUI() {
		super("Server");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		textpanel = new TextPanel();
		Border paneEdge = BorderFactory.createLineBorder(new Color(64,128,0),10);
		textpanel.setBorder(paneEdge);
		this.getContentPane().add(textpanel);
		setSize(500, 300);
		setVisible(true);
	}

	public static void main(String[] args) {
		TCPServerGUI server = new TCPServerGUI();
		server.listen();
	}

	public void listen() {
		try {
			ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
			textpanel.add("Server is listening...\n");
			while (true) {
				Socket clientSocket = serverSocket.accept();
				// start a thread that waits for a client message coming in
				ConnectionThread c = new ConnectionThread(clientSocket);
			}
		} catch (IOException ioException) {
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
				textpanel.add("from " + clientSocket.getInetAddress() + ":"
						+ clientSocket.getPort() + ">" + data);
				String reversed = new StringBuffer(data).reverse().toString();
				out.writeUTF(reversed);
				textpanel.add("... returned:" + reversed + "\n");
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