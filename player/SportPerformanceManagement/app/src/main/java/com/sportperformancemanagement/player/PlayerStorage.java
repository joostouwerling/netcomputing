package com.sportperformancemanagement.player;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.sportperformancemanagement.common.Player;

/**
 * Created by joostouwerling on 21/03/16.
 */
public class PlayerStorage {

    public static final String TAG = "PlayerStorage";

    public static final String PLAYER_NAME = "PlayerData";

    SharedPreferences settings;

    public PlayerStorage(Context ctx) {
        settings = ctx.getSharedPreferences(PLAYER_NAME, 0);
    }

    public boolean hasPlayer() {
        try {
            if (settings.getInt("player_id", -1) == -1)
                return false;
            if (settings.getString("player_name", null) == null)
                return false;
            return true;
        } catch (Exception ex) {
            Log.e(TAG, "Error while checking hasPlayer()", ex);
            return false;
        }
    }

    public void setPlayer(Player p) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("player_id", p.getId());
        editor.putString("player_name", p.getName());
        editor.commit();
    }

    public Player getPlayer() {
        if (!hasPlayer())
            return null;
        int id = settings.getInt("player_id", -1);
        String name = settings.getString("player_name", null);
        return new Player(id, name);

    }

}
