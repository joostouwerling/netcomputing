package eu.sportperformancemanagement.player;

import eu.sportperformancemanagement.common.LocationPacket;
import eu.sportperformancemanagement.common.Match;
import eu.sportperformancemanagement.common.Player;

import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Date;

/**
 * Send the location to the server given by match, over UDP. It uses the Location Packet
 * in the common package to correctly format the message.
 *
 * @author Joost Ouwerling
 */
public class LocationSender {

    /**
     * A tag used for logging.
     */
    private static final String TAG = "LocationSender";

    /**
     * The server address where the packet needs to be send to.
     */
    private String mServerAddress;

    /**
     * The server port
     */
    private int mServerPort;

    /**
     * The match id which belongs to this packet
     */
    private int mMatchId;

    /**
     * The player id which belongs to this packet
     */
    private int mPlayerId;

    /**
     * The UDP socket for sending location packets to the server
     */
    DatagramSocket mSocket;

    /**
     * The inet address of the server
     */
    InetAddress mHost;

    /**
     * Set up the sender object
     * @param m the match data, which holds match id, server and server port
     * @param player the player for which location monitoring is done. Used to fetch player id
     */
    public LocationSender(Match m, Player player) {
        mServerAddress = m.getServer();
        mServerPort = m.getPort();
        mMatchId = m.getId();
        mPlayerId = player.getId();
    }

    /**
     * Send an (Android) Location to the server. It formats the data in a Location packet and send
     * the string representation using an AsynTask to the webserver, to prevent clogging of the UI
     * thread
     * @param loc the location
     */
    public void send(Location loc) {
        LocationPacket lp = new LocationPacket(mPlayerId, mMatchId, new Date(), loc.getLatitude(), loc.getLongitude());
        new SendLocationTask().execute(lp.toString());
    }

    /**
     * A class which sends the string representation of the Location Packet to the server.
     * It extends AsyncTask, so it runs in its own thread. This prevents clogging of the UI thread.
     */
    private class SendLocationTask extends AsyncTask<String, String, Boolean> {

        protected Boolean doInBackground(String... args) {
            Log.i(TAG, "Sending " + args[0]);
            try {
                if (mSocket == null)
                    mSocket = new DatagramSocket();
                if (mHost == null)
                    mHost = InetAddress.getByName(mServerAddress);
                DatagramPacket locdata = new DatagramPacket(args[0].getBytes(), args[0].length(), mHost, mServerPort);
                mSocket.send(locdata);
            } catch (Exception e) {
                Log.e(TAG, "Could not send location: " + e.toString());
                return false;
            }
            return true;
        }

    }
}
