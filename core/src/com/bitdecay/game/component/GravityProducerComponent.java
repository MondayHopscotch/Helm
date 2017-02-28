package com.bitdecay.game.component;

/**
 * Created by Monday on 2/17/2017.
 */

public class GravityProducerComponent extends GameComponent {
    public float size;
    public float inner;
    public float ringCount = 4;
    public float animateSpeed = .3f;
    public boolean repels;

    public GravityProducerComponent(float size, boolean repels) {
        this.size = size;
        this.repels = repels;
    }
}
