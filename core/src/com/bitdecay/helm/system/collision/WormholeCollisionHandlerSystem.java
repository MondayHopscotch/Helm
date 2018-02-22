package com.bitdecay.helm.system.collision;

import com.badlogic.gdx.math.Vector2;
import com.bitdecay.helm.collision.CollisionKind;
import com.bitdecay.helm.math.Geom;
import com.bitdecay.helm.sound.SFXLibrary;
import com.bitdecay.helm.system.AbstractIteratingGameSystem;

/**
 * Created by Monday on 3/19/2017.
 */

public class WormholeCollisionHandlerSystem extends AbstractIteratingGameSystem {
    public WormholeCollisionHandlerSystem(com.bitdecay.helm.GamePilot pilot) {
        super(pilot);
    }

    @Override
    public void actOnSingle(com.bitdecay.helm.GameEntity entity, float delta) {
        com.bitdecay.helm.component.TransformComponent transform = entity.getComponent(com.bitdecay.helm.component.TransformComponent.class);
        com.bitdecay.helm.component.collide.CollisionGeometryComponent geom = entity.getComponent(com.bitdecay.helm.component.collide.CollisionGeometryComponent.class);
        com.bitdecay.helm.component.collide.CollidedWithComponent collidedWithComponent = entity.getComponent(com.bitdecay.helm.component.collide.CollidedWithComponent.class);
        if (CollisionKind.PLAYER.equals(collidedWithComponent.with)) {
            float[] otherGeom = collidedWithComponent.delivererGeometry;
            Vector2 center = new Vector2();
            for (int i = 1; i < otherGeom.length; i += 2) {
                center.add(otherGeom[i-1], otherGeom[i]);
            }
            center.scl(1/ (otherGeom.length / 2.0f));
            if (Geom.distance(transform.position, center) > geom.originalGeom[0]) {
                // center of body too far away still
                return;
            }
             entity.removeComponent(com.bitdecay.helm.component.collide.CollidedWithComponent.class);

            com.bitdecay.helm.system.util.SecondLocationComponent secondLocationComponent = entity.getComponent(com.bitdecay.helm.system.util.SecondLocationComponent.class);

            com.bitdecay.helm.GameEntity hitter = collidedWithComponent.entity;
            if (hitter.hasComponents(com.bitdecay.helm.component.TransformComponent.class)) {
                com.bitdecay.helm.component.TransformComponent hitterTransform = hitter.getComponent(com.bitdecay.helm.component.TransformComponent.class);
                hitterTransform.position.set(secondLocationComponent.position);
                pilot.doSound(com.bitdecay.helm.sound.SoundMode.PLAY, SFXLibrary.SHIP_WARP);
            }
        }
    }

    @Override
    public boolean canActOn(com.bitdecay.helm.GameEntity entity) {
        return entity.hasComponents(
                com.bitdecay.helm.component.TransformComponent.class,
                com.bitdecay.helm.component.collide.CollisionGeometryComponent.class,
                com.bitdecay.helm.system.util.SecondLocationComponent.class,
                com.bitdecay.helm.component.collide.CollidedWithComponent.class
        );
    }
}
