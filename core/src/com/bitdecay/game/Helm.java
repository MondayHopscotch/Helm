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

public class Helm extends Game {

    public static Preferences prefs;

    public AssetManager assets;
    public Skin skin;
    public float fontScale = 1;

    @Override
    public void create() {
        Helm.prefs = Gdx.app.getPreferences("helm-pref");
        assets = new AssetManager();
        SFXLibrary.loadAllAsync(assets);
        MusicLibrary.loadAllAsync(assets);
        loadSkinSync(assets);
        skin = new Skin(Gdx.files.internal("skin/skin.json"), assets.get("skin/ui.atlas", TextureAtlas.class));

        skin.getFont("defaultFont").setUseIntegerPositions(true);
        // super arbitrary number to try to get fonts to scale nicely for different screens
        fontScale = (int)(Gdx.graphics.getWidth() / 300f);

        setScreen(new TitleScreen(this));
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
