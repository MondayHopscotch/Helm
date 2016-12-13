package com.bitdecay.game.component;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by Monday on 12/12/2016.
 */
public class ShipBodyComponent extends GameComponent {
    public Vector2[] bodyPoints;
    public float angle;


    public ShipBodyComponent(Vector2[] points) {
        bodyPoints = points;
        angle = 0;
    }
}
