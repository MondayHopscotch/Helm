package com.bitdecay.helm.system.collision;

import com.bitdecay.helm.component.BodyDefComponent;
import com.bitdecay.helm.component.CrashComponent;
import com.bitdecay.helm.component.RateLandingComponent;
import com.bitdecay.helm.component.RenderColorComponent;
import com.bitdecay.helm.component.TransformComponent;
import com.bitdecay.helm.component.VelocityComponent;
import com.bitdecay.helm.component.collide.CollidedWithComponent;
import com.bitdecay.helm.component.collide.PlayerCollisionComponent;
import com.bitdecay.helm.system.AbstractIteratingGameSystem;

/**
 * Created by Monday on 12/14/2016.
 */
public class PlayerCollisionHandlerSystem extends AbstractIteratingGameSystem {

    public PlayerCollisionHandlerSystem(com.bitdecay.helm.GamePilot pilot) {
        super(pilot);
    }

    @Override
    public void actOnSingle(com.bitdecay.helm.GameEntity entity, float delta) {
        com.bitdecay.helm.component.collide.CollidedWithComponent collided = entity.getComponent(com.bitdecay.helm.component.collide.CollidedWithComponent.class);

        com.bitdecay.helm.component.TransformComponent transform = entity.getComponent(com.bitdecay.helm.component.TransformComponent.class);
        com.bitdecay.helm.component.BodyDefComponent bodyDef = entity.getComponent(com.bitdecay.helm.component.BodyDefComponent.class);

        float[] playerGeom = com.bitdecay.helm.math.Geom.transformPoints(bodyDef.bodyPoints, transform);

        // Will need to specially handle various kinds of collisions here.
        // i.e. restart the level / play death screen if it is a wall
        //      show score and move to next level if successfully landed on platform
        switch(collided.with) {

            case LANDING_PLATFORM:
                entity.addComponent(new RateLandingComponent(collided.geom, playerGeom, collided.delivererGeometry));
                entity.removeComponent(CollidedWithComponent.class);
                break;
            case WORMHOLE:
                // other systems handle this case
                break;
            default:
                entity.addComponent(new CrashComponent(collided.with));
                entity.removeComponent(CollidedWithComponent.class);
        }
    }

    @Override
    public boolean canActOn(com.bitdecay.helm.GameEntity entity) {
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
