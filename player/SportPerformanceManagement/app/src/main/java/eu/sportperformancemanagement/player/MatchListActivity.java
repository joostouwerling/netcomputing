package eu.sportperformancemanagement.player;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import eu.sportperformancemanagement.common.Match;
import eu.sportperformancemanagement.common.MatchJSON;
import eu.sportperformancemanagement.common.SpmConstants;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * This activity lists all matches and let the player select the match he
 * wants to monitor.
 *
 * @author Joost Ouwerling
 */
public class MatchListActivity extends AppCompatActivity {

    /**
     * Tag for logging
     */
    private static final String TAG = "MatchListActivity";

    /**
     * URL of the rest webservice which holds the matches
     */
    private static final String URL_MATCHES = SpmConstants.WEBSERVICE_BASE_URL + "matches";

    /**
     * The RecyclerView that holds the matches
     */
    private RecyclerView mRecyclerView;

    /**
     * The adapter that maps Match objects to VieeHolders
     */
    private MatchesAdapter mAdapter;

    /**
     * The layout manager for the RecyclerView
     */
    private RecyclerView.LayoutManager mLayoutManager;

    /**
     * A progress bar that indicates loading of data
     */
    private ProgressBar mProgressBar;

    /**
     * This is called when the activity is created. It sets the correct layout
     * and starts loading the matches. When the user hasn't set a player yet, redirect him
     * to PlayerActivity.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * Check if the user has set a player id
         */
        PlayerStorage playerStorage = new PlayerStorage(this);
        if (!playerStorage.hasPlayer()) {
            goToPlayerSettings();
            return;
        }

        /**
         * Set the basic layout to activity_show_matches
         */
        setContentView(R.layout.activity_show_matches);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_matches);
        setSupportActionBar(toolbar);

        /**
         * Load reference to the recycler view, and set the layout manager as
         * a Linear Layout manager. ALso create a MatchAdapter and pass it to the Recycler View
         */
        mRecyclerView = (RecyclerView) findViewById(R.id.match_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MatchesAdapter();
        mRecyclerView.setAdapter(mAdapter);

        /**
         * Find a reference to the progress bar
         */
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar_matches);

        /**
         * Load the matches!
         */
        loadMatches();
    }

    /**
     * When matches are received, hide the progress bar, show the match list
     * @param matches the match array
     */
    private void setMatches (Match[] matches) {
        mAdapter.setMatches(matches);
        hideProgressBar();
        showMatchList();
    }

    /**
     * Show a short message using a toast
     * @param message
     */
    private void showMessage (String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Hide the progress bar
     */
    private void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }

    /**
     * Show the progress bar
     */
    private void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    /**
     * Hide the match list
     */
    private void hideMatchList() {
        mRecyclerView.setVisibility(View.GONE);
    }

    /**
     * Show the match list
     */
    private void showMatchList() {
        mRecyclerView.setVisibility(View.VISIBLE);
    }


    /**
     * Use Volley to load the matches from the webservice URL. It fetches a JSON Array
     * and uses MatcJSON to decode it to matches. It then calls setMatches so the recycler
     * view is updated. Volley is used for easy async fetching of data
     *
     * If an error occurs, show a message.
     */
    private void loadMatches() {
        JsonArrayRequest sr = new JsonArrayRequest(Request.Method.GET, URL_MATCHES, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray matches) {
                        try {
                            Log.i(TAG, "Received JSON response: " + matches.toString());
                            setMatches(MatchJSON.jsonToMatches(matches));
                        } catch (JSONException ex) {
                            Log.e(TAG, ex.toString());
                            showMessage("Could not parse the response. Try again later");
                            hideProgressBar();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Error in requesting the JSON.", error);
                        showMessage("Could not retrieve matches from server. Try again later.");
                        hideProgressBar();
                    }
                }
        );
        // Hide the match list, show the progress bar
        showProgressBar();
        hideMatchList();
        // Fire the request!
        Volley.newRequestQueue(this).add(sr);
    }

    /**
     * Create and send an Intent to PlayerActivity
     */
    private void goToPlayerSettings() {
        Intent intent = new Intent(this, PlayerActivity.class);
        startActivity(intent);
    }

    /**
     * Inflate the menu with the menu items in menu_list_matches
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_list_matches, menu);
        return true;
    }

    /**
     * Listener for the menu item selection. Either refreshes the data or
     * sends the user to the player setting
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        // Set the player
        if (id == R.id.action_set_player) {
            goToPlayerSettings();
            return true;
        }
        // Refresh the data
        if (id == R.id.action_refresh_matches) {
            loadMatches();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
