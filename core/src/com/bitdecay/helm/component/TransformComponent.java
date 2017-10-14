package com.bitdecay.helm.component;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by Monday on 12/15/2016.
 */
public class TransformComponent extends GameComponent {
    public Vector2 position = new Vector2();
    public float angle;

    public TransformComponent(Vector2 initialPosition, float initialAngle) {
        position.set(initialPosition);
        angle = initialAngle;
    }
}
