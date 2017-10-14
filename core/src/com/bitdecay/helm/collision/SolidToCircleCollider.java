package com.bitdecay.helm.collision;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Monday on 2/18/2017.
 */

public class SolidToCircleCollider extends Collider {

    private boolean flippedInputs;

    public SolidToCircleCollider(com.bitdecay.helm.component.collide.CollisionGeometryComponent geom1, com.bitdecay.helm.component.collide.CollisionGeometryComponent geom2, boolean flippedInputs) {
        super(geom1, geom2);
        this.flippedInputs = flippedInputs;
    }

    @Override
    public boolean collisionFound() {
        Vector2 start = new Vector2();
        Vector2 end = new Vector2();
        Vector2 center = new Vector2(geom2.posX, geom2.posY);
        for (int i = 1; i < geom1WorkingSet.length; i += 2) {
            if (i == 1) {
                start.set(geom1WorkingSet[geom1WorkingSet.length - 2], geom1WorkingSet[geom1WorkingSet.length - 1]);
                end.set(geom1WorkingSet[i-1], geom1WorkingSet[i]);
            } else {
                start.set(geom1WorkingSet[i - 3], geom1WorkingSet[i - 2]);
                end.set(geom1WorkingSet[i - 1], geom1WorkingSet[i]);
            }
            if (Intersector.intersectSegmentCircle(start, end, center, geom2WorkingSet[0] * geom2WorkingSet[0])) {
                return true;
            }
        }
        return false;
    }

    @Override
    public float[] getGeom1WorkingSet() {
        if (flippedInputs) {
            return super.getGeom2WorkingSet();
        } else {
            return super.getGeom1WorkingSet();
        }
    }

    @Override
    public float[] getGeom2WorkingSet() {
        if (flippedInputs) {
            return super.getGeom1WorkingSet();
        } else {
            return super.getGeom2WorkingSet();
        }
    }
}
