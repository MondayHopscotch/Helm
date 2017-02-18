package com.bitdecay.game.component.control;

import com.badlogic.gdx.math.Rectangle;
import com.bitdecay.game.component.GameComponent;

/**
 * Created by Monday on 12/8/2016.
 */
public class BoostControlComponent extends GameComponent {
    public Rectangle activeArea;
    public boolean pressed;

    public BoostControlComponent(Rectangle area) {
        activeArea = area;
    }
}
