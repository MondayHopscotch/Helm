package com.bitdecay.game.prefs;

/**
 * Created by Monday on 1/4/2017.
 */
public class GamePrefs {
    public static final String HIGH_SCORE = "highScore";
    public static final String BEST_TIME = "bestTime";

    public static final int SCORE_NOT_SET = Integer.MIN_VALUE;
    public static final float TIME_NOT_SET = Float.POSITIVE_INFINITY;

    public static final String USE_LEFT_HANDED_CONTROLS = "leftHandedControls";
    public static final boolean USE_LEFT_HANDED_CONTROLS_DEFAULT = false;

    public static final String USE_JOYSTICK_STEERING = "useDynamicSteeringControls";
    public static final boolean USE_JOYSTICK_STEERING_DEFAULT = false;

    public static final String JOYSTICK_STEERING_WIDTH = "simpleSteeringWidthRatio";
    public static final float JOYSTICK_STEERING_WIDTH_DEFAULT = 0.5f;

    public static final String JOYSTICK_STEERING_HEIGHT = "simpleSteeringHeightRatio";
    public static final float JOYSTICK_STEERING_HEIGHT_DEFAULT = 0.5f;

    public static final float JOYSTICK_STEERING_MAX = 0.9f;
    public static final float JOYSTICK_STEERING_MIN = 0.1f;

    // See usages for understanding this number. It's used in multiple ways
    public static final String SENSITIVITY = "steeringSensitivity";
    public static final int SENSITIVITY_MIN = -100;
    public static final int SENSITIVITY_MAX = 200;
    public static final int SENSITIVITY_DEFAULT = 0;
}
