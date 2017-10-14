package com.bitdecay.helm.entities;

import com.badlogic.gdx.math.Vector2;
import com.bitdecay.helm.GameEntity;
import com.bitdecay.helm.Helm;
import com.bitdecay.helm.collision.CollisionDirection;
import com.bitdecay.helm.component.DelayedAddComponent;
import com.bitdecay.helm.component.collide.GeometryComponentFactory;
import com.bitdecay.helm.component.control.BoostControlComponent;
import com.bitdecay.helm.component.CameraFollowComponent;
import com.bitdecay.helm.component.FuelComponent;
import com.bitdecay.helm.component.GravityAffectedComponent;
import com.bitdecay.helm.component.collide.PlayerCollisionComponent;
import com.bitdecay.helm.component.ProximityComponent;
import com.bitdecay.helm.component.RenderColorComponent;
import com.bitdecay.helm.component.BodyDefComponent;
import com.bitdecay.helm.component.control.SteeringControlComponent;
import com.bitdecay.helm.component.TransformComponent;
import com.bitdecay.helm.component.ShipLaunchComponent;
import com.bitdecay.helm.math.Geom;
import com.bitdecay.helm.prefs.GamePrefs;
import com.bitdecay.helm.unlock.palette.GameColors;

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
