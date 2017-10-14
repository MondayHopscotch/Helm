package com.bitdecay.helm.system;

import com.badlogic.gdx.utils.Array;

/**
 * Created by Monday on 12/12/2016.
 */
public abstract class AbstractIteratingGameSystem extends AbstractBaseGameSystem {

    public AbstractIteratingGameSystem(com.bitdecay.helm.GamePilot pilot) {
        super(pilot);
    }

    @Override
    public void act(Array<com.bitdecay.helm.GameEntity> entities, float delta) {
        before();
        Array.ArrayIterator<com.bitdecay.helm.GameEntity> iter = new Array.ArrayIterator(entities);
        com.bitdecay.helm.GameEntity entity;
        while (iter.hasNext()) {
            entity = iter.next();
            if (canActOn(entity)) {
                actOnSingle(entity, delta);
            }
        }
        after();
    }

    public abstract void actOnSingle(com.bitdecay.helm.GameEntity entity, float delta);

    @Override
    public void before() {
        // no-op. Override to implement logic
    }

    @Override
    public void after() {
        // no-op. Override to implement logic
    }
}
