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
        return Helm.prefs.getInteger(getScoreKey(), GamePrefs.SCORE_NOT_SET);
    }

    public boolean maybeSetNewHighScore(int newScore) {
        int oldHighScore = getHighScore();

        if (newScore > oldHighScore) {
            saveHighScore(newScore);
            System.out.println("Saving new high score for " + levelDef.name + ": " + newScore);
            return true;
        } else {
            return false;
        }
    }

    public float getBestTime() {
        return Helm.prefs.getFloat(getTimeKey(), GamePrefs.TIME_NOT_SET);
    }

    public boolean maybeSetNewBestTime(float newTime) {
        float oldBestTime = getBestTime();

        if (newTime < oldBestTime) {
            saveBestTime(newTime);
            System.out.println("Saving new best time for " + levelDef.name + ": " + newTime);
            return true;
        } else {
            return false;
        }
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
