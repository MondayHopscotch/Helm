package com.bitdecay.helm.component;

/**
 * Created by Monday on 1/12/2017.
 */
public class ProximityComponent extends GameComponent {
    public float radius;
    public GameComponent applyWhenTriggered;
    public boolean isMaster = false;

    public ProximityComponent(float radius, GameComponent addWhenTriggered, boolean master) {
        this.radius = radius;
        this.applyWhenTriggered = addWhenTriggered;
        isMaster = master;
    }
}
