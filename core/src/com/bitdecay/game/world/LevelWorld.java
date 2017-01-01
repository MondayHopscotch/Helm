package com.bitdecay.game.world;

import com.badlogic.gdx.utils.Array;

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

    public int getTotalScore() {
        int total = 0;
        for (Integer levelScore : levelScores.values()) {
            total += levelScore;
        }
        return total;

    }
}
