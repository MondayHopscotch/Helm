package com.bitdecay.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.bitdecay.game.GameEntity;
import com.bitdecay.game.collision.CollisionDirection;
import com.bitdecay.game.component.BoostControlComponent;
import com.bitdecay.game.component.CameraFollowComponent;
import com.bitdecay.game.component.CollisionGeometryComponent;
import com.bitdecay.game.component.FuelComponent;
import com.bitdecay.game.component.GravityAffectedComponent;
import com.bitdecay.game.component.PlayerCollisionComponent;
import com.bitdecay.game.component.ProximityComponent;
import com.bitdecay.game.component.RenderColorComponent;
import com.bitdecay.game.component.BodyDefComponent;
import com.bitdecay.game.component.SteeringControlComponent;
import com.bitdecay.game.component.TransformComponent;
import com.bitdecay.game.component.ShipLaunchComponent;
import com.bitdecay.game.math.Geom;

/**
 * Created by Monday on 12/12/2016.
 */
public class ShipEntity extends GameEntity {

    public ShipEntity(Vector2 startPosition, int startingFuel) {
        addComponent(new ShipLaunchComponent(1.5f));
        addComponent(new FuelComponent(startingFuel));

        addComponent(new BoostControlComponent(new Rectangle(Gdx.graphics.getWidth()/2, 0, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight())));
        addComponent(new SteeringControlComponent(new Rectangle(0, 0, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight())));

        float[] geomPoints = new float[]{-100, 50, 100, 0, -100, -50};
        addComponent(new CollisionGeometryComponent(geomPoints, CollisionDirection.RECEIVES));
        addComponent(new PlayerCollisionComponent());

        addComponent(new BodyDefComponent(geomPoints));
        addComponent(new TransformComponent(startPosition, Geom.ROTATION_UP));
        addComponent(new GravityAffectedComponent());

        addComponent(new ProximityComponent(0, null, true));
        addComponent(new CameraFollowComponent());
        addComponent(new RenderColorComponent(Color.WHITE));
    }
}
