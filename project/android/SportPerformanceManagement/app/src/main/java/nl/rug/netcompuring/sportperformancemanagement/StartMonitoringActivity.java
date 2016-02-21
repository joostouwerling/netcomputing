package nl.rug.netcompuring.sportperformancemanagement;

import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.common.api.GoogleApiClient;

import static com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import static com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;

public class StartMonitoringActivity extends AppCompatActivity implements ConnectionCallbacks, OnConnectionFailedListener {


    private GoogleApiClient mGoogleApiClient;
    private boolean isConnected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_monitoring);
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
                findLocation();
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

        if (mGoogleApiClient == null) {
            mGoogleApiClient= new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();

        }
    }

    private void findLocation() {
        if (!isConnected)
            return;
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location != null) {
            Toast toast = Toast.makeText(getApplicationContext(), location.toString(), Toast.LENGTH_LONG);
            toast.show();
        }
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

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }



    @Override
    public void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected())
            mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        isConnected = true;
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
