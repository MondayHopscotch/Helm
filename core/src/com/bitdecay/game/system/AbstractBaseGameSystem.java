package com.bitdecay.game.system;

import com.bitdecay.game.GamePilot;

/**
 * Created by Monday on 12/15/2016.
 */
public abstract class AbstractBaseGameSystem implements GameSystem {

    public GamePilot pilot;

    public AbstractBaseGameSystem(GamePilot pilot) {
        this.pilot = pilot;
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
