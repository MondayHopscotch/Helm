package com.bitdecay.helm.tutorial;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.bitdecay.helm.GameEntity;
import com.bitdecay.helm.Helm;
import com.bitdecay.helm.component.FuelComponent;
import com.bitdecay.helm.component.GravityAffectedComponent;
import com.bitdecay.helm.component.TransformComponent;
import com.bitdecay.helm.component.VelocityComponent;
import com.bitdecay.helm.component.control.SteeringControlComponent;
import com.bitdecay.helm.menu.RotatingLabel;
import com.bitdecay.helm.persist.JsonUtils;
import com.bitdecay.helm.screen.LevelPlayer;
import com.bitdecay.helm.ui.UpdatingContainer;
import com.bitdecay.helm.world.LevelDefinition;

/**
 * Created by Monday on 11/8/2017.
 */

public class FuelPhase extends PagedPhase {
    private Helm game;
    private LevelPlayer player;
    private GameEntity ship;
    private VelocityComponent velocity;
    private SteeringControlComponent steering;
    private FuelComponent fuel;


    @Override
    public void start(Helm game, LevelPlayer player, Stage stage) {
        this.game = game;
        this.player = player;
        init(stage);

        LevelDefinition fuelTutorialLevel = JsonUtils.unmarshal(LevelDefinition.class, Gdx.files.internal("level/tutorial/tut_boost.json"));
        player.loadLevel(fuelTutorialLevel);
        TutorialUtils.removeLandingFocus(player.allEntities);

        ship = TutorialUtils.getShip(player.allEntities);
        TutorialUtils.preLaunchShip(ship);

        velocity = ship.getComponent(VelocityComponent.class);
        steering = ship.getComponent(SteeringControlComponent.class);
        fuel = ship.getComponent(FuelComponent.class);

        // take off the velocity and steering and put it back on later
        ship.removeComponent(VelocityComponent.class);
        ship.removeComponent(SteeringControlComponent.class);


        makePages();
    }

    private void makePages() {
        final Vector2 playerLocation = ship.getComponent(TransformComponent.class).position;

        final UpdatingContainer thrusterPage = TutorialUtils.getPage(game.fontScale, game.skin,
                "The ship also comes equipped",
                "with a thruster"
        );
        thrusterPage.updater = new Runnable() {
            @Override
            public void run() {
                Vector3 project = player.gameCam.project(new Vector3(playerLocation.x, playerLocation.y, 0));
                thrusterPage.setPosition(project.x, project.y - thrusterPage.getPrefHeight() / 2);
            }
        };
        pages.add(thrusterPage);

        final UpdatingContainer halfFuelPage = TutorialUtils.getPage(game.fontScale, game.skin,
                "The thruster consumes",
                "fuel as it is used"
        );
        halfFuelPage.updater = new Runnable() {
            @Override
            public void run() {
                Vector3 project = player.gameCam.project(new Vector3(playerLocation.x, playerLocation.y, 0));
                halfFuelPage.setPosition(project.x, project.y - halfFuelPage.getPrefHeight() / 2);
            }
        };
        halfFuelPage.action = new Runnable() {
            @Override
            public void run() {
                fuel.fuelRemaining = fuel.maxFuel / 2;
            }
        };
        pages.add(halfFuelPage);

        final UpdatingContainer fuelLinePage = TutorialUtils.getPage(game.fontScale, game.skin,
                "We've given you half fuel.",
                "Note the fuel line on the ship"
        );
        fuelLinePage.updater = new Runnable() {
            @Override
            public void run() {
                Vector3 project = player.gameCam.project(new Vector3(playerLocation.x, playerLocation.y, 0));
                project.add(0, game.fontScale * -20, 0);
                fuelLinePage.setPosition(project.x, project.y - fuelLinePage.getPrefHeight() / 2);
            }
        };
        pages.add(fuelLinePage);
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
