package eu.sportperformancemanagement.player;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import eu.sportperformancemanagement.common.Player;
import eu.sportperformancemanagement.common.PlayerJSON;
import eu.sportperformancemanagement.common.SpmConstants;

import org.json.JSONArray;
import org.json.JSONException;

public class PlayerActivity extends AppCompatActivity {

    /**
     * Tag used for logging
     */
    public static final String TAG = "PlayerActivity";

    /**
     * The webservice URL for players
     */
    private static final String URL_PLAYERS = SpmConstants.WEBSERVICE_BASE_URL + "players";

    /**
     * A spinner where the players are in
     */
    private Spinner playerSpinner;

    /**
     * An array with Players
     */
    private Player[] playerArray = null;

    /**
     * The wrapper for the shared preference storage
     */
    private PlayerStorage playerStorage;

    /**
     * A reference to the progress bar
     */
    private ProgressBar mProgressBar;

    /**
     * A reference to the normal layout if this activity
     */
    private LinearLayout mLinLayout;

    /**
     * Create this activity. Instantiates layout and loads players
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * Set the layout, toolbar and action bar
         */
        setContentView(R.layout.activity_player);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Set player");
        }

        /**
         * Find references to the UI elements and set the on click listener for the btn
         */
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar_players);
        mLinLayout = (LinearLayout) findViewById(R.id.layout_set_player);
        playerSpinner = (Spinner) findViewById(R.id.players_spinner);
        Button saveBtn = (Button) findViewById(R.id.btn_save_player);
        saveBtn.setOnClickListener(savePlayer);

        /**
         * Set the player storage and load the players
         */
        playerStorage = new PlayerStorage(this);
        loadPlayers();
    }

    /**
     * When the button is clicked, check:
     * - if the players are loaded
     * - if a valid item was selected
     * if all correct, set the correct player in the player storage.
     */
    private View.OnClickListener savePlayer = new View.OnClickListener() {
        public void onClick(View v) {
            if (playerArray == null) {
                showMessage("Players were not yet loaded. Try again later");
                return;
            }
            if (playerSpinner.getSelectedItemPosition() == -1) {
                showMessage("Please select a player first");
                return;
            }
            Player player = playerArray[playerSpinner.getSelectedItemPosition()];
            playerStorage.setPlayer(player);
            Log.i(TAG, "Set player to " + player.getName());
            showMessage("You are now " + player.getName());
        }
    };


    /**
     * Set the players in the spinner, when data is received.
     * @param players a player array
     */
    private void setPlayers(Player[] players) {
        // Make an array of player names
        playerArray = players;
        String[] playerNames = new String[players.length];
        for (int i = 0; i < players.length; i++)
            playerNames[i] = players[i].getName();

        // Fill the spinner by using an ArrayAdapter<String>, for simple spinner items
        ArrayAdapter<String> playerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, playerNames);
        playerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        playerSpinner.setAdapter(playerArrayAdapter);

        // Hide the progress bar and show the player list
        hideProgressBar();
        showPlayerList();

        // Set the correct player, if there is one
        if (!playerStorage.hasPlayer())
            return;
        Player playerNow = playerStorage.getPlayer();
        for (int i = 0; i < players.length; i++)
            if (players[i].getId() == playerNow.getId())
                playerSpinner.setSelection(i);
    }

    /**
     * Show a message using a toast
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
     * Hide the player list
     */
    private void hidePlayerList() {
        mLinLayout.setVisibility(View.GONE);
    }


    /**
     * Show the player list
     */
    private void showPlayerList() {
        mLinLayout.setVisibility(View.VISIBLE);
    }

    /**
     * This method loads the players from the URL_PLAYERS webservice and decodes the
     * returned JSON< before setting the players in the spinner. Uses the common PlayerJSOn to
     * decode the incoming JSOn to players.
     *
     * It uses Volley to easily make asynchronous requests to the server, to prevent clogging the
     * UI thread. When something fails in making the request, an error message is shown.
     */
    private void loadPlayers() {
        JsonArrayRequest sr = new JsonArrayRequest(Request.Method.GET, URL_PLAYERS, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray players) {
                        try {
                            Log.i(TAG, "Received JSON response: " + players.toString());
                            setPlayers(PlayerJSON.jsonToPlayers(players));
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
                        showMessage("Could not retrieve players from server. Try again later.");
                        hideProgressBar();
                    }
                });
        // Hide the player list and show the progress bar
        hidePlayerList();
        showProgressBar();
        // Fire the request
        Volley.newRequestQueue(this).add(sr);
    }

    /**
     * Inflate the menu with menu_set_player from the menu resources
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_set_player, menu);
        return true;
    }


    /**
     * Listen for selected menu items. This is either refreshin data or going back to match list
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        // E.T. phone homeee
        if (id == android.R.id.home) {
            NavUtils.navigateUpTo(this, new Intent(this, MatchListActivity.class));
            return true;
        }

        // refresh the data
        if (id == R.id.action_refresh_players) {
            loadPlayers();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
