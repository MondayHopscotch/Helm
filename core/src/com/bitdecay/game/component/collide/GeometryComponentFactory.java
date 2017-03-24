package com.bitdecay.game.component.collide;

import com.badlogic.gdx.math.Vector2;
import com.bitdecay.game.collision.CollisionDirection;

/**
 * Created by Monday on 2/18/2017.
 */

public class GeometryComponentFactory {

    public static CollisionGeometryComponent getCircleGeomComponent(float radius, int direction) {
        return new CollisionGeometryComponent(new float[]{radius}, direction);
    }

    public static CollisionGeometryComponent getLineGeomComponent(Vector2 start, Vector2 end, int direction) {
        return new CollisionGeometryComponent(new float[]{start.x, start.y, end.x, end.y}, direction);
    }

    public static CollisionGeometryComponent getPolygonGeomComponent(float[] geomPoints, int direction) {
        return new CollisionGeometryComponent(geomPoints, direction);
    }
}
