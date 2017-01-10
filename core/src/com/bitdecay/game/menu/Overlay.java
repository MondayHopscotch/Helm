package com.bitdecay.game.menu;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.bitdecay.game.GamePilot;

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
            int secondsAsInt = (int) timeInSeconds;
            int minutes = secondsAsInt / 60;
            int seconds = secondsAsInt - (minutes * 60);
            int fractional = (int) ((timeInSeconds - secondsAsInt) * 1000);
            if (minutes > 0) {
                timerLabel.setText(String.format("%02d:%02d.%03d", minutes, seconds, fractional));
            } else {
                timerLabel.setText(String.format("%02d.%03d", seconds, fractional));
            }
        }
    }

    public void updateAndDraw() {
        stage.act();
        stage.draw();
    }
}
