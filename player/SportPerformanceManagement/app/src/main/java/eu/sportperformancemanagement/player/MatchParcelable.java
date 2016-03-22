package eu.sportperformancemanagement.player;

import android.os.Parcel;
import android.os.Parcelable;

import eu.sportperformancemanagement.common.Match;

/**
 * This class extends the "common" Match and makes it a Parcelable class.
 * In this way, it can be used as an extra arg in an Intent.
 *
 * @author Joost Ouwerling
 */
public class MatchParcelable extends Match implements Parcelable {

    /**
     * Constructor, same as for Match objects
     * @param id
     * @param name
     * @param server
     * @param port
     */
    public MatchParcelable(int id, String name,  String server, int port)
    {
        super(id, name, server, port);
    }

    /** Parcelable implementation **/
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Write this objects contents to the out parcel
     * @param out
     * @param flags
     */
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(getId());
        out.writeString(getName());
        out.writeString(getServer());
        out.writeInt(getPort());
    }

    /**
     * Mandatory CREATOR object, which is enforced by the Parcelable interface.
     * It creates a MatchParcelable from a parcel and makes an array of match parcelables.
     */
    public static final Parcelable.Creator<MatchParcelable> CREATOR = new Parcelable.Creator<MatchParcelable>() {
        public MatchParcelable createFromParcel(Parcel in) {
            return new MatchParcelable(in);
        }

        public MatchParcelable[] newArray(int size) {
            return new MatchParcelable[size];
        }
    };

    /**
     * Make a Match Parcelable from the in parcel
     * @param in
     */
    private MatchParcelable(Parcel in) {
        this(in.readInt(), in.readString(), in.readString(), in.readInt());
    }

    /**
     * Static method, which makes a MatchParcelable from this match.
     * @param m the match which needs to become parcelable
     * @return the match m in parcelable format
     */
    public static MatchParcelable make(Match m) {
        return new MatchParcelable(m.getId(), m.getName(), m.getServer(), m.getPort());
    }

}
