package com.bitdecay.game.system;

import com.bitdecay.game.GameEntity;
import com.bitdecay.game.component.CollisionGeometryComponent;
import com.bitdecay.game.component.RotationComponent;

/**
 * Created by Monday on 12/14/2016.
 */
public class CollisionRotationAlignmentSystem extends AbstractIteratingGameSystem {
    @Override
    public void actOnSingle(GameEntity entity, float delta) {
        CollisionGeometryComponent geom = entity.getComponent(CollisionGeometryComponent.class);
        RotationComponent rotation = entity.getComponent(RotationComponent.class);

        geom.rotation = rotation.angle;
    }

    @Override
    public boolean canActOn(GameEntity entity) {
        return entity.hasComponent(CollisionGeometryComponent.class) &&
                entity.hasComponent(RotationComponent.class);
    }
}