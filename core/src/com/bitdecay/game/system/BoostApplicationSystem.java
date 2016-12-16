package com.bitdecay.game.system;

import com.badlogic.gdx.math.Vector2;
import com.bitdecay.game.GameEntity;
import com.bitdecay.game.GamePilot;
import com.bitdecay.game.component.BoostActivateButton;
import com.bitdecay.game.component.BoosterComponent;
import com.bitdecay.game.component.TransformComponent;
import com.bitdecay.game.component.VelocityComponent;
import com.bitdecay.game.math.Geom;

/**
 * Created by Monday on 12/14/2016.
 */
public class BoostApplicationSystem extends AbstractIteratingGameSystem {

    public BoostApplicationSystem(GamePilot pilot) {
        super(pilot);
    }

    @Override
    public void actOnSingle(GameEntity entity, float delta) {
        BoosterComponent boost = entity.getComponent(BoosterComponent.class);

        BoostActivateButton button = entity.getComponent(BoostActivateButton.class);

        VelocityComponent velocity = entity.getComponent(VelocityComponent.class);

        TransformComponent transform = entity.getComponent(TransformComponent.class);

        if (button.pressed) {
            Vector2 boostVector = Geom.rotateSinglePoint(new Vector2( boost.strength, 0), transform.angle);
            velocity.currentVelocity.add(boostVector.scl(delta));
        }
    }

    @Override
    public boolean canActOn(GameEntity entity) {
        return entity.hasComponent(BoosterComponent.class) &&
                entity.hasComponent(BoostActivateButton.class) &&
                entity.hasComponent(TransformComponent.class) &&
                entity.hasComponent(VelocityComponent.class);
    }
}
