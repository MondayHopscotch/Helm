package com.bitdecay.helm.tutorial;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.bitdecay.helm.Helm;
import com.bitdecay.helm.ui.UpdatingContainer;

/**
 * Created by Monday on 11/8/2017.
 */

public abstract class PagedPhase implements TutorialPhase {
    protected Stage stage;


    protected UpdatingContainer livePage;

    protected Array<UpdatingContainer> pages;
    protected int currentPage = -1;

    protected void init(Stage stage) {
        currentPage = -1;
        this.stage = stage;
        stage.setDebugAll(Helm.debug);
        pages = new Array<>();
    }

    @Override
    public boolean touchUp(int screenX, int screenY) {
        return nextPage();
    }

    protected boolean nextPage() {
        stage.clear();

        if (livePage != null && livePage.action != null) {
            livePage.action.run();
        }

        livePage = null;

        currentPage++;
        if (currentPage >= pages.size) {
            System.out.println("NO PAGES LEFT!");
            return false;
        }

        // this is just to consume the clicks so a player can't unintentionally interact
        // with the game while the stage has pages left.
        final ClickListener tempListener = new ClickListener();
        stage.addListener(tempListener);

        final UpdatingContainer page = pages.get(currentPage);
        livePage = page;
        page.addAction(
                Actions.sequence(
                        Actions.fadeOut(0),
                        Actions.fadeIn(0.2f)
                )
        );
        stage.addActor(page);
        return true;
    }
}
