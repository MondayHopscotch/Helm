package com.bitdecay.game.system;

import com.bitdecay.game.GameEntity;
import com.bitdecay.game.component.CollidedWithComponent;
import com.bitdecay.game.component.CollisionKindComponent;
import com.bitdecay.game.component.PlayerCollisionComponent;
import com.bitdecay.game.component.VelocityComponent;
import com.bitdecay.game.component.WaitingToStartComponent;

/**
 * Created by Monday on 12/14/2016.
 */
public class PlayerCollisionHandlerSystem extends AbstractIteratingGameSystem {
    @Override
    public void actOnSingle(GameEntity entity, float delta) {
        CollidedWithComponent with = entity.getComponent(CollidedWithComponent.class);
        System.out.println("Player has collided with " + with.with.toString());
        entity.removeComponent(CollidedWithComponent.class);

        // Will need to specially handle various kinds of collisions here.
        // i.e. restart the level / play death screen if it is a wall
        //      show score and move to next level if successfully landed on platform
        entity.removeComponent(VelocityComponent.class);
        entity.addComponent(new WaitingToStartComponent());
    }

    @Override
    public boolean canActOn(GameEntity entity) {
        return entity.hasComponent(PlayerCollisionComponent.class) &&
                entity.hasComponent(CollidedWithComponent.class) &&
                entity.hasComponent(VelocityComponent.class); // this velocity piece probably won't stay (?)
    }
}
