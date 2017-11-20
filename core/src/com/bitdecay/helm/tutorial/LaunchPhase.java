package com.bitdecay.helm.tutorial;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.bitdecay.helm.GameEntity;
import com.bitdecay.helm.Helm;
import com.bitdecay.helm.component.SteeringComponent;
import com.bitdecay.helm.component.VelocityComponent;
import com.bitdecay.helm.component.control.BoostControlComponent;
import com.bitdecay.helm.input.ActiveTouch;
import com.bitdecay.helm.menu.RotatingLabel;
import com.bitdecay.helm.screen.LevelPlayer;
import com.bitdecay.helm.system.PlayerStartLevelSystem;
import com.bitdecay.helm.ui.UpdatingContainer;

/**
 * Created by Monday on 10/30/2017.
 */

public class LaunchPhase implements TutorialPhase {
    private Helm game;
    private LevelPlayer player;
    private GameEntity ship;
    private Stage stage;
    private BoostControlComponent boostControl;

    private static final float timeBeforeLaunch = .5f;
    private float elapsed = 0;

    @Override
    public void start(Helm game, LevelPlayer player, Stage stage) {
        this.game = game;
        this.player = player;
        this.stage = stage;

        ship = TutorialUtils.getShip(player.allEntities);

        boostControl = ship.getComponent(BoostControlComponent.class);
        // take this off the ship so they don't launch until we are ready for them to.
        ship.removeComponent(BoostControlComponent.class);

        makePages();
        elapsed = 0;
    }


    private void makePages() {
        final Vector2 boostCenter = getBoostCenter(boostControl);
        RotatingLabel boostLabel1 = new RotatingLabel("Tap in this area", game.fontScale, game.skin);
        boostLabel1.setOrigin(Align.center);
        RotatingLabel boostLabel3 = new RotatingLabel("to launch your ship!", game.fontScale, game.skin);
        boostLabel3.setOrigin(Align.center);

        Table boostTable = new Table();
        boostTable.setTouchable(Touchable.disabled);
        boostTable.align(Align.left);
        boostTable.add(boostLabel1).center();
        boostTable.row();
        boostTable.add(boostLabel3).center();

        final UpdatingContainer page1 = new UpdatingContainer(boostTable);
        page1.updater = new Runnable() {
            @Override
            public void run() {
                page1.setPosition(boostCenter.x, boostCenter.y);
            }
        };
        page1.updater.run();
        stage.addActor(page1);
    }

    private Vector2 getBoostCenter(BoostControlComponent boostControl) {
        Vector2 center = new Vector2(boostControl.activeArea.x, boostControl.activeArea.y);
        center.add(boostControl.activeArea.getWidth() / 2, boostControl.activeArea.getHeight() / 2);
        return center;
    }

    @Override
    public boolean update(ShapeRenderer shaper, float delta) {
        elapsed += delta;
        if (boostControl != null) {
            Rectangle rect = boostControl.activeArea;
            shaper.setColor(Color.WHITE);

            DrawUtils.drawDottedRect(shaper, rect);
        }

        if (ship.hasComponent(SteeringComponent.class) && ship.hasComponent(VelocityComponent.class)) {
            if (ship.getComponent(VelocityComponent.class).currentVelocity.y <= 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY) {
        if (elapsed <= timeBeforeLaunch) {
            // ensure they are on this page for just a bit so they can read
            return true;
        }

        if (boostControl.activeArea.contains(screenX, screenY)) {
            ship.addComponent(boostControl);
            PlayerStartLevelSystem startSystem = player.getSystem(PlayerStartLevelSystem.class);
            startSystem.touches.activeTouches.add(new ActiveTouch(0, screenX, screenY));
        }
        return false;
    }
}
