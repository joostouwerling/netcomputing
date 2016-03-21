package com.sportperformancemanagement.player;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationListener;

/**
 * https://github.com/googlesamples/android-play-location/blob/master/LocationUpdates/app/src/main/java/com/google/android/gms/location/sample/locationupdates/MainActivity.java
 * Created by joostouwerling on 02/03/16.
 */
public class LocationMonitor implements
        ConnectionCallbacks, OnConnectionFailedListener, LocationListener {

    public static final long UPDATE_INTERVAL = 5000;
    public static final long FAST_UPDATE_INTERVAL = 1000;

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Boolean mIsRequestingLocations;
    private LocationSender mSender;
    private static final String TAG = "LocationMonitor";

    public LocationMonitor (LocationSender sender, Context ctx) {
        Log.i(TAG, "Building LocationMonitor");
        mSender = sender;
        mIsRequestingLocations = false;
        buildGoogleApiClient(ctx);
    }

    public void startMonitoring() {
        if (!mIsRequestingLocations)
        {
            mIsRequestingLocations = true;
            if (mGoogleApiClient.isConnected())
                startLocationUpdates();
        }
    }

    public void stopMonitoring() {
        if (mIsRequestingLocations)
        {
            mIsRequestingLocations = false;
            if (mGoogleApiClient.isConnected())
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    private void startLocationUpdates() {
        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);
        } catch (SecurityException se) {
            Log.i(TAG, se.toString());
        }
    }

    private void buildGoogleApiClient(Context ctx) {
        Log.i(TAG, "Building Google Api Client");
        mGoogleApiClient = new GoogleApiClient.Builder(ctx)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
        createLocationRequest();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        Log.i(TAG, "Connected to GoogleApiClient");
        if (mIsRequestingLocations) {
            startLocationUpdates();
        }
    }

    /**
     * Callback that fires when the location changes.
     */
    @Override
    public void onLocationChanged(Location location) {
        Log.i(TAG, "Received location: " + location.toString());
        mSender.send(location);
    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }

    private void createLocationRequest() {
        Log.i(TAG, "Creating location request");
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FAST_UPDATE_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

}
