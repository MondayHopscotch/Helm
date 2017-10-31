package com.bitdecay.helm.tutorial;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.bitdecay.helm.GameEntity;
import com.bitdecay.helm.Helm;
import com.bitdecay.helm.component.BoosterComponent;
import com.bitdecay.helm.component.CameraFollowComponent;
import com.bitdecay.helm.component.ShipLaunchComponent;
import com.bitdecay.helm.component.SteeringComponent;
import com.bitdecay.helm.component.TransformComponent;
import com.bitdecay.helm.component.control.SteeringControlComponent;
import com.bitdecay.helm.entities.ShipEntity;
import com.bitdecay.helm.persist.JsonUtils;
import com.bitdecay.helm.screen.LevelPlayer;
import com.bitdecay.helm.world.LevelDefinition;

/**
 * Created by Monday on 10/26/2017.
 */

public class SteeringPhase implements TutorialPhase {
    private LevelPlayer player;

    private TransformComponent shipTransform;
    private float totalSpin = 0;
    private float lastAngle = 0;

    @Override
    public void start(Helm game, LevelPlayer player, Stage stage) {
        this.player = player;
        LevelDefinition tutorial1 = JsonUtils.unmarshal(LevelDefinition.class, Gdx.files.internal("level/tutorial/tut1.json"));
        player.loadLevel(tutorial1);
        fixShipForSteering();
    }

    public boolean update(ShapeRenderer shaper) {
        totalSpin += Math.abs(shipTransform.angle - lastAngle);
        lastAngle = shipTransform.angle;

        if (totalSpin < MathUtils.PI2 * 2) {
            for (GameEntity entity : player.allEntities) {
                SteeringControlComponent steeringControl = entity.getComponent(SteeringControlComponent.class);
                if (steeringControl != null) {
                    Rectangle rect = steeringControl.activeArea;
                    shaper.setColor(Color.WHITE);
                    DrawUtils.drawDottedRect(shaper, rect);
                }
            }
            return false;
        } else {
            return true;
        }
    }

    private void fixShipForSteering() {
        for (GameEntity entity : player.allEntities) {
            if (entity instanceof ShipEntity) {
                shipTransform = entity.getComponent(TransformComponent.class);
                lastAngle = shipTransform.angle;

                entity.removeComponent(ShipLaunchComponent.class);
                entity.removeComponent(BoosterComponent.class);
                entity.removeComponent(CameraFollowComponent.class);

                entity.addComponent(new SteeringComponent());
            }
        }
    }
}
