package com.bitdecay.helm.tutorial;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.bitdecay.helm.GameEntity;
import com.bitdecay.helm.Helm;
import com.bitdecay.helm.component.TransformComponent;
import com.bitdecay.helm.component.control.SteeringControlComponent;
import com.bitdecay.helm.math.Geom;
import com.bitdecay.helm.menu.RotatingLabel;
import com.bitdecay.helm.screen.LevelPlayer;
import com.bitdecay.helm.system.collision.LandingSystem;
import com.bitdecay.helm.ui.UpdatingContainer;

/**
 * Created by Monday on 11/19/2017.
 */

public class LandingAnglePhase extends PagedPhase {
    private float rotation = 0.02f;

    private SteeringControlComponent steering;
    private TransformComponent transform;
    private float maxRot = MathUtils.PI / 2 + LandingSystem.MAX_LANDING_ANGLE * 2;
    private float minRot = MathUtils.PI / 2 - LandingSystem.MAX_LANDING_ANGLE * 2;;
    private GameEntity ship;

    @Override
    public void start(Helm game, final LevelPlayer player, Stage stage) {
        init(stage);
        // get ship
        ship = TutorialUtils.getShip(player.allEntities);
        steering = ship.getComponent(SteeringControlComponent.class);
        ship.removeComponent(SteeringControlComponent.class);

        transform = ship.getComponent(TransformComponent.class);

        final Vector2 landingLocation = TutorialUtils.getLandingLocation(player.allEntities);
        final UpdatingContainer page1 = TutorialUtils.getPage(game.fontScale, game.skin,
                "The icon will show if",
                "the ship's angle is correct"
        );
        page1.updater = new Runnable() {
            @Override
            public void run() {
                Vector3 project = player.gameCam.project(new Vector3(landingLocation.x, landingLocation.y, 0));
                page1.setPosition(project.x, project.y - page1.getPrefHeight() / 2);
            }
        };
        pages.add(page1);

        final UpdatingContainer page3 = TutorialUtils.getPage(game.fontScale, game.skin,
                "Land as slowly as possible.",
                "Too much speed and the",
                "ship will crash"
        );
        page3.updater = new Runnable() {
            @Override
            public void run() {
                Vector3 project = player.gameCam.project(new Vector3(landingLocation.x, landingLocation.y, 0));
                page3.setPosition(project.x, project.y - page1.getPrefHeight() / 2);
            }
        };
        pages.add(page3);

        nextPage();
    }

    @Override
    public boolean update(ShapeRenderer shaper, float delta) {
        super.update(shaper, delta);

        if (transform.angle > maxRot) {
            rotation *= -1;
            transform.angle = maxRot;
        } else if (transform.angle < minRot) {
                rotation *= -1;
                transform.angle = minRot;
        }
        transform.angle += rotation;

        if (currentPage >= pages.size) {
            transform.angle = Geom.ROTATION_UP;
            ship.addComponent(steering);
            return true;
        }

        return false;
    }
}
