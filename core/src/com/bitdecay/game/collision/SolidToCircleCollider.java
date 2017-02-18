package com.bitdecay.game.collision;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.bitdecay.game.component.collide.CollisionGeometryComponent;

/**
 * Created by Monday on 2/18/2017.
 */

public class SolidToCircleCollider extends Collider {
    public SolidToCircleCollider(CollisionGeometryComponent geom1, CollisionGeometryComponent geom2) {
        super(geom1, geom2);
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
}
