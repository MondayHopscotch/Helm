package com.bitdecay.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.bitdecay.game.screen.GameScreen;
import com.bitdecay.game.screen.TitleScreen;

public class Helm extends Game {

    public static final String HIGH_SCORE = "highScore";

    public Preferences prefs;

    @Override
    public void create() {
        prefs = Gdx.app.getPreferences("helm-pref");
        setScreen(new TitleScreen(this));
    }
}
