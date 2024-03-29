package com.bitdecay.helm.screen;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.bitdecay.helm.GameEntity;
import com.bitdecay.helm.GamePilot;
import com.bitdecay.helm.Helm;
import com.bitdecay.helm.camera.FollowOrthoCamera;
import com.bitdecay.helm.component.PlayerActiveComponent;
import com.bitdecay.helm.component.ReplayActiveComponent;
import com.bitdecay.helm.entities.FocusPointEntity;
import com.bitdecay.helm.entities.GravityWellEntity;
import com.bitdecay.helm.entities.LandingPlatformEntity;
import com.bitdecay.helm.entities.LineSegmentEntity;
import com.bitdecay.helm.entities.ShipEntity;
import com.bitdecay.helm.entities.WormholeEntity;
import com.bitdecay.helm.input.InputRecord;
import com.bitdecay.helm.input.InputReplay;
import com.bitdecay.helm.system.PlayerStartLevelSystem;
import com.bitdecay.helm.system.ResolvePlayerExplosionSystem;
import com.bitdecay.helm.system.camera.ProximityFocusSystem;
import com.bitdecay.helm.system.collision.CollisionSystem;
import com.bitdecay.helm.system.collision.LandingSystem;
import com.bitdecay.helm.system.collision.WormholeCollisionHandlerSystem;
import com.bitdecay.helm.system.input.AbstractInputSystem;
import com.bitdecay.helm.system.input.InputSystemFactory;
import com.bitdecay.helm.system.movement.BoostSystem;
import com.bitdecay.helm.system.movement.GravityApplicationSystem;
import com.bitdecay.helm.system.camera.CameraUpdateSystem;
import com.bitdecay.helm.system.collision.CollisionAlignmentSystem;
import com.bitdecay.helm.system.collision.CrashSystem;
import com.bitdecay.helm.system.movement.GravityFinderSystem;
import com.bitdecay.helm.system.movement.MovementSystem;
import com.bitdecay.helm.system.movement.PlayerBoundarySystem;
import com.bitdecay.helm.system.movement.SteeringSystem;
import com.bitdecay.helm.system.render.DebugFocusPointSystem;
import com.bitdecay.helm.system.render.IndicateStartReplaySystem;
import com.bitdecay.helm.system.render.LandingHintSystem;
import com.bitdecay.helm.system.render.RenderBodySystem;
import com.bitdecay.helm.system.render.RenderExplosionSystem;
import com.bitdecay.helm.system.render.RenderFuelSystem;
import com.bitdecay.helm.system.render.RenderWormholeSystem;
import com.bitdecay.helm.system.util.DelayedAddSystem;
import com.bitdecay.helm.system.GameSystem;
import com.bitdecay.helm.system.collision.PlayerCollisionHandlerSystem;
import com.bitdecay.helm.system.input.ReplayInputSystem;
import com.bitdecay.helm.system.render.RenderBoostSystem;
import com.bitdecay.helm.system.render.RenderGravityWellSystem;
import com.bitdecay.helm.system.util.ProximityRemovalSystem;
import com.bitdecay.helm.system.util.RemoveComponentSystem;
import com.bitdecay.helm.system.util.TimerSystem;
import com.bitdecay.helm.unlock.StatName;
import com.bitdecay.helm.world.LevelDefinition;
import com.bitdecay.helm.world.LineSegment;
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
    boolean tickActive = false;

    float recordingAngle = Float.NEGATIVE_INFINITY;
    boolean lastRecordedBoost = false;
    boolean boostToggled = lastRecordedBoost;

    private static final int BASE_CAM_BUFFER = 500;
    public final FollowOrthoCamera gameCam;

    private GamePilot pilot;
    private boolean isReplay;

    OrthographicCamera screenCam;

    ShapeRenderer shapeRenderer;

    Array<GameSystem> gameSystems = new Array<>(1);

    Array<GameSystem> inputSystems = new Array<>(1);

    Array<GameSystem> gameRenderSystems = new Array<>(1);

    Array<GameSystem> screenRenderSystems = new Array<>(1);


    public Array<GameEntity> allEntities = new Array<>(1000);

    Array<GameEntity> pendingAdds = new Array<>(100);
    Array<GameEntity> pendingRemoves = new Array<>(100);

    private final InputMultiplexer inputMux;
    private PlayerBoundarySystem playerBoundarySystem;

    public Vector2 universalGravity = new Vector2();
    public Vector2 originalGravity = new Vector2();


    public LevelPlayer(GamePilot pilot, boolean isReplay) {
        this.pilot = pilot;
        this.isReplay = isReplay;


        float camHeight = 1080;
        float camWidth = camHeight * Helm.aspectRatio;

        screenCam = new OrthographicCamera(camWidth, camHeight);
        screenCam.translate(screenCam.viewportWidth / 2, screenCam.viewportHeight / 2);
        screenCam.update();

        gameCam = new FollowOrthoCamera(camWidth, camHeight);
        gameCam.minZoom = 10;
        gameCam.maxZoom = .2f;
        gameCam.buffer = BASE_CAM_BUFFER;
        gameCam.snapSpeed = .1f;

        shapeRenderer = new ShapeRenderer();

        inputMux = new InputMultiplexer();

        initSystems();
    }

    private void initSystems() {
        List<AbstractInputSystem> inputSystems = InputSystemFactory.getInputSystems(pilot);
        for (AbstractInputSystem inputSystem : inputSystems) {
            this.inputSystems.add(inputSystem);
            inputMux.addProcessor(inputSystem);
            addGameplaySystem(inputSystem);
        }

        BoostSystem boostSystem = new BoostSystem(pilot);

        SteeringSystem steeringSystem = new SteeringSystem(pilot);

        PlayerStartLevelSystem startSystem = new PlayerStartLevelSystem(pilot);
        inputMux.addProcessor(startSystem);

        GravityFinderSystem gravityFinderSystem = new GravityFinderSystem(pilot);
        GravityApplicationSystem gravityApplicationSystem = new GravityApplicationSystem(pilot);

        MovementSystem movementSystem = new MovementSystem(pilot);

        CameraUpdateSystem cameraSystem = new CameraUpdateSystem(pilot, gameCam);

        CollisionAlignmentSystem collisionAlignmentSystem = new CollisionAlignmentSystem(pilot);
        CollisionSystem collisionSystem = new CollisionSystem(pilot);
        PlayerCollisionHandlerSystem playerCollisionSystem = new PlayerCollisionHandlerSystem(pilot);
        WormholeCollisionHandlerSystem wormholeCollisionHandlerSystem = new WormholeCollisionHandlerSystem(pilot);

        ProximityRemovalSystem proximityRemovalSystem = new ProximityRemovalSystem(pilot);
        ProximityFocusSystem cameraProximitySystem = new ProximityFocusSystem(pilot);

        DelayedAddSystem delaySystem = new DelayedAddSystem(pilot);
        RemoveComponentSystem removeComponentSystem = new RemoveComponentSystem(pilot);

        LandingSystem landingSystem = new LandingSystem(pilot);

        playerBoundarySystem = new PlayerBoundarySystem(pilot);

        CrashSystem crashSystem = new CrashSystem(pilot);

        ResolvePlayerExplosionSystem resolveExplosionSystem = new ResolvePlayerExplosionSystem(pilot);

        TimerSystem timerSystem = new TimerSystem(pilot);

        if (isReplay) {
            /*
             * We have to add the replay input system BEFORE the game systems so it can
             * properly configure the input for the coming world step
             */
            ReplayInputSystem replayInputSystem = new ReplayInputSystem(pilot);
            addGameplaySystem(replayInputSystem);
        }

        addGameplaySystem(cameraSystem);
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

        RenderBodySystem renderBodySystem = new RenderBodySystem(pilot, shapeRenderer);
        RenderBoostSystem renderBoostSystem = new RenderBoostSystem(pilot, shapeRenderer);
        RenderFuelSystem renderFuelSystem = new RenderFuelSystem(pilot, shapeRenderer);
        RenderExplosionSystem renderExplosionSystem = new RenderExplosionSystem(pilot, shapeRenderer);
        RenderGravityWellSystem renderGravityWellSystem = new RenderGravityWellSystem(pilot, shapeRenderer);
        RenderWormholeSystem renderWormholeSystem = new RenderWormholeSystem(pilot, shapeRenderer);
        LandingHintSystem landingHintSystem = new LandingHintSystem(pilot, shapeRenderer);

        gameRenderSystems.add(renderBoostSystem);
        gameRenderSystems.add(renderBodySystem);
        gameRenderSystems.add(renderFuelSystem);
        gameRenderSystems.add(renderExplosionSystem);
        gameRenderSystems.add(renderGravityWellSystem);
        gameRenderSystems.add(renderWormholeSystem);
        gameRenderSystems.add(landingHintSystem);


        if (isReplay) {
            IndicateStartReplaySystem startReplaySystem = new IndicateStartReplaySystem(pilot);
            gameRenderSystems.add(startReplaySystem);
        }

        if (Helm.debug) {
            DebugFocusPointSystem debugFocusPointSystem = new DebugFocusPointSystem(pilot, shapeRenderer);
            gameRenderSystems.add(debugFocusPointSystem);
        }
    }

    public <T extends GameSystem> T getSystem(Class<T> clazz) {
        for (GameSystem gameSystem : gameSystems) {
            if (clazz.isAssignableFrom(gameSystem.getClass())) {
                return clazz.cast(gameSystem);
            }
        }
        return null;
    }

    private void addGameplaySystem(GameSystem system) {
        system.setLevelPlayer(this);
        gameSystems.add(system);
    }

    public void resetAllButInputSystems() {
        for (GameSystem gameSystem : gameSystems) {
            if (!inputSystems.contains(gameSystem, true)) {
                gameSystem.reset();
            } else {
                if (gameSystem instanceof AbstractInputSystem) {
                    if (((AbstractInputSystem) gameSystem).shouldReset()) {
                        gameSystem.reset();
                    }
                }
            }
        }
    }

    public void resetInputSystems() {
        for (GameSystem system : inputSystems) {
            system.reset();
        }
    }

    public void loadLevel(LevelDefinition levelDef) {
        recordedInput.levelDef = levelDef;

        resetLevel(levelDef);

        ShipEntity ship = new ShipEntity(levelDef.startPosition, levelDef.startingFuel);
        ship.addComponent(new PlayerActiveComponent());
//        printMatchingGameSystems(ship);
        allEntities.add(ship);
    }

    public void loadReplay(InputReplay replay) {
        resetLevel(replay.levelDef);

        ShipEntity ship = new ShipEntity(replay.levelDef.startPosition, replay.levelDef.startingFuel);
        ship.addComponent(new ReplayActiveComponent(replay));
        allEntities.add(ship);
    }

    protected void resetLevel(LevelDefinition levelDef) {
        resetQueued = true;

        allEntities.clear();

        for (LineSegment line : levelDef.levelLines) {
            allEntities.add(new LineSegmentEntity(line));
        }

        if (levelDef.finishPlatform.area() > 0) {
            LandingPlatformEntity plat = new LandingPlatformEntity(levelDef.finishPlatform, levelDef.finishPlatformRotation * MathUtils.degreesToRadians);
            allEntities.add(plat);
        }

        for (Circle well: levelDef.gravityWells) {
            GravityWellEntity gravityWell = new GravityWellEntity(well, false);
            allEntities.add(gravityWell);
        }

        for (Circle field: levelDef.repulsionFields) {
            GravityWellEntity gravityWell = new GravityWellEntity(field, true);
            allEntities.add(gravityWell);
        }

        for (WormholePair pair: levelDef.wormholes) {
            WormholeEntity wormhole = new WormholeEntity(pair);
            allEntities.add(wormhole);
        }

        for (Circle focus: levelDef.focusPoints) {
            FocusPointEntity focusPoint = new FocusPointEntity(focus);
            allEntities.add(focusPoint);
        }

        originalGravity.set(levelDef.gravity);
        universalGravity.set(levelDef.gravity);

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
            tick(DELTA_STEP);
            deltaRemainder -= DELTA_STEP;
        }
    }

    protected void tick(float delta) {
        handleTickCount();
        for (int i = 0; i < gameSystems.size; i++) {
            gameSystems.get(i).act(allEntities, delta);
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
        if (tickActive) {
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

    public void addEntity(GameEntity entity) {
        pendingAdds.add(entity);
    }

    public void removeEntity(GameEntity entity) {
        pendingRemoves.add(entity);
    }

    public void recordNewAngle(float angle) {
        recordingAngle = angle;
    }

    public void recordNewBoostToggle() {
        boostToggled = true;
    }

    public void beginTickCapture() {
        resetQueued = true;
        tickActive = true;
    }

    public void stopTickCapture() {
        tickActive = false;
    }

    public int getTick() {
        return tick;
    }

    public void countStat(StatName statName, int amount) {
        if (!isReplay) {
            Helm.stats.count(statName, amount);
        }
    }

    public void rollStat(StatName statName, float amount) {
        if (!isReplay) {
            Helm.stats.roll(statName, amount);
        }
    }

    public void dispose() {
        for (GameSystem gameSystem : gameSystems) {
            gameSystem.dispose();
        }

        for (GameSystem gameRenderSystem : gameRenderSystems) {
            gameRenderSystem.dispose();
        }

    }
}
