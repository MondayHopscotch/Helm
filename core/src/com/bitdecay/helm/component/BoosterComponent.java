package com.bitdecay.helm.component;

/**
 * Created by Monday on 12/12/2016.
 */
public class BoosterComponent extends GameComponent {
    public float strength;
    public boolean engaged;

    public BoosterComponent(float strength) {
        this.strength = strength;
    }
}
