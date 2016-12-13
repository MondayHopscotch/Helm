package com.bitdecay.game.system;

import com.badlogic.gdx.utils.Array;
import com.bitdecay.game.GameEntity;

/**
 * Created by Monday on 12/12/2016.
 */
public abstract class AbstractIteratingGameSystem implements GameSystem {
    @Override
    public void act(Array<GameEntity> entities, float delta) {
        Array.ArrayIterator<GameEntity> iter = new Array.ArrayIterator(entities);
        GameEntity entity;
        while (iter.hasNext()) {
            entity = iter.next();
            if (canActOn(entity)) {
                actOnSingle(entity, delta);
            }
        }
    }

    public abstract void actOnSingle(GameEntity entity, float delta);
}
