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

        if (levelPlayer.getTick() == inputRecord.tick) {
            // do stuff
            if (inputRecord.angle!= Float.NEGATIVE_INFINITY && entity.hasComponent(SteeringControlComponent.class)) {
                SteeringControlComponent steering = entity.getComponent(SteeringControlComponent.class);
                steering.angle = inputRecord.angle;
            }

            if (entity.hasComponent(BoostControlComponent.class)) {
                BoostControlComponent boost = entity.getComponent(BoostControlComponent.class);
                boost.pressed = inputRecord.boosting;
            }
            replay.nextInput++;
        }
    }

    @Override
    public boolean canActOn(GameEntity entity) {
        return entity.hasComponent(ReplayActiveComponent.class);
    }
}
