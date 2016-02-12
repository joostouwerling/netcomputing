package org.netcomputing.sockets.hello;
import java.io.*;
import java.net.*;

public class TCPClient {
	static String SERVER_ADDRESS = "localhost";
	static int SERVER_PORT = 7374;

	public static void main(String args[]) {
		Socket s=null;
		DataOutputStream out;
		DataInputStream in;
		try {
			s = new Socket(SERVER_ADDRESS, SERVER_PORT);
			in = new DataInputStream(s.getInputStream());
			out = new DataOutputStream(s.getOutputStream());
			out.writeUTF("Hello from client");
			String data = in.readUTF();
			System.out.println("From server: " + data);
		} catch (UnknownHostException e) {
			System.err.println(e);
		} catch (IOException e) {
			System.err.println(e);
		} finally {
			if (s != null)
				try {
					s.close();
				} catch (IOException e) {
					System.err.println(e);
				}
		}
	}
}
