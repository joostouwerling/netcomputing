package eu.sportperformancemanagement.player;

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
 * This class allows monitoring for locations using the google services. When it receives a location,
 * it tells the LocationSender in mSender, such that the location can be send to the server.
 *
 * Partly based on the following example by Google:
 * https://github.com/googlesamples/android-play-location/blob/master/LocationUpdates/app/src/main/java/com/google/android/gms/location/sample/locationupdates/MainActivity.java
 *
 * @author Joost Ouwerling <j.t.ouwerling@student.rug.nl>
 */
public class LocationMonitor implements
        ConnectionCallbacks, OnConnectionFailedListener, LocationListener {

    /**
     * A tag used for logging.
     */
    private static final String TAG = "LocationMonitor";


    /**
     * The maximum update interval time
     */
    public static final long UPDATE_INTERVAL = 5000;

    /**
     * The fast interval time. The app may update faster than UPDATE UINTERVAL, but never faster
     * than FAST UPDATE INTERVAL.
     */
    public static final long FAST_UPDATE_INTERVAL = 1000;

    /**
     * Used for the Google API's
     */
    private GoogleApiClient mGoogleApiClient;

    /**
     * A location request object. This is different than the eu.sportperformancemanagement.common
     * LocatioNRequest class. This one is provided by the google api and is used to tell the
     * api services how often and with what priority the locations should be provided.
     */
    private LocationRequest mLocationRequest;

    /**
     * A boolean value indicating whether or not we are requesting locations right now.
     */
    private Boolean mIsRequestingLocations;

    /**
     * A reference to a LocationSender object, which sends the retriueved locations to a server
     */
    private LocationSender mSender;

    /**
     * Constructs the object and builds the google api client.
     * @param sender the location sender where the locations need to go to.
     * @param ctx the context in which the monitoring happens (i.e. an Activity)
     */
    public LocationMonitor (LocationSender sender, Context ctx) {
        Log.i(TAG, "Building LocationMonitor");
        mSender = sender;
        mIsRequestingLocations = false;
        buildGoogleApiClient(ctx);
    }

    /**
     * Start monitoring if:
     * - We are not already monitoring, and
     * - the google api client is connected
     */
    public void startMonitoring() {
        if (!mIsRequestingLocations)
        {
            mIsRequestingLocations = true;
            if (mGoogleApiClient.isConnected())
                startLocationUpdates();
        }
    }

    /**
     * Stop monitoring if
     * - we are requesting locations, and
     * - the google api client is connected
     */
    public void stopMonitoring() {
        if (mIsRequestingLocations)
        {
            mIsRequestingLocations = false;
            if (mGoogleApiClient.isConnected())
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    /**
     * Start location updates. If this fails, log the error.
     */
    private void startLocationUpdates() {
        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);
        } catch (SecurityException se) {
            Log.i(TAG, se.toString());
        }
    }

    /**
     * Build the Google API client for requesting locations in the given context ctx.
     * It adds itself as the listeners and adds location services as the necessary api.
     * Afterwards, create the location request
     * @param ctx
     */
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

    /**
     * Create a location request with the parameters set in this class.
     * The priority is high accuracy.
     */
    private void createLocationRequest() {
        Log.i(TAG, "Creating location request");
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FAST_UPDATE_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    /**
     * When we are connected, start listebning for location updates!
     * @param connectionHint
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        Log.i(TAG, "Connected to GoogleApiClient");
        if (mIsRequestingLocations) {
            startLocationUpdates();
        }
    }

    /**
     * Callback that fires when the location changes.
     * Let the LocationSender send the location to the server
     */
    @Override
    public void onLocationChanged(Location location) {
        Log.i(TAG, "Received location: " + location.toString());
        mSender.send(location);
    }

    /**
     * When the connection is suspended, reconnect to the api.
     * @param cause
     */
    @Override
    public void onConnectionSuspended(int cause) {
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    /**
     * Failure. Log the error for debugging.
     * @param result
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }

}
