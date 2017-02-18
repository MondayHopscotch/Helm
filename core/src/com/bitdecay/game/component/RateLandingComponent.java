package com.bitdecay.game.component;

/**
 * Created by Monday on 12/17/2016.
 */
public class RateLandingComponent extends GameComponent {
    public float[] playerGeom;
    public float[] landingGeometry;

    public RateLandingComponent(float[] playerGeom, float[] landingGeometry) {
        this.playerGeom = playerGeom;
        this.landingGeometry = landingGeometry;
    }
}
