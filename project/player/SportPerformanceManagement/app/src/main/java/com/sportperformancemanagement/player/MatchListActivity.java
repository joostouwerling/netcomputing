package com.sportperformancemanagement.player;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.sportperformancemanagement.player.sportperformancemanagement.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MatchListActivity extends AppCompatActivity {

    private static final String TAG = "MatchListActivity";

    private static final String URL_MATCHES = "http://joostouwerling.com/netcomputing/matches.json";

    private RecyclerView mRecyclerView;
    private MatchesAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        mRecyclerView.setVisibility(View.VISIBLE);
        hideProgressBar();
    }

    private void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }

    private void showErrorMessage (String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }



    private void loadMatches() {
        JsonObjectRequest sr = new JsonObjectRequest(Request.Method.GET, URL_MATCHES, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.i(TAG, "Received JSON response: " + response.toString());
                            JSONArray matches = response.getJSONArray("matches");
                            Log.i(TAG, "Received matches: " + Integer.toString(matches.length()));
                            Match[] matchArray = new Match[matches.length()];
                            JSONObject match;
                            for (int i = 0; i < matches.length(); i++) {
                                match = matches.getJSONObject(i);
                                matchArray[i] = new Match(
                                        match.getString("name"),
                                        match.getInt("id"),
                                        match.getString("server"),
                                        match.getInt("port"));
                            }
                            setMatches(matchArray);
                        } catch (JSONException ex) {
                            Log.e(TAG, ex.toString());
                            showErrorMessage("Could not parse the response. Try again later");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Error in requesting the JSON.");
                        Log.e(TAG, error.toString());
                        showErrorMessage("Could not retrieve matches from server. Try again later.");
                        hideProgressBar();
                    }
                }
        );
        Volley.newRequestQueue(this).add(sr);
    }

}
