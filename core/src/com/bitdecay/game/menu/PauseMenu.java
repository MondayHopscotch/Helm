package com.bitdecay.game.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.bitdecay.game.GamePilot;

/**
 * Created by Monday on 1/5/2017.
 */
public class PauseMenu {

    public final Stage stage;
    private final ClickListener pauseListener;
    private GamePilot pilot;
    private final Table pauseMenu;

    public PauseMenu(final GamePilot pilot) {
        this.pilot = pilot;

        pauseListener = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                pilot.togglePause();
                pauseMenu.setVisible(!pauseMenu.isVisible());
            }
        };

        stage = new Stage();
//        stage.setDebugAll(true);

        Table mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.align(Align.topRight);

        pauseMenu = new Table();
        pauseMenu.setFillParent(true);
        pauseMenu.align(Align.center);
        Label pauseDisplayLabel = new Label("Paused", pilot.getHelm().skin);
        pauseDisplayLabel.setFontScale(pilot.getHelm().fontScale * 3);
        pauseMenu.add(pauseDisplayLabel).center();
        pauseMenu.setVisible(false);
        pauseMenu.addListener(pauseListener);

        TextButton quitButton = new TextButton("Abandon Current Run", pilot.getHelm().skin);
        quitButton.getLabel().setFontScale(pilot.getHelm().fontScale * 0.8f);
        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                pilot.returnToMenus();
            }
        });
        pauseMenu.row();
        pauseMenu.add(quitButton).center();

        Label pauseButtonLabel = new Label("...", pilot.getHelm().skin);
        pauseButtonLabel.setFontScale(pilot.getHelm().fontScale);
        pauseButtonLabel.setAlignment(Align.topRight);
        pauseButtonLabel.setOrigin(Align.topRight);

        pauseButtonLabel.addListener(pauseListener);

        int screenWidth = Gdx.graphics.getWidth();
        mainTable.add(pauseButtonLabel).padRight(screenWidth / 25).width(screenWidth / 10).height(screenWidth / 10);

        stage.addActor(mainTable);
        stage.addActor(pauseMenu);
    }

    public void updateAndDraw() {
        stage.act();
        stage.draw();
    }
}
