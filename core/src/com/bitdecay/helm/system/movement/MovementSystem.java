package com.bitdecay.helm.system.movement;

import com.bitdecay.helm.component.VelocityComponent;
import com.bitdecay.helm.system.AbstractIteratingGameSystem;

/**
 * Created by Monday on 12/12/2016.
 */
public class MovementSystem extends AbstractIteratingGameSystem {
    public MovementSystem(com.bitdecay.helm.GamePilot pilot) {
        super(pilot);
    }

    @Override
    public void actOnSingle(com.bitdecay.helm.GameEntity entity, float delta) {
        com.bitdecay.helm.component.TransformComponent transform = entity.getComponent(com.bitdecay.helm.component.TransformComponent.class);
        VelocityComponent velocity = entity.getComponent(VelocityComponent.class);

        transform.position.add(velocity.currentVelocity);
    }

    @Override
    public boolean canActOn(com.bitdecay.helm.GameEntity entity) {
        return entity.hasComponent(com.bitdecay.helm.component.TransformComponent.class) &&
                entity.hasComponent(VelocityComponent.class);
    }
}
