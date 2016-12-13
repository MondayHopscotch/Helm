package com.bitdecay.game.system;

import com.bitdecay.game.GameEntity;
import com.bitdecay.game.component.PositionComponent;
import com.bitdecay.game.component.VelocityComponent;

/**
 * Created by Monday on 12/12/2016.
 */
public class MovementSystem extends AbstractIteratingGameSystem {
    @Override
    public void actOnSingle(GameEntity entity, float delta) {
        PositionComponent position = entity.getComponent(PositionComponent.class);
        VelocityComponent velocity = entity.getComponent(VelocityComponent.class);

        position.position.add(velocity.currentVelocity);
        System.out.println(velocity.currentVelocity);
    }

    @Override
    public boolean canActOn(GameEntity entity) {
        return entity.hasComponent(PositionComponent.class) &&
                entity.hasComponent(VelocityComponent.class);
    }
}
