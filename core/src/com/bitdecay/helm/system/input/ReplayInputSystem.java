package com.bitdecay.helm.system.input;

import com.bitdecay.helm.component.ReplayActiveComponent;
import com.bitdecay.helm.component.control.BoostControlComponent;
import com.bitdecay.helm.component.control.SteeringControlComponent;
import com.bitdecay.helm.input.InputRecord;
import com.bitdecay.helm.system.AbstractIteratingGameSystem;

/**
 * Created by Monday on 2/26/2017.
 */

public class ReplayInputSystem extends AbstractIteratingGameSystem {

    public ReplayInputSystem(com.bitdecay.helm.GamePilot pilot) {
        super(pilot);
    }

    @Override
    public void actOnSingle(com.bitdecay.helm.GameEntity entity, float delta) {
        ReplayActiveComponent replay = entity.getComponent(ReplayActiveComponent.class);
        if (replay.nextInput >= replay.input.inputRecords.size) {
            // we are done looking at inputs
            return;
        }

        InputRecord inputRecord = replay.input.inputRecords.get(replay.nextInput);

        int tick = levelPlayer.getTick();
        if (tick == inputRecord.tick) {
            // do stuff
            if (inputRecord.angle != Float.NEGATIVE_INFINITY) {
                SteeringControlComponent steering = entity.getComponent(SteeringControlComponent.class);
                steering.angle = inputRecord.angle;
                System.out.println("REPLAY: TICK " + tick + " Setting angle: " + inputRecord.angle);
            }

            BoostControlComponent boost = entity.getComponent(BoostControlComponent.class);
            if (inputRecord.boostToggled) {
                boost.pressed = !boost.pressed;
            }
            System.out.println("REPLAY: TICK " + tick + " Setting boost: " + boost.pressed);

            replay.nextInput++;
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
