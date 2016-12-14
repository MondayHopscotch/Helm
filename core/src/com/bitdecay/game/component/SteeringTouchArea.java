package com.bitdecay.game.component;

import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Monday on 12/12/2016.
 */
public class SteeringTouchArea extends GameComponent {
    public Rectangle activeArea;

    public SteeringTouchArea(Rectangle area) {
        activeArea = new Rectangle(area);
    }
}
