package com.bitdecay.helm.tutorial;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.bitdecay.helm.GameEntity;
import com.bitdecay.helm.Helm;
import com.bitdecay.helm.component.GravityAffectedComponent;
import com.bitdecay.helm.component.TransformComponent;
import com.bitdecay.helm.component.VelocityComponent;
import com.bitdecay.helm.component.control.SteeringControlComponent;
import com.bitdecay.helm.menu.RotatingLabel;
import com.bitdecay.helm.screen.LevelPlayer;
import com.bitdecay.helm.ui.UpdatingContainer;

/**
 * Created by Monday on 11/8/2017.
 */

public class FuelPhase extends PagedPhase {
    private Helm game;
    private LevelPlayer player;
    private GameEntity ship;
    private VelocityComponent velocity;
    private SteeringControlComponent steering;


    @Override
    public void start(Helm game, LevelPlayer player, Stage stage) {
        this.game = game;
        this.player = player;
        init(stage);

        ship = TutorialUtils.getShip(player.allEntities);

        velocity = ship.getComponent(VelocityComponent.class);
        steering = ship.getComponent(SteeringControlComponent.class);
        // take off the velocity and steering and put it back on later
        ship.removeComponent(VelocityComponent.class);
        ship.removeComponent(SteeringControlComponent.class);

        makePages();
    }

    private void makePages() {
        final Vector2 playerLocation = ship.getComponent(TransformComponent.class).position;
        RotatingLabel fuelLabel1 = new RotatingLabel("The thruster uses fuel.", game.fontScale, game.skin);
        fuelLabel1.setOrigin(Align.center);
        RotatingLabel fuelLabel2 = new RotatingLabel("Pay attention to the", game.fontScale, game.skin);
        fuelLabel2.setOrigin(Align.center);
        RotatingLabel fuelLabel3 = new RotatingLabel("fuel line on the ship!", game.fontScale, game.skin);
        fuelLabel3.setOrigin(Align.center);

        Table fuelTable = new Table();
        fuelTable.setTouchable(Touchable.disabled);
        fuelTable.align(Align.left);
        fuelTable.add(fuelLabel1).center();
        fuelTable.row();
        fuelTable.add(fuelLabel2).center();
        fuelTable.row();
        fuelTable.add(fuelLabel3).center();

        final UpdatingContainer page1 = new UpdatingContainer(fuelTable);
        page1.updater = new Runnable() {
            @Override
            public void run() {
                Vector3 project = player.gameCam.project(new Vector3(playerLocation.x, playerLocation.y, 0));
                page1.setPosition(project.x, project.y - page1.getPrefHeight()/2);
            }
        };
        page1.updater.run();
        pages.add(page1);

        nextPage();
    }

    @Override
    public boolean update(ShapeRenderer shaper, float delta) {
        super.update(shaper, delta);
        if (currentPage >= pages.size) {
            ship.addComponent(velocity);
            ship.addComponent(steering);
            ship.addComponent(new GravityAffectedComponent());
            return true;
        } else {
            return false;
        }
    }
}
