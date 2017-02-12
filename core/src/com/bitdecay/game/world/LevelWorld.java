package com.bitdecay.game.world;

import com.badlogic.gdx.utils.Array;
import com.bitdecay.game.prefs.GamePrefs;

/**
 * Created by Monday on 12/23/2016.
 */
public abstract class LevelWorld {
    public Array<LevelInstance> levels;

    protected LevelWorld(int levelCount) {
        levels = new Array<>(levelCount);
    }

    public abstract String getWorldName();

    public void addLevelInstance(LevelDefinition levelDef) {
        levels.add(new LevelInstance(levelDef));
    }

    public int getHighScore() {
        int total = 0;
        for (LevelInstance levelInst : levels) {
            total += levelInst.getHighScore();
        }
        return total;
    }

    public float getBestTime() {
        float total = 0;
        for (LevelInstance levelInst : levels) {
            float levelTime = levelInst.getBestTime();
            if (levelTime == GamePrefs.TIME_NOT_SET) {
                // if they haven't finished all levels, don't show a total
                return GamePrefs.TIME_NOT_SET;
            } else {
                total += levelTime;
            }
        }
        return total;
    }
}
