package com.bitdecay.helm.screen;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.bitdecay.helm.Helm;
import com.bitdecay.helm.sound.SoundMode;

/**
 * Created by Monday on 11/20/2017.
 */

public class AudioUtils {
    public static void doSound(Helm game, SoundMode mode, String soundName) {
        Sound sfx = game.assets.get(soundName, Sound.class);

        if (SoundMode.PLAY.equals(mode)) {
            sfx.play();
        } else if (SoundMode.STOP.equals(mode)) {
            sfx.stop();
        }
    }

    public static void doMusic(Helm game, SoundMode mode, String musicName) {
        Music music = game.assets.get(musicName, Music.class);

        switch(mode) {
            case START:
            case PLAY:
            case RESUME:
                music.setLooping(true);
                if (!music.isPlaying()) {
                    music.play();
                }
                break;
            case PAUSE:
                if (music.isPlaying()) {
                    music.pause();
                }
                break;
            case STOP:
                music.stop();
                break;
        }
        if (SoundMode.PLAY.equals(mode) && !music.isPlaying()) {
            music.play();
        } else if (SoundMode.STOP.equals(mode) && music.isPlaying()) {
            music.stop();
        }
    }
}
