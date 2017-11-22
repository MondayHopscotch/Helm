package com.bitdecay.helm.tutorial;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.bitdecay.helm.Helm;
import com.bitdecay.helm.screen.LevelPlayer;
import com.bitdecay.helm.ui.UpdatingContainer;

/**
 * Created by Monday on 11/20/2017.
 */

public class CrashPhase extends PagedPhase {

    private boolean alreadyStarted;

    @Override
    public void start(Helm game, LevelPlayer player, Stage stage) {
        if (alreadyStarted) {
            return;
        }

        alreadyStarted = true;

        init(stage);

        UpdatingContainer page1 = TutorialUtils.getPage(game.fontScale, game.skin,
                "Colliding with anything other",
                "than the landing platform",
                "will result in the ship crashing"
        );
        page1.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        pages.add(page1);

        nextPage();
    }

    @Override
    public boolean update(ShapeRenderer shaper, float delta) {
        super.update(shaper, delta);
        return currentPage >= pages.size;
    }

    @Override
    public boolean touchDown(int screenX, int screenY) {
        return super.touchDown(screenX, screenY);
    }
}
