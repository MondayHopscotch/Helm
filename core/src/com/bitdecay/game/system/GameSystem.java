package com.bitdecay.game.system;

import com.badlogic.gdx.utils.Array;
import com.bitdecay.game.GameEntity;
import com.bitdecay.game.screen.LevelPlayer;

/**
 * Created by Monday on 12/8/2016.
 */
public interface GameSystem {
    void before();
    void after();

    void act(Array<GameEntity> entities, float delta);
    boolean canActOn(GameEntity entity);

    void reset();

    void setLevelPlayer(LevelPlayer levelPlayer);
}
