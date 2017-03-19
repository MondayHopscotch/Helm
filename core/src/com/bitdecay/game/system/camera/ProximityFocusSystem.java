package com.bitdecay.game.system.camera;

import com.badlogic.gdx.math.Vector2;
import com.bitdecay.game.GameEntity;
import com.bitdecay.game.GamePilot;
import com.bitdecay.game.component.CameraFollowComponent;
import com.bitdecay.game.component.ProximityComponent;
import com.bitdecay.game.component.TransformComponent;
import com.bitdecay.game.system.AbstractIteratingGameSystem;

import java.util.ArrayList;

/**
 * Created by Monday on 1/16/2017.
 */

public class ProximityFocusSystem extends AbstractIteratingGameSystem {
    GameEntity master;
    ArrayList<GameEntity> proximityEntities = new ArrayList<>();

    public ProximityFocusSystem(GamePilot pilot) {
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
        ProximityComponent proximity;
        CameraFollowComponent camera;
        for (GameEntity entity : proximityEntities) {
            transform = entity.getComponent(TransformComponent.class);
            proximity = entity.getComponent(ProximityComponent.class);
            camera = entity.getComponent(CameraFollowComponent.class);

            camera.offset.set(masterFocusLocation).sub(transform.position).nor().scl(proximity.radius);
        }

    }

    @Override
    public void actOnSingle(GameEntity entity, float delta) {
        ProximityComponent proximity = entity.getComponent(ProximityComponent.class);
        if (proximity.isMaster) {
            master = entity;
        } else {
            proximityEntities.add(entity);
        }


    }

    @Override
    public boolean canActOn(GameEntity entity) {
        return entity.hasComponents(
                TransformComponent.class,
                ProximityComponent.class,
                CameraFollowComponent.class
        );
    }
}
