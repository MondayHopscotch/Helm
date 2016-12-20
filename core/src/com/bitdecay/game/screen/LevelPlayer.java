package com.bitdecay.game.screen;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.bitdecay.game.GameEntity;
import com.bitdecay.game.GamePilot;
import com.bitdecay.game.camera.FollowOrthoCamera;
import com.bitdecay.game.entities.LandingPlatformEntity;
import com.bitdecay.game.entities.LineSegmentEntity;
import com.bitdecay.game.entities.ShipEntity;
import com.bitdecay.game.system.BoostSystem;
import com.bitdecay.game.system.BoosterInputSystem;
import com.bitdecay.game.system.CameraUpdateSystem;
import com.bitdecay.game.system.CollisionAlignmentSystem;
import com.bitdecay.game.system.CollisionSystem;
import com.bitdecay.game.system.CrashSystem;
import com.bitdecay.game.system.DelayedAddSystem;
import com.bitdecay.game.system.GameSystem;
import com.bitdecay.game.system.GravitySystem;
import com.bitdecay.game.system.LandingSystem;
import com.bitdecay.game.system.MovementSystem;
import com.bitdecay.game.system.PlayerCollisionHandlerSystem;
import com.bitdecay.game.system.PlayerStartLevelSystem;
import com.bitdecay.game.system.RenderBodySystem;
import com.bitdecay.game.system.RenderBoostSystem;
import com.bitdecay.game.system.RenderFuelSystem;
import com.bitdecay.game.system.SteeringInputSystem;
import com.bitdecay.game.system.SteeringSystem;
import com.bitdecay.game.world.LevelDefinition;
import com.bitdecay.game.world.LineSegment;

/**
 * Created by Monday on 12/18/2016.
 */
public class LevelPlayer {

    private GamePilot pilot;

    FollowOrthoCamera cam;
    ShapeRenderer shapeRenderer;

    Array<GameSystem> gameSystems = new Array<>(1);

    Array<GameSystem> inputSystems = new Array<>(1);

    Array<GameSystem> renderSystems = new Array<>(1);


    Array<GameEntity> allEntities = new Array<>(1000);
    private final InputMultiplexer inputMux;


    public LevelPlayer(GamePilot pilot) {
        this.pilot = pilot;

        cam = new FollowOrthoCamera(1920, 1080);
        cam.minZoom = 3;
        cam.maxZoom = .5f;
        cam.buffer = 2000;

        shapeRenderer = new ShapeRenderer();

        inputMux = new InputMultiplexer();

        initSystems();
    }

    private void initSystems() {
        BoosterInputSystem boosterInputSystem = new BoosterInputSystem(pilot);
        inputSystems.add(boosterInputSystem);
        inputMux.addProcessor(boosterInputSystem);

        BoostSystem boostSystem = new BoostSystem(pilot);

        SteeringInputSystem steeringInputSystem = new SteeringInputSystem(pilot);
        inputSystems.add(steeringInputSystem);
        inputMux.addProcessor(steeringInputSystem);

        SteeringSystem steeringSystem = new SteeringSystem(pilot);

        PlayerStartLevelSystem startSystem = new PlayerStartLevelSystem(pilot);
        inputSystems.add(startSystem);
        inputMux.addProcessor(startSystem);

        GravitySystem gravitySystem = new GravitySystem(pilot);

        MovementSystem movementSystem = new MovementSystem(pilot);

        CameraUpdateSystem cameraSystem = new CameraUpdateSystem(pilot, cam);

        CollisionAlignmentSystem collisionAlignmentSystem = new CollisionAlignmentSystem(pilot);
        CollisionSystem collisionSystem = new CollisionSystem(pilot);
        PlayerCollisionHandlerSystem playerCollisionSystem = new PlayerCollisionHandlerSystem(pilot);

        DelayedAddSystem delaySystem = new DelayedAddSystem(pilot);

        LandingSystem landingSystem = new LandingSystem(pilot);

        CrashSystem crashSystem = new CrashSystem(pilot);

        gameSystems.add(cameraSystem);
        gameSystems.add(boosterInputSystem);
        gameSystems.add(boostSystem);
        gameSystems.add(startSystem);
        gameSystems.add(gravitySystem);
        gameSystems.add(steeringInputSystem);
        gameSystems.add(steeringSystem);
        gameSystems.add(movementSystem);
        gameSystems.add(collisionAlignmentSystem);
        gameSystems.add(collisionSystem);
        gameSystems.add(playerCollisionSystem);
        gameSystems.add(delaySystem);
        gameSystems.add(landingSystem);
        gameSystems.add(crashSystem);

        RenderBodySystem renderBodySystem = new RenderBodySystem(pilot, shapeRenderer);
        RenderBoostSystem renderBoostSystem = new RenderBoostSystem(pilot, shapeRenderer);
        RenderFuelSystem renderFuelSystem = new RenderFuelSystem(pilot, shapeRenderer);

        renderSystems.add(renderBoostSystem);
        renderSystems.add(renderBodySystem);
        renderSystems.add(renderFuelSystem);
    }

    public void resetInputSystems() {
        for (GameSystem system : inputSystems) {
            system.reset();
        }
    }

    public void loadLevel(LevelDefinition levelDef) {
        allEntities.clear();

        for (LineSegment line : levelDef.levelLines) {
            allEntities.add(new LineSegmentEntity(line));
        }

        if (levelDef.finishPlatform.area() > 0) {
            LandingPlatformEntity plat = new LandingPlatformEntity(levelDef.finishPlatform);
            allEntities.add(plat);
        }

        ShipEntity ship = new ShipEntity(levelDef.startPosition, levelDef.startingFuel);
        allEntities.add(ship);
    }

    public void printMatchingGameSystems(GameEntity entity) {
        System.out.println("Entity " + entity.getClass().getSimpleName() + " matches the following systems:");
        for (GameSystem system : gameSystems) {
            if (system.canActOn(entity)) {
                System.out.println(system.getClass().getSimpleName());
            }
        }
    }

    public void update(float delta) {
        for (GameSystem system : gameSystems) {
            system.act(allEntities, delta);
        }
        cam.update(delta);
    }

    public void render(float delta) {
        shapeRenderer.setProjectionMatrix(cam.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        for (GameSystem system : renderSystems) {
            system.act(allEntities, delta);
        }
        shapeRenderer.end();
    }

    public InputProcessor getInput() {
        return inputMux;
    }
}
