package com.bitdecay.game;

import com.bitdecay.game.scoring.LandingScore;
import com.bitdecay.game.sound.SoundMode;
import com.bitdecay.game.system.render.GamePalette;

/**
 * Created by Monday on 12/15/2016.
 */
public interface GamePilot {

    void requestRestartLevel();

    void doSound(SoundMode mode, String soundName);

    void doMusic(SoundMode mode, String soundName);

    void finishLevel(LandingScore score);

    void returnToMenus(boolean isQuit);

    Helm getHelm();

    void togglePause();

    void setTime(float secondsElapsed);

    boolean isDebug();

    void saveLastReplay();
}
