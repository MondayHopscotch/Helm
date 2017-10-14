package com.bitdecay.helm.entities;

import com.badlogic.gdx.math.Vector2;
import com.bitdecay.helm.collision.CollisionDirection;
import com.bitdecay.helm.collision.CollisionKind;
import com.bitdecay.helm.component.collide.CollisionKindComponent;
import com.bitdecay.helm.component.RenderColorComponent;
import com.bitdecay.helm.component.TransformComponent;
import com.bitdecay.helm.component.collide.GeometryComponentFactory;
import com.bitdecay.helm.math.Geom;
import com.bitdecay.helm.unlock.palette.GameColors;

/**
 * Created by Monday on 12/14/2016.
 */
public class LineSegmentEntity extends com.bitdecay.helm.GameEntity {
    public LineSegmentEntity(com.bitdecay.helm.world.LineSegment line) {
        Vector2 midpoint = line.endPoint.cpy().sub(line.startPoint);
        com.bitdecay.helm.world.LineSegment copy = new com.bitdecay.helm.world.LineSegment(line.startPoint.cpy().sub(midpoint), line.endPoint.cpy().sub(midpoint));

        float[] geomPoints = new float[]{copy.startPoint.x, copy.startPoint.y, copy.endPoint.x, copy.endPoint.y};
        addComponent(new com.bitdecay.helm.component.BodyDefComponent(geomPoints));
        addComponent(GeometryComponentFactory.getLineGeomComponent(copy.startPoint, copy.endPoint, CollisionDirection.DELIVERS));
        addComponent(new CollisionKindComponent(CollisionKind.WALL));

        addComponent(new TransformComponent(midpoint, Geom.NO_ROTATION));
        addComponent(new RenderColorComponent(GameColors.LEVEL_SEGMENT));
    }
}
