package com.bitdecay.game.entities;

import com.badlogic.gdx.math.Vector2;
import com.bitdecay.game.GameEntity;
import com.bitdecay.game.Helm;
import com.bitdecay.game.collision.CollisionDirection;
import com.bitdecay.game.component.DelayedAddComponent;
import com.bitdecay.game.component.collide.GeometryComponentFactory;
import com.bitdecay.game.component.control.BoostControlComponent;
import com.bitdecay.game.component.CameraFollowComponent;
import com.bitdecay.game.component.FuelComponent;
import com.bitdecay.game.component.GravityAffectedComponent;
import com.bitdecay.game.component.collide.PlayerCollisionComponent;
import com.bitdecay.game.component.ProximityComponent;
import com.bitdecay.game.component.RenderColorComponent;
import com.bitdecay.game.component.BodyDefComponent;
import com.bitdecay.game.component.control.SteeringControlComponent;
import com.bitdecay.game.component.TransformComponent;
import com.bitdecay.game.component.ShipLaunchComponent;
import com.bitdecay.game.math.Geom;
import com.bitdecay.game.prefs.GamePrefs;
import com.bitdecay.game.unlock.palette.GameColors;

/**
 * Created by Monday on 12/12/2016.
 */
public class ShipEntity extends GameEntity {

    public ShipEntity(Vector2 startPosition, int startingFuel) {
        addComponent(new ShipLaunchComponent(1.5f));
        addComponent(new FuelComponent(startingFuel));

        boolean lefty = Helm.prefs.getBoolean(GamePrefs.USE_LEFT_HANDED_CONTROLS, GamePrefs.USE_LEFT_HANDED_CONTROLS_DEFAULT);

        addComponent(new BoostControlComponent(lefty));
        addComponent(new SteeringControlComponent(lefty));

        float[] geomPoints = new float[]{-100, 50, 100, 0, -100, -50};
        addComponent(GeometryComponentFactory.getPolygonGeomComponent(geomPoints, CollisionDirection.BOTH | CollisionDirection.PLAYER));
        addComponent(new PlayerCollisionComponent());

        addComponent(new BodyDefComponent(geomPoints));
        addComponent(new TransformComponent(startPosition, Geom.ROTATION_UP));
        addComponent(new GravityAffectedComponent());

        addComponent(new ProximityComponent(0, null, true));
        addComponent(new CameraFollowComponent());
        addComponent(new RenderColorComponent(GameColors.SHIP_BODY));

        addComponent(new DelayedAddComponent());
    }
}
