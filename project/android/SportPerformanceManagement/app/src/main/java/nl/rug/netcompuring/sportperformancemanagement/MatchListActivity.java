package nl.rug.netcompuring.sportperformancemanagement;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;


public class MatchListActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_matches);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_matches);
        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.match_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MatchesAdapter(Match.generateArray(20));
        mRecyclerView.setAdapter(mAdapter);
    }

}
