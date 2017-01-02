package com.bitdecay.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.bitdecay.game.screen.GameScreen;
import com.bitdecay.game.screen.TitleScreen;
import com.bitdecay.game.sound.MusicLibrary;
import com.bitdecay.game.sound.SFXLibrary;

public class Helm extends Game {
    public static final String HIGH_SCORE = "highScore";

    public AssetManager assets;

    public Preferences prefs;

    @Override
    public void create() {
        prefs = Gdx.app.getPreferences("helm-pref");
        assets = new AssetManager();
        SFXLibrary.loadAllAsync(assets);
        MusicLibrary.loadAllAsync(assets);
        setScreen(new TitleScreen(this));
    }

    @Override
    public void dispose() {
        super.dispose();
        assets.dispose();
    }
}
