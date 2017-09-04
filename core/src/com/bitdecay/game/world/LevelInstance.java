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

    public int getScoreNeededForMedal(MedalUtils.LevelRating rating) {
        switch(rating) {
            case BRONZE:
                return levelDef.bronzeScore;
            case SILVER:
                return levelDef.silverScore;
            case GOLD:
                return levelDef.goldScore;
            case DEV:
                return levelDef.devScore;
            default:
                return 0;
        }
    }

    public float getTimeNeededForMedal(MedalUtils.LevelRating rating) {
        switch(rating) {
            case BRONZE:
                return levelDef.bronzeTime;
            case SILVER:
                return levelDef.silverTime;
            case GOLD:
                return levelDef.goldTime;
            case DEV:
                return levelDef.devTime;
            default:
                return Float.POSITIVE_INFINITY;
        }
    }

    public int getHighScore() {
        return Helm.prefs.getInteger(getScoreKey(), GamePrefs.SCORE_NOT_SET);
    }

    // returns the number of points scored over the previous best
    public int maybeSetNewHighScore(int newScore) {
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
            return newScore - Math.max(0, oldHighScore); // if no score is set, make sure we don't go negative
        } else {
            return 0;
        }
    }

    public float getBestTime() {
        return Helm.prefs.getFloat(getTimeKey(), GamePrefs.TIME_NOT_SET);
    }

    // returns the number of seconds trimmed off the previous best
    public float maybeSetNewBestTime(float newTime) {
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
            return newTime - oldBestTime;
        } else {
            return 0;
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
