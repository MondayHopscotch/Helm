package com.bitdecay.helm.component.control;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Monday on 12/8/2016.
 */
public class BoostControlComponent extends com.bitdecay.helm.component.GameComponent {
    public Rectangle activeArea;
    public boolean pressed;

    public BoostControlComponent(boolean leftHandedControls) {
        activeArea = new Rectangle(Gdx.graphics.getWidth()/2, 0, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight());
        if (leftHandedControls) {
            activeArea.setX(0);
        }
    }
}
