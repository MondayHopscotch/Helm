package com.bitdecay.game.component.control;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.bitdecay.game.component.GameComponent;

/**
 * Created by Monday on 12/8/2016.
 */
public class BoostControlComponent extends GameComponent {
    public Rectangle activeArea;
    public boolean pressed;

    public BoostControlComponent(boolean leftHandedControls) {
        activeArea = new Rectangle(Gdx.graphics.getWidth()/2, 0, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight());
        if (leftHandedControls) {
            activeArea.setX(0);
        }
    }
}
