package com.bitdecay.helm.system;

import com.bitdecay.helm.GamePilot;
import com.bitdecay.helm.screen.LevelPlayer;

/**
 * Created by Monday on 12/15/2016.
 */
public abstract class AbstractBaseGameSystem implements GameSystem {

    public GamePilot pilot;
    public LevelPlayer levelPlayer;

    public boolean disabled = false;

    public AbstractBaseGameSystem(com.bitdecay.helm.GamePilot pilot) {
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

    @Override
    public boolean isDisabled() {
        // override to implement
        return disabled;
    }

    @Override
    public void dispose() {
        // override to implement
    }
}
