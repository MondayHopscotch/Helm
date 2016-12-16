package com.bitdecay.game;

import com.bitdecay.game.sound.SoundMode;

/**
 * Created by Monday on 12/15/2016.
 */
public interface GamePilot {

    void requestRestartLevel();

    void doSound(SoundMode mode, String soundName);

    void doMusic(SoundMode mode, String soundName);
}
