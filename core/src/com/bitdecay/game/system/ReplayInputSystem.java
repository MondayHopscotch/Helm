package com.bitdecay.game.system;

import com.bitdecay.game.GameEntity;
import com.bitdecay.game.GamePilot;
import com.bitdecay.game.component.ReplayActiveComponent;
import com.bitdecay.game.component.control.BoostControlComponent;
import com.bitdecay.game.component.control.SteeringControlComponent;
import com.bitdecay.game.input.InputRecord;

/**
 * Created by Monday on 2/26/2017.
 */

public class ReplayInputSystem extends AbstractIteratingGameSystem {
    public ReplayInputSystem(GamePilot pilot) {
        super(pilot);
    }

    @Override
    public void actOnSingle(GameEntity entity, float delta) {
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
            boost.pressed = inputRecord.boosting;
            System.out.println("REPLAY: TICK " + tick + " Setting boost: " + inputRecord.boosting);

            replay.nextInput++;
        }
    }

    @Override
    public boolean canActOn(GameEntity entity) {
        return entity.hasComponents(
                ReplayActiveComponent.class,
                SteeringControlComponent.class,
                BoostControlComponent.class,
                BoostControlComponent.class
        );
    }
}
