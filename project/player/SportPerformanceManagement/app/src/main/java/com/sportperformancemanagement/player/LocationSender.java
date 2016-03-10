package com.sportperformancemanagement.player;

import com.sportperformancemanagement.common.LocationPacket;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Date;

/**
 * Created by joostouwerling on 02/03/16.
 */
public class LocationSender {

    private static final String TAG = "LocationSender";

    private String mServerAddress;
    private int mServerPort;
    private int mMatchId;

    DatagramSocket mSocket;
    InetAddress mHost;

    public LocationSender(Match m) {
        mServerAddress = m.getServer();
        mServerPort = m.getPort();
        mMatchId = m.getId();
    }

    public void send(Location loc) {
        LocationPacket lp = new LocationPacket(1, mMatchId, new Date(), loc.getLatitude(), loc.getLongitude());
        new SendLocationTask().execute(lp.toString());
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
