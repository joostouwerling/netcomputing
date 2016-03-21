package com.sportperformancemanagement.player;

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
import com.sportperformancemanagement.common.Player;
import com.sportperformancemanagement.common.PlayerJSON;
import com.sportperformancemanagement.player.sportperformancemanagement.R;

import org.json.JSONArray;
import org.json.JSONException;

public class PlayerActivity extends AppCompatActivity {

    public static final String TAG = "PlayerActivity";

    private static final String URL_PLAYERS = "http://webservice.sportperformancemanagement.eu:8080/Webservice/rest/players";

    private Spinner playerSpinner;
    private Player[] playerArray = null;
    private PlayerStorage playerStorage;
    private ProgressBar mProgressBar;
    private LinearLayout mLinLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Set player");
        }

        playerStorage = new PlayerStorage(this);

        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar_players);
        mLinLayout = (LinearLayout) findViewById(R.id.layout_set_player);

        Button saveBtn = (Button) findViewById(R.id.btn_save_player);
        saveBtn.setOnClickListener(savePlayer);

        playerSpinner = (Spinner) findViewById(R.id.players_spinner);
        loadPlayers();
    }

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

    private void setPlayers(Player[] players) {
        playerArray = players;
        String[] playerNames = new String[players.length];
        for (int i = 0; i < players.length; i++)
            playerNames[i] = players[i].getName();

        // Fill the array
        ArrayAdapter<String> playerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, playerNames);
        playerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        playerSpinner.setAdapter(playerArrayAdapter);

        hideProgressBar();
        showPlayerList();

        // Set the correct player, if there is
        if (!playerStorage.hasPlayer())
            return;
        Player playerNow = playerStorage.getPlayer();
        for (int i = 0; i < players.length; i++)
            if (players[i].getId() == playerNow.getId())
                playerSpinner.setSelection(i);
    }

    private void showMessage (String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }
    private void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void hidePlayerList() {
        mLinLayout.setVisibility(View.GONE);
    }

    private void showPlayerList() {
        mLinLayout.setVisibility(View.VISIBLE);
    }

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
        hidePlayerList();
        showProgressBar();
        Volley.newRequestQueue(this).add(sr);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_set_player, menu);
        return true;
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

        if (id == R.id.action_refresh_players) {
            loadPlayers();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
