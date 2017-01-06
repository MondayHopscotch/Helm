package com.bitdecay.game.menu;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.bitdecay.game.GamePilot;

/**
 * Created by Monday on 1/5/2017.
 */
public class PauseMenu {

    public final Stage stage;
    private GamePilot pilot;

    public PauseMenu(final GamePilot pilot) {
        this.pilot = pilot;
        stage = new Stage();

        Table mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.align(Align.topRight);

        Label pauseLabel = new Label("...", pilot.getHelm().skin);
        pauseLabel.setFontScale(6);
        pauseLabel.setAlignment(Align.topRight);
        pauseLabel.setOrigin(Align.topRight);

        pauseLabel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                pilot.togglePause();
            }
        });

        mainTable.add(pauseLabel).padRight(40).width(200).height(200);

        stage.addActor(mainTable);
    }

    public void updateAndDraw() {
        stage.act();
        stage.draw();
    }
}
