package com.bitdecay.game.component;

/**
 * Created by Monday on 3/20/2017.
 */

public class WormholeComponent extends GameComponent {
    public float inSize;
    public float inner;
    public float outSize;

    public float angle = 0;
    public float twistFactor = 0.9f;

    public int drawCount = 5;
    public float animateSpeed = .2f;

    public WormholeComponent(float entranceSize, float exitSize) {
        inSize = entranceSize;
        outSize = exitSize;
    }
}
