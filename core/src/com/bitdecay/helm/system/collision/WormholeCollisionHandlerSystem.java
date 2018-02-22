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

    Vector2 min = new Vector2();
    Vector2 max = new Vector2();
    Vector2 center = new Vector2();

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
            min.set(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY);
            max.set(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY);
            center.setZero();
            for (int i = 1; i < otherGeom.length; i += 2) {
                min.x = Math.min(min.x, otherGeom[i-1]);
                min.y = Math.min(min.y, otherGeom[i]);
                max.x = Math.max(max.x, otherGeom[i-1]);
                max.y = Math.max(max.y, otherGeom[i]);
            }
            center.set((min.x + max.x) / 2, (min.y + max.y) / 2);
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
