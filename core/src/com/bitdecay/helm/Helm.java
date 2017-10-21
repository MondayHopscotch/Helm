package com.bitdecay.helm;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ObjectSet;
import com.bitdecay.helm.screen.SplashScreen;
import com.bitdecay.helm.sound.MusicLibrary;
import com.bitdecay.helm.sound.SFXLibrary;
import com.bitdecay.helm.system.render.GamePalette;
import com.bitdecay.helm.unlock.LiveStat;
import com.bitdecay.helm.unlock.Statistics;
import com.bitdecay.helm.unlock.palette.PaletteList;

public class Helm extends Game {
    public static boolean debug;

    public static float aspectRatio = 16 / 9f;

    public static Preferences prefs;
    public static Statistics stats;

    public AssetManager assets;
    public Skin skin;
    public float fontScale = 1;

    // this is a screen cache so we can properly dispose things when our game closes
    private ObjectSet<Screen> screens = new ObjectSet<>();

    public GamePalette palette;

    @Override
    public void create() {
        aspectRatio = 1.0f * Gdx.graphics.getWidth() / Gdx.graphics.getHeight();

        Helm.prefs = Gdx.app.getPreferences("helm-pref");
        stats = new com.bitdecay.helm.unlock.Statistics();
        stats.init(Helm.prefs);
        checkUpdateClears();

        palette = PaletteList.valueOf(Helm.prefs.getString(com.bitdecay.helm.prefs.GamePrefs.CHOSEN_PALETTE, com.bitdecay.helm.unlock.palette.PaletteList.STANDARD.name())).palette;

        assets = new AssetManager();
        skin = new Skin(Gdx.files.internal("skin/skin.json"), new TextureAtlas(Gdx.files.internal("skin/ui.atlas")));
        skin.getFont("defaultFont").setUseIntegerPositions(true);
        // super arbitrary number to try to get fonts to scale nicely for different screens
        fontScale = (int) (Gdx.graphics.getHeight() / 168.75f);

        setScreen(new SplashScreen(this));
    }

    private void checkUpdateClears() {
        String[] updatesRequiringClear = new String[]{
                "clear_required_0.4",
                "clear_required_0.6",
                "clear_required_0.7"
        };

        boolean prefClearRequired = false;

        for (String updateSetting : updatesRequiringClear) {
            if (Helm.prefs.getBoolean(updateSetting, true)) {
                System.out.println("Clearing prefs for '" + updateSetting + "'");
                prefClearRequired = true;
            }
        }

        if (prefClearRequired) {
            // preserve our play time
            LiveStat flightTime = Helm.stats.getLiveStat(com.bitdecay.helm.unlock.StatName.FLIGHT_TIME);
            Helm.prefs.clear();
            flightTime.save(Helm.prefs);
            for (String updateSetting : updatesRequiringClear) {
                Helm.prefs.putBoolean(updateSetting, false);
            }
            Helm.prefs.flush();
        }
    }

    @Override
    public void setScreen(Screen screen) {
        super.setScreen(screen);
        screens.add(screen);
    }

    @Override
    public void dispose() {
        super.dispose();

        for (Screen screen : screens.iterator()) {
            screen.dispose();
        }
        screens.clear();

        assets.dispose();
        if (Helm.prefs != null) {
            Helm.prefs.flush();
        }
    }
}
