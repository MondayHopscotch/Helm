package com.bitdecay.helm.desktop.editor;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.bitdecay.helm.Helm;

/**
 * Created by Monday on 1/1/2017.
 */
public class HelmEditor extends Game {

    private EditorScreen editorScreen;
    private com.bitdecay.helm.desktop.editor.LevelTestScreen levelTestScreen;


    @Override
    public void create() {
        Helm.debug = true;
        Helm.prefs = Gdx.app.getPreferences("test-prefs");
        Helm.stats = new com.bitdecay.helm.unlock.Statistics(); // doesn't need to load anything
        editorScreen = new EditorScreen(this);
        levelTestScreen = new com.bitdecay.helm.desktop.editor.LevelTestScreen(this);
        showEditor();
    }

    public void testLevel(com.bitdecay.helm.world.LevelDefinition level) {
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
