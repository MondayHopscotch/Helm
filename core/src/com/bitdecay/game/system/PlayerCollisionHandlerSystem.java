package com.bitdecay.game.system;

import com.bitdecay.game.GameEntity;
import com.bitdecay.game.GamePilot;
import com.bitdecay.game.collision.CollisionKind;
import com.bitdecay.game.component.CollidedWithComponent;
import com.bitdecay.game.component.PlayerCollisionComponent;
import com.bitdecay.game.component.TransformComponent;
import com.bitdecay.game.component.VelocityComponent;
import com.bitdecay.game.component.WaitingToStartComponent;
import com.bitdecay.game.sound.MusicLibrary;
import com.bitdecay.game.sound.SoundMode;

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

        pilot.doMusic(SoundMode.STOP, MusicLibrary.SHIP_BOOST);

        // Will need to specially handle various kinds of collisions here.
        // i.e. restart the level / play death screen if it is a wall
        //      show score and move to next level if successfully landed on platform
        entity.removeComponent(VelocityComponent.class);
        if (CollisionKind.LANDING_PLATFORM.equals(collided.with)) {
            // check landing and either pass/fail
        } else if (CollisionKind.WALL.equals(collided.with)) {
            // fail and let player restart level
            pilot.requestRestartLevel();
        }
        if (!entity.hasComponent(WaitingToStartComponent.class)) {
            entity.addComponent(new WaitingToStartComponent());
        }

    }

    @Override
    public boolean canActOn(GameEntity entity) {
        return entity.hasComponent(PlayerCollisionComponent.class) &&
                entity.hasComponent(CollidedWithComponent.class) &&
                entity.hasComponent(VelocityComponent.class) && // this velocity piece probably won't stay (?)
                entity.hasComponent(TransformComponent.class);
    }
}
