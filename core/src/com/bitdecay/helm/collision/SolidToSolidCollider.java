package com.bitdecay.helm.collision;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;

/**
 * Created by Monday on 2/18/2017.
 */

public class SolidToSolidCollider extends Collider {
    public SolidToSolidCollider(com.bitdecay.helm.component.collide.CollisionGeometryComponent geom1, com.bitdecay.helm.component.collide.CollisionGeometryComponent geom2) {
        super(geom1, geom2);
    }

    public SolidToSolidCollider() {
        super();
    }

    @Override
    public boolean collisionFound() {
        boolean collisionFound;
        // TODO: Figure out if this is true: this doesn't appear to be commutative operation
        collisionFound = Intersector.intersectPolygons(new Polygon(geom1WorkingSet), new Polygon(geom2WorkingSet), new Polygon());
        collisionFound |= Intersector.intersectPolygons(new Polygon(geom2WorkingSet), new Polygon(geom1WorkingSet), new Polygon());
        return collisionFound;
    }
}
