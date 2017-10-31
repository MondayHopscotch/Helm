package com.bitdecay.helm.tutorial;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.bitdecay.helm.GameEntity;
import com.bitdecay.helm.Helm;
import com.bitdecay.helm.component.BoosterComponent;
import com.bitdecay.helm.component.FuelComponent;
import com.bitdecay.helm.component.VelocityComponent;
import com.bitdecay.helm.component.control.BoostControlComponent;
import com.bitdecay.helm.entities.ShipEntity;
import com.bitdecay.helm.screen.LevelPlayer;
import com.bitdecay.helm.system.PlayerStartLevelSystem;


/**
 * Created by Monday on 10/26/2017.
 */

public class BoostingPhase implements TutorialPhase {
    private LevelPlayer player;

    private FuelComponent fuelComponent;

    @Override
    public void start(Helm game, LevelPlayer player, Stage stage) {
        this.player = player;
        for (GameEntity entity : player.allEntities) {
            if (entity instanceof ShipEntity) {
                entity.addComponent(new BoosterComponent(PlayerStartLevelSystem.PLAYER_BOOST_STRENGTH / 5));
                entity.addComponent(new VelocityComponent());
                entity.getComponent(FuelComponent.class).maxFuel = 100;
                entity.getComponent(FuelComponent.class).fuelRemaining = 100;

                fuelComponent = entity.getComponent(FuelComponent.class);
            }
        }
    }

    @Override
    public boolean update(ShapeRenderer shaper) {
        for (GameEntity entity : player.allEntities) {
            BoostControlComponent boostControl = entity.getComponent(BoostControlComponent.class);
            if (boostControl != null) {
                Rectangle rect = boostControl.activeArea;
                shaper.setColor(Color.WHITE);

                DrawUtils.drawDottedRect(shaper, rect);
            }
        }

        if (fuelComponent.fuelRemaining <= 0) {
            return true;
        } else {
            return false;
        }
    }
}
