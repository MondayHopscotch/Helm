package com.bitdecay.helm.system.input;

import com.bitdecay.helm.component.BoostCountComponent;
import com.bitdecay.helm.component.HasSteeredComponent;
import com.bitdecay.helm.component.ReplayActiveComponent;
import com.bitdecay.helm.component.control.BoostControlComponent;
import com.bitdecay.helm.component.control.SteeringControlComponent;
import com.bitdecay.helm.input.InputRecord;
import com.bitdecay.helm.math.Geom;
import com.bitdecay.helm.system.AbstractIteratingGameSystem;

/**
 * Created by Monday on 2/26/2017.
 */

public class ReplayInputSystem extends AbstractIteratingGameSystem {
    private HasSteeredComponent hasSteered;
    private BoostCountComponent boostCounter;
    private ReplayActiveComponent replay;

    public ReplayInputSystem(com.bitdecay.helm.GamePilot pilot) {
        super(pilot);
    }

    @Override
    public void actOnSingle(com.bitdecay.helm.GameEntity entity, float delta) {
        if (replay == null) {
            replay = entity.getComponent(ReplayActiveComponent.class);
        }
        if (replay.nextInput >= replay.input.inputRecords.size) {
            // we are done looking at inputs, reset the controls.
            BoostControlComponent boost = entity.getComponent(BoostControlComponent.class);
            boost.pressed = false;

            SteeringControlComponent steering = entity.getComponent(SteeringControlComponent.class);
            steering.angle = Geom.ROTATION_UP;
            return;
        }

        InputRecord inputRecord = replay.input.inputRecords.get(replay.nextInput);

        int tick = levelPlayer.getTick();
        if (tick == inputRecord.tick) {
            // do stuff
            if (inputRecord.angle != Float.NEGATIVE_INFINITY) {
                SteeringControlComponent steering = entity.getComponent(SteeringControlComponent.class);
                steering.angle = inputRecord.angle;
                if (entity.hasComponent(HasSteeredComponent.class)) {
                    hasSteered = entity.getComponent(HasSteeredComponent.class);
                    hasSteered.playerHasSteered = true;
                }
            }

            BoostControlComponent boost = entity.getComponent(BoostControlComponent.class);
            if (inputRecord.boostToggled) {
                if (boost.pressed == false) {
                    // we just pushed boost
                    if (entity.hasComponent(BoostCountComponent.class)) {
                        boostCounter = entity.getComponent(BoostCountComponent.class);
                        boostCounter.boostCount++;
                    }
                }
                boost.pressed = !boost.pressed;
            }

            replay.nextInput++;
        }
    }

    @Override
    public void reset() {
        if (hasSteered != null) {
            hasSteered.playerHasSteered = false;
            hasSteered = null;
        }

        if (boostCounter != null) {
            boostCounter.boostCount = 0;
            boostCounter = null;
        }

        if (replay != null) {
            replay.nextInput = 0;
        }
    }

    @Override
    public boolean canActOn(com.bitdecay.helm.GameEntity entity) {
        return entity.hasComponents(
                ReplayActiveComponent.class,
                SteeringControlComponent.class,
                BoostControlComponent.class,
                BoostControlComponent.class
        );
    }
}
