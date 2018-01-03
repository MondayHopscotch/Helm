package com.bitdecay.helm.system.collision;

import com.badlogic.gdx.math.Vector2;
import com.bitdecay.helm.GameEntity;
import com.bitdecay.helm.GamePilot;
import com.bitdecay.helm.collision.CollisionKind;
import com.bitdecay.helm.component.TransformComponent;
import com.bitdecay.helm.component.collide.CollidedWithComponent;
import com.bitdecay.helm.component.collide.CollisionGeometryComponent;
import com.bitdecay.helm.math.Geom;
import com.bitdecay.helm.sound.SFXLibrary;
import com.bitdecay.helm.sound.SoundMode;
import com.bitdecay.helm.system.AbstractIteratingGameSystem;
import com.bitdecay.helm.system.util.SecondLocationComponent;

/**
 * Created by Monday on 3/19/2017.
 */

public class WormholeCollisionHandlerSystem extends AbstractIteratingGameSystem {
    public WormholeCollisionHandlerSystem(GamePilot pilot) {
        super(pilot);
    }

    @Override
    public void actOnSingle(com.bitdecay.helm.GameEntity entity, float delta) {
        TransformComponent transform = entity.getComponent(TransformComponent.class);
        CollisionGeometryComponent geom = entity.getComponent(CollisionGeometryComponent.class);
        CollidedWithComponent collidedWithComponent = entity.getComponent(CollidedWithComponent.class);
        if (CollisionKind.PLAYER.equals(collidedWithComponent.with)) {
            float[] otherGeom = collidedWithComponent.delivererGeometry;
            for (int i = 1; i < otherGeom.length; i += 2) {
                if (Geom.distance(transform.position, new Vector2(otherGeom[i-1], otherGeom[i])) > geom.originalGeom[0]) {
                    // the body wasn't fully in the wormhole, so don't do anything yet
                    return;
                }
            }
            entity.removeComponent(CollidedWithComponent.class);

            SecondLocationComponent secondLocationComponent = entity.getComponent(SecondLocationComponent.class);

            GameEntity hitter = collidedWithComponent.entity;
            if (hitter.hasComponents(TransformComponent.class)) {
                TransformComponent hitterTransform = hitter.getComponent(TransformComponent.class);
                hitterTransform.position.set(secondLocationComponent.position);
                pilot.doSound(SoundMode.PLAY, SFXLibrary.SHIP_WARP);
            }
        }
    }

    @Override
    public boolean canActOn(com.bitdecay.helm.GameEntity entity) {
        return entity.hasComponents(
                TransformComponent.class,
                CollisionGeometryComponent.class,
                SecondLocationComponent.class,
                CollidedWithComponent.class
        );
    }
}
