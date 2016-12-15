package com.bitdecay.game.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.bitdecay.game.GameEntity;
import com.bitdecay.game.component.BodyDefComponent;
import com.bitdecay.game.component.CameraFollowComponent;
import com.bitdecay.game.component.CollisionGeometryComponent;
import com.bitdecay.game.component.CollisionKindComponent;
import com.bitdecay.game.component.PositionComponent;
import com.bitdecay.game.component.RenderColorComponent;
import com.bitdecay.game.component.RotationComponent;
import com.bitdecay.game.math.Geom;

/**
 * Created by Monday on 12/14/2016.
 */
public class LandingPlatformEntity extends GameEntity {

    public LandingPlatformEntity(Rectangle shape) {
        float[] geomPoints = Geom.rectangleToFloatPoints(shape);
        addComponent(new BodyDefComponent(geomPoints));
        addComponent(new CollisionGeometryComponent(geomPoints, CollisionGeometryComponent.Direction.DELIVERS));
        addComponent(new CollisionKindComponent(CollisionKindComponent.CollisionKind.LANDING_PLATFORM));


        addComponent(new PositionComponent(new Vector2()));
        addComponent(new RotationComponent(Geom.NO_ROTATION));
        addComponent(new RenderColorComponent(Color.GREEN));
        addComponent(new CameraFollowComponent());
    }
}
