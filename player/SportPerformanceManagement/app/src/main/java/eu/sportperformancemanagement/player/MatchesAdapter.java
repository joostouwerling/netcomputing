package eu.sportperformancemanagement.player;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import eu.sportperformancemanagement.common.Match;

/**
 * This class adapts matches to the recycler view.
 *
 * @author Joost Ouwerling <j.t.ouwerling@student.rug.nl>
 */

public class MatchesAdapter extends RecyclerView.Adapter <MatchesAdapter.ViewHolder> {

    /**
     * An array of the matches we want to view
     */
    Match[] mDataSet;

    /**
     * This ViewHolder defines the view of a single Match item in the list
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mTextName;
        public TextView mTextDetails;
        public View mView;
        public Match mMatch;

        public ViewHolder (View view) {
            super(view);
            mView = view;
            mTextName = (TextView) view.findViewById(R.id.match_list_item_name);
            mTextDetails = (TextView) view.findViewById(R.id.match_list_item_details);
        }

        public String toString () {
            return super.toString() + " " + mTextName.getText() + " " + mTextDetails.getId();
        }
    }

    /**
     * Initialize with an empty array
     */
    public MatchesAdapter () {
        mDataSet = new Match[0];
    }

    /**
     * Set the match array. Notifies data changed, so the recycler view is updated
     * @param matches a match array
     */
    public void setMatches(Match[] matches) {
        mDataSet = matches;
        notifyDataSetChanged();
    }

    /**
     * Create a view holder in the parent group. It uses match_list_item xml class for
     * defining how a single match looks.
     * @return a ViewHolder
     */
    @Override
    public MatchesAdapter.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.match_list_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    /**
     * This is called when a viewholder is bound to a certain match. The match is given by
     * the position of the Match in the dataset. The UI elements of the view holder are set
     * to the values corresponding to the match.
     *
     * A callback to the onClick is also implemented, which creates an intent for MatchDetailActivity
     * It adds a MatchParcelable as extra arguments to the intent, so the detail activity knows
     * which match was selected.
     *
     * @param vh the viewholder which is used
     * @param position the position of the Match in the dataset
     */
    @Override
    public void onBindViewHolder(final ViewHolder vh, int position) {
        vh.mMatch = mDataSet[position];
        vh.mTextName.setText(mDataSet[position].getName());
        vh.mTextDetails.setText(mDataSet[position].getServer() + ":" + Integer.toString(mDataSet[position].getPort()));
        vh.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent  = new Intent(context, MatchDetailActivity.class);
                intent.putExtra(MatchDetailActivity.ARG_MATCH, MatchParcelable.make(vh.mMatch));
                context.startActivity(intent);
            }
        });
    }

    /**
     * @return the length of the data set
     */
    @Override
    public int getItemCount () {
        return mDataSet.length;
    }


}
