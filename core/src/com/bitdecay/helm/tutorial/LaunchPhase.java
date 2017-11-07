package com.bitdecay.helm.tutorial;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.bitdecay.helm.GameEntity;
import com.bitdecay.helm.Helm;
import com.bitdecay.helm.component.SteeringComponent;
import com.bitdecay.helm.component.TransformComponent;
import com.bitdecay.helm.component.VelocityComponent;
import com.bitdecay.helm.component.control.BoostControlComponent;
import com.bitdecay.helm.entities.LandingPlatformEntity;
import com.bitdecay.helm.entities.ShipEntity;
import com.bitdecay.helm.input.ActiveTouch;
import com.bitdecay.helm.menu.RotatingLabel;
import com.bitdecay.helm.persist.JsonUtils;
import com.bitdecay.helm.screen.LevelPlayer;
import com.bitdecay.helm.system.PlayerStartLevelSystem;
import com.bitdecay.helm.ui.UpdatingContainer;
import com.bitdecay.helm.world.LevelDefinition;

/**
 * Created by Monday on 10/30/2017.
 */

public class LaunchPhase implements TutorialPhase {
    private Helm game;
    private LevelPlayer player;
    private Stage stage;
    private UpdatingContainer livePage;

    private Array<UpdatingContainer> pages;
    private int currentPage = -1;
    private ClickListener stageClickListener;
    private GameEntity ship;

    @Override
    public void start(Helm game, LevelPlayer player, Stage stage) {
        this.game = game;
        this.player = player;
        this.stage = stage;
        pages = new Array<>();

        stageClickListener = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if (livePage != null && livePage.action != null) {
                    livePage.action.run();
                }
                nextPage();
            }
        };

        LevelDefinition tutorial1 = JsonUtils.unmarshal(LevelDefinition.class, Gdx.files.internal("level/testCollisions.json"));
        player.loadLevel(tutorial1);

        stage.clear();

        findShip();

        findPlayerBoost();

        addPlayerInfo();
    }

    private void findShip() {
        for (GameEntity entity : player.allEntities) {
            if (entity instanceof ShipEntity) {
                ship = entity;
            }
        }
    }

    private BoostControlComponent findPlayerBoost() {
        return ship.getComponent(BoostControlComponent.class);
    }

    private void addPlayerInfo() {
        // we should create a 'tracking update container' that is given a reference to a TransformComponent
        // it should be updatable such that it can follow it properly

        final Vector2 playerLocation = getPlayerLocation();
        RotatingLabel playerLabel = new RotatingLabel("This is your ship!", game.fontScale, game.skin, stageClickListener);
        playerLabel.setOrigin(Align.center);
        final UpdatingContainer page1 = new UpdatingContainer(playerLabel);
        page1.updater = new Runnable() {
            @Override
            public void run() {
                Vector3 project = player.gameCam.project(new Vector3(playerLocation.x, playerLocation.y, 0));
                page1.setPosition(project.x, project.y);
            }
        };
        pages.add(page1);

        final Vector2 landingLocation = getLandingLocation();
        RotatingLabel landingLabel1 = new RotatingLabel("This is where", game.fontScale, game.skin, stageClickListener);
        landingLabel1.setOrigin(Align.center);

        RotatingLabel landingLabel2 = new RotatingLabel("you need to get!", game.fontScale, game.skin, stageClickListener);
        landingLabel2.setOrigin(Align.center);

        Table landingTable = new Table();
        landingTable.align(Align.left);
        landingTable.add(landingLabel1).center();
        landingTable.row();
        landingTable.add(landingLabel2).center();

        final UpdatingContainer page2 = new UpdatingContainer(landingTable);
        page2.updater = new Runnable() {
            @Override
            public void run() {
                Vector3 project = player.gameCam.project(new Vector3(landingLocation.x, landingLocation.y, 0));
                page2.setPosition(project.x, project.y);
            }
        };
        pages.add(page2);

        final Vector2 boostCenter = getBoostCenter();
        RotatingLabel boostLabel1 = new RotatingLabel("Tap anywhere on this", game.fontScale, game.skin, stageClickListener);
        boostLabel1.setOrigin(Align.center);
        RotatingLabel boostLabel2 = new RotatingLabel("side of the screen", game.fontScale, game.skin, stageClickListener);
        boostLabel2.setOrigin(Align.center);
        RotatingLabel boostLabel3 = new RotatingLabel("to launch your ship!", game.fontScale, game.skin, stageClickListener);
        boostLabel3.setOrigin(Align.center);

        Table boostTable = new Table();
        boostTable.align(Align.left);
        boostTable.add(boostLabel1).center();
        boostTable.row();
        boostTable.add(boostLabel2).center();
        boostTable.row();
        boostTable.add(boostLabel3).center();

        final UpdatingContainer page3 = new UpdatingContainer(boostTable);
        page3.updater = new Runnable() {
            @Override
            public void run() {
                page3.setPosition(boostCenter.x, boostCenter.y);
            }
        };
        page3.action = new Runnable() {
            @Override
            public void run() {
                // launch the ship for the player!
                PlayerStartLevelSystem startSystem = player.getSystem(PlayerStartLevelSystem.class);
                startSystem.touches.activeTouches.add(new ActiveTouch(0, (int) boostCenter.x, (int) boostCenter.y));
            }
        };
        pages.add(page3);

        nextPage();
    }

    private void nextPage() {
        stage.clear();
        currentPage++;
        if (currentPage >= pages.size) {
            System.out.println("NO PAGES LEFT!");
            return;
        }

        UpdatingContainer page = pages.get(currentPage);
        livePage = page;
        stage.addActor(page);
        stage.addListener(stageClickListener);
    }

    private Vector2 getPlayerLocation() {
        return ship.getComponent(TransformComponent.class).position;
    }

    private Vector2 getLandingLocation() {
        for (GameEntity entity : player.allEntities) {
            if (entity instanceof LandingPlatformEntity) {
                return entity.getComponent(TransformComponent.class).position;
            }
        }
        return null;
    }

    private Vector2 getBoostCenter() {
        BoostControlComponent component = ship.getComponent(BoostControlComponent.class);
        Vector2 center = new Vector2(component.activeArea.x, component.activeArea.y);
        center.add(component.activeArea.getWidth() / 2, component.activeArea.getHeight() / 2);
        return center;
    }

    @Override
    public boolean update(ShapeRenderer shaper) {
        if (ship.hasComponent(SteeringComponent.class) && ship.hasComponent(VelocityComponent.class)) {
            if (ship.getComponent(VelocityComponent.class).currentVelocity.len() < 0.1f) {
                return true;
            }
        }
        return false;
    }
}
