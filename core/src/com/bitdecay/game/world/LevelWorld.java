package com.bitdecay.game.world;

import com.badlogic.gdx.utils.Array;

/**
 * Created by Monday on 12/23/2016.
 */
public abstract class LevelWorld {
    Array<LevelDefinition> levels;
    int currentLevel = 0;

    protected LevelWorld(int levelCount) {
        levels = new Array<>(levelCount);
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
}
