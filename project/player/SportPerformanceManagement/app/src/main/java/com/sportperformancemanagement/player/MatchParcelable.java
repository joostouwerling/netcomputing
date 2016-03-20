package com.sportperformancemanagement.player;

import android.os.Parcel;
import android.os.Parcelable;

import com.sportperformancemanagement.common.Match;

/**
 * Created by joostouwerling on 24/02/16.
 */
public class MatchParcelable extends Match implements Parcelable {

    public MatchParcelable(int id, String name,  String server, int port)
    {
        super(id, name, server, port);
    }

    /** Parcelable implementation **/
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(getId());
        out.writeString(getName());
        out.writeString(getServer());
        out.writeInt(getPort());
    }

    public static final Parcelable.Creator<MatchParcelable> CREATOR = new Parcelable.Creator<MatchParcelable>() {
        public MatchParcelable createFromParcel(Parcel in) {
            return new MatchParcelable(in);
        }

        public MatchParcelable[] newArray(int size) {
            return new MatchParcelable[size];
        }
    };

    private MatchParcelable(Parcel in) {
        this(in.readInt(), in.readString(), in.readString(), in.readInt());
    }

    public static MatchParcelable make(Match m) {
        return new MatchParcelable(m.getId(), m.getName(), m.getServer(), m.getPort());
    }

    public Match toMatch() {
        return new Match(getId(), getName(), getServer(), getPort());
    }

}
