package com.bitdecay.game.system;

import com.bitdecay.game.GameEntity;
import com.bitdecay.game.GamePilot;
import com.bitdecay.game.collision.CollisionKind;
import com.bitdecay.game.component.collide.CollidedWithComponent;
import com.bitdecay.game.component.CrashComponent;
import com.bitdecay.game.component.collide.PlayerCollisionComponent;
import com.bitdecay.game.component.RateLandingComponent;
import com.bitdecay.game.component.RenderColorComponent;
import com.bitdecay.game.component.TransformComponent;
import com.bitdecay.game.component.VelocityComponent;

/**
 * Created by Monday on 12/14/2016.
 */
public class PlayerCollisionHandlerSystem extends AbstractIteratingGameSystem {

    public PlayerCollisionHandlerSystem(GamePilot pilot) {
        super(pilot);
    }

    @Override
    public void actOnSingle(GameEntity entity, float delta) {
        CollidedWithComponent collided = entity.getComponent(CollidedWithComponent.class);
        entity.removeComponent(CollidedWithComponent.class);

        // Will need to specially handle various kinds of collisions here.
        // i.e. restart the level / play death screen if it is a wall
        //      show score and move to next level if successfully landed on platform
        if (CollisionKind.LANDING_PLATFORM.equals(collided.with)) {
            entity.addComponent(new RateLandingComponent(collided.delivererGeometry));
//        } else if (CollisionKind.WALL.equals(collided.with)) {
        } else {
            entity.addComponent(new CrashComponent());
        }
    }

    @Override
    public boolean canActOn(GameEntity entity) {
        return entity.hasComponent(PlayerCollisionComponent.class) &&
                entity.hasComponent(CollidedWithComponent.class) &&
                entity.hasComponent(VelocityComponent.class) && // this velocity piece probably won't stay (?)
                entity.hasComponent(TransformComponent.class) &&
                entity.hasComponent(RenderColorComponent.class);
    }
}
