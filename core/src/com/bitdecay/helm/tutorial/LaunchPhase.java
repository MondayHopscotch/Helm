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

public class LaunchPhase extends PagedPhase {
    private Helm game;
    private LevelPlayer player;
    private GameEntity ship;
    private BoostControlComponent boostControl;

    @Override
    public void start(Helm game, LevelPlayer player, Stage stage) {
        this.game = game;
        this.player = player;
        init(stage);

        ship = TutorialUtils.getShip(player.allEntities);

        boostControl = ship.getComponent(BoostControlComponent.class);

        makePages();
    }


    private void makePages() {
        final Vector2 boostCenter = getBoostCenter(boostControl);
        RotatingLabel boostLabel1 = new RotatingLabel("Tap on this side", game.fontScale, game.skin);
        boostLabel1.setOrigin(Align.center);
        RotatingLabel boostLabel2 = new RotatingLabel("of the screen", game.fontScale, game.skin);
        boostLabel2.setOrigin(Align.center);
        RotatingLabel boostLabel3 = new RotatingLabel("to launch your ship!", game.fontScale, game.skin);
        boostLabel3.setOrigin(Align.center);

        Table boostTable = new Table();
        boostTable.setTouchable(Touchable.disabled);
        boostTable.align(Align.left);
        boostTable.add(boostLabel1).center();
        boostTable.row();
        boostTable.add(boostLabel2).center();
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
        page1.action = new Runnable() {
            @Override
            public void run() {
                // launch the ship for the player!
                PlayerStartLevelSystem startSystem = player.getSystem(PlayerStartLevelSystem.class);
                startSystem.touches.activeTouches.add(new ActiveTouch(0, (int) boostCenter.x, (int) boostCenter.y));
            }
        };
        pages.add(page1);

        nextPage();
    }

    private Vector2 getBoostCenter(BoostControlComponent boostControl) {
        Vector2 center = new Vector2(boostControl.activeArea.x, boostControl.activeArea.y);
        center.add(boostControl.activeArea.getWidth() / 2, boostControl.activeArea.getHeight() / 2);
        return center;
    }

    @Override
    public boolean update(ShapeRenderer shaper) {
        if (boostControl != null) {
            Rectangle rect = boostControl.activeArea;
            shaper.setColor(Color.WHITE);

            DrawUtils.drawDottedRect(shaper, rect);
        }

        if (ship.hasComponent(SteeringComponent.class) && ship.hasComponent(VelocityComponent.class)) {
            if (ship.getComponent(VelocityComponent.class).currentVelocity.len() < 0.1f) {
                return true;
            }
        }
        return false;
    }
}
