package com.bitdecay.game.system.movement;

import com.bitdecay.game.GameEntity;
import com.bitdecay.game.GamePilot;
import com.bitdecay.game.component.TransformComponent;
import com.bitdecay.game.component.VelocityComponent;
import com.bitdecay.game.system.AbstractIteratingGameSystem;

/**
 * Created by Monday on 12/12/2016.
 */
public class MovementSystem extends AbstractIteratingGameSystem {
    public MovementSystem(GamePilot pilot) {
        super(pilot);
    }

    @Override
    public void actOnSingle(GameEntity entity, float delta) {
        TransformComponent transform = entity.getComponent(TransformComponent.class);
        VelocityComponent velocity = entity.getComponent(VelocityComponent.class);

        transform.position.add(velocity.currentVelocity);
    }

    @Override
    public boolean canActOn(GameEntity entity) {
        return entity.hasComponent(TransformComponent.class) &&
                entity.hasComponent(VelocityComponent.class);
    }
}
