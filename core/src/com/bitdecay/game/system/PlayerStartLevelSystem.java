package com.bitdecay.game.system;

import com.badlogic.gdx.math.Vector2;
import com.bitdecay.game.GameEntity;
import com.bitdecay.game.component.BoostActivateButton;
import com.bitdecay.game.component.VelocityComponent;
import com.bitdecay.game.component.WaitingToStartComponent;

/**
 * Created by Monday on 12/14/2016.
 */
public class PlayerStartLevelSystem extends AbstractIteratingGameSystem {

    private static final Vector2 LAUNCH_VELOCITY = new Vector2(0, 10);

    @Override
    public void actOnSingle(GameEntity entity, float delta) {
        BoostActivateButton button = entity.getComponent(BoostActivateButton.class);
        if (button.pressed) {
            VelocityComponent velocity = new VelocityComponent();
            velocity.currentVelocity.set(LAUNCH_VELOCITY);
            entity.addComponent(velocity);
            entity.removeComponent(WaitingToStartComponent.class);
        }
    }

    @Override
    public boolean canActOn(GameEntity entity) {
        return entity.hasComponent(WaitingToStartComponent.class) &&
                entity.hasComponent(BoostActivateButton.class);
    }
}
