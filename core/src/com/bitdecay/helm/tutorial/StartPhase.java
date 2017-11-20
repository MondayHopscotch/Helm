package com.bitdecay.helm.tutorial;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
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

        nextPage();
    }

    private Vector2 getPlayerLocation() {
        return ship.getComponent(TransformComponent.class).position;
    }

    @Override
    public boolean update(ShapeRenderer shaper, float delta) {
        super.update(shaper, delta);

        if (currentPage >= pages.size) {
            return true;
        } else {
            return false;
        }
    }
}
