package com.bitdecay.helm.entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Monday on 12/14/2016.
 */
public class LandingPlatformEntity extends com.bitdecay.helm.GameEntity {

    public LandingPlatformEntity(Rectangle shape, float rotation) {
        Rectangle copy = new Rectangle(shape);
        copy.x -= shape.x;
        copy.y -= shape.y;

    float[] geomPoints = com.bitdecay.helm.math.Geom.rectangleToFloatPoints(copy, 0);
        addComponent(new com.bitdecay.helm.component.BodyDefComponent(geomPoints));
        addComponent(com.bitdecay.helm.component.collide.GeometryComponentFactory.getPolygonGeomComponent(geomPoints, com.bitdecay.helm.collision.CollisionDirection.DELIVERS));
        addComponent(new com.bitdecay.helm.component.collide.CollisionKindComponent(com.bitdecay.helm.collision.CollisionKind.LANDING_PLATFORM));

        addComponent(new com.bitdecay.helm.component.TransformComponent(new Vector2(shape.x, shape.y), rotation));
        addComponent(new com.bitdecay.helm.component.RenderColorComponent(com.bitdecay.helm.unlock.palette.GameColors.LANDING_PLATFORM));
        addComponent(new com.bitdecay.helm.component.CameraFollowComponent());
    }
}
