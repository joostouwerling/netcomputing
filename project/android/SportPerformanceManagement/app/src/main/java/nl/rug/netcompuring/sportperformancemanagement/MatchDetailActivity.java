package nl.rug.netcompuring.sportperformancemanagement;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.common.api.GoogleApiClient;

import static com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import static com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;

public class MatchDetailActivity extends AppCompatActivity implements
        ConnectionCallbacks, OnConnectionFailedListener, LocationListener {

    public static final String ARG_MATCH_ID = "match_id";


    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private int mCountLocations = 0;
    private String mMatchId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_match);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final TextView tv_explanation = (TextView) findViewById(R.id.explanation);
        final FloatingActionButton fab_start = (FloatingActionButton) findViewById(R.id.fab_start);
        final FloatingActionButton fab_pause = (FloatingActionButton) findViewById(R.id.fab_pause);

        fab_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fab_pause.show();
                fab_start.hide();
                tv_explanation.setText(R.string.pause_explanation);
                Snackbar.make(view, "Monitoring started!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        fab_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fab_pause.hide();
                fab_start.show();
                tv_explanation.setText(R.string.start_explanation);
                Snackbar.make(view, "Monitoring ended!", Snackbar.LENGTH_LONG).show();
            }
        });

        if (savedInstanceState == null)
            mMatchId = getIntent().getStringExtra(this.ARG_MATCH_ID);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Match #" + mMatchId);
        }

        if (mGoogleApiClient == null) {
            Log.i("MatchDetailActivity", "Building mGoogleApiClient");
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        if (mLocationRequest == null) {
            Log.i("MatchDetailActivity", "Making LocationRequest");
            mLocationRequest = LocationRequest.create()
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                    .setInterval(5 * 1000)
                    .setFastestInterval(1 * 1000);
        }
    }

    private void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    private void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }


    @Override
    public void onLocationChanged(Location loc) {
        Log.w("MatchDetailActivity", Integer.toString(mCountLocations));
        mCountLocations++;
        Toast.makeText(this, mCountLocations + ": " + loc.toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start_monitoring, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == android.R.id.home) {
            NavUtils.navigateUpTo(this, new Intent(this, MatchListActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient == null) {
            return;
        }
        if (!mGoogleApiClient.isConnected())
            mGoogleApiClient.connect();
        if (mGoogleApiClient.isConnected())
            startLocationUpdates();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mGoogleApiClient == null)
            return;

        if (mGoogleApiClient.isConnected())
            stopLocationUpdates();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        Log.i("MatchDetailActivity", "onConnected");
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("MatchDetailActivity", "onConnectionSuspended");
        Toast.makeText(this, "Connection suspended!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i("MatchDetailActivity", "onConnectionFailed");
        Toast.makeText(this, "Connection failed!", Toast.LENGTH_SHORT).show();
    }
}
