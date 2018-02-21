package com.bitdecay.helm.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.bitdecay.helm.GamePilot;
import com.bitdecay.helm.Helm;
import com.bitdecay.helm.screen.AudioUtils;
import com.bitdecay.helm.screen.ScreenElements;
import com.bitdecay.helm.sound.SFXLibrary;
import com.bitdecay.helm.sound.SoundMode;
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

        float iconSize = pilot.getHelm().fontScale * 0.5f;

        pauseListener = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                pilot.doSound(SoundMode.PLAY, SFXLibrary.MENU_BOOP);
                Gdx.input.vibrate(10);
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
        pauseMenu.add(pauseDisplayLabel).align(Align.center).padTop(pilot.getHelm().fontScale * 5);
        pauseMenu.row();
        pauseMenu.setVisible(false);
        pauseMenu.addListener(pauseListener);

        TextureRegionDrawable resumeIcon = new TextureRegionDrawable(TextureCache.getIcon(pilot.getHelm(), "next_icon"));
        BitImageButton resumeButton = new BitImageButton(resumeIcon, resumeIcon, iconSize, pilot.getHelm().skin);
        resumeButton.addListener(pauseListener);

        TextureRegionDrawable quitIcon = new TextureRegionDrawable(TextureCache.getIcon(pilot.getHelm(), "exit_icon"));
        BitImageButton quitButton = new BitImageButton(quitIcon, quitIcon, iconSize, pilot.getHelm().skin);
        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
//                pilot.doSound(SoundMode.PLAY, SFXLibrary.MENU_BOOP);
                Gdx.input.vibrate(10);
                pilot.returnToMenus(true);
            }
        });

        Table buttonTable = new Table();
        buttonTable.align(Align.bottom);
        buttonTable.add(quitButton).padRight(pilot.getHelm().fontScale * 10);
        buttonTable.add(resumeButton).padLeft(pilot.getHelm().fontScale * 10);
        pauseMenu.add(buttonTable).padBottom(pilot.getHelm().fontScale * 5).expand().fill();

        Label pauseButtonLabel = new Label("...", pilot.getHelm().skin);
        pauseButtonLabel.setFontScale(pilot.getHelm().fontScale);
        pauseButtonLabel.setAlignment(Align.topRight);
        pauseButtonLabel.setOrigin(Align.topRight);

        pauseButtonLabel.addListener(pauseListener);

        int screenWidth = Gdx.graphics.getWidth();
        mainTable.add(pauseButtonLabel).width(screenWidth / 10).height(screenWidth / 10);

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

    public void doPause() {
        pauseListener.clicked(null, 0, 0);
    }
}
