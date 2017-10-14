package com.bitdecay.helm.system.movement;

import com.badlogic.gdx.math.Vector2;

import java.util.Iterator;

/**
 * Created by Monday on 2/17/2017.
 */

public class GravityApplicationSystem extends com.bitdecay.helm.system.AbstractIteratingGameSystem {
    // this number gets our gravitational pull into a reasonable range. Think of it as "mass"
    public static final float GRAVITY_DENSITY = 8000;

    // this number tweaks how close to the surface the point of strongest gravity is.
    // 1 means on the surface of the well, 0 means at the center of the well
    public static final float SOURCE_GRAV_MODIFIER = .8f;

    // I was hoping this would let me tune gravity, but numbers between [1, 2) cause really weird behavior
    public static final float GRAVITY_DECAY_POWER = 2f;

    Vector2 workingGravityVector = new Vector2();

    public GravityApplicationSystem(com.bitdecay.helm.GamePilot pilot) {
        super(pilot);
    }

    @Override
    public void actOnSingle(com.bitdecay.helm.GameEntity entity, float delta) {

        com.bitdecay.helm.component.TransformComponent transform = entity.getComponent(com.bitdecay.helm.component.TransformComponent.class);
        com.bitdecay.helm.component.VelocityComponent velocity = entity.getComponent(com.bitdecay.helm.component.VelocityComponent.class);
        com.bitdecay.helm.component.GravityAffectedComponent gravity = entity.getComponent(com.bitdecay.helm.component.GravityAffectedComponent.class);

        Vector2 cumulativeGravity = workingGravityVector.set(levelPlayer.universalGravity).add(gravity.worldGravityModifier);

        com.bitdecay.helm.GameEntity sourceEntity;
        com.bitdecay.helm.component.GravityProducerComponent sourceGravity;
        com.bitdecay.helm.component.TransformComponent sourceTransform;
        float distance;
        float gravityScalar;
        Vector2 workingDirection = new Vector2();
        Iterator<com.bitdecay.helm.GameEntity> iter = GravityFinderSystem.foundGravitySources.iterator();
        while (iter.hasNext()) {
            sourceEntity = iter.next();
            sourceGravity = sourceEntity.getComponent(com.bitdecay.helm.component.GravityProducerComponent.class);
            sourceTransform = sourceEntity.getComponent(com.bitdecay.helm.component.TransformComponent.class);
            workingDirection.set(sourceTransform.position).sub(transform.position); // we want the vector pointing toward the source of gravity
            distance = workingDirection.len();

            workingDirection.nor();

            gravityScalar = calculateGravityStrength(sourceGravity, distance);
            cumulativeGravity.add(workingDirection.scl(gravityScalar));
        }

        if (Math.abs(cumulativeGravity.x) < .0001f) {
            cumulativeGravity.x = 0;
        }

        if (Math.abs(cumulativeGravity.y) < .0001f) {
            cumulativeGravity.y = 0;
        }

        velocity.currentVelocity.add(cumulativeGravity.scl(delta));
    }

    private float calculateGravityStrength(com.bitdecay.helm.component.GravityProducerComponent producer, float distance) {
        // we modify the distance to get the surface of the black hole to be a little stronger
        float size = producer.size;
        distance -= (size * SOURCE_GRAV_MODIFIER);
        float strength = (float) ((size * GRAVITY_DENSITY) / (Math.pow(distance, GRAVITY_DECAY_POWER)));
        if (producer.repels) {
            strength *= -1;
        }
        return strength;
    }

    @Override
    public boolean canActOn(com.bitdecay.helm.GameEntity entity) {
        return entity.hasComponents(
                com.bitdecay.helm.component.GravityAffectedComponent.class,
                com.bitdecay.helm.component.TransformComponent.class,
                com.bitdecay.helm.component.VelocityComponent.class
        );
    }
}
