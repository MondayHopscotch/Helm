package com.bitdecay.helm.tutorial;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.bitdecay.helm.GameEntity;
import com.bitdecay.helm.component.BodyDefComponent;
import com.bitdecay.helm.component.TransformComponent;
import com.bitdecay.helm.entities.LandingPlatformEntity;
import com.bitdecay.helm.entities.ShipEntity;
import com.bitdecay.helm.math.Geom;

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

    public static Vector2 getLandingLocation(Array<GameEntity> allEntities) {
        for (GameEntity entity : allEntities) {
            if (entity instanceof LandingPlatformEntity) {
                float widthOfPoints = Geom.getWidthOfPoints(entity.getComponent(BodyDefComponent.class).bodyPoints);
                Vector2 midPoint = new Vector2(widthOfPoints / 2, 0);
                TransformComponent transform = entity.getComponent(TransformComponent.class);
                midPoint.rotateRad(transform.angle);
                return midPoint.add(transform.position);
            }
        }
        return null;
    }
}
