package com.bitdecay.game.component;

/**
 * Created by Monday on 12/14/2016.
 */
public class CollisionGeometryComponent extends GameComponent {
    public float[] originalGeom;

    public float posX;
    public float posY;

    public float rotation;

    public boolean colliding;

    public Direction direction;

    public CollisionGeometryComponent(float[] geomPoints, Direction direction) {
        originalGeom = new float[geomPoints.length];
        System.arraycopy(geomPoints, 0, originalGeom, 0, geomPoints.length);
        this.direction = direction;
    }

    public enum Direction {
        RECEIVES,
        DELIVERS;
    }
}
