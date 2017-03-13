package com.bitdecay.game.entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.bitdecay.game.GameEntity;
import com.bitdecay.game.collision.CollisionDirection;
import com.bitdecay.game.collision.CollisionKind;
import com.bitdecay.game.component.BodyDefComponent;
import com.bitdecay.game.component.CameraFollowComponent;
import com.bitdecay.game.component.collide.CollisionKindComponent;
import com.bitdecay.game.component.RenderColorComponent;
import com.bitdecay.game.component.TransformComponent;
import com.bitdecay.game.component.collide.GeometryComponentFactory;
import com.bitdecay.game.math.Geom;
import com.bitdecay.game.world.GameColors;

/**
 * Created by Monday on 12/14/2016.
 */
public class LandingPlatformEntity extends GameEntity {

    public LandingPlatformEntity(Rectangle shape, float rotation) {
        Rectangle copy = new Rectangle(shape);
        copy.x -= shape.x;
        copy.y -= shape.y;

    float[] geomPoints = Geom.rectangleToFloatPoints(copy, 0);
        addComponent(new BodyDefComponent(geomPoints));
        addComponent(GeometryComponentFactory.getPolygonGeomComponent(geomPoints, CollisionDirection.DELIVERS));
        addComponent(new CollisionKindComponent(CollisionKind.LANDING_PLATFORM));

        addComponent(new TransformComponent(new Vector2(shape.x, shape.y), rotation));
        addComponent(new RenderColorComponent(GameColors.LANDING_PLATFORM));
        addComponent(new CameraFollowComponent());
    }
}
