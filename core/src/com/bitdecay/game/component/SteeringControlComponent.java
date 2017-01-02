package com.bitdecay.game.component;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Monday on 12/12/2016.
 */
public class SteeringControlComponent extends GameComponent {

    public static final float ANGLE_NOT_SET = Float.POSITIVE_INFINITY;

    public Rectangle activeArea;
    public float angle = ANGLE_NOT_SET; // default to signify that no input received yet

    public float sensitivity;
    public Vector2 startPoint;
    public Vector2 endPoint;

    public SteeringControlComponent(Rectangle area) {
        activeArea = new Rectangle(area);
    }
}
