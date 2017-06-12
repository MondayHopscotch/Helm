package com.bitdecay.game.component;

import com.bitdecay.game.unlock.palette.GameColors;

/**
 * Created by Monday on 12/14/2016.
 */
public class RenderColorComponent extends GameComponent {
    public GameColors color;

    public RenderColorComponent(GameColors color) {
        this.color = color;
    }
}
