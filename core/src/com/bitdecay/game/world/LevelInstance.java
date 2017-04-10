package com.bitdecay.game.world;

import com.bitdecay.game.Helm;
import com.bitdecay.game.menu.MedalUtils;
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
            MedalUtils.LevelRating oldRating = MedalUtils.getScoreRank(this, oldHighScore);
            MedalUtils.LevelRating newRating = MedalUtils.getScoreRank(this, newScore);
            if (!newRating.equals(oldRating)) {
                // remove our old medal
                Helm.stats.count(oldRating.statName(), -1);
                Helm.stats.count(newRating.statName(), 1);
            }

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
            MedalUtils.LevelRating oldRating = MedalUtils.getTimeRank(this, oldBestTime);
            MedalUtils.LevelRating newRating = MedalUtils.getTimeRank(this, newTime);
            if (!newRating.equals(oldRating)) {
                // remove our old medal
                Helm.stats.count(oldRating.statName(), -1);
                Helm.stats.count(newRating.statName(), 1);
            }

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
