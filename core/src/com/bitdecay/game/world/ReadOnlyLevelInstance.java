package com.bitdecay.game.world;

/**
 * Created by Monday on 4/9/2017.
 */

public class ReadOnlyLevelInstance extends LevelInstance {
    public ReadOnlyLevelInstance(LevelDefinition def) {
        super(def);
    }

    @Override
    public boolean maybeSetNewBestTime(float newTime) {
        return false;
    }

    @Override
    public boolean maybeSetNewHighScore(int newScore) {
        return false;
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
