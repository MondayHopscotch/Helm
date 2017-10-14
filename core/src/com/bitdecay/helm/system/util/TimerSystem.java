package com.bitdecay.helm.system.util;

import com.bitdecay.helm.system.AbstractIteratingGameSystem;

/**
 * Created by Monday on 1/9/2017.
 */
public class TimerSystem extends AbstractIteratingGameSystem {
    public TimerSystem(com.bitdecay.helm.GamePilot pilot) {
        super(pilot);
    }

    @Override
    public void reset() {
        pilot.setTime(0);
    }

    @Override
    public void actOnSingle(com.bitdecay.helm.GameEntity entity, float delta) {
        com.bitdecay.helm.component.TimerComponent timer = entity.getComponent(com.bitdecay.helm.component.TimerComponent.class);
        timer.secondsElapsed += delta;
        pilot.setTime(timer.secondsElapsed);
    }

    @Override
    public boolean canActOn(com.bitdecay.helm.GameEntity entity) {
        return entity.hasComponent(com.bitdecay.helm.component.TimerComponent.class);
    }
}
