package com.bitdecay.game.scoring;

import com.badlogic.gdx.utils.Array;

/**
 * Created by Monday on 6/14/2017.
 */

public class ScoreStamps {
    private static Array<String> pendingStamps = new Array<>();

    public static void addPendingStamp(String message) {
        pendingStamps.add(message);
    }

    public static Array<String> getPendingStamps() {
        return pendingStamps;
    }

    public static void clearPendingStamps() {
        pendingStamps.clear();
    }
}
