package com.bitdecay.helm.tutorial;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.bitdecay.helm.GameEntity;
import com.bitdecay.helm.Helm;
import com.bitdecay.helm.component.BodyDefComponent;
import com.bitdecay.helm.component.TransformComponent;
import com.bitdecay.helm.entities.LandingPlatformEntity;
import com.bitdecay.helm.math.Geom;
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

        final Vector2 playerLocation = getPlayerLocation();
        RotatingLabel playerLabel = new RotatingLabel("This is your ship!", game.fontScale, game.skin, stageClickListener);
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

        final Vector2 landingLocation = getLandingLocation();
        RotatingLabel landingLabel1 = new RotatingLabel("This is where", game.fontScale, game.skin, stageClickListener);
        landingLabel1.setOrigin(Align.center);

        RotatingLabel landingLabel2 = new RotatingLabel("you need to get!", game.fontScale, game.skin, stageClickListener);
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
        page2.updater.run();
        pages.add(page2);

        nextPage();
    }

    private Vector2 getPlayerLocation() {
        return ship.getComponent(TransformComponent.class).position;
    }

    private Vector2 getLandingLocation() {
        for (GameEntity entity : player.allEntities) {
            if (entity instanceof LandingPlatformEntity) {
                float widthOfPoints = Geom.getWidthOfPoints(entity.getComponent(BodyDefComponent.class).bodyPoints);
                Vector2 midPoint = new Vector2(widthOfPoints / 2, 0);
                TransformComponent transform = entity.getComponent(TransformComponent.class);
                midPoint.rotateRad(transform.angle);
                return midPoint.add(transform.position);
            }
        }
        return null;
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
