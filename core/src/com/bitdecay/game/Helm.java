package com.bitdecay.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.bitdecay.game.camera.FollowOrthoCamera;
import com.bitdecay.game.component.BoostActivateButton;
import com.bitdecay.game.entities.LineSegmentEntity;
import com.bitdecay.game.entities.ShipEntity;
import com.bitdecay.game.system.CameraUpdateSystem;
import com.bitdecay.game.system.CollisionPositionAlignmentSystem;
import com.bitdecay.game.system.CollisionRotationAlignmentSystem;
import com.bitdecay.game.system.CollisionSystem;
import com.bitdecay.game.system.GameSystem;
import com.bitdecay.game.system.BoosterActivationSystem;
import com.bitdecay.game.system.GravitySystem;
import com.bitdecay.game.system.MovementSystem;
import com.bitdecay.game.system.RenderBodySystem;
import com.bitdecay.game.system.SteeringSystem;
import com.bitdecay.game.world.LevelDefinition;
import com.bitdecay.game.world.LineSegment;

public class Helm extends ApplicationAdapter {

    FollowOrthoCamera cam;

    SpriteBatch batch;
    ShapeRenderer shapeRenderer;

    Array<GameSystem> allSystems = new Array<>(1);

    Array<GameEntity> allEntities = new Array<>(1000);

    @Override
    public void create() {
        cam = new FollowOrthoCamera(1920, 1080);
        cam.minZoom = 3;
        cam.maxZoom = .5f;
        cam.buffer = 500;

        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        InputMultiplexer inputMux = new InputMultiplexer();
        Gdx.input.setInputProcessor(inputMux);

        BoosterActivationSystem boostSystem = new BoosterActivationSystem();
        inputMux.addProcessor(boostSystem);

        GravitySystem gravitySystem = new GravitySystem();

        SteeringSystem steeringSystem = new SteeringSystem();
        inputMux.addProcessor(steeringSystem);

        MovementSystem movementSystem = new MovementSystem();

        RenderBodySystem renderBodySystem = new RenderBodySystem(shapeRenderer);

        CameraUpdateSystem cameraSystem = new CameraUpdateSystem(cam);

        CollisionPositionAlignmentSystem collisionPositionSystem = new CollisionPositionAlignmentSystem();
        CollisionRotationAlignmentSystem collisionRotationSystem = new CollisionRotationAlignmentSystem();
        CollisionSystem collisionSystem = new CollisionSystem();

        allSystems.add(cameraSystem);
        allSystems.add(boostSystem);
        allSystems.add(gravitySystem);
        allSystems.add(steeringSystem);
        allSystems.add(movementSystem);
        allSystems.add(collisionPositionSystem);
        allSystems.add(collisionRotationSystem);
        allSystems.add(collisionSystem);
        allSystems.add(renderBodySystem);

        ShipEntity ship = new ShipEntity();
        printMatchingSystems(ship);
        allEntities.add(ship);

        LevelDefinition testLevel = getTestLevel();
        for (LineSegment line : testLevel.levelLines) {
            allEntities.add(new LineSegmentEntity(line));
        }
    }

    private LevelDefinition getTestLevel() {
        Array<LineSegment> testLines = new Array<>(10);
        testLines.add(new LineSegment(new Vector2(0, -400), new Vector2(100, -400)));

        return new LevelDefinition(testLines);
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();

        if (delta > .5f) {
            delta = .5f;
        }

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        cam.update(delta);

        shapeRenderer.setProjectionMatrix(cam.combined);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.circle(0, 0, 100);
        shapeRenderer.circle(1000, 800, 50);
        shapeRenderer.end();


        for (GameSystem system : allSystems) {
            system.act(allEntities, delta);
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose();
    }

    private void printMatchingSystems(GameEntity entity) {
        for (GameSystem system : allSystems) {
            if (system.canActOn(entity)) {
                System.out.println(system.getClass().getSimpleName());
            }
        }

    }
}
