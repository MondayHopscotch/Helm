package com.bitdecay.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.bitdecay.game.screen.GameScreen;
import com.bitdecay.game.screen.TitleScreen;
import com.bitdecay.game.sound.MusicLibrary;
import com.bitdecay.game.sound.SFXLibrary;
import com.bitdecay.game.unlock.Statistics;

public class Helm extends Game {
    public static boolean debug;

    public static Preferences prefs;
    public static Statistics stats;

    public AssetManager assets;
    public Skin skin;
    public float fontScale = 1;

    @Override
    public void create() {
        Helm.prefs = Gdx.app.getPreferences("helm-pref");
        checkUpdateClears();

        assets = new AssetManager();
        SFXLibrary.loadAllAsync(assets);
        MusicLibrary.loadAllAsync(assets);
        loadSkinSync(assets);
        skin = new Skin(Gdx.files.internal("skin/skin.json"), assets.get("skin/ui.atlas", TextureAtlas.class));

        skin.getFont("defaultFont").setUseIntegerPositions(true);
        // super arbitrary number to try to get fonts to scale nicely for different screens
        fontScale = (int)(Gdx.graphics.getWidth() / 300f);

        stats = new Statistics();
        stats.init(Helm.prefs);

        setScreen(new TitleScreen(this));
    }

    private void checkUpdateClears() {
        String update0dot4 = "clear_required_0.4";
        if (Helm.prefs.getBoolean(update0dot4, true)) {
            System.out.println("Clearing prefs for '" + update0dot4 + "'");
            Helm.prefs.clear();
            Helm.prefs.putBoolean(update0dot4, false);
            Helm.prefs.flush();
        }
    }

    private void loadSkinSync(AssetManager assets) {
        assets.load("skin/ui.atlas", TextureAtlas.class);
        assets.finishLoadingAsset("skin/ui.atlas");
    }

    @Override
    public void dispose() {
        super.dispose();
        assets.dispose();
        if (Helm.prefs != null) {
            Helm.prefs.flush();
        }
    }
}
