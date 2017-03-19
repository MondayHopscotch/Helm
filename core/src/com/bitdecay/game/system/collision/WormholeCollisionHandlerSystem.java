package com.bitdecay.game.system.collision;

import com.bitdecay.game.GameEntity;
import com.bitdecay.game.GamePilot;
import com.bitdecay.game.collision.CollisionKind;
import com.bitdecay.game.component.TransformComponent;
import com.bitdecay.game.component.collide.CollidedWithComponent;
import com.bitdecay.game.system.AbstractIteratingGameSystem;
import com.bitdecay.game.system.util.SecondLocationComponent;

/**
 * Created by Monday on 3/19/2017.
 */

public class WormholeCollisionHandlerSystem extends AbstractIteratingGameSystem {
    public WormholeCollisionHandlerSystem(GamePilot pilot) {
        super(pilot);
    }

    @Override
    public void actOnSingle(GameEntity entity, float delta) {
        CollidedWithComponent collidedWithComponent = entity.getComponent(CollidedWithComponent.class);
        if (CollisionKind.PLAYER.equals(collidedWithComponent.with)) {
            System.out.println("WE DONE HIT THE PLAYER!");
            entity.removeComponent(CollidedWithComponent.class);

            SecondLocationComponent secondLocationComponent = entity.getComponent(SecondLocationComponent.class);

            GameEntity hitter = collidedWithComponent.entity;
            if (hitter.hasComponents(TransformComponent.class)) {
                TransformComponent hitterTransform = hitter.getComponent(TransformComponent.class);
                hitterTransform.position.set(secondLocationComponent.position);
            }
        }
    }

    @Override
    public boolean canActOn(GameEntity entity) {
        return entity.hasComponents(
                SecondLocationComponent.class,
                CollidedWithComponent.class
        );
    }
}
