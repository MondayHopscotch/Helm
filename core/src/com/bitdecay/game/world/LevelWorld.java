package com.bitdecay.game.world;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Array;
import com.bitdecay.game.Helm;
import com.bitdecay.game.prefs.GamePrefs;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Monday on 12/23/2016.
 */
public abstract class LevelWorld {
    Array<LevelDefinition> levels;
    Map<LevelDefinition, LevelRun> levelRuns;
    int currentLevel = 0;

    protected LevelWorld(int levelCount) {
        levels = new Array<>(levelCount);
        levelRuns = new HashMap<>();
    }

    public abstract String getWorldName();

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
        return getWorldName() + GamePrefs.HIGH_SCORE;
    }

    public String getTimeKey() {
        return getWorldName() + GamePrefs.BEST_TIME;
    }

    public boolean hasNextLevel() {
        return currentLevel + 1 < levels.size;
    }

    public LevelDefinition getNextLevel() {
        currentLevel++;
        if (currentLevel >= levels.size) {
            return null;
        } else {
            return levels.get(currentLevel);
        }
    }

    public LevelDefinition getCurrentLevel() {
        return levels.get(currentLevel);
    }

    public void setLevelScore(LevelDefinition level, int score) {
        if (!levelRuns.containsKey(level)) {
            levelRuns.put(level, new LevelRun());
        }
        levelRuns.get(level).score = score;
    }

    public void setLevelTime(LevelDefinition level, float time) {
        if (!levelRuns.containsKey(level)) {
            levelRuns.put(level, new LevelRun());
        }
        levelRuns.get(level).time = time;
    }

    public int getCurrentRunTotalScore() {
        int total = 0;
        for (LevelRun levelRun : levelRuns.values()) {
            total += levelRun.score;
        }
        return total;
    }

    public float getCurrentRunTotalTime() {
        float total = 0;
        for (LevelRun levelRun : levelRuns.values()) {
            total += levelRun.time;
        }
        return total;
    }

    public void maybeSaveNewRecords() {
        int total = getCurrentRunTotalScore();

        int oldHighScore = getHighScore();
        String scoreKey = getWorldName() + GamePrefs.HIGH_SCORE;
        if (Helm.prefs.contains(scoreKey)) {
            oldHighScore = Helm.prefs.getInteger(scoreKey);
        }

        if (total > oldHighScore) {
            saveHighScore(total);
            System.out.println("Scorer: SAVING NEW SCORE: " + total);
        }

        float runTime = getCurrentRunTotalTime();

        float oldBestTime = getBestTime();
        String timeKey = getTimeKey();
        if (Helm.prefs.contains(timeKey)) {
            oldBestTime = Helm.prefs.getFloat(timeKey);
        }

        if (runTime < oldBestTime) {
            saveBestTime(runTime);
            System.out.println("Scorer: SAVING NEW BEST TIME: " + runTime);
        }
    }

    private class LevelRun {
        int score;
        float time;
    }
}
