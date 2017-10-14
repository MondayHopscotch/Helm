package com.bitdecay.helm.menu;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

/**
 * Created by Monday on 1/9/2017.
 */
public class Overlay {
    public final Stage stage;
    private final Skin skin;
    private Label timerLabel;

    public Overlay(final com.bitdecay.helm.GamePilot pilot) {
        stage = new Stage();
        skin = pilot.getHelm().skin;

        Table mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.align(Align.top);
        mainTable.setOrigin(Align.top);

        timerLabel = new Label("timerLabel", skin);
        timerLabel.setFontScale(pilot.getHelm().fontScale);
        timerLabel.setAlignment(Align.left);
        mainTable.add(timerLabel).width(200);

        stage.addActor(mainTable);
    }

    public void setTime(float timeInSeconds) {
        if (timeInSeconds <= 0) {
            timerLabel.setText("");
        } else {
            timerLabel.setText(com.bitdecay.helm.time.TimerUtils.getFormattedTime(timeInSeconds));
        }
    }

    public void updateAndDraw() {
        stage.act();
        stage.draw();
    }
}
