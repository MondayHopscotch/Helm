package com.bitdecay.game.system;

import com.bitdecay.game.GameEntity;
import com.bitdecay.game.GamePilot;
import com.bitdecay.game.component.SteeringComponent;
import com.bitdecay.game.component.control.SteeringControlComponent;
import com.bitdecay.game.component.TransformComponent;

/**
 * Created by Monday on 12/12/2016.
 */
public class SteeringSystem extends AbstractIteratingGameSystem {
    public SteeringSystem(GamePilot pilot) {
        super(pilot);
    }

    @Override
    public void actOnSingle(GameEntity entity, float delta) {
        SteeringControlComponent control = entity.getComponent(SteeringControlComponent.class);
        SteeringComponent steering = entity.getComponent(SteeringComponent.class);
        TransformComponent transform = entity.getComponent(TransformComponent.class);

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
    public boolean canActOn(GameEntity entity) {
        return entity.hasComponent(SteeringControlComponent.class) &&
                entity.hasComponent(SteeringComponent.class) &&
                entity.hasComponent(TransformComponent.class);
    }
}
