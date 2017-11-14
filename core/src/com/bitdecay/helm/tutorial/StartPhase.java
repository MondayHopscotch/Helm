package com.bitdecay.helm.tutorial;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.bitdecay.helm.GameEntity;
import com.bitdecay.helm.Helm;
import com.bitdecay.helm.component.TransformComponent;
import com.bitdecay.helm.menu.RotatingLabel;
import com.bitdecay.helm.screen.LevelPlayer;
import com.bitdecay.helm.ui.UpdatingContainer;

/**
 * Created by Monday on 11/7/2017.
 */

public class StartPhase extends PagedPhase {

    private Helm game;
    private LevelPlayer player;
    private GameEntity ship;

    @Override
    public void start(Helm game, final LevelPlayer player, Stage stage) {
        this.game = game;
        this.player = player;
        init(stage);

        ship = TutorialUtils.getShip(player.allEntities);

        RotatingLabel introLabel = new RotatingLabel("Welcome cadet!", game.fontScale, game.skin);
        introLabel.setOrigin(Align.center);
        UpdatingContainer welcomePage = new UpdatingContainer(introLabel);
        welcomePage.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        pages.add(welcomePage);

        RotatingLabel introLabel2 = new RotatingLabel("Let's quickly cover", game.fontScale, game.skin);
        introLabel2.setOrigin(Align.center);

        RotatingLabel introLabel3 = new RotatingLabel("the basics", game.fontScale, game.skin);
        introLabel3.setOrigin(Align.center);

        Table introTable = new Table();
        introTable.align(Align.center);
        introTable.setOrigin(Align.center);
        introTable.add(introLabel2).center();
        introTable.row();
        introTable.add(introLabel3).center();

        final UpdatingContainer introPage1 = new UpdatingContainer(introTable);
        introPage1.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        pages.add(introPage1);

        RotatingLabel phaseLabel = new RotatingLabel("Phase 1: Flight Control", game.fontScale * 2, game.skin);
        phaseLabel.setOrigin(Align.center);
        UpdatingContainer phasePage = new UpdatingContainer(phaseLabel);
        phasePage.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        pages.add(phasePage);

        final Vector2 playerLocation = getPlayerLocation();
        RotatingLabel playerLabel = new RotatingLabel("This is your ship!", game.fontScale, game.skin);
        playerLabel.setOrigin(Align.center);
        final UpdatingContainer page1 = new UpdatingContainer(playerLabel);
        page1.updater = new Runnable() {
            @Override
            public void run() {
                Vector3 project = player.gameCam.project(new Vector3(playerLocation.x, playerLocation.y, 0));
                page1.setPosition(project.x, project.y);
            }
        };
        page1.updater.run();
        pages.add(page1);

//        final Vector2 landingLocation = TutorialUtils.getLandingLocation(player.allEntities);
//        RotatingLabel landingLabel1 = new RotatingLabel("This is where", game.fontScale, game.skin);
//        landingLabel1.setOrigin(Align.center);
//
//        RotatingLabel landingLabel2 = new RotatingLabel("you need to get...", game.fontScale, game.skin);
//        landingLabel2.setOrigin(Align.center);
//
//        Table landingTable = new Table();
//        landingTable.align(Align.left);
//        landingTable.add(landingLabel1).center();
//        landingTable.row();
//        landingTable.add(landingLabel2).center();
//
//        final UpdatingContainer page2 = new UpdatingContainer(landingTable);
//        page2.updater = new Runnable() {
//            @Override
//            public void run() {
//                Vector3 project = player.gameCam.project(new Vector3(landingLocation.x, landingLocation.y, 0));
//                page2.setPosition(project.x, project.y);
//            }
//        };
//        page2.updater.run();
//        pages.add(page2);

//        RotatingLabel landingLabel3 = new RotatingLabel("We'll get back to", game.fontScale, game.skin);
//        landingLabel3.setOrigin(Align.center);
//
//        RotatingLabel landingLabel4 = new RotatingLabel("this in a bit.", game.fontScale, game.skin);
//        landingLabel4.setOrigin(Align.center);

//        Table landingTable2 = new Table();
//        landingTable2.align(Align.left);
//        landingTable2.add(landingLabel3).center();
//        landingTable2.row();
//        landingTable2.add(landingLabel4).center();

//        final UpdatingContainer page3 = new UpdatingContainer(landingTable2);
//        page3.updater = new Runnable() {
//            @Override
//            public void run() {
//                Vector3 project = player.gameCam.project(new Vector3(landingLocation.x, landingLocation.y, 0));
//                page3.setPosition(project.x, project.y);
//            }
//        };
//        page3.updater.run();
//        pages.add(page3);

        nextPage();
    }

    private Vector2 getPlayerLocation() {
        return ship.getComponent(TransformComponent.class).position;
    }

    @Override
    public boolean update(ShapeRenderer shaper) {
        if (currentPage >= pages.size) {
            return true;
        } else {
            return false;
        }
    }
}
