package com.bitdecay.helm.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.bitdecay.helm.Helm;
import com.bitdecay.helm.camera.FollowOrthoCamera;
import com.bitdecay.helm.component.ReplayActiveComponent;
import com.bitdecay.helm.entities.FocusPointEntity;
import com.bitdecay.helm.entities.LandingPlatformEntity;
import com.bitdecay.helm.input.InputRecord;
import com.bitdecay.helm.input.InputReplay;
import com.bitdecay.helm.system.collision.WormholeCollisionHandlerSystem;
import com.bitdecay.helm.system.movement.GravityApplicationSystem;
import com.bitdecay.helm.system.camera.CameraUpdateSystem;
import com.bitdecay.helm.system.collision.CollisionAlignmentSystem;
import com.bitdecay.helm.system.collision.CrashSystem;
import com.bitdecay.helm.system.util.DelayedAddSystem;
import com.bitdecay.helm.system.GameSystem;
import com.bitdecay.helm.system.collision.PlayerCollisionHandlerSystem;
import com.bitdecay.helm.system.input.ReplayInputSystem;
import com.bitdecay.helm.system.render.RenderBoostSystem;
import com.bitdecay.helm.system.render.RenderGravityWellSystem;
import com.bitdecay.helm.world.WormholePair;

import java.util.List;

/**
 * Created by Monday on 12/18/2016.
 */
public class LevelPlayer {

    public static final float DELTA_STEP = 1/60f;
    float deltaRemainder;

    int tick = 0;
    InputReplay recordedInput = new InputReplay();
    boolean resetQueued = false;
    boolean captureActive = false;

    float recordingAngle = Float.NEGATIVE_INFINITY;
    boolean lastRecordedBoost = false;
    boolean boostToggled = lastRecordedBoost;

    private static final int BASE_CAM_BUFFER = 500;
    FollowOrthoCamera gameCam;

    private com.bitdecay.helm.GamePilot pilot;
    private boolean isReplay;

    OrthographicCamera screenCam;

    ShapeRenderer shapeRenderer;

    Array<GameSystem> gameSystems = new Array<>(1);

    Array<GameSystem> inputSystems = new Array<>(1);

    Array<GameSystem> gameRenderSystems = new Array<>(1);

    Array<GameSystem> screenRenderSystems = new Array<>(1);


    Array<com.bitdecay.helm.GameEntity> allEntities = new Array<>(1000);

    Array<com.bitdecay.helm.GameEntity> pendingAdds = new Array<>(100);
    Array<com.bitdecay.helm.GameEntity> pendingRemoves = new Array<>(100);

    private final InputMultiplexer inputMux;
    private com.bitdecay.helm.system.movement.PlayerBoundarySystem playerBoundarySystem;

    public Vector2 universalGravity = new Vector2();


    public LevelPlayer(com.bitdecay.helm.GamePilot pilot, boolean isReplay) {
        this.pilot = pilot;
        this.isReplay = isReplay;

        screenCam = new OrthographicCamera(1920, 1080);
        screenCam.translate(screenCam.viewportWidth / 2, screenCam.viewportHeight / 2);
        screenCam.update();

        float screenRatio = 1.0f * Gdx.graphics.getWidth() / Gdx.graphics.getHeight();

        gameCam = new FollowOrthoCamera(1080 * screenRatio, 1080);
        gameCam.minZoom = 10;
        gameCam.maxZoom = .2f;
        gameCam.buffer = BASE_CAM_BUFFER;
        gameCam.snapSpeed = .1f;

        shapeRenderer = new ShapeRenderer();

        inputMux = new InputMultiplexer();

        initSystems();
    }

    private void initSystems() {
        List<com.bitdecay.helm.system.input.AbstractInputSystem> inputSystems = com.bitdecay.helm.system.input.InputSystemFactory.getInputSystems(pilot);
        for (com.bitdecay.helm.system.input.AbstractInputSystem inputSystem : inputSystems) {
            this.inputSystems.add(inputSystem);
            inputMux.addProcessor(inputSystem);
            addGameplaySystem(inputSystem);
        }

        com.bitdecay.helm.system.movement.BoostSystem boostSystem = new com.bitdecay.helm.system.movement.BoostSystem(pilot);

        com.bitdecay.helm.system.movement.SteeringSystem steeringSystem = new com.bitdecay.helm.system.movement.SteeringSystem(pilot);

        com.bitdecay.helm.system.PlayerStartLevelSystem startSystem = new com.bitdecay.helm.system.PlayerStartLevelSystem(pilot);
        inputMux.addProcessor(startSystem);

        com.bitdecay.helm.system.movement.GravityFinderSystem gravityFinderSystem = new com.bitdecay.helm.system.movement.GravityFinderSystem(pilot);
        GravityApplicationSystem gravityApplicationSystem = new GravityApplicationSystem(pilot);

        com.bitdecay.helm.system.movement.MovementSystem movementSystem = new com.bitdecay.helm.system.movement.MovementSystem(pilot);

        CameraUpdateSystem cameraSystem = new CameraUpdateSystem(pilot, gameCam);

        CollisionAlignmentSystem collisionAlignmentSystem = new CollisionAlignmentSystem(pilot);
        com.bitdecay.helm.system.collision.CollisionSystem collisionSystem = new com.bitdecay.helm.system.collision.CollisionSystem(pilot);
        PlayerCollisionHandlerSystem playerCollisionSystem = new PlayerCollisionHandlerSystem(pilot);
        WormholeCollisionHandlerSystem wormholeCollisionHandlerSystem = new WormholeCollisionHandlerSystem(pilot);

        com.bitdecay.helm.system.util.ProximityRemovalSystem proximityRemovalSystem = new com.bitdecay.helm.system.util.ProximityRemovalSystem(pilot);
        com.bitdecay.helm.system.camera.ProximityFocusSystem cameraProximitySystem = new com.bitdecay.helm.system.camera.ProximityFocusSystem(pilot);

        DelayedAddSystem delaySystem = new DelayedAddSystem(pilot);
        com.bitdecay.helm.system.util.RemoveComponentSystem removeComponentSystem = new com.bitdecay.helm.system.util.RemoveComponentSystem(pilot);

        com.bitdecay.helm.system.collision.LandingSystem landingSystem = new com.bitdecay.helm.system.collision.LandingSystem(pilot);

        playerBoundarySystem = new com.bitdecay.helm.system.movement.PlayerBoundarySystem(pilot);

        CrashSystem crashSystem = new CrashSystem(pilot);

        com.bitdecay.helm.system.ResolvePlayerExplosionSystem resolveExplosionSystem = new com.bitdecay.helm.system.ResolvePlayerExplosionSystem(pilot);

        com.bitdecay.helm.system.util.TimerSystem timerSystem = new com.bitdecay.helm.system.util.TimerSystem(pilot);

        ReplayInputSystem replayInputSystem = new ReplayInputSystem(pilot);

        addGameplaySystem(cameraSystem);

        addGameplaySystem(replayInputSystem);
        addGameplaySystem(boostSystem);
        addGameplaySystem(steeringSystem);

        addGameplaySystem(startSystem);
        addGameplaySystem(gravityFinderSystem);
        addGameplaySystem(gravityApplicationSystem);
        addGameplaySystem(movementSystem);
        addGameplaySystem(collisionAlignmentSystem);
        addGameplaySystem(collisionSystem);
        addGameplaySystem(playerCollisionSystem);
        addGameplaySystem(wormholeCollisionHandlerSystem);

        addGameplaySystem(proximityRemovalSystem);
        addGameplaySystem(cameraProximitySystem);
        addGameplaySystem(delaySystem);
        addGameplaySystem(removeComponentSystem);
        addGameplaySystem(playerBoundarySystem);
        addGameplaySystem(crashSystem);
        addGameplaySystem(landingSystem);
        addGameplaySystem(resolveExplosionSystem);
        addGameplaySystem(timerSystem);

        com.bitdecay.helm.system.render.RenderBodySystem renderBodySystem = new com.bitdecay.helm.system.render.RenderBodySystem(pilot, shapeRenderer);
        RenderBoostSystem renderBoostSystem = new RenderBoostSystem(pilot, shapeRenderer);
        com.bitdecay.helm.system.render.RenderFuelSystem renderFuelSystem = new com.bitdecay.helm.system.render.RenderFuelSystem(pilot, shapeRenderer);
        com.bitdecay.helm.system.render.RenderExplosionSystem renderExplosionSystem = new com.bitdecay.helm.system.render.RenderExplosionSystem(pilot, shapeRenderer);
        RenderGravityWellSystem renderGravityWellSystem = new RenderGravityWellSystem(pilot, shapeRenderer);
        com.bitdecay.helm.system.render.RenderWormholeSystem renderWormholeSystem = new com.bitdecay.helm.system.render.RenderWormholeSystem(pilot, shapeRenderer);
        com.bitdecay.helm.system.render.RenderSpeedFlamesSystem renderFlamesSystem = new com.bitdecay.helm.system.render.RenderSpeedFlamesSystem(pilot, shapeRenderer);
        com.bitdecay.helm.system.render.LandingHintSystem landingHintSystem = new com.bitdecay.helm.system.render.LandingHintSystem(pilot, shapeRenderer);

        gameRenderSystems.add(renderBoostSystem);
        gameRenderSystems.add(renderBodySystem);
        gameRenderSystems.add(renderFuelSystem);
        gameRenderSystems.add(renderExplosionSystem);
        gameRenderSystems.add(renderGravityWellSystem);
        gameRenderSystems.add(renderWormholeSystem);
//        gameRenderSystems.add(renderFlamesSystem);
        gameRenderSystems.add(landingHintSystem);

        com.bitdecay.helm.system.render.RenderSteeringSystem renderSteeringSystem = new com.bitdecay.helm.system.render.RenderSteeringSystem(pilot, screenCam, shapeRenderer);
        screenRenderSystems.add(renderSteeringSystem);

        if (pilot.isDebug()) {
            com.bitdecay.helm.system.render.DebugFocusPointSystem debugFocusPointSystem = new com.bitdecay.helm.system.render.DebugFocusPointSystem(pilot, shapeRenderer);
            gameRenderSystems.add(debugFocusPointSystem);
        }
    }

    private void addGameplaySystem(GameSystem system) {
        system.setLevelPlayer(this);
        gameSystems.add(system);
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

    public void loadLevel(com.bitdecay.helm.world.LevelDefinition levelDef) {
        recordedInput.levelDef = levelDef;

        resetLevel(levelDef);

        com.bitdecay.helm.entities.ShipEntity ship = new com.bitdecay.helm.entities.ShipEntity(levelDef.startPosition, levelDef.startingFuel);
        ship.addComponent(new com.bitdecay.helm.component.PlayerActiveComponent());
        printMatchingGameSystems(ship);
        allEntities.add(ship);
    }

    public void loadReplay(InputReplay replay) {
        resetLevel(replay.levelDef);

        com.bitdecay.helm.entities.ShipEntity ship = new com.bitdecay.helm.entities.ShipEntity(replay.levelDef.startPosition, replay.levelDef.startingFuel);
        ship.addComponent(new ReplayActiveComponent(replay));
        printMatchingGameSystems(ship);
        allEntities.add(ship);
    }

    protected void resetLevel(com.bitdecay.helm.world.LevelDefinition levelDef) {
        resetQueued = true;

        allEntities.clear();

        for (com.bitdecay.helm.world.LineSegment line : levelDef.levelLines) {
            allEntities.add(new com.bitdecay.helm.entities.LineSegmentEntity(line));
        }

        if (levelDef.finishPlatform.area() > 0) {
            LandingPlatformEntity plat = new LandingPlatformEntity(levelDef.finishPlatform, levelDef.finishPlatformRotation * MathUtils.degreesToRadians);
            allEntities.add(plat);
        }

        for (Circle well: levelDef.gravityWells) {
            com.bitdecay.helm.entities.GravityWellEntity gravityWell = new com.bitdecay.helm.entities.GravityWellEntity(well, false);
            allEntities.add(gravityWell);
        }

        for (Circle field: levelDef.repulsionFields) {
            com.bitdecay.helm.entities.GravityWellEntity gravityWell = new com.bitdecay.helm.entities.GravityWellEntity(field, true);
            allEntities.add(gravityWell);
        }

        for (WormholePair pair: levelDef.wormholes) {
            com.bitdecay.helm.entities.WormholeEntity wormhole = new com.bitdecay.helm.entities.WormholeEntity(pair);
            allEntities.add(wormhole);
        }

        for (Circle focus: levelDef.focusPoints) {
            FocusPointEntity focusPoint = new FocusPointEntity(focus);
            allEntities.add(focusPoint);
        }

        universalGravity.set(levelDef.gravity);

        resetAllButInputSystems();

        updateBoundarySystem(levelDef.levelLines);
    }

    private void updateBoundarySystem(Array<com.bitdecay.helm.world.LineSegment> levelLines) {
        float minX = Float.POSITIVE_INFINITY;
        float maxX = Float.NEGATIVE_INFINITY;

        float minY = Float.POSITIVE_INFINITY;
        float maxY = Float.NEGATIVE_INFINITY;

        for (com.bitdecay.helm.world.LineSegment line : levelLines) {
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

    public void printMatchingGameSystems(com.bitdecay.helm.GameEntity entity) {
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
            tick(DELTA_STEP);
            deltaRemainder -= DELTA_STEP;
        }
    }

    protected void tick(float delta) {
        handleTickCount();

        for (GameSystem system : gameSystems) {
            system.act(allEntities, delta);
        }
        scaleCamBuffer();
        gameCam.update(delta);

        allEntities.addAll(pendingAdds);
        allEntities.removeAll(pendingRemoves, true);

        pendingAdds.clear();
        pendingRemoves.clear();

        maybeRecordInput();
    }

    private void handleTickCount() {
        if (resetQueued) {
            tick = 0;
            recordedInput.reset();
            resetQueued = false;
        }
        if (captureActive) {
            tick++;
        }
    }

    private void maybeRecordInput() {
        if (isReplay) {
            // don't need to record when we are playing a replay file
            return;
        }

        if (recordingAngle != Float.NEGATIVE_INFINITY || boostToggled) {
            InputRecord newRecord = new InputRecord(tick);
            if (recordingAngle != Float.NEGATIVE_INFINITY) {
                newRecord.angle = recordingAngle;
                if (Helm.debug) {
                    System.out.println("TICK " + tick + " New angle '" + recordingAngle + "'");
                }
                recordingAngle = Float.NEGATIVE_INFINITY;
            }
            if (boostToggled) {
                newRecord.boostToggled = true;
                if (Helm.debug) {
                    System.out.println("TICK " + tick + " Boost Toggled");
                }
                boostToggled = false;
            }
            recordedInput.inputRecords.add(newRecord);
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

    public void addEntity(com.bitdecay.helm.GameEntity entity) {
        pendingAdds.add(entity);
    }

    public void removeEntity(com.bitdecay.helm.GameEntity entity) {
        pendingRemoves.add(entity);
    }

    public void recordNewAngle(float angle) {
        recordingAngle = angle;
    }

    public void recordNewBoostToggle() {
        boostToggled = true;
    }

    public void beginInputReplayCapture() {
        resetQueued = true;
        captureActive = true;
    }

    public void stopReplayCapture() {
        captureActive = false;
    }

    public int getTick() {
        return tick;
    }

    public void countStat(com.bitdecay.helm.unlock.StatName statName, int amount) {
        if (!isReplay) {
            Helm.stats.count(statName, amount);
        }
    }

    public void rollStat(com.bitdecay.helm.unlock.StatName statName, float amount) {
        if (!isReplay) {
            Helm.stats.roll(statName, amount);
        }
    }
}
