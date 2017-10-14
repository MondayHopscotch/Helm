package com.bitdecay.helm.system.movement;

import com.badlogic.gdx.math.Vector2;
import com.bitdecay.helm.collision.CollisionKind;
import com.bitdecay.helm.component.CrashComponent;
import com.bitdecay.helm.component.VelocityComponent;
import com.bitdecay.helm.system.AbstractIteratingGameSystem;

/**
 * Created by Monday on 1/7/2017.
 */
public class PlayerBoundarySystem extends AbstractIteratingGameSystem {
    private static final float PLAYER_DEATH_ZONE_BUFFER = 3000;

    private Vector2 center = new Vector2();
    private Vector2 workingVector= new Vector2();
    private float radius;

    public PlayerBoundarySystem(com.bitdecay.helm.GamePilot pilot) {
        super(pilot);
    }

    public void setKillRadius(Vector2 center, float levelRadius) {
        this.center.set(center);
        this.radius = levelRadius + PLAYER_DEATH_ZONE_BUFFER;
    }

    @Override
    public void actOnSingle(com.bitdecay.helm.GameEntity entity, float delta) {
        workingVector.set(center);
        com.bitdecay.helm.component.TransformComponent transform = entity.getComponent(com.bitdecay.helm.component.TransformComponent.class);
        if (workingVector.sub(transform.position).len() > radius) {
            entity.addComponent(new CrashComponent(CollisionKind.LEVEL_BOUNDARY));
        }
    }

    @Override
    public boolean canActOn(com.bitdecay.helm.GameEntity entity) {
        return entity.hasComponent(com.bitdecay.helm.component.TransformComponent.class) &&
                entity.hasComponent(VelocityComponent.class);
    }
}
