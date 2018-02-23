package com.bitdecay.helm.prefs;

/**
 * Created by Monday on 1/4/2017.
 */
public class GamePrefs {
    public static final String LAST_OPENED_VERSION = "lastOpenedVersion";

    public static final String CHOSEN_PALETTE = "chosenPalette";

    public static final String HIGH_SCORE = "highScore";
    public static final String BEST_TIME = "bestTime";

    public static final int SCORE_NOT_SET = Integer.MIN_VALUE;
    public static final float TIME_NOT_SET = Float.POSITIVE_INFINITY;

    public static final String USE_LEFT_HANDED_CONTROLS = "leftHandedControls";
    public static final boolean USE_LEFT_HANDED_CONTROLS_DEFAULT = false;

    // See usages for understanding this number. It's used in multiple ways
    public static final String SENSITIVITY = "steeringSensitivity";
    public static final int SENSITIVITY_MIN = -100;
    public static final int SENSITIVITY_MAX = 200;
    public static final int SENSITIVITY_DEFAULT = 25;

    public static final String MUSIC_ENABLED = "musicEnabled";
    public static final boolean MUSIC_ENABLED_DEFAULT = true;

    public static final String TUTORIAL_COMPLETE = "tutorialComplete";
    public static final boolean TUTORIAL_COMPLETE_DEFAULT = false;

    public static final String ALERTED_OF_DEV_MEDALS = "alertedOfDevMedals";
    public static final boolean ALERTED_OF_DEV_MEDALS_DEFAULT = false;

    public static final String SHOW_DEV_MEDAL_DIALOG = "showDevDialog";
    public static final boolean SHOW_DEV_MEDAL_DIALOG_DEFAULT = false;
}
