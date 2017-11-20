package com.bitdecay.helm.tutorial;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.bitdecay.helm.GameEntity;
import com.bitdecay.helm.Helm;
import com.bitdecay.helm.component.DelayedAddComponent;
import com.bitdecay.helm.component.ShipLaunchComponent;
import com.bitdecay.helm.menu.RotatingLabel;
import com.bitdecay.helm.persist.JsonUtils;
import com.bitdecay.helm.screen.LevelPlayer;
import com.bitdecay.helm.system.PlayerStartLevelSystem;
import com.bitdecay.helm.ui.UpdatingContainer;
import com.bitdecay.helm.world.LevelDefinition;

/**
 * Created by Monday on 10/30/2017.
 */

public class LandingInfoPhase extends PagedPhase {
    private Helm game;
    private LevelPlayer player;

    @Override
    public void start(Helm game, LevelPlayer player, Stage stage) {
        this.game = game;
        this.player = player;

        LevelDefinition tutorial1 = JsonUtils.unmarshal(LevelDefinition.class, Gdx.files.internal("level/tutorial/tut2.json"));
        player.loadLevel(tutorial1);

        // get ship
        GameEntity ship = TutorialUtils.getShip(player.allEntities);

        // add components
        ship.removeComponent(ShipLaunchComponent.class);
        DelayedAddComponent delays = PlayerStartLevelSystem.addPlayerStartComponents(new DelayedAddComponent());
        for (DelayedAddComponent.DelayedAdd delay : delays.delays) {
            ship.addComponent(delay.component);
        }

        init(stage);
        makePages();
    }

    private void makePages() {
        RotatingLabel phaseLabel = new RotatingLabel("Phase 2: Landing", game.fontScale * 2, game.skin);
        phaseLabel.setOrigin(Align.center);
        UpdatingContainer phasePage = new UpdatingContainer(phaseLabel);
        phasePage.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        pages.add(phasePage);

        final Vector2 landingLocation = TutorialUtils.getLandingLocation(player.allEntities);
        RotatingLabel boostLabel1 = new RotatingLabel("This is the", game.fontScale, game.skin);
        boostLabel1.setOrigin(Align.center);
        RotatingLabel boostLabel2 = new RotatingLabel("landing platform", game.fontScale, game.skin);
        boostLabel2.setOrigin(Align.center);

        Table landingTable1 = new Table();
        landingTable1.setTouchable(Touchable.disabled);
        landingTable1.align(Align.left);
        landingTable1.add(boostLabel1).center();
        landingTable1.row();
        landingTable1.add(boostLabel2).center();

        final UpdatingContainer landingLocationPage = new UpdatingContainer(landingTable1);
        landingLocationPage.updater = new Runnable() {
            @Override
            public void run() {
                Vector3 project = player.gameCam.project(new Vector3(landingLocation.x, landingLocation.y, 0));
                landingLocationPage.setPosition(project.x, project.y - landingLocationPage.getPrefHeight() / 2);
            }
        };
        landingLocationPage.updater.run();
        pages.add(landingLocationPage);

        RotatingLabel missionLabel1 = new RotatingLabel("Your goal is to get", game.fontScale, game.skin);
        missionLabel1.setOrigin(Align.center);
        RotatingLabel missionLabel2 = new RotatingLabel("the ship here safely", game.fontScale, game.skin);
        missionLabel2.setOrigin(Align.center);

        Table missionTable = new Table();
        missionTable.setTouchable(Touchable.disabled);
        missionTable.align(Align.left);
        missionTable.add(missionLabel1).center();
        missionTable.row();
        missionTable.add(missionLabel2).center();

        final UpdatingContainer missionPage = new UpdatingContainer(missionTable);
        missionPage.updater = new Runnable() {
            @Override
            public void run() {
                Vector3 project = player.gameCam.project(new Vector3(landingLocation.x, landingLocation.y, 0));
                missionPage.setPosition(project.x, project.y - missionPage.getPrefHeight() / 2);
            }
        };
        missionPage.updater.run();
        pages.add(missionPage);

        RotatingLabel boostLabel3 = new RotatingLabel("You must land upright and", game.fontScale, game.skin);
        boostLabel3.setOrigin(Align.center);
        RotatingLabel boostLabel4 = new RotatingLabel("slowly to avoid crashing", game.fontScale, game.skin);
        boostLabel4.setOrigin(Align.center);

        Table landingTable2 = new Table();
        landingTable2.setTouchable(Touchable.disabled);
        landingTable2.align(Align.left);
        landingTable2.add(boostLabel3).center();
        landingTable2.row();
        landingTable2.add(boostLabel4).center();

        final UpdatingContainer page2 = new UpdatingContainer(landingTable2);
        page2.updater = new Runnable() {
            @Override
            public void run() {
                Vector3 project = player.gameCam.project(new Vector3(landingLocation.x, landingLocation.y, 0));
                page2.setPosition(project.x, project.y - page2.getPrefHeight() / 2);
            }
        };
        page2.updater.run();
        pages.add(page2);

        nextPage();
    }

    @Override
    public boolean update(ShapeRenderer shaper) {
        return currentPage >= pages.size;
    }
}
