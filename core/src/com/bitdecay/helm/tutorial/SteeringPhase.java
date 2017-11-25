package com.bitdecay.helm.tutorial;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.bitdecay.helm.GameEntity;
import com.bitdecay.helm.Helm;
import com.bitdecay.helm.component.GravityAffectedComponent;
import com.bitdecay.helm.component.TransformComponent;
import com.bitdecay.helm.component.VelocityComponent;
import com.bitdecay.helm.component.control.BoostControlComponent;
import com.bitdecay.helm.component.control.SteeringControlComponent;
import com.bitdecay.helm.entities.ShipEntity;
import com.bitdecay.helm.math.Geom;
import com.bitdecay.helm.menu.RotatingLabel;
import com.bitdecay.helm.screen.LevelPlayer;
import com.bitdecay.helm.ui.UpdatingContainer;

/**
 * Created by Monday on 10/26/2017.
 */

public class SteeringPhase implements TutorialPhase {
    private LevelPlayer player;
    private Stage stage;

    private TransformComponent shipTransform;
    private SteeringControlComponent steering;
    private float totalSpin = 0;
    private float lastAngle = 0;
    private GameEntity ship;
    private BoostControlComponent boostControl;
    private boolean finishedSpinning;

    private float angleForFinish = MathUtils.PI / 2;

    @Override
    public void start(Helm game, final LevelPlayer player, Stage stage) {
        stage.clear();

        finishedSpinning = false;

        this.player = player;
        this.stage = stage;
        fixShipForSteering();

        final Vector2 steeringCenter = steering.activeArea.getCenter(new Vector2());
        final UpdatingContainer page1 = TutorialUtils.getPage(game.fontScale, game.skin,
                "Drag left and right",
                "in this area",
                "to steer your ship"
        );
        page1.updater = new Runnable() {
            @Override
            public void run() {
                page1.setPosition(steeringCenter.x, steeringCenter.y);
            }
        };
        page1.updater.run();
        stage.addActor(page1);
    }

    private void fixShipForSteering() {
        for (GameEntity entity : player.allEntities) {
            if (entity instanceof ShipEntity) {
                ship = entity;
                break;
            }
        }
        shipTransform = ship.getComponent(TransformComponent.class);
        steering = ship.getComponent(SteeringControlComponent.class);

        boostControl = ship.getComponent(BoostControlComponent.class);
        ship.removeComponent(BoostControlComponent.class);

        lastAngle = shipTransform.angle;

        if (ship.hasComponent(VelocityComponent.class)) {
            ship.getComponent(VelocityComponent.class).currentVelocity.set(0, 0);
        }
        ship.removeComponent(GravityAffectedComponent.class);
    }

    public boolean update(ShapeRenderer shaper, float delta) {
        float spin = Math.abs(shipTransform.angle - lastAngle);
        while (spin > 1) {
            // they can't spin this fast, so they just passed "0" on the unit circle
            spin -= 1;
        }
        totalSpin += spin;

        lastAngle = shipTransform.angle;

        if (steering != null) {
            Rectangle rect = steering.activeArea;
            shaper.setColor(Color.WHITE);
            DrawUtils.drawDottedRect(shaper, rect);
        }

        if (finishedSpinning) {
            ship.addComponent(boostControl);
            shipTransform.angle = Geom.ROTATION_UP;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean touchUp(int screenX, int screenY) {
        if (totalSpin > angleForFinish) {
            finishedSpinning = true;
        }

        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY) {
        return false;
    }
}
