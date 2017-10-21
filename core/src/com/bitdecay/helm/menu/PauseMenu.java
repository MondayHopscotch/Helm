package com.bitdecay.helm.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.bitdecay.helm.GamePilot;
import com.bitdecay.helm.Helm;
import com.bitdecay.helm.screen.ScreenElements;
import com.bitdecay.helm.world.LevelInstance;

/**
 * Created by Monday on 1/5/2017.
 */
public class PauseMenu {

    public final Stage stage;
    private final ClickListener pauseListener;
    private GamePilot pilot;
    private final Table pauseMenu;
    private LevelInstance level;
    private final Table goalsTable;

    public PauseMenu(final GamePilot pilot) {
        this.pilot = pilot;

        pauseListener = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                pilot.togglePause();
                pauseMenu.setVisible(!pauseMenu.isVisible());
                goalsTable.setVisible(!goalsTable.isVisible());
            }
        };

        stage = new Stage();
        if (Helm.debug) {
            stage.setDebugAll(true);
        }

        Table mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.align(Align.topRight);

        pauseMenu = new Table();
        pauseMenu.setFillParent(true);
        pauseMenu.align(Align.center);
        Label pauseDisplayLabel = new Label("Paused", pilot.getHelm().skin);
        pauseDisplayLabel.setFontScale(pilot.getHelm().fontScale * 3);
        pauseMenu.add(pauseDisplayLabel);
        pauseMenu.row();
        pauseMenu.setVisible(false);
        pauseMenu.addListener(pauseListener);

        TextButton resumeButton = new TextButton("Resume", pilot.getHelm().skin);
        resumeButton.getLabel().setFontScale(pilot.getHelm().fontScale * 0.8f);
        resumeButton.addListener(pauseListener);

        TextButton quitButton = new TextButton("Abandon", pilot.getHelm().skin);
        quitButton.getLabel().setFontScale(pilot.getHelm().fontScale * 0.8f);
        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                pilot.returnToMenus(true);
            }
        });

        Table buttonTable = new Table();
        buttonTable.align(Align.center);
        buttonTable.add(resumeButton).padRight(pilot.getHelm().fontScale * 10);
        buttonTable.add(quitButton).padLeft(pilot.getHelm().fontScale * 10);
        pauseMenu.add(buttonTable);

        Label pauseButtonLabel = new Label("...", pilot.getHelm().skin);
        pauseButtonLabel.setFontScale(pilot.getHelm().fontScale);
        pauseButtonLabel.setAlignment(Align.topRight);
        pauseButtonLabel.setOrigin(Align.topRight);

        pauseButtonLabel.addListener(pauseListener);

        int screenWidth = Gdx.graphics.getWidth();
        mainTable.add(pauseButtonLabel).padRight(screenWidth / 25).width(screenWidth / 10).height(screenWidth / 10);

        goalsTable = new Table(pilot.getHelm().skin);
        goalsTable.setFillParent(true);
        goalsTable.setVisible(false);
        goalsTable.align(Align.bottom);

        stage.addActor(mainTable);
        stage.addActor(pauseMenu);
        stage.addActor(goalsTable);
    }

    public void updateAndDraw() {
        stage.act();
        stage.draw();
    }

    public void setLevel(LevelInstance level) {
        this.level = level;
        ScreenElements.getGoalsElement(goalsTable, pilot.getHelm(), level, pilot.getHelm().skin);
    }
}