package com.bitdecay.game.desktop.editor;

import com.badlogic.gdx.Game;

/**
 * Created by Monday on 1/1/2017.
 */
public class HelmEditor extends Game {
    @Override
    public void create() {
        setScreen(new EditorScreen());
    }
}
