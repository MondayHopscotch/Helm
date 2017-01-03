package com.bitdecay.game.component;

import com.bitdecay.game.collision.CollisionDirection;

/**
 * Created by Monday on 12/14/2016.
 */
public class CollisionGeometryComponent extends GameComponent {
    public float[] originalGeom;

    public float posX;
    public float posY;

    public float rotation;

    public boolean colliding;

    public CollisionDirection direction;

    public CollisionGeometryComponent(float[] geomPoints, CollisionDirection direction) {
        originalGeom = new float[geomPoints.length];
        System.arraycopy(geomPoints, 0, originalGeom, 0, geomPoints.length);
        this.direction = direction;
    }
}
