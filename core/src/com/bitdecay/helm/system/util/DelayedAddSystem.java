package com.bitdecay.helm.system.util;

import com.badlogic.gdx.utils.Array;

/**
 * Created by Monday on 12/16/2016.
 */
public class DelayedAddSystem extends com.bitdecay.helm.system.AbstractIteratingGameSystem {
    public DelayedAddSystem(com.bitdecay.helm.GamePilot pilot) {
        super(pilot);
    }

    @Override
    public void actOnSingle(com.bitdecay.helm.GameEntity entity, float delta) {
        com.bitdecay.helm.component.DelayedAddComponent delayedAdd = entity.getComponent(com.bitdecay.helm.component.DelayedAddComponent.class);
        Array.ArrayIterator<com.bitdecay.helm.component.DelayedAddComponent.DelayedAdd> iter = new Array.ArrayIterator<>(delayedAdd.delays);

        com.bitdecay.helm.component.DelayedAddComponent.DelayedAdd singleDelay;
        while (iter.hasNext()) {
            singleDelay = iter.next();
            singleDelay.delay -= delta;
            if (singleDelay.delay <= 0) {
                iter.remove();
                entity.addComponent(singleDelay.component);
            }
        }
    }

    @Override
    public boolean canActOn(com.bitdecay.helm.GameEntity entity) {
        return entity.hasComponent(com.bitdecay.helm.component.DelayedAddComponent.class);
    }
}
