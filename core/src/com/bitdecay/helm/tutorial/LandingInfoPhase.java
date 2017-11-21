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
import com.bitdecay.helm.component.VelocityComponent;
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

        TutorialUtils.preLaunchShip(ship);
        ship.removeComponent(VelocityComponent.class);

        init(stage);
        makePages();
    }

    private void makePages() {
        UpdatingContainer phasePage = TutorialUtils.getPage(game.fontScale * 2, game.skin, "Phase 2: Landing");
        phasePage.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        pages.add(phasePage);

        final Vector2 landingLocation = TutorialUtils.getLandingLocation(player.allEntities);
        final UpdatingContainer landingLocationPage = TutorialUtils.getPage(game.fontScale, game.skin,
                "This is the",
                "landing platform"
        );
        landingLocationPage.updater = new Runnable() {
            @Override
            public void run() {
                Vector3 project = player.gameCam.project(new Vector3(landingLocation.x, landingLocation.y, 0));
                landingLocationPage.setPosition(project.x, project.y - landingLocationPage.getPrefHeight() / 2);
            }
        };
        landingLocationPage.updater.run();
        pages.add(landingLocationPage);

        final UpdatingContainer missionPage = TutorialUtils.getPage(game.fontScale, game.skin,
                "Your mission is to get",
                "the ship here safely"
        );
        missionPage.updater = new Runnable() {
            @Override
            public void run() {
                Vector3 project = player.gameCam.project(new Vector3(landingLocation.x, landingLocation.y, 0));
                missionPage.setPosition(project.x, project.y - missionPage.getPrefHeight() / 2);
            }
        };
        missionPage.updater.run();
        pages.add(missionPage);

        final UpdatingContainer page2 = TutorialUtils.getPage(game.fontScale, game.skin,
                "You must land upright and",
                "slowly to avoid crashing"
        );
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
    public boolean update(ShapeRenderer shaper, float delta) {
        super.update(shaper, delta);

        return currentPage >= pages.size;
    }
}
