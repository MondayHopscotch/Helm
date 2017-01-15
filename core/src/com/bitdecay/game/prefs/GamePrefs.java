package com.bitdecay.game.prefs;

/**
 * Created by Monday on 1/4/2017.
 */
public class GamePrefs {
    public static final String HIGH_SCORE = "highScore";
    public static final String BEST_TIME = "bestTime";
    public static float TIME_NOT_SET = Float.POSITIVE_INFINITY;

    public static final String USE_JOYSTICK_STEERING = "useDynamicSteeringControls";

    public static final boolean USE_JOYSTICK_STEERING_DEFAULT = false;
    public static final String SIMPLE_STEERING_WIDTH = "simpleSteeringWidthRatio";

    public static final float SIMPLE_STEERING_WIDTH_DEFAULT = 0.3f;
    public static final String SIMPLE_STEERING_HEIGHT = "simpleSteeringHeightRatio";

    public static final float SIMPLE_STEERING_HEIGHT_DEFAULT = 0.3f;
    // See usages for understanding this number. It's used in multiple ways
    public static final String SENSITIVITY = "steeringSensitivity";
    public static final int SENSITIVITY_MIN = -100;
    public static final int SENSITIVITY_MAX = 100;
    public static final int SENSITIVITY_DEFAULT = 0;
}
