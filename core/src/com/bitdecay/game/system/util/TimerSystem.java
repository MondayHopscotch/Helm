package com.bitdecay.game.system.util;

import com.bitdecay.game.GameEntity;
import com.bitdecay.game.GamePilot;
import com.bitdecay.game.Helm;
import com.bitdecay.game.component.TimerComponent;
import com.bitdecay.game.system.AbstractIteratingGameSystem;

/**
 * Created by Monday on 1/9/2017.
 */
public class TimerSystem extends AbstractIteratingGameSystem {
    public TimerSystem(GamePilot pilot) {
        super(pilot);
    }

    @Override
    public void reset() {
        pilot.setTime(0);
    }

    @Override
    public void actOnSingle(GameEntity entity, float delta) {
        TimerComponent timer = entity.getComponent(TimerComponent.class);
        timer.secondsElapsed += delta;
        if (Helm.debug) {
            pilot.setTime(levelPlayer.getTick());
        } else {
            pilot.setTime(timer.secondsElapsed);
        }
    }

    @Override
    public boolean canActOn(GameEntity entity) {
        return entity.hasComponent(TimerComponent.class);
    }
}
