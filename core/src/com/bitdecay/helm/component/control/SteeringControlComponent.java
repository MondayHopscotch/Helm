package com.bitdecay.helm.component.control;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Monday on 12/12/2016.
 */
public class SteeringControlComponent extends com.bitdecay.helm.component.GameComponent {

    public static final float ANGLE_NOT_SET = Float.POSITIVE_INFINITY;

    public Rectangle activeArea;
    public float angle = ANGLE_NOT_SET; // default to signify that no input received yet

    public float sensitivity;
    public Vector2 startPoint;
    public Vector2 endPoint;

    public SteeringControlComponent(boolean leftHandedControls) {
        activeArea = new Rectangle(0, 0, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight());
        if (leftHandedControls) {
            activeArea.setX(Gdx.graphics.getWidth() - activeArea.getWidth());
        }
    }
}
