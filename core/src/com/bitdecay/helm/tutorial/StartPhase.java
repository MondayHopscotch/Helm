package com.bitdecay.helm.tutorial;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
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

        UpdatingContainer welcomePage = TutorialUtils.getPage(game.fontScale * 2, game.skin, "Welcome cadet!");
        welcomePage.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        pages.add(welcomePage);

        final UpdatingContainer introPage1 = TutorialUtils.getPage(game.fontScale * 2, game.skin,
                "Let's quickly cover",
                "the basics"
        );
        introPage1.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        pages.add(introPage1);

        UpdatingContainer phasePage = TutorialUtils.getPage(game.fontScale * 2, game.skin, "Phase 1: Flight Control");
        phasePage.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        pages.add(phasePage);

        nextPage();
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
