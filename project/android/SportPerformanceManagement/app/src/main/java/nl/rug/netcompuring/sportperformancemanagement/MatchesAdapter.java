package nl.rug.netcompuring.sportperformancemanagement;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MatchesAdapter extends RecyclerView.Adapter <MatchesAdapter.ViewHolder> {

    Match[] mDataSet;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mTextName;
        public TextView mTextId;
        public View mView;
        public Match mMatch;

        public ViewHolder (View view) {
            super(view);
            mView = view;
            mTextName = (TextView) view.findViewById(R.id.match_list_item_name);
            mTextId = (TextView) view.findViewById(R.id.match_list_item_id);
        }

        public String toString () {
            return super.toString() + " " + mTextName.getText() + " " + mTextId.getId();
        }
    }

    public MatchesAdapter (Match[] dataset) {
        mDataSet = dataset;
    }

    @Override
    public MatchesAdapter.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.match_list_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder vh, int position) {
        vh.mMatch = mDataSet[position];
        vh.mTextName.setText(mDataSet[position].getName());
        vh.mTextId.setText(mDataSet[position].getId());
        vh.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent  = new Intent(context, MatchDetailActivity.class);
                intent.putExtra(MatchDetailActivity.ARG_MATCH_ID, vh.mMatch.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount () {
        return mDataSet.length;
    }

}