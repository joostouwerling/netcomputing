package eu.sportperformancemanagement.player;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import eu.sportperformancemanagement.common.Player;

/**
 *
 * This class wraps access to the Shared Preferences, where the
 * player data is stored.
 *
 * @author Joost Ouwerling
 */
public class PlayerStorage {

    /**
     * The tag used for logging
     */
    public static final String TAG = "PlayerStorage";

    /**
     * The name of our data in the shared prefs
     */
    public static final String PLAYER_NAME = "PlayerData";

    /**
     * Reference to the shared prefs
     */
    SharedPreferences settings;

    /**
     * Create the shared prefs objects for the context given in ctx
     * @param ctx
     */
    public PlayerStorage(Context ctx) {
        settings = ctx.getSharedPreferences(PLAYER_NAME, 0);
    }

    /**
     * @return true if this settings contain valid player data, false otherwise.
     */
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

    /**
     * Set player data to p in the shared prefs
     * @param p
     */
    public void setPlayer(Player p) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("player_id", p.getId());
        editor.putString("player_name", p.getName());
        editor.commit();
    }

    /**
     * fetch a player from the settings and return it.
     * @return a Player if there is data, otherwise null
     */
    public Player getPlayer() {
        if (!hasPlayer())
            return null;
        int id = settings.getInt("player_id", -1);
        String name = settings.getString("player_name", null);
        return new Player(id, name);

    }

}
