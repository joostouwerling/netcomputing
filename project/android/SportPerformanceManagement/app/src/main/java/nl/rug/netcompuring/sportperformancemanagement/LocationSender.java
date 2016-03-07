package nl.rug.netcompuring.sportperformancemanagement;

import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by joostouwerling on 02/03/16.
 */
public class LocationSender {

    private static final String TAG = "LocationSender";

    private String mServerAddress;
    private int mServerPort;
    private String mMatchId;

    DatagramSocket mSocket;
    InetAddress mHost;

    public LocationSender(String matchId, String address, int port) {
        mServerAddress = address;
        mServerPort = port;
        mMatchId = matchId;
    }

    public void send(Location loc) {
        new SendLocationTask().execute(loc.getLatitude() + "\n" + loc.getLongitude() + "\n" + mMatchId);
    }

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
                e.printStackTrace();
                return false;
            }
            return true;
        }

    }
}