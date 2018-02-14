package com.bitdecay.helm.tutorial;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.bitdecay.helm.Helm;
import com.bitdecay.helm.menu.RotatingLabel;
import com.bitdecay.helm.screen.LevelPlayer;
import com.bitdecay.helm.ui.UpdatingContainer;

/**
 * Created by Monday on 11/19/2017.
 */

public class WrapUpPhase extends PagedPhase {
    @Override
    public void start(Helm game, LevelPlayer player, Stage stage) {
        init(stage);

        UpdatingContainer gratsPage = TutorialUtils.getPage(game.fontScale * 2, game.skin, "Success!");
        gratsPage.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        pages.add(gratsPage);

        final UpdatingContainer pausePage = TutorialUtils.getPage(game.fontScale, game.skin,
                "If you need to pause or",
                "want to exit from a level, tap",
                "the three dots in the top left"
        );
        pausePage.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        pages.add(pausePage);

        final UpdatingContainer page1 = TutorialUtils.getPage(game.fontScale, game.skin,
                "Check out the Options",
                "menu from the Title Screen",
                "for tuning settings"
        );
        page1.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        pages.add(page1);

        UpdatingContainer goodLuckPage = TutorialUtils.getPage(game.fontScale * 2, game.skin,"Good Luck, Cadet!");
        goodLuckPage.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        pages.add(goodLuckPage);

        nextPage();
    }

    @Override
    public boolean update(ShapeRenderer shaper, float delta) {
        super.update(shaper, delta);

        return currentPage >= pages.size;
    }
}
