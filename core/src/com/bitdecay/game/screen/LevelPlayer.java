package com.bitdecay.game.screen;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.bitdecay.game.GameEntity;
import com.bitdecay.game.GamePilot;
import com.bitdecay.game.camera.FollowOrthoCamera;
import com.bitdecay.game.entities.FocusPointEntity;
import com.bitdecay.game.entities.LandingPlatformEntity;
import com.bitdecay.game.entities.LineSegmentEntity;
import com.bitdecay.game.entities.ShipEntity;
import com.bitdecay.game.system.BoostSystem;
import com.bitdecay.game.system.BoosterInputSystem;
import com.bitdecay.game.system.ProximityFocusSystem;
import com.bitdecay.game.system.CameraUpdateSystem;
import com.bitdecay.game.system.CollisionAlignmentSystem;
import com.bitdecay.game.system.CollisionSystem;
import com.bitdecay.game.system.CrashSystem;
import com.bitdecay.game.system.DelayedAddSystem;
import com.bitdecay.game.system.GameSystem;
import com.bitdecay.game.system.GravitySystem;
import com.bitdecay.game.system.PlayerBoundarySystem;
import com.bitdecay.game.system.LandingSystem;
import com.bitdecay.game.system.MovementSystem;
import com.bitdecay.game.system.PlayerCollisionHandlerSystem;
import com.bitdecay.game.system.PlayerStartLevelSystem;
import com.bitdecay.game.system.ProximityRemovalSystem;
import com.bitdecay.game.system.RemoveComponentSystem;
import com.bitdecay.game.system.TimerSystem;
import com.bitdecay.game.system.render.DebugFocusPointSystem;
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

    public static final float DELTA_STEP = 1/60f;
    float deltaRemainder;

    private static final int BASE_CAM_BUFFER = 500;
    FollowOrthoCamera gameCam;

    private GamePilot pilot;
    OrthographicCamera screenCam;

    ShapeRenderer shapeRenderer;

    Array<GameSystem> gameSystems = new Array<>(1);

    Array<GameSystem> inputSystems = new Array<>(1);

    Array<GameSystem> gameRenderSystems = new Array<>(1);

    Array<GameSystem> screenRenderSystems = new Array<>(1);


    Array<GameEntity> allEntities = new Array<>(1000);
    private final InputMultiplexer inputMux;
    private PlayerBoundarySystem playerBoundarySystem;


    public LevelPlayer(GamePilot pilot) {
        this.pilot = pilot;

        screenCam = new OrthographicCamera(1920, 1080);
        screenCam.translate(screenCam.viewportWidth / 2, screenCam.viewportHeight / 2);
        screenCam.update();

        gameCam = new FollowOrthoCamera(1920, 1080);
        gameCam.minZoom = 10;
        gameCam.maxZoom = .2f;
        gameCam.buffer = BASE_CAM_BUFFER;
        gameCam.snapSpeed = .02f;

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
        ProximityRemovalSystem proximityRemovalSystem = new ProximityRemovalSystem(pilot);
        ProximityFocusSystem cameraProximitySystem = new ProximityFocusSystem(pilot);

        DelayedAddSystem delaySystem = new DelayedAddSystem(pilot);
        RemoveComponentSystem removeComponentSystem = new RemoveComponentSystem(pilot);

        LandingSystem landingSystem = new LandingSystem(pilot);

        playerBoundarySystem = new PlayerBoundarySystem(pilot);

        CrashSystem crashSystem = new CrashSystem(pilot);

        TimerSystem timerSystem = new TimerSystem(pilot);

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
        gameSystems.add(proximityRemovalSystem);
        gameSystems.add(cameraProximitySystem);
        gameSystems.add(delaySystem);
        gameSystems.add(removeComponentSystem);
        gameSystems.add(playerBoundarySystem);
        gameSystems.add(crashSystem);
        gameSystems.add(landingSystem);
        gameSystems.add(timerSystem);

        RenderBodySystem renderBodySystem = new RenderBodySystem(pilot, shapeRenderer);
        RenderBoostSystem renderBoostSystem = new RenderBoostSystem(pilot, shapeRenderer);
        RenderFuelSystem renderFuelSystem = new RenderFuelSystem(pilot, shapeRenderer);

        gameRenderSystems.add(renderBoostSystem);
        gameRenderSystems.add(renderBodySystem);
        gameRenderSystems.add(renderFuelSystem);

        RenderSteeringSystem renderSteeringSystem = new RenderSteeringSystem(pilot, screenCam, shapeRenderer);
        screenRenderSystems.add(renderSteeringSystem);

        if (pilot.isDebug()) {
            DebugFocusPointSystem debugFocusPointSystem = new DebugFocusPointSystem(pilot, shapeRenderer);
            gameRenderSystems.add(debugFocusPointSystem);
        }
    }

    public void resetAllButInputSystems() {
        for (GameSystem gameSystem : gameSystems) {
            if (!inputSystems.contains(gameSystem, true)) {
                gameSystem.reset();
            }
        }
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

        for (Circle focus: levelDef.focusPoints) {
            FocusPointEntity focusPoint = new FocusPointEntity(focus);
            allEntities.add(focusPoint);
        }

        ShipEntity ship = new ShipEntity(levelDef.startPosition, levelDef.startingFuel);
        printMatchingGameSystems(ship);
        allEntities.add(ship);

        resetAllButInputSystems();


        updateBoundarySystem(levelDef.levelLines);
    }

    private void updateBoundarySystem(Array<LineSegment> levelLines) {
        float minX = Float.POSITIVE_INFINITY;
        float maxX = Float.NEGATIVE_INFINITY;

        float minY = Float.POSITIVE_INFINITY;
        float maxY = Float.NEGATIVE_INFINITY;

        for (LineSegment line : levelLines) {
            minX = Math.min(minX, line.startPoint.x);
            maxX = Math.max(maxX, line.startPoint.x);
            minX = Math.min(minX, line.endPoint.x);
            maxX = Math.max(maxX, line.endPoint.x);

            minY = Math.min(minY, line.startPoint.y);
            maxY = Math.max(maxY, line.startPoint.y);
            minY = Math.min(minY, line.endPoint.y);
            maxY = Math.max(maxY, line.endPoint.y);
        }

        float largestDimension = Math.max(Math.abs(maxY - minY), Math.abs(maxX - minX));
        // measurement is from center of level
        largestDimension /= 2;

        playerBoundarySystem.setKillRadius(new Vector2((minX + maxX) / 2, (minY + maxY) / 2), largestDimension);
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
        deltaRemainder += delta;
        while (deltaRemainder > DELTA_STEP) {
            deltaRemainder -= DELTA_STEP;
            for (GameSystem system : gameSystems) {
                system.act(allEntities, delta);
            }
            scaleCamBuffer();
            gameCam.update(delta);
        }
    }

    private void scaleCamBuffer() {
        gameCam.buffer = Math.max(BASE_CAM_BUFFER * gameCam.zoom, BASE_CAM_BUFFER);
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
