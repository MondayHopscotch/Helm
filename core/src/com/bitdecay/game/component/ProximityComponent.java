package com.bitdecay.game.component;

/**
 * Created by Monday on 1/12/2017.
 */
public class ProximityComponent extends GameComponent {
    public float radius;
    public GameComponent applyWhenTriggered;

    public ProximityComponent(float radius, GameComponent addWhenTriggered) {
        this.radius = radius;
        this.applyWhenTriggered = addWhenTriggered;
    }
}
