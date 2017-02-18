package com.bitdecay.game.system;

import com.badlogic.gdx.math.Vector2;
import com.bitdecay.game.GameEntity;
import com.bitdecay.game.GamePilot;
import com.bitdecay.game.component.CrashComponent;
import com.bitdecay.game.component.GravityAffectedComponent;
import com.bitdecay.game.component.GravityProducerComponent;
import com.bitdecay.game.component.TransformComponent;
import com.bitdecay.game.component.VelocityComponent;

import java.util.Iterator;

/**
 * Created by Monday on 2/17/2017.
 */

public class GravityApplicationSystem extends AbstractIteratingGameSystem {
    // this number gets our gravitational pull into a reasonable range. Think of it as "mass"
    public static final float GRAVITY_DENSITY = 10000;

    Vector2 workingGravityVector = new Vector2();

    public GravityApplicationSystem(GamePilot pilot) {
        super(pilot);
    }

    @Override
    public void actOnSingle(GameEntity entity, float delta) {

        TransformComponent transform = entity.getComponent(TransformComponent.class);
        VelocityComponent velocity = entity.getComponent(VelocityComponent.class);
        GravityAffectedComponent gravity = entity.getComponent(GravityAffectedComponent.class);

        Vector2 cumulativeGravity = workingGravityVector.set(GravityFinderSystem.universalGravity).add(gravity.worldGravityModifier);

        GameEntity sourceEntity;
        GravityProducerComponent sourceGravity;
        TransformComponent sourceTransform;
        float distance;
        float gravityScalar;
        Vector2 workingDirection = new Vector2();
        Iterator<GameEntity> iter = GravityFinderSystem.foundGravitySources.iterator();
        while (iter.hasNext()) {
            sourceEntity = iter.next();
            sourceGravity = sourceEntity.getComponent(GravityProducerComponent.class);
            sourceTransform = sourceEntity.getComponent(TransformComponent.class);
            workingDirection.set(sourceTransform.position).sub(transform.position); // we want the vector pointing toward the source of gravity
            distance = workingDirection.len();

            workingDirection.nor();

            gravityScalar = calculateGravityStrength(sourceGravity.size, distance);
            cumulativeGravity.add(workingDirection.scl(gravityScalar));
        }

        if (Math.abs(cumulativeGravity.x) < .0001f) {
            cumulativeGravity.x = 0;
        }

        if (Math.abs(cumulativeGravity.y) < .0001f) {
            cumulativeGravity.y = 0;
        }

        System.out.println("Gravity Total: " + cumulativeGravity);
        velocity.currentVelocity.add(cumulativeGravity.scl(delta));
    }

    private float calculateGravityStrength(float size, float distance) {
        return (size * GRAVITY_DENSITY) / (distance * distance);
    }

    @Override
    public boolean canActOn(GameEntity entity) {
        return entity.hasComponents(
                GravityAffectedComponent.class,
                TransformComponent.class,
                VelocityComponent.class
        );
    }
}
