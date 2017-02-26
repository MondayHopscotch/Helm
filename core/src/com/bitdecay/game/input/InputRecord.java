package com.bitdecay.game.input;

/**
 * Created by Monday on 2/26/2017.
 */

public class InputRecord {
    public int tick;
    public float angle;
    public boolean boosting;

    public InputRecord(int tick) {
        this.tick = tick;
    }

    public InputRecord(int tick, float angle, boolean boost) {
        this.tick = tick;
        this.angle = angle;
        this.boosting = boost;
    }
}
