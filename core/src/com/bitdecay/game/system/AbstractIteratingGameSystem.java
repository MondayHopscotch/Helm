package com.bitdecay.game.system;

import com.badlogic.gdx.utils.Array;
import com.bitdecay.game.GameEntity;
import com.bitdecay.game.GamePilot;

/**
 * Created by Monday on 12/12/2016.
 */
public abstract class AbstractIteratingGameSystem extends AbstractBaseGameSystem {

    public AbstractIteratingGameSystem(GamePilot pilot) {
        super(pilot);
    }

    @Override
    public void act(Array<GameEntity> entities, float delta) {
        before();
        Array.ArrayIterator<GameEntity> iter = new Array.ArrayIterator(entities);
        GameEntity entity;
        while (iter.hasNext()) {
            entity = iter.next();
            if (canActOn(entity)) {
                actOnSingle(entity, delta);
            }
        }
        after();
    }

    public abstract void actOnSingle(GameEntity entity, float delta);

    @Override
    public void before() {
        // no-op. Override to implement logic
    }

    @Override
    public void after() {
        // no-op. Override to implement logic
    }
}
