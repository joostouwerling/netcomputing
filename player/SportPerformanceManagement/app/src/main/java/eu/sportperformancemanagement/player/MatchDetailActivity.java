package eu.sportperformancemanagement.player;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.MenuItem;
import android.widget.TextView;

import eu.sportperformancemanagement.common.Match;
import eu.sportperformancemanagement.common.Player;

/**
 * The activity for showing a match details. It can also start monitoring
 * for locations for this particular match.
 *
 * @author Joost Ouwerling
 */
public class MatchDetailActivity extends AppCompatActivity  {

    /**
     * Used to fetch the match from the incoming intent
     */
    public static final String ARG_MATCH = "match";

    /**
     * A reference to the location monitor object
     */
    private LocationMonitor mLocationMonitor;

    /**
     * The match of this activity
     */
    private Match mMatch;

    /**
     * The player which was selected by the user
     */
    private Player mPlayer;


    /**
     * This is called when the activity is started. If the bundle is empty, we need to fetch
     * the match data from  the incoming intent.
     * @param savedInstanceState contains the match data
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * First, check if the player is set. If not, redirect to the player set activity
         */
        PlayerStorage playerStorage = new PlayerStorage(this);
        if (!playerStorage.hasPlayer()) {
            startActivity(new Intent(this, PlayerActivity.class));
            return;
        }
        mPlayer = playerStorage.getPlayer();

        /**
         * Set the correct layout with toolbar and action bar
         */
        setContentView(R.layout.activity_show_match);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(mMatch.getName());
        }

        /**
         * Find references for the UI elements.
         */
        final TextView tv_explanation = (TextView) findViewById(R.id.explanation);
        final FloatingActionButton fab_start = (FloatingActionButton) findViewById(R.id.fab_start);
        final FloatingActionButton fab_pause = (FloatingActionButton) findViewById(R.id.fab_pause);

        /**
         * When the start FAB is clicked, start listening for location updates.
         * Also, change the text, hide this bhutton and show the stop button.
         */
        fab_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fab_pause.show();
                fab_start.hide();
                tv_explanation.setText(R.string.pause_explanation);
                Snackbar.make(view, "Monitoring started!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                startMonitoring();
            }
        });


        /**
         * When the start FAB is clicked, stop listening for location updates.
         * Also, change the text, hide this bhutton and show the start button.
         */
        fab_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fab_pause.hide();
                fab_start.show();
                tv_explanation.setText(R.string.start_explanation);
                Snackbar.make(view, "Monitoring ended!", Snackbar.LENGTH_LONG).show();
                stopMonitoring();
            }
        });

        /**
         * Saved instance state is null? Fresh activity
         * Fetch the match from the intent
         */
        if (savedInstanceState == null)
            mMatch = (Match) getIntent().getParcelableExtra(ARG_MATCH);

    }

    /**
     * Start monitoring for locations on this match/player combi
     */
    private void startMonitoring() {
        if (mLocationMonitor == null)
            mLocationMonitor = new LocationMonitor(new LocationSender(mMatch, mPlayer), getApplicationContext());
        mLocationMonitor.startMonitoring();
    }

    /**
     * Stop monitoring for locations
     */
    private void stopMonitoring() {
        if (mLocationMonitor != null)
            mLocationMonitor.stopMonitoring();
    }


    /**
     * Add a handler for the "return" button in thje toolbar
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpTo(this, new Intent(this, MatchListActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * Resume monitoring
     */
    @Override
    public void onResume() {
        super.onResume();
        if (mLocationMonitor != null)
            mLocationMonitor.startMonitoring();
    }

    /**
     * Pause monitoring
     */
    @Override
    public void onPause() {
        super.onPause();
        if (mLocationMonitor != null)
            mLocationMonitor.stopMonitoring();
    }

}
