package com.bitdecay.game.system;

import com.bitdecay.game.GameEntity;
import com.bitdecay.game.component.CollisionGeometryComponent;
import com.bitdecay.game.component.PositionComponent;

/**
 * Created by Monday on 12/14/2016.
 */
public class CollisionPositionAlignmentSystem extends AbstractIteratingGameSystem {
    @Override
    public void actOnSingle(GameEntity entity, float delta) {
        CollisionGeometryComponent geom = entity.getComponent(CollisionGeometryComponent.class);
        PositionComponent pos = entity.getComponent(PositionComponent.class);

        geom.posX = pos.position.x;
        geom.posY = pos.position.y;
    }

    @Override
    public boolean canActOn(GameEntity entity) {
        return entity.hasComponent(CollisionGeometryComponent.class) &&
                entity.hasComponent(PositionComponent.class);
    }
}
