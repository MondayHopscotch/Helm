package com.bitdecay.helm.tutorial;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.bitdecay.helm.GameEntity;
import com.bitdecay.helm.Helm;
import com.bitdecay.helm.component.TransformComponent;
import com.bitdecay.helm.entities.ShipEntity;
import com.bitdecay.helm.persist.JsonUtils;
import com.bitdecay.helm.screen.LevelPlayer;
import com.bitdecay.helm.world.LevelDefinition;

/**
 * Created by Monday on 10/30/2017.
 */

public class LaunchPhase implements TutorialPhase {
    private Helm game;
    private LevelPlayer player;
    private Stage stage;
    private Container container;

    @Override
    public void start(Helm game, LevelPlayer player, Stage stage) {
        this.game = game;
        this.player = player;
        this.stage = stage;

        LevelDefinition tutorial1 = JsonUtils.unmarshal(LevelDefinition.class, Gdx.files.internal("level/testCollisions.json"));
        player.loadLevel(tutorial1);

        stage.clear();

        addPlayerInfo();
    }

    private void addPlayerInfo() {
        Vector2 playerLocation = getPlayerLocation();
        Vector3 unproject = player.gameCam.unproject(new Vector3(playerLocation.x, playerLocation.y, 0));
        Label playerLabel = new Label("This is your ship!", game.skin);
        playerLabel.setFontScale(game.fontScale);

        // we should create a 'tracking update container' that is given a reference to a TransformComponent
        // it should be updatable such that it can follow it properly
        container = new Container(playerLabel);
        container.setPosition(unproject.x, unproject.y);

        stage.addActor(container);
        stage.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                System.out.println("CLICKED!");
                stage.clear();
            }
        });
    }

    private Vector2 getPlayerLocation() {
        for (GameEntity entity : player.allEntities) {
            if (entity instanceof ShipEntity) {
                return entity.getComponent(TransformComponent.class).position;
            }
        }
        return null;
    }

    @Override
    public boolean update(ShapeRenderer shaper) {
        Vector2 playerLocation = getPlayerLocation();
        Vector3 project = player.gameCam.project(new Vector3(playerLocation.x, playerLocation.y, 0));
        container.setPosition(project.x, project.y);
        return false;
    }
}
