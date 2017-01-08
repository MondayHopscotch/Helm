package com.bitdecay.game.scoring;

/**
 * Created by Monday on 12/17/2016.
 */
public class LandingScore {
    public static final int MAX_ANGLE_SCORE = 10000;
    public static final int MAX_SPEED_SCORE = 10000;
    public static final int MAX_ACCURACY_SCORE = 10000;

    public int angleScore;
    public int speedScore;
    public int accuracyScore;
    public float fuelLeft;
    public int fuelScore;

    public int total() {
        return angleScore + speedScore + accuracyScore + fuelScore;
    }
}
