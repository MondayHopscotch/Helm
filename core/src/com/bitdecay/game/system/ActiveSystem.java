package com.bitdecay.game.system;

import com.bitdecay.game.GameEntity;
import com.bitdecay.game.GamePilot;
import com.bitdecay.game.component.ActiveComponent;

/**
 * Created by Monday on 12/15/2016.
 */
public class ActiveSystem extends AbstractIteratingGameSystem {
    public ActiveSystem(GamePilot pilot) {
        super(pilot);
    }

    @Override
    public void actOnSingle(GameEntity entity, float delta) {
        ActiveComponent active = entity.getComponent(ActiveComponent.class);
        if (!active.active) {
            if (active.flipControlTimer > 0) {
                active.flipControlTimer -= delta;
                if (active.flipControlTimer <= 0) {
                    active.flipControlTimer = 0;
                    active.active = !active.active;
                }
            }
        }
    }

    @Override
    public boolean canActOn(GameEntity entity) {
        return entity.hasComponent(ActiveComponent.class);
    }
}
