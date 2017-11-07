package com.bitdecay.helm.tutorial;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.bitdecay.helm.GameEntity;
import com.bitdecay.helm.Helm;
import com.bitdecay.helm.component.GravityAffectedComponent;
import com.bitdecay.helm.component.TransformComponent;
import com.bitdecay.helm.component.VelocityComponent;
import com.bitdecay.helm.component.control.SteeringControlComponent;
import com.bitdecay.helm.entities.ShipEntity;
import com.bitdecay.helm.menu.RotatingLabel;
import com.bitdecay.helm.screen.LevelPlayer;
import com.bitdecay.helm.ui.UpdatingContainer;

/**
 * Created by Monday on 10/26/2017.
 */

public class SteeringPhase implements TutorialPhase {
    private LevelPlayer player;

    private TransformComponent shipTransform;
    private SteeringControlComponent steering;
    private float totalSpin = 0;
    private float lastAngle = 0;

    @Override
    public void start(Helm game, final LevelPlayer player, Stage stage) {
        this.player = player;
        fixShipForSteering();

        final Vector2 steeringCenter = steering.activeArea.getCenter(new Vector2());
        RotatingLabel steeringLabel1 = new RotatingLabel("Drag left or right", game.fontScale, game.skin, new ClickListener());
        steeringLabel1.setOrigin(Align.center);
        RotatingLabel steeringLabel2 = new RotatingLabel("in this area", game.fontScale, game.skin, new ClickListener());
        steeringLabel2.setOrigin(Align.center);
        RotatingLabel steeringLabel3 = new RotatingLabel("to steer your ship", game.fontScale, game.skin, new ClickListener());
        steeringLabel3.setOrigin(Align.center);

        Table steeringTable = new Table();
        steeringTable.align(Align.left);
        steeringTable.add(steeringLabel1).center();
        steeringTable.row();
        steeringTable.add(steeringLabel2).center();
        steeringTable.row();
        steeringTable.add(steeringLabel3).center();

        final UpdatingContainer page1 = new UpdatingContainer(steeringTable);
        page1.updater = new Runnable() {
            @Override
            public void run() {
                page1.setPosition(steeringCenter.x, steeringCenter.y);
            }
        };
        stage.addActor(page1);
    }

    public boolean update(ShapeRenderer shaper) {
        totalSpin += Math.abs(shipTransform.angle - lastAngle);
        lastAngle = shipTransform.angle;

        if (totalSpin < MathUtils.PI2 * 2) {
            if (steering != null) {
                Rectangle rect = steering.activeArea;
                shaper.setColor(Color.WHITE);
                DrawUtils.drawDottedRect(shaper, rect);
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

                steering = entity.getComponent(SteeringControlComponent.class);

                lastAngle = shipTransform.angle;

                if (entity.hasComponent(VelocityComponent.class)) {
                    entity.getComponent(VelocityComponent.class).currentVelocity.set(0, 0);
                    entity.removeComponent(GravityAffectedComponent.class);
                }
            }
        }
    }
}
