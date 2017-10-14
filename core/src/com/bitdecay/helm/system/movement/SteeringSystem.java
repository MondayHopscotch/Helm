package com.bitdecay.helm.system.movement;

import com.bitdecay.helm.component.control.SteeringControlComponent;
import com.bitdecay.helm.system.AbstractIteratingGameSystem;

/**
 * Created by Monday on 12/12/2016.
 */
public class SteeringSystem extends AbstractIteratingGameSystem {
    public SteeringSystem(com.bitdecay.helm.GamePilot pilot) {
        super(pilot);
    }

    @Override
    public void actOnSingle(com.bitdecay.helm.GameEntity entity, float delta) {
        SteeringControlComponent control = entity.getComponent(SteeringControlComponent.class);
        com.bitdecay.helm.component.SteeringComponent steering = entity.getComponent(com.bitdecay.helm.component.SteeringComponent.class);
        com.bitdecay.helm.component.TransformComponent transform = entity.getComponent(com.bitdecay.helm.component.TransformComponent.class);

        if (control.angle == SteeringControlComponent.ANGLE_NOT_SET) {
            // no input received yet. Set value to ships current rotation
            control.angle = transform.angle;
        }

        if (transform.angle != control.angle) {
            if (steering.steerSpeed > 0) {
                // handle turning speed
            } else {
                // instant turning
                transform.angle = control.angle;
            }
        }
    }

    @Override
    public boolean canActOn(com.bitdecay.helm.GameEntity entity) {
        return entity.hasComponent(SteeringControlComponent.class) &&
                entity.hasComponent(com.bitdecay.helm.component.SteeringComponent.class) &&
                entity.hasComponent(com.bitdecay.helm.component.TransformComponent.class);
    }
}
