package com.bitdecay.game.desktop.editor;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.bitdecay.game.Helm;
import com.bitdecay.game.unlock.stats.Statistics;
import com.bitdecay.game.world.LevelDefinition;

/**
 * Created by Monday on 1/1/2017.
 */
public class HelmEditor extends Game {

    private EditorScreen editorScreen;
    private LevelTestScreen levelTestScreen;


    @Override
    public void create() {
        Helm.debug = true;
        Helm.prefs = Gdx.app.getPreferences("test-prefs");
        Helm.stats = new Statistics(); // doesn't need to load anything
        editorScreen = new EditorScreen(this);
        levelTestScreen = new LevelTestScreen(this);
        showEditor();
    }

    public void testLevel(LevelDefinition level) {
        Gdx.input.setInputProcessor(null);
        levelTestScreen.setLevel(level);
        setScreen(levelTestScreen);
    }

    public void showEditor() {
        Gdx.input.setInputProcessor(null);

        Gdx.input.getInputProcessor();
        setScreen(editorScreen);
    }
}
