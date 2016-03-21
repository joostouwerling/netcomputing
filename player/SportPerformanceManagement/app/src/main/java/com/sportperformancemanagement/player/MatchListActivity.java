package com.sportperformancemanagement.player;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.sportperformancemanagement.common.Match;
import com.sportperformancemanagement.common.MatchJSON;
import com.sportperformancemanagement.player.sportperformancemanagement.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MatchListActivity extends AppCompatActivity {

    private static final String TAG = "MatchListActivity";

    private static final String URL_MATCHES = "http://webservice.sportperformancemanagement.eu:8080/Webservice/rest/matches";

    private RecyclerView mRecyclerView;
    private MatchesAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProgressBar mProgressBar;
    private PlayerStorage playerStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        playerStorage = new PlayerStorage(this);
        if (!playerStorage.hasPlayer()) {
            goToPlayerSettings();
            return;
        }

        setContentView(R.layout.activity_show_matches);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_matches);
        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.match_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar_matches);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MatchesAdapter();
        mRecyclerView.setAdapter(mAdapter);

        loadMatches();
    }

    private void setMatches (Match[] matches) {
        mAdapter.setMatches(matches);
        hideProgressBar();
        showMatchList();
    }


    private void showErrorMessage (String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }
    private void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideMatchList() {
        mRecyclerView.setVisibility(View.GONE);
    }

    private void showMatchList() {
        mRecyclerView.setVisibility(View.VISIBLE);
    }


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
                            showErrorMessage("Could not parse the response. Try again later");
                            hideProgressBar();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Error in requesting the JSON.", error);
                        showErrorMessage("Could not retrieve matches from server. Try again later.");
                        hideProgressBar();
                    }
                }
        );
        showProgressBar();
        hideMatchList();
        Volley.newRequestQueue(this).add(sr);
    }

    private void goToPlayerSettings() {
        Intent intent = new Intent(this, PlayerActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_list_matches, menu);
        return true;
    }

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
