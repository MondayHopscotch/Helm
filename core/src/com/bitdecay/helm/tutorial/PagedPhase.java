package com.bitdecay.helm.tutorial;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.bitdecay.helm.ui.UpdatingContainer;

/**
 * Created by Monday on 11/8/2017.
 */

public abstract class PagedPhase implements TutorialPhase {
    protected Stage stage;


    protected UpdatingContainer livePage;

    protected Array<UpdatingContainer> pages;
    protected int currentPage = -1;

    protected ClickListener nextPageListener;

    protected void init(Stage stage) {
        this.stage = stage;
        stage.clear();
        pages = new Array<>();

        nextPageListener = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if (livePage != null && livePage.action != null) {
                    livePage.action.run();
                }
                nextPage();
            }
        };
    }


    protected void nextPage() {
        stage.clear();
        currentPage++;
        if (currentPage >= pages.size) {
            System.out.println("NO PAGES LEFT!");
            return;
        }

        final UpdatingContainer page = pages.get(currentPage);
        livePage = page;
        page.addAction(
                Actions.sequence(
                        Actions.fadeOut(0),
                        Actions.fadeIn(0.2f),
                        Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                page.addListener(nextPageListener);
                            }
                        })
                )
        );
        stage.addActor(page);
        stage.addListener(nextPageListener);
    }
}
