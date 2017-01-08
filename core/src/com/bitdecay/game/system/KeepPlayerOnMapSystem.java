package com.bitdecay.game.system;

import com.badlogic.gdx.math.Vector2;
import com.bitdecay.game.GameEntity;
import com.bitdecay.game.GamePilot;
import com.bitdecay.game.component.CrashComponent;
import com.bitdecay.game.component.TransformComponent;
import com.bitdecay.game.component.VelocityComponent;

/**
 * Created by Monday on 1/7/2017.
 */
public class KeepPlayerOnMapSystem extends AbstractIteratingGameSystem {
    private static final float PLAYER_DEATH_ZONE_BUFFER = 3000;

    private Vector2 center = new Vector2();
    private Vector2 workingVector= new Vector2();
    private float radius;

    public KeepPlayerOnMapSystem(GamePilot pilot) {
        super(pilot);
    }

    public void setKillRadius(Vector2 center, float levelRadius) {
        this.center.set(center);
        this.radius = levelRadius + PLAYER_DEATH_ZONE_BUFFER;
    }

    @Override
    public void actOnSingle(GameEntity entity, float delta) {
        workingVector.set(center);
        TransformComponent transform = entity.getComponent(TransformComponent.class);
        if (workingVector.sub(transform.position).len() > radius) {
            entity.addComponent(new CrashComponent());
        }
    }

    @Override
    public boolean canActOn(GameEntity entity) {
        return entity.hasComponent(TransformComponent.class) &&
                entity.hasComponent(VelocityComponent.class);
    }
}
