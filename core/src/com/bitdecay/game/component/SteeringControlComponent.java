package com.bitdecay.game.component;

import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Monday on 12/12/2016.
 */
public class SteeringControlComponent extends GameComponent {
    public Rectangle activeArea;
    public float angle = Float.MAX_VALUE; // default to signify that no input received yet

    public SteeringControlComponent(Rectangle area) {
        activeArea = new Rectangle(area);
    }
}
