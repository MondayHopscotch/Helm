package com.bitdecay.game.input;

/**
 * Created by Monday on 2/26/2017.
 */

public class InputRecord {
    public int tick;
    public float angle = Float.NEGATIVE_INFINITY;
    public boolean boostToggled;

    public InputRecord() {
        // Here for JSON
    }

    public InputRecord(int tick) {
        this.tick = tick;
    }

    public InputRecord(int tick, float angle, boolean boost) {
        this.tick = tick;
        this.angle = angle;
        this.boostToggled = boost;
    }
}
