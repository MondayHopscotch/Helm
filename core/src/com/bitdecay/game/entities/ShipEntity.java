package com.bitdecay.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.bitdecay.game.GameEntity;
import com.bitdecay.game.collision.CollisionKind;
import com.bitdecay.game.component.BoostControlComponent;
import com.bitdecay.game.component.CameraFollowComponent;
import com.bitdecay.game.component.CollisionGeometryComponent;
import com.bitdecay.game.component.CollisionKindComponent;
import com.bitdecay.game.component.GravityComponent;
import com.bitdecay.game.component.PlayerCollisionComponent;
import com.bitdecay.game.component.RenderColorComponent;
import com.bitdecay.game.component.BodyDefComponent;
import com.bitdecay.game.component.SteeringControlComponent;
import com.bitdecay.game.component.TransformComponent;
import com.bitdecay.game.component.WaitingToStartComponent;
import com.bitdecay.game.math.Geom;

/**
 * Created by Monday on 12/12/2016.
 */
public class ShipEntity extends GameEntity {

    public ShipEntity(Vector2 startPosition) {
        addComponent(new WaitingToStartComponent());

        addComponent(new BoostControlComponent(new Rectangle(Gdx.graphics.getWidth()/2, 0, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight())));
        addComponent(new SteeringControlComponent(new Rectangle(0, 0, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight())));

        float[] geomPoints = new float[]{-100, -50, -100, 50, 100, 0};
        addComponent(new CollisionGeometryComponent(geomPoints, CollisionGeometryComponent.Direction.RECEIVES));
        addComponent(new CollisionKindComponent(CollisionKind.PLAYER));
        addComponent(new PlayerCollisionComponent());

        addComponent(new BodyDefComponent(geomPoints));
        addComponent(new TransformComponent(startPosition, Geom.ROTATION_UP));
        addComponent(new GravityComponent());

        addComponent(new CameraFollowComponent());
        addComponent(new RenderColorComponent(Color.WHITE));
    }
}
