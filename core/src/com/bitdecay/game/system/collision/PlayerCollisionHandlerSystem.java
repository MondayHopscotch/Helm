package com.bitdecay.game.system.collision;

import com.badlogic.gdx.math.GeometryUtils;
import com.bitdecay.game.GameEntity;
import com.bitdecay.game.GamePilot;
import com.bitdecay.game.collision.CollisionKind;
import com.bitdecay.game.component.BodyDefComponent;
import com.bitdecay.game.component.collide.CollidedWithComponent;
import com.bitdecay.game.component.CrashComponent;
import com.bitdecay.game.component.collide.PlayerCollisionComponent;
import com.bitdecay.game.component.RateLandingComponent;
import com.bitdecay.game.component.RenderColorComponent;
import com.bitdecay.game.component.TransformComponent;
import com.bitdecay.game.component.VelocityComponent;
import com.bitdecay.game.math.Geom;
import com.bitdecay.game.system.AbstractIteratingGameSystem;

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

        TransformComponent transform = entity.getComponent(TransformComponent.class);
        BodyDefComponent bodyDef = entity.getComponent(BodyDefComponent.class);

        float[] playerGeom = Geom.transformPoints(bodyDef.bodyPoints, transform);

        // Will need to specially handle various kinds of collisions here.
        // i.e. restart the level / play death screen if it is a wall
        //      show score and move to next level if successfully landed on platform
        if (CollisionKind.LANDING_PLATFORM.equals(collided.with)) {
            entity.addComponent(new RateLandingComponent(collided.geom, playerGeom, collided.delivererGeometry));
//        } else if (CollisionKind.WALL.equals(collided.with)) {
        } else {
            entity.addComponent(new CrashComponent(collided.with));
        }
    }

    @Override
    public boolean canActOn(GameEntity entity) {
        return entity.hasComponents(
                PlayerCollisionComponent.class,
                BodyDefComponent.class,
                CollidedWithComponent.class,
                VelocityComponent.class, // this velocity piece probably won't stay (?)
                TransformComponent.class,
                RenderColorComponent.class
        );
    }
}
