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
import com.bitdecay.helm.component.TransformComponent;
import com.bitdecay.helm.component.control.BoostControlComponent;
import com.bitdecay.helm.entities.LandingPlatformEntity;
import com.bitdecay.helm.entities.ShipEntity;
import com.bitdecay.helm.persist.JsonUtils;
import com.bitdecay.helm.screen.LevelPlayer;
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

    private BoostControlComponent playerBoost;

    private Array<UpdatingContainer> pages;
    private int currentPage = -1;
    private ClickListener stageClickListener;

    @Override
    public void start(Helm game, LevelPlayer player, Stage stage) {
        this.game = game;
        this.player = player;
        this.stage = stage;
        pages = new Array<>();

        stageClickListener = new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                nextPage();
            }
        };

        LevelDefinition tutorial1 = JsonUtils.unmarshal(LevelDefinition.class, Gdx.files.internal("level/testCollisions.json"));
        player.loadLevel(tutorial1);

        stage.clear();

        findPlayerBoost();

        addPlayerInfo();
    }

    private void findPlayerBoost() {
        for (GameEntity entity : player.allEntities) {
            BoostControlComponent boostControl = entity.getComponent(BoostControlComponent.class);
            if (boostControl != null) {
                playerBoost = boostControl;
            }
        }
    }

    private void addPlayerInfo() {
        // we should create a 'tracking update container' that is given a reference to a TransformComponent
        // it should be updatable such that it can follow it properly

        final Vector2 playerLocation = getPlayerLocation();
        Label playerLabel = new Label("This is your ship!", game.skin);
        playerLabel.setFontScale(game.fontScale);
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
        Label landingLabel1 = new Label("This is where", game.skin);
        landingLabel1.setFontScale(game.fontScale);
        landingLabel1.setOrigin(Align.center);

        Label landingLabel2 = new Label("you need to get!", game.skin);
        landingLabel2.setFontScale(game.fontScale);
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
        for (GameEntity entity : player.allEntities) {
            if (entity instanceof ShipEntity) {
                return entity.getComponent(TransformComponent.class).position;
            }
        }
        return null;
    }

    private Vector2 getLandingLocation() {
        for (GameEntity entity : player.allEntities) {
            if (entity instanceof LandingPlatformEntity) {
                return entity.getComponent(TransformComponent.class).position;
            }
        }
        return null;
    }

    @Override
    public boolean update(ShapeRenderer shaper) {
        if (livePage != null) {
            livePage.update(shaper);
            return false;
        } else {
            return true;
        }
    }
}
