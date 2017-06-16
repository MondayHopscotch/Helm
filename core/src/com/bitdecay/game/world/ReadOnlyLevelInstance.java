package com.bitdecay.game.world;

/**
 * Created by Monday on 4/9/2017.
 */

public class ReadOnlyLevelInstance extends LevelInstance {
    public ReadOnlyLevelInstance(LevelDefinition def) {
        super(def);
    }

    @Override
    public float maybeSetNewBestTime(float newTime) {
        return 0;
    }

    @Override
    public int maybeSetNewHighScore(int newScore) {
        return 0;
    }

    @Override
    public void saveHighScore(int total) {
        // no-op
    }

    @Override
    public void saveBestTime(float time) {
        // no-op
    }
}
