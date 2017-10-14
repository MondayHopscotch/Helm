package com.bitdecay.helm.system.camera;

import com.badlogic.gdx.math.Vector2;
import com.bitdecay.helm.component.CameraFollowComponent;
import com.bitdecay.helm.component.TransformComponent;
import com.bitdecay.helm.system.AbstractIteratingGameSystem;

import java.util.ArrayList;

/**
 * Created by Monday on 1/16/2017.
 */

public class ProximityFocusSystem extends AbstractIteratingGameSystem {
    com.bitdecay.helm.GameEntity master;
    ArrayList<com.bitdecay.helm.GameEntity> proximityEntities = new ArrayList<>();

    public ProximityFocusSystem(com.bitdecay.helm.GamePilot pilot) {
        super(pilot);
    }

    @Override
    public void before() {
        master = null;
        proximityEntities.clear();
    }

    @Override
    public void after() {
        if (master == null) {
            return;
        }

        Vector2 masterFocusLocation = master.getComponent(TransformComponent.class).position;

        TransformComponent transform;
        com.bitdecay.helm.component.ProximityComponent proximity;
        CameraFollowComponent camera;
        for (com.bitdecay.helm.GameEntity entity : proximityEntities) {
            transform = entity.getComponent(TransformComponent.class);
            proximity = entity.getComponent(com.bitdecay.helm.component.ProximityComponent.class);
            camera = entity.getComponent(CameraFollowComponent.class);

            camera.offset.set(masterFocusLocation).sub(transform.position).nor().scl(proximity.radius);
        }

    }

    @Override
    public void actOnSingle(com.bitdecay.helm.GameEntity entity, float delta) {
        com.bitdecay.helm.component.ProximityComponent proximity = entity.getComponent(com.bitdecay.helm.component.ProximityComponent.class);
        if (proximity.isMaster) {
            master = entity;
        } else {
            proximityEntities.add(entity);
        }


    }

    @Override
    public boolean canActOn(com.bitdecay.helm.GameEntity entity) {
        return entity.hasComponents(
                TransformComponent.class,
                com.bitdecay.helm.component.ProximityComponent.class,
                CameraFollowComponent.class
        );
    }
}
