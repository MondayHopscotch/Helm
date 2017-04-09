package com.bitdecay.game.world;

import com.badlogic.gdx.utils.Array;
import com.bitdecay.game.menu.MedalUtils;
import com.bitdecay.game.prefs.GamePrefs;

/**
 * Created by Monday on 12/23/2016.
 */
public class WorldInstance {
    public Array<LevelInstance> levels;
    public final int requiredLevelsForUnlock;

    public String name;

    public WorldInstance(int requiredLevelsForUnlock) {
        levels = new Array<>();
        this.requiredLevelsForUnlock = requiredLevelsForUnlock;
    }

    public String getWorldName() {
        return name;
    }

    public void addLevelInstance(LevelDefinition levelDef) {
        levels.add(new LevelInstance(levelDef));
    }

    public MedalUtils.LevelRating getBestScoreMedal() {
        MedalUtils.LevelRating lowestRating = MedalUtils.LevelRating.DEV;
        for (LevelInstance levelInst : levels) {
            MedalUtils.LevelRating levelRank = MedalUtils.getScoreRank(levelInst);
            if (levelRank.ordinal() < lowestRating.ordinal()) {
                lowestRating = levelRank;
            }
        }
        return lowestRating;
    }

    public MedalUtils.LevelRating getBestTimeMedal() {
        MedalUtils.LevelRating lowestRating = MedalUtils.LevelRating.DEV;
        for (LevelInstance levelInst : levels) {
            MedalUtils.LevelRating levelRank = MedalUtils.getTimeRank(levelInst);
            if (levelRank.ordinal() < lowestRating.ordinal()) {
                lowestRating = levelRank;
            }
        }
        return lowestRating;
    }

    public int getHighScore() {
        int total = 0;
        for (LevelInstance levelInst : levels) {
            int score = levelInst.getHighScore();
            if (score != GamePrefs.SCORE_NOT_SET) {
                total += score;
            }
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
