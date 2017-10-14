package com.bitdecay.helm.world;

import com.badlogic.gdx.math.Circle;

/**
 * Created by Monday on 3/19/2017.
 */

public class WormholePair {
    public Circle entrance;
    public Circle exit;

    public WormholePair() {
        // Here for JSON
    }

    public WormholePair(Circle in, Circle out) {
        entrance = in;
        exit = out;
    }
}
