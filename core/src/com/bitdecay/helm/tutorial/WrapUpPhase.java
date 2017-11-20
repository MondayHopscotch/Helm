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

        RotatingLabel gratsMessage = new RotatingLabel("That's it!", game.fontScale * 2, game.skin);
        gratsMessage.setOrigin(Align.center);
        UpdatingContainer gratsPage = new UpdatingContainer(gratsMessage);
        gratsPage.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        pages.add(gratsPage);


        RotatingLabel optionsLabel = new RotatingLabel("Check out the Options", game.fontScale, game.skin);
        optionsLabel.setOrigin(Align.center);
        RotatingLabel optionsLabel2 = new RotatingLabel("menu from the Title Screen", game.fontScale, game.skin);
        optionsLabel2.setOrigin(Align.center);

        Table optionsTable = new Table();
        optionsTable.setTouchable(Touchable.disabled);
        optionsTable.align(Align.left);
        optionsTable.add(optionsLabel).center();
        optionsTable.row();
        optionsTable.add(optionsLabel2).center();

        final UpdatingContainer page1 = new UpdatingContainer(optionsTable);
        page1.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        pages.add(page1);

        RotatingLabel goodLuckLabel = new RotatingLabel("Good Luck, Cadet!", game.fontScale * 2, game.skin);
        gratsMessage.setOrigin(Align.center);
        UpdatingContainer goodLuckPage = new UpdatingContainer(goodLuckLabel);
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
