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
    Map<LevelDefinition, Integer> levelScores;
    int currentLevel = 0;

    protected LevelWorld(int levelCount) {
        levels = new Array<>(levelCount);
        levelScores = new HashMap<>();
    }

    public abstract String getWorldName();

    public int getHighScore() {
        return Helm.prefs.getInteger(getScoreKey(), 0);
    }

    public void setHighScore(Preferences prefs, int total) {
        prefs.putInteger(getScoreKey(), total);
        prefs.flush();
    }

    private String getScoreKey() {
        return getWorldName() + GamePrefs.HIGH_SCORE;
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
        levelScores.put(level, score);
    }

    public int getCurrentRunTotalScore() {
        int total = 0;
        for (Integer levelScore : levelScores.values()) {
            total += levelScore;
        }
        return total;

    }
}
