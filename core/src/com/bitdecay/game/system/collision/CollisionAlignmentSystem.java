package com.bitdecay.game.system.collision;

import com.bitdecay.game.GameEntity;
import com.bitdecay.game.GamePilot;
import com.bitdecay.game.component.collide.CollisionGeometryComponent;
import com.bitdecay.game.component.TransformComponent;
import com.bitdecay.game.system.AbstractIteratingGameSystem;

/**
 * Created by Monday on 12/14/2016.
 */
public class CollisionAlignmentSystem extends AbstractIteratingGameSystem {
    public CollisionAlignmentSystem(GamePilot pilot) {
        super(pilot);
    }

    @Override
    public void actOnSingle(GameEntity entity, float delta) {
        CollisionGeometryComponent geom = entity.getComponent(CollisionGeometryComponent.class);
        TransformComponent transform = entity.getComponent(TransformComponent.class);

        geom.posX = transform.position.x;
        geom.posY = transform.position.y;
        geom.rotation = transform.angle;
    }

    @Override
    public boolean canActOn(GameEntity entity) {
        return entity.hasComponent(CollisionGeometryComponent.class) &&
                entity.hasComponent(TransformComponent.class);
    }
}