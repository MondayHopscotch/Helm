package com.bitdecay.game;

import com.badlogic.gdx.Game;
import com.bitdecay.game.screen.GameScreen;

public class Helm extends Game {
    @Override
    public void create() {
        setScreen(new GameScreen(this));
    }
}
