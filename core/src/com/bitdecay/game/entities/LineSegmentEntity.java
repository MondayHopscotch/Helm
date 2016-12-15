package com.bitdecay.game.entities;

import com.badlogic.gdx.math.Vector2;
import com.bitdecay.game.GameEntity;
import com.bitdecay.game.component.CollisionGeometryComponent;
import com.bitdecay.game.component.PositionComponent;
import com.bitdecay.game.component.RotationComponent;
import com.bitdecay.game.component.ShipBodyComponent;
import com.bitdecay.game.math.Geom;
import com.bitdecay.game.world.LineSegment;

/**
 * Created by Monday on 12/14/2016.
 */
public class LineSegmentEntity extends GameEntity {
    public LineSegmentEntity(LineSegment line) {
        float[] geomPoints = new float[]{line.startPoint.x, line.startPoint.y, line.endPoint.x, line.endPoint.y};
        addComponent(new ShipBodyComponent(geomPoints));
        addComponent(new CollisionGeometryComponent(geomPoints));
        addComponent(new PositionComponent(Vector2.Zero));
        addComponent(new RotationComponent(Geom.NO_ROTATION));
    }
}
