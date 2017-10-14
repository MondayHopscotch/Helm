package com.bitdecay.helm.collision;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.bitdecay.helm.component.collide.CollisionGeometryComponent;

/**
 * Created by Monday on 2/18/2017.
 */

public class SolidToLineCollider extends Collider {
    public SolidToLineCollider(CollisionGeometryComponent geom1, CollisionGeometryComponent geom2) {
        super(geom1, geom2);
    }

    @Override
    public boolean collisionFound() {
        Vector2 geom2Start = new Vector2(geom2WorkingSet[0], geom2WorkingSet[1]);
        Vector2 geom2End = new Vector2(geom2WorkingSet[2], geom2WorkingSet[3]);
        return Intersector.intersectSegmentPolygon(geom2Start, geom2End, new Polygon(geom1WorkingSet));
    }
}
