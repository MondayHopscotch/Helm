package com.bitdecay.helm.tutorial;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Align;
import com.bitdecay.helm.GameEntity;
import com.bitdecay.helm.Helm;
import com.bitdecay.helm.component.DelayedAddComponent;
import com.bitdecay.helm.component.ShipLaunchComponent;
import com.bitdecay.helm.component.VelocityComponent;
import com.bitdecay.helm.menu.RotatingLabel;
import com.bitdecay.helm.persist.JsonUtils;
import com.bitdecay.helm.screen.LevelPlayer;
import com.bitdecay.helm.system.PlayerStartLevelSystem;
import com.bitdecay.helm.ui.UpdatingContainer;
import com.bitdecay.helm.world.LevelDefinition;

/**
 * Created by Monday on 11/19/2017.
 */

public class LandingSpeedPhase extends PagedPhase {

    private GameEntity ship;

    @Override
    public void start(Helm game, final LevelPlayer player, Stage stage) {
        init(stage);

        LevelDefinition tutorial1 = JsonUtils.unmarshal(LevelDefinition.class, Gdx.files.internal("level/tutorial/tut2.json"));
        player.loadLevel(tutorial1);
        player.universalGravity.set(player.originalGravity);

        // get ship
        ship = TutorialUtils.getShip(player.allEntities);

        // add components
        ship.removeComponent(ShipLaunchComponent.class);
        DelayedAddComponent delays = PlayerStartLevelSystem.addPlayerStartComponents(new DelayedAddComponent());
        for (DelayedAddComponent.DelayedAdd delay : delays.delays) {
            ship.addComponent(delay.component);
        }

        final Vector2 landingLocation = TutorialUtils.getLandingLocation(player.allEntities);
        RotatingLabel speedLabel4 = new RotatingLabel("Try landing the ship gently.", game.fontScale, game.skin);
        speedLabel4.setOrigin(Align.center);

        final UpdatingContainer page2 = new UpdatingContainer(speedLabel4);
        page2.updater = new Runnable() {
            @Override
            public void run() {
                Vector3 project = player.gameCam.project(new Vector3(landingLocation.x, landingLocation.y, 0));
                page2.setPosition(project.x, project.y - page2.getPrefHeight() / 2);
            }
        };

        page2.action = new Runnable() {
            @Override
            public void run() {
                ship.addComponent(new VelocityComponent());
            }
        };
        pages.add(page2);
        nextPage();
    }

    @Override
    public boolean update(ShapeRenderer shaper) {
        // The phase will automatically end once the player successfully lands.
        return false;
    }
}
