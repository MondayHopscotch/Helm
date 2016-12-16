package com.bitdecay.game.system;

import com.badlogic.gdx.math.Vector2;
import com.bitdecay.game.GameEntity;
import com.bitdecay.game.GamePilot;
import com.bitdecay.game.component.GravityComponent;
import com.bitdecay.game.component.VelocityComponent;

/**
 * Created by Monday on 12/12/2016.
 */
public class GravitySystem extends AbstractIteratingGameSystem {

    Vector2 universalGravity = new Vector2(0, -10);

    public GravitySystem(GamePilot pilot) {
        super(pilot);
    }

    @Override
    public void actOnSingle(GameEntity entity, float delta) {

        VelocityComponent velocity = entity.getComponent(VelocityComponent.class);

        GravityComponent gravity = entity.getComponent(GravityComponent.class);

        Vector2 cumulativeGravity = universalGravity.cpy().add(gravity.gravityModifier);

        velocity.currentVelocity.add(cumulativeGravity.scl(delta));
    }

    @Override
    public boolean canActOn(GameEntity entity) {
        return entity.hasComponent(GravityComponent.class) &&
                entity.hasComponent(VelocityComponent.class);
    }
}
