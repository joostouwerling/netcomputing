package UDP;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.sql.SQLException;
import database.Connector;

public class UDPServer {

	public static final int SERVER_PORT = 7376;
	
	public static byte[] getBytes(Package pack ) throws IOException{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutput out = null;
		out = new ObjectOutputStream(bos);
		out.writeObject(pack);
		byte[] yourBytes = bos.toByteArray();
		return yourBytes; 
	}
	
	public static void main(String[] args) throws Exception {
		Connector connector = new Connector(); 
		DatagramSocket aSocket = null;
		try {
			aSocket = new DatagramSocket(SERVER_PORT);
			byte[] buffer = new byte[1000];
			System.out.println("Server is waiting");
			while (true) {
				DatagramPacket request = new DatagramPacket(buffer, buffer.length);
				aSocket.receive(request);
				String receivedString = new String(request.getData()); 
				String parameters[] = receivedString.split("\\r?\\n");
				float magnitude = Float.parseFloat(parameters[2]); 
				float longitude = Float.parseFloat(parameters[3]); 
				try {
					connector.storePackage(parameters[0], parameters[1], magnitude, longitude);
					connector.printDatabase();
	            } catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (aSocket != null)
				aSocket.close();
		}
		try {
			connector.closeConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}