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
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.bitdecay.game.component.BoostActivateButton;
import com.bitdecay.game.entities.ShipEntity;
import com.bitdecay.game.system.GameSystem;
import com.bitdecay.game.system.BoosterActivationSystem;
import com.bitdecay.game.system.GravitySystem;
import com.bitdecay.game.system.MovementSystem;
import com.bitdecay.game.system.RenderBodySystem;
import com.bitdecay.game.system.SteeringSystem;

public class Helm extends ApplicationAdapter {

    OrthographicCamera cam;

	SpriteBatch batch;
    ShapeRenderer shapeRenderer;
	Texture img;

	Array<GameSystem> allSystems = new Array<>(1);

	Array<GameEntity> allEntities = new Array<>(1000);

	@Override
	public void create () {
        cam = new OrthographicCamera(1920, 1080);
		batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
		img = new Texture("badlogic.jpg");

        InputMultiplexer inputMux = new InputMultiplexer();
        Gdx.input.setInputProcessor(inputMux);

        BoosterActivationSystem boostSystem = new BoosterActivationSystem();
        inputMux.addProcessor(boostSystem);

        GravitySystem gravitySystem = new GravitySystem();

        SteeringSystem steeringSystem = new SteeringSystem();
        inputMux.addProcessor(steeringSystem);

        MovementSystem movementSystem = new MovementSystem();

        RenderBodySystem renderBodySystem = new RenderBodySystem(shapeRenderer);

        allSystems.add(boostSystem);
        allSystems.add(gravitySystem);
        allSystems.add(steeringSystem);
        allSystems.add(renderBodySystem);
        allSystems.add(movementSystem);


        ShipEntity ship = new ShipEntity();
        printMatchingSystems(ship);
        allEntities.add(ship);
	}

	@Override
	public void render () {
        float delta = Gdx.graphics.getDeltaTime();

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//        batch.begin();
//        batch.draw(img, 0, 0);
//        batch.end();

        cam.update();

        shapeRenderer.setProjectionMatrix(cam.combined);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.circle(0, 0, 100);
        shapeRenderer.end();

        for (GameSystem system : allSystems) {
            system.act(allEntities, delta);
        }

    }

	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}

    private void printMatchingSystems(GameEntity entity) {
        for (GameSystem system : allSystems) {
            if (system.canActOn(entity)) {
                System.out.println(system.getClass().getSimpleName());
            }
        }

    }
}
