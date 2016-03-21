package com.sportperformancemanagement.player;

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

import com.sportperformancemanagement.common.Match;
import com.sportperformancemanagement.common.Player;
import com.sportperformancemanagement.player.sportperformancemanagement.R;

public class MatchDetailActivity extends AppCompatActivity  {

    public static final String ARG_MATCH = "match";
    public static final String TAG = "MatchDetailActivity";

    private LocationMonitor mLocationMonitor;
    private Match mMatch;
    private Player mPlayer;
    private PlayerStorage playerStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        playerStorage = new PlayerStorage(this);
        if (!playerStorage.hasPlayer()) {
            startActivity(new Intent(this, PlayerActivity.class));
            return;
        }
        mPlayer = playerStorage.getPlayer();

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
                startMonitoring();
            }
        });

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

        if (savedInstanceState == null)
            mMatch = (Match) getIntent().getParcelableExtra(ARG_MATCH);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(mMatch.getName());
        }

    }

    private void startMonitoring() {
        if (mLocationMonitor == null)
            mLocationMonitor = new LocationMonitor(new LocationSender(mMatch, mPlayer), getApplicationContext());
        mLocationMonitor.startMonitoring();
    }

    private void stopMonitoring() {
        if (mLocationMonitor != null)
            mLocationMonitor.stopMonitoring();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            NavUtils.navigateUpTo(this, new Intent(this, MatchListActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mLocationMonitor != null)
            mLocationMonitor.startMonitoring();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mLocationMonitor != null)
            mLocationMonitor.stopMonitoring();
    }

}
