package com.bitdecay.game.component;

/**
 * Created by Monday on 12/12/2016.
 */
public class ShipBodyComponent extends GameComponent {
    public float[] bodyPoints;

    public ShipBodyComponent(float[] shape) {
        bodyPoints = shape;
    }
}
