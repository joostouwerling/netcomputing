package eu.sportperformancemanagement.dataserver;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

import eu.sportperformancemanagement.common.LocationPacket;

/**
 * This class implements Runnable and listens for incoming
 * LocationPackets, sent by the Player (Android App). It
 * uses UDP sockets to listen for packets. WHen the packets come in,
 * LocationPacket has a static method that tries to parse the string.
 * 
 * If parsing was succesfull, the program tries to store the location packet
 * in the database, so it can be retrieved when heatmaps are made.
 * 
 * @author Joost Ouwerling <j.t.ouwerling@student.rug.nl>
 *
 */
public class LocationListener implements Runnable {
	
	/**
	 * Used for logging
	 */
	private static final Logger logger =
	        Logger.getLogger(LocationListener.class.getName());
	
	/**
	 * The socket used for handling location packets
	 */
	private DatagramSocket socket = null;
	
	/**
	 * The port on which this server is listening for locations.
	 */
	public static final int DEFAULT_PORT = 7376;
	
	/**
	 * The actual port to use
	 */
	private int port;
	
	
	/**
	 * Used for storing the incoming location packets.
	 */
	private LocationDAO locationsDao;
	
	/**
	 * Start Listening on port "port"
	 * @param port
	 */
	public LocationListener(int port) {
		this.port = port;
	}
	
	/**
	 * Use the default port.
	 */
	public LocationListener() {
		this(DEFAULT_PORT);
	}
	
	
	/**
	 * Open the socket and start listening
	 */
	public void run() {
		locationsDao = new LocationDAO();
		if (!openSocket())
			return;
		listen();
	}
	
	/**
	 * Open the Datagram Socket If it fails, the exception is
	 * logged and false is returned, so the thread can be closed.
	 */
	private boolean openSocket() {
		try {
			socket = new DatagramSocket(port);
			logger.log(Level.INFO, "Socket opened at port " + port);
		} catch (SocketException ex) {
			logger.log(Level.SEVERE, "Could not start datagram socket. Exiting LocationListener.", ex);
			return false;
		}
		return true;
	}
	
	/**
	 * Listen for incoming location packets. When a packet
	 * is received, send it to handlePacket(). When an exception occurs
	 * in the socket process, log the error and close the socket. 
	 */
	private void listen() {
		byte[] buffer = new byte[1000];
		try {
			logger.log(Level.INFO, "Started listening for packets");
			while (true) {
				DatagramPacket request = new DatagramPacket(buffer, buffer.length);
				socket.receive(request);
				String receivedString = new String(request.getData());
				logger.log(Level.INFO, "Receved the following location packet: " + receivedString);
				handlePacket(receivedString);
			}
		} catch (Exception ex) {
			logger.log(Level.SEVERE, "Error parsing an incoming request", ex);
		}
		socket.close();
	}
	
	/**
	 * Handle a single packet. When the location packet is null,
	 * the packet could not be read. Log this and return. This will
	 * not stop the thread, so other valid packets will still be read. 
	 * If parsing is succesfull, store the location packet in the database.
	 */
	private void handlePacket(String packet) {
		LocationPacket locationPacket = LocationPacket.parsePacket(packet);
		if (locationPacket == null) {
			logger.log(Level.WARNING, "Packet was not parseable. \n" + packet);
			return;
		}
		try {
			locationsDao.insert(locationPacket);
			logger.log(Level.INFO, "Packet inserted into database.");
		} catch (Exception ex) {
			logger.log(Level.WARNING, "Could not insert the packet in the database.", ex);
		}
	}
	
}
