package com.bitdecay.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.bitdecay.game.GameEntity;
import com.bitdecay.game.GamePilot;
import com.bitdecay.game.Helm;
import com.bitdecay.game.camera.FollowOrthoCamera;
import com.bitdecay.game.entities.LandingPlatformEntity;
import com.bitdecay.game.entities.LineSegmentEntity;
import com.bitdecay.game.entities.ShipEntity;
import com.bitdecay.game.system.ActiveSystem;
import com.bitdecay.game.system.BoostApplicationSystem;
import com.bitdecay.game.system.BoosterActivationSystem;
import com.bitdecay.game.system.CameraUpdateSystem;
import com.bitdecay.game.system.CollisionAlignmentSystem;
import com.bitdecay.game.system.CollisionSystem;
import com.bitdecay.game.system.GameSystem;
import com.bitdecay.game.system.GravitySystem;
import com.bitdecay.game.system.MovementSystem;
import com.bitdecay.game.system.PlayerCollisionHandlerSystem;
import com.bitdecay.game.system.PlayerStartLevelSystem;
import com.bitdecay.game.system.RenderBodySystem;
import com.bitdecay.game.system.SteeringSystem;
import com.bitdecay.game.world.LevelDefinition;
import com.bitdecay.game.world.LineSegment;

/**
 * Created by Monday on 12/15/2016.
 */
public class GameScreen implements Screen, GamePilot {
    private Helm game;
    FollowOrthoCamera cam;

    ShapeRenderer shapeRenderer;

    Array<GameSystem> allSystems = new Array<>(1);

    private final LevelDefinition currentLevel;
    Array<GameEntity> allEntities = new Array<>(1000);

    private boolean reloadQueued;

    public GameScreen(Helm game) {
        this.game = game;
        cam = new FollowOrthoCamera(1920, 1080);
        cam.minZoom = 3;
        cam.maxZoom = .5f;
        cam.buffer = 2000;

        shapeRenderer = new ShapeRenderer();

        InputMultiplexer inputMux = new InputMultiplexer();
        Gdx.input.setInputProcessor(inputMux);

        BoosterActivationSystem boostButtonSystem = new BoosterActivationSystem(this);
        inputMux.addProcessor(boostButtonSystem);

        BoostApplicationSystem boostApplicationSystem = new BoostApplicationSystem(this);

        PlayerStartLevelSystem startSystem = new PlayerStartLevelSystem(this);
        inputMux.addProcessor(startSystem);

        GravitySystem gravitySystem = new GravitySystem(this);

        SteeringSystem steeringSystem = new SteeringSystem(this);
        inputMux.addProcessor(steeringSystem);

        MovementSystem movementSystem = new MovementSystem(this);

        RenderBodySystem renderBodySystem = new RenderBodySystem(this, shapeRenderer);

        CameraUpdateSystem cameraSystem = new CameraUpdateSystem(this, cam);

        CollisionAlignmentSystem collisionAlignmentSystem = new CollisionAlignmentSystem(this);
        CollisionSystem collisionSystem = new CollisionSystem(this);
        PlayerCollisionHandlerSystem playerCollisionSystem = new PlayerCollisionHandlerSystem(this);

        ActiveSystem activeSystem = new ActiveSystem(this);

        allSystems.add(cameraSystem);
        allSystems.add(boostButtonSystem);
        allSystems.add(boostApplicationSystem);
        allSystems.add(startSystem);
        allSystems.add(gravitySystem);
        allSystems.add(steeringSystem);
        allSystems.add(movementSystem);
        allSystems.add(collisionAlignmentSystem);
        allSystems.add(collisionSystem);
        allSystems.add(playerCollisionSystem);
        allSystems.add(renderBodySystem);
        allSystems.add(activeSystem);

        currentLevel = getTestLevel();
        requestRestartLevel();
    }

    private void setLevel(LevelDefinition level) {
        allEntities.clear();

        for (LineSegment line : level.levelLines) {
            allEntities.add(new LineSegmentEntity(line));
        }

        if (level.finishPlatform.area() > 0) {
            LandingPlatformEntity plat = new LandingPlatformEntity(level.finishPlatform);
            printMatchingSystems(plat);
            allEntities.add(plat);
        }

        ShipEntity ship = new ShipEntity(level.startPosition);
        printMatchingSystems(ship);
        allEntities.add(ship);
    }

    @Override
    public void show() {

    }

    private LevelDefinition getTestLevel() {
        LevelDefinition testLevel = new LevelDefinition();


        Array<LineSegment> testLines = new Array<>(10);
        testLines.add(new LineSegment(new Vector2(-800, 300), new Vector2(0, -400)));
        testLines.add(new LineSegment(new Vector2(0, -400), new Vector2(100, -400)));
        testLines.add(new LineSegment(new Vector2(100, -400), new Vector2(300, -600)));
        testLevel.finishPlatform.set(new Rectangle(300, -650, 150, 50));
        testLines.add(new LineSegment(new Vector2(450, -600), new Vector2(800, -300)));
        testLines.add(new LineSegment(new Vector2(800, -300), new Vector2(1000, 300)));


        testLevel.levelLines = testLines;

        testLevel.startPosition =  Vector2.Zero;


        return testLevel;
    }

    private void printMatchingSystems(GameEntity entity) {
        System.out.println("Entity " + entity.getClass().getSimpleName() + " matches the following systems:");
        for (GameSystem system : allSystems) {
            if (system.canActOn(entity)) {
                System.out.println(system.getClass().getSimpleName());
            }
        }

    }

    @Override
    public void requestRestartLevel() {
        reloadQueued = true;
        setLevel(currentLevel);
    }

    @Override
    public void render(float delta) {
        if (delta > .5f) {
            delta = .5f;
        }
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        cam.update(delta);

        shapeRenderer.setProjectionMatrix(cam.combined);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.circle(0, 0, 100);
        shapeRenderer.end();


        for (GameSystem system : allSystems) {
            system.act(allEntities, delta);
        }

        if (reloadQueued) {
            setLevel(getTestLevel());
            reloadQueued = false;
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }
}
