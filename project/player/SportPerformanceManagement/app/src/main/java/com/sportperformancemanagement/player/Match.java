package com.sportperformancemanagement.player;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by joostouwerling on 24/02/16.
 */
public class Match implements Parcelable {

    private String name;
    private int id;
    private String server;
    private int port;

    public Match(String n, int id, String server, int port)
    {
        setName(n);
        setId(id);
        setServer(server);
        setPort(port);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public static Match[] generateArray (int num) {
        Match[] matches = new Match[num];
        for (int i = 0; i < num; i++)
            matches[i] = new Match("Match " + i, i, "joostouwerling.com", 7376);
        return matches;
    }

    /** Parcelable implementation **/
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(name);
        out.writeInt(id);
        out.writeString(server);
        out.writeInt(port);
    }

    public static final Parcelable.Creator<Match> CREATOR = new Parcelable.Creator<Match>() {
        public Match createFromParcel(Parcel in) {
            return new Match(in);
        }
        public Match[] newArray (int size) {
            return new Match[size];
        }
    };

    private Match(Parcel in) {
        name = in.readString();
        id = in.readInt();
        server = in.readString();
        port = in.readInt();
    }

}
