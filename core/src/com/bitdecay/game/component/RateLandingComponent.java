package com.bitdecay.game.component;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by Monday on 12/17/2016.
 */
public class RateLandingComponent extends GameComponent {
    public Vector2 landingVector;
    public float landingAngle;

    public RateLandingComponent(Vector2 landingVector, float landingAngle) {
        this.landingVector = landingVector;
        this.landingAngle = landingAngle;
    }
}
