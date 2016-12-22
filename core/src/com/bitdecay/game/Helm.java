package com.bitdecay.game;

import com.badlogic.gdx.Game;
import com.bitdecay.game.screen.GameScreen;
import com.bitdecay.game.screen.TitleScreen;

public class Helm extends Game {
    @Override
    public void create() {
        setScreen(new TitleScreen(this));
    }
}
