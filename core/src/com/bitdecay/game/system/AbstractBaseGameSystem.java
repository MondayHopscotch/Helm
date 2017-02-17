package com.bitdecay.game.system;

import com.bitdecay.game.GamePilot;
import com.bitdecay.game.screen.LevelPlayer;

/**
 * Created by Monday on 12/15/2016.
 */
public abstract class AbstractBaseGameSystem implements GameSystem {

    public GamePilot pilot;
    public LevelPlayer levelPlayer;

    public AbstractBaseGameSystem(GamePilot pilot) {
        this.pilot = pilot;
    }

    @Override
    public void setLevelPlayer(LevelPlayer levelPlayer) {
        this.levelPlayer = levelPlayer;
    }

    @Override
    public void before() {
        // override to implement
    }

    @Override
    public void after() {
        // override to implement
    }

    @Override
    public void reset() {
        // override to implement
    }
}
