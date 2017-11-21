package com.bitdecay.helm.tutorial;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.bitdecay.helm.GameEntity;
import com.bitdecay.helm.Helm;
import com.bitdecay.helm.component.BodyDefComponent;
import com.bitdecay.helm.component.CameraFollowComponent;
import com.bitdecay.helm.component.DelayedAddComponent;
import com.bitdecay.helm.component.ShipLaunchComponent;
import com.bitdecay.helm.component.TransformComponent;
import com.bitdecay.helm.component.VelocityComponent;
import com.bitdecay.helm.entities.LandingPlatformEntity;
import com.bitdecay.helm.entities.ShipEntity;
import com.bitdecay.helm.math.Geom;
import com.bitdecay.helm.menu.RotatingLabel;
import com.bitdecay.helm.system.PlayerStartLevelSystem;
import com.bitdecay.helm.ui.UpdatingContainer;

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

    public static void preLaunchShip(GameEntity ship) {
        // add components
        ship.removeComponent(ShipLaunchComponent.class);
        DelayedAddComponent delays = PlayerStartLevelSystem.addPlayerStartComponents(new DelayedAddComponent());
        for (DelayedAddComponent.DelayedAdd delay : delays.delays) {
            ship.addComponent(delay.component);
        }
        ship.addComponent(new VelocityComponent());
    }

    public static void removeLandingFocus(Array<GameEntity> entities) {
        for (GameEntity entity : entities) {
            if (entity instanceof LandingPlatformEntity) {
                entity.removeComponent(CameraFollowComponent.class);
                return;
            }
        }
    }

    public static UpdatingContainer getPage(float fontSize, Skin skin, String... strings) {
        Table table = new Table();
        table.align(Align.center);

        for (String line : strings) {
            RotatingLabel label = new RotatingLabel(line, fontSize, skin);
            label.setOrigin(Align.center);
            table.add(label).center();
            table.row();
        }

        return new UpdatingContainer(table);
    }
}
