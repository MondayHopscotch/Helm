package com.bitdecay.game.system.util;

import com.badlogic.gdx.utils.Array;
import com.bitdecay.game.GameEntity;
import com.bitdecay.game.GamePilot;
import com.bitdecay.game.component.DelayedAddComponent;
import com.bitdecay.game.system.AbstractIteratingGameSystem;

/**
 * Created by Monday on 12/16/2016.
 */
public class DelayedAddSystem extends AbstractIteratingGameSystem {
    public DelayedAddSystem(GamePilot pilot) {
        super(pilot);
    }

    @Override
    public void actOnSingle(GameEntity entity, float delta) {
        DelayedAddComponent delayedAdd = entity.getComponent(DelayedAddComponent.class);
        Array.ArrayIterator<DelayedAddComponent.DelayedAdd> iter = new Array.ArrayIterator<>(delayedAdd.delays);

        DelayedAddComponent.DelayedAdd singleDelay;
        while (iter.hasNext()) {
            singleDelay = iter.next();
            singleDelay.delay -= delta;
            if (singleDelay.delay <= 0) {
                iter.remove();
                entity.addComponent(singleDelay.component);
            }
        }
        if (delayedAdd.delays.items.length <= 0) {
            entity.removeComponent(DelayedAddComponent.class);
        }
    }

    @Override
    public boolean canActOn(GameEntity entity) {
        return entity.hasComponent(DelayedAddComponent.class);
    }
}
