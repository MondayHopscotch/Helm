package com.bitdecay.game.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.bitdecay.game.GameEntity;
import com.bitdecay.game.collision.CollisionDirection;
import com.bitdecay.game.collision.CollisionKind;
import com.bitdecay.game.component.CollisionGeometryComponent;
import com.bitdecay.game.component.CollisionKindComponent;
import com.bitdecay.game.component.RenderColorComponent;
import com.bitdecay.game.component.BodyDefComponent;
import com.bitdecay.game.component.TransformComponent;
import com.bitdecay.game.math.Geom;
import com.bitdecay.game.world.LineSegment;

/**
 * Created by Monday on 12/14/2016.
 */
public class LineSegmentEntity extends GameEntity {
    public LineSegmentEntity(LineSegment line) {
        Vector2 midpoint = line.endPoint.cpy().sub(line.startPoint);
        LineSegment copy = new LineSegment(line.startPoint.cpy().sub(midpoint), line.endPoint.cpy().sub(midpoint));

        float[] geomPoints = new float[]{copy.startPoint.x, copy.startPoint.y, copy.endPoint.x, copy.endPoint.y};
        addComponent(new BodyDefComponent(geomPoints));
        addComponent(new CollisionGeometryComponent(geomPoints, CollisionDirection.DELIVERS));
        addComponent(new CollisionKindComponent(CollisionKind.WALL));

        addComponent(new TransformComponent(midpoint, Geom.NO_ROTATION));
        addComponent(new RenderColorComponent(Color.RED));
    }
}
