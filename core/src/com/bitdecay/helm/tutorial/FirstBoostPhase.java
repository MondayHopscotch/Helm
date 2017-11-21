package com.bitdecay.helm.tutorial;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.bitdecay.helm.GameEntity;
import com.bitdecay.helm.Helm;
import com.bitdecay.helm.component.BoosterComponent;
import com.bitdecay.helm.component.FuelComponent;
import com.bitdecay.helm.component.GravityAffectedComponent;
import com.bitdecay.helm.component.VelocityComponent;
import com.bitdecay.helm.component.control.BoostControlComponent;
import com.bitdecay.helm.menu.RotatingLabel;
import com.bitdecay.helm.persist.JsonUtils;
import com.bitdecay.helm.screen.LevelPlayer;
import com.bitdecay.helm.ui.UpdatingContainer;
import com.bitdecay.helm.world.LevelDefinition;


/**
 * Created by Monday on 10/26/2017.
 */

public class FirstBoostPhase implements TutorialPhase {
    private LevelPlayer player;

    private FuelComponent fuelComponent;
    private BoostControlComponent controls;
    private BoosterComponent boost;

    private GameEntity ship;
    private Stage stage;
    private boolean done;
    private boolean started;

    @Override
    public void start(Helm game, LevelPlayer player, final Stage stage) {
        if (started) {
            // if we are being restarted, then the player already played this phase
            done = true;
        }

        started = false;

        this.player = player;
        this.stage = stage;

        LevelDefinition fuelTutorialLevel = JsonUtils.unmarshal(LevelDefinition.class, Gdx.files.internal("level/tutorial/tut_boost.json"));
        player.loadLevel(fuelTutorialLevel);

        ship = TutorialUtils.getShip(player.allEntities);
        TutorialUtils.preLaunchShip(ship);
        TutorialUtils.removeLandingFocus(player.allEntities);

        ship.removeComponent(GravityAffectedComponent.class);

        fuelComponent = ship.getComponent(FuelComponent.class);
        controls = ship.getComponent(BoostControlComponent.class);
        boost = ship.getComponent(BoosterComponent.class);

        final Vector2 boostCenter = controls.activeArea.getCenter(new Vector2());
        final UpdatingContainer page1 = TutorialUtils.getPage(game.fontScale, game.skin,
                "Touch this area",
                "to use your thruster"
        );
        page1.updater = new Runnable() {
            @Override
            public void run() {
                page1.setPosition(boostCenter.x, boostCenter.y);
            }
        };
        page1.updater.run();
        stage.addActor(page1);
    }

    @Override
    public boolean update(ShapeRenderer shaper, float delta) {
        Rectangle rect = controls.activeArea;
        shaper.setColor(Color.WHITE);

        DrawUtils.drawDottedRect(shaper, rect);

        if (TutorialUtils.getShip(player.allEntities) == null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean touchUp(int screenX, int screenY) {
        if (!started && controls.activeArea.contains(screenX, screenY)) {
            started = true;
            ship.addComponent(new GravityAffectedComponent());
        }
        return false;
    }
}
