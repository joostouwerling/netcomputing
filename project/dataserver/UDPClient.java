package UDP;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class UDPClient {

	public static void main(String[] args) {
		DatagramSocket aSocket = null;
		System.out.println("Client is running");
		try {
			aSocket = new DatagramSocket();
			InetAddress aHost = InetAddress.getByName("localhost");
			String message = "Joost\n" + new SimpleDateFormat("dd-MM-yyyy").format(new Date()) + "\n" + "423.123\n" + "2123.1230\n";  
			byte[] m = message.getBytes(); 
			DatagramPacket request = new DatagramPacket(m, m.length, aHost,UDPServer.SERVER_PORT);	
			aSocket.send(request);
			System.out.println("Package sent");
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
