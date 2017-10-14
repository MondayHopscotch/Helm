package com.bitdecay.helm.sound;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;

/**
 * Created by Monday on 12/15/2016.
 */
public class MusicLibrary {

    public static final String MUSIC_DIR = "sound/music/";

    public static final String SHIP_BOOST = MUSIC_DIR + "BoostNoise.ogg";

    public static final String AMBIENT_MUSIC = MUSIC_DIR + "ROZKOL_Ambient_I.ogg";

    public static final String[] allMusic = new String[] {
            SHIP_BOOST,
            AMBIENT_MUSIC
    };

    public static void loadAllAsync(AssetManager assets) {
        for (String musicName : allMusic) {
            assets.load(musicName, Music.class);
        }
    }
}
