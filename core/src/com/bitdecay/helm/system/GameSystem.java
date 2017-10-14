package com.bitdecay.helm.system;

import com.badlogic.gdx.utils.Array;

/**
 * Created by Monday on 12/8/2016.
 */
public interface GameSystem {
    void before();
    void after();

    void act(Array<com.bitdecay.helm.GameEntity> entities, float delta);
    boolean canActOn(com.bitdecay.helm.GameEntity entity);

    void reset();

    void setLevelPlayer(com.bitdecay.helm.screen.LevelPlayer levelPlayer);
}
