package com.bitdecay.helm.tutorial;

import com.badlogic.gdx.utils.Array;
import com.bitdecay.helm.GameEntity;
import com.bitdecay.helm.entities.ShipEntity;

/**
 * Created by Monday on 11/8/2017.
 */

public class TutorialUtils {
    public static GameEntity getShip(Array<GameEntity> allEntities) {
        for (GameEntity entity : allEntities) {
            if (entity instanceof ShipEntity) {
                return entity;
            }
        }
        return null;
    }
}
