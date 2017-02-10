package com.bitdecay.game.world;

import com.bitdecay.game.Helm;
import com.bitdecay.game.prefs.GamePrefs;

/**
 * Created by Monday on 2/9/2017.
 */

public class LevelInstance {
    public final LevelDefinition levelDef;

    public LevelInstance(LevelDefinition def) {
        levelDef = def;
    }

    public int getHighScore() {
        return Helm.prefs.getInteger(getScoreKey(), 0);
    }

    public float getBestTime() {
        return Helm.prefs.getFloat(getTimeKey(), Float.POSITIVE_INFINITY);
    }

    public void saveHighScore(int total) {
        Helm.prefs.putInteger(getScoreKey(), total);
        Helm.prefs.flush();
    }

    public void saveBestTime(float time) {
        Helm.prefs.putFloat(getTimeKey(), time);
        Helm.prefs.flush();
    }

    public String getScoreKey() {
        return levelDef.name + GamePrefs.HIGH_SCORE;
    }

    public String getTimeKey() {
        return levelDef.name + GamePrefs.BEST_TIME;
    }
}
