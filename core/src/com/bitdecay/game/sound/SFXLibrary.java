package com.bitdecay.game.sound;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;

/**
 * Created by Monday on 12/15/2016.
 */
public class SFXLibrary {

    public static final String SFX_DIR = "sound/fx/";

    public static final String SHIP_LAUNCH = SFX_DIR + "Launch.ogg";
    public static final String SHIP_CRASH = SFX_DIR + "CrashHigh.ogg";
    public static final String SHIP_EXPLODE = SFX_DIR + "Explosion.ogg";

    public static final String LABEL_DISPLAY = SFX_DIR + "LabelDisplay.ogg";
    public static final String SCORE_POP = SFX_DIR + "ScorePop.ogg";
    public static final String HIGH_SCORE_BEAT = SFX_DIR + "HighScoreBeat.ogg";
    public static final String NEXT_LEVEL = SFX_DIR + "NextLevel.ogg";

    public static final String[] allSounds = new String[] {
            SHIP_LAUNCH,
            SHIP_CRASH,
            SHIP_EXPLODE,
            LABEL_DISPLAY,
            SCORE_POP,
            HIGH_SCORE_BEAT,
            NEXT_LEVEL
    };

    public static void loadAllAsync(AssetManager assets) {
        for (String sfxName : allSounds) {
            assets.load(sfxName, Sound.class);
        }
    }
}
