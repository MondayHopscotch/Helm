package com.bitdecay.game.desktop.editor;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.bitdecay.game.world.LevelDefinition;

/**
 * Created by Monday on 1/1/2017.
 */
public class HelmEditor extends Game {

    private EditorScreen editorScreen;
    private LevelTestScreen levelTestScreen;


    @Override
    public void create() {
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
