package com.bitdecay.helm.component;

import com.bitdecay.helm.component.collide.CollisionGeometryComponent;

/**
 * Created by Monday on 12/17/2016.
 */
public class RateLandingComponent extends GameComponent {
    public CollisionGeometryComponent geom;
    public float[] playerGeom;
    public float[] landingGeometry;

    public RateLandingComponent(CollisionGeometryComponent geom, float[] playerGeom, float[] landingGeometry) {
        this.geom = geom;
        this.playerGeom = playerGeom;
        this.landingGeometry = landingGeometry;
    }
}
