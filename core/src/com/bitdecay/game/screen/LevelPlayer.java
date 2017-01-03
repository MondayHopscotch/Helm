package com.bitdecay.game.screen;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
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
import com.bitdecay.game.system.render.RenderBodySystem;
import com.bitdecay.game.system.render.RenderBoostSystem;
import com.bitdecay.game.system.render.RenderFuelSystem;
import com.bitdecay.game.system.SteeringInputSystem;
import com.bitdecay.game.system.SteeringSystem;
import com.bitdecay.game.system.render.RenderSteeringSystem;
import com.bitdecay.game.world.LevelDefinition;
import com.bitdecay.game.world.LineSegment;

/**
 * Created by Monday on 12/18/2016.
 */
public class LevelPlayer {

    private static final int BASE_CAM_BUFFER = 500;
    private GamePilot pilot;

    OrthographicCamera screenCam;
    FollowOrthoCamera gameCam;
    ShapeRenderer shapeRenderer;

    Array<GameSystem> gameSystems = new Array<>(1);

    Array<GameSystem> inputSystems = new Array<>(1);

    Array<GameSystem> gameRenderSystems = new Array<>(1);

    Array<GameSystem> screenRenderSystems = new Array<>(1);


    Array<GameEntity> allEntities = new Array<>(1000);
    private final InputMultiplexer inputMux;


    public LevelPlayer(GamePilot pilot) {
        this.pilot = pilot;

        screenCam = new OrthographicCamera(1920, 1080);
        screenCam.translate(screenCam.viewportWidth / 2, screenCam.viewportHeight / 2);
        screenCam.update();

        gameCam = new FollowOrthoCamera(1920, 1080);
        gameCam.minZoom = 10;
        gameCam.maxZoom = .2f;
        gameCam.buffer = BASE_CAM_BUFFER;
        gameCam.snapSpeed = .5f;

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
        inputMux.addProcessor(startSystem);

        GravitySystem gravitySystem = new GravitySystem(pilot);

        MovementSystem movementSystem = new MovementSystem(pilot);

        CameraUpdateSystem cameraSystem = new CameraUpdateSystem(pilot, gameCam);

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

        gameRenderSystems.add(renderBoostSystem);
        gameRenderSystems.add(renderBodySystem);
        gameRenderSystems.add(renderFuelSystem);

        RenderSteeringSystem renderSteeringSystem = new RenderSteeringSystem(pilot, screenCam, shapeRenderer);
        screenRenderSystems.add(renderSteeringSystem);
    }

    public void resetInputSystems() {
        for (GameSystem system : inputSystems) {
            system.reset();
        }
    }

    public void loadLevel(LevelDefinition levelDef) {
        allEntities.clear();

        resetInputSystems();

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
        scaleCamBuffer();
        gameCam.update(delta);
    }

    private void scaleCamBuffer() {
        gameCam.buffer = Math.max(BASE_CAM_BUFFER * gameCam.zoom, 500);
    }

    public void render(float delta) {
        shapeRenderer.setProjectionMatrix(gameCam.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        for (GameSystem system : gameRenderSystems) {
            system.act(allEntities, delta);
        }
        shapeRenderer.setProjectionMatrix(screenCam.combined);
        for (GameSystem system : screenRenderSystems) {
            system.act(allEntities, delta);
        }
        shapeRenderer.end();
    }

    public InputProcessor getInput() {
        return inputMux;
    }
}
