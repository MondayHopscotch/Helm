package com.bitdecay.helm.collision;

import com.bitdecay.helm.component.collide.CollisionGeometryComponent;

/**
 * Created by Monday on 2/18/2017.
 */

public class NoOpCollider extends Collider {
    public NoOpCollider(CollisionGeometryComponent geom1, CollisionGeometryComponent geom2) {
        super(geom1, geom2);
    }

    public NoOpCollider() {

    }

    @Override
    public boolean collisionFound() {
        return false;
    }
}
