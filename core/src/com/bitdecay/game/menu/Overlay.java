package com.bitdecay.game.menu;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.TimeUtils;
import com.bitdecay.game.GamePilot;
import com.bitdecay.game.time.TimerUtils;

/**
 * Created by Monday on 1/9/2017.
 */
public class Overlay {
    public final Stage stage;
    private final Skin skin;
    private Label timerLabel;

    public Overlay(final GamePilot pilot) {
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
            timerLabel.setText(TimerUtils.getFormattedTime(timeInSeconds));
        }
    }

    public void updateAndDraw() {
        stage.act();
        stage.draw();
    }
}
