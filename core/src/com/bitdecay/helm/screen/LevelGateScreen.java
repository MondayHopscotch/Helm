package com.bitdecay.helm.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.bitdecay.helm.Helm;
import com.bitdecay.helm.menu.MedalUtils;
import com.bitdecay.helm.sound.SFXLibrary;
import com.bitdecay.helm.sound.SoundMode;
import com.bitdecay.helm.time.TimerUtils;
import com.bitdecay.helm.unlock.palette.GameColors;
import com.bitdecay.helm.world.LevelInstance;

/**
 * Created by Monday on 9/4/2017.
 */

public class LevelGateScreen extends InputAdapter implements Screen {

    private static final float NO_INPUT_WAIT_TIME = 0.25f;
    private static final float MAX_WAIT_TIME = 10f;

    public static LevelGateScreen gateScreen;

    public static LevelGateScreen get(Helm helm, GameScreen after) {
        if (gateScreen == null) {
            gateScreen = new LevelGateScreen(helm);
        }

        gateScreen.timePassed = 0;
        gateScreen.after = after;
        gateScreen.pendingTransition = true;
        gateScreen.build();
        return gateScreen;
    }

    protected final Helm game;
    Stage stage;
    Skin skin;

    private float timePassed = 0;
    private boolean touchTriggered = false;

    private boolean pendingTransition = true;

    private GameScreen after;

    private LevelGateScreen(Helm game) {
        this.game = game;

        stage = new Stage();
        if (Helm.debug) {
            stage.setDebugAll(true);
        }
        skin = game.skin;
    }

    private void goToNextScreen(boolean fast) {
        AudioUtils.doSound(game, SoundMode.PLAY, SFXLibrary.MENU_BOOP);
        Gdx.input.vibrate(10);
        if (fast) {
            stage.addAction(Transitions.getQuickFadeOut(new Runnable() {
                @Override
                public void run() {
                    game.setScreen(TransitionColorScreen.get(game, game.palette.get(GameColors.BACKGROUND), after));
                }
            }));
        } else {
            stage.addAction(Transitions.getFadeOut(new Runnable() {
                @Override
                public void run() {
                    game.setScreen(TransitionColorScreen.get(game, game.palette.get(GameColors.BACKGROUND), after));
                }
            }));
        }
    }

    private void build() {
        stage.clear();

        Table screenTable = new Table(skin);
        screenTable.setFillParent(true);

        Label levelTitleLabel = new Label(after.currentLevel.levelDef.name, skin);
        levelTitleLabel.setFontScale(2 * game.fontScale);
        levelTitleLabel.setAlignment(Align.center);
        levelTitleLabel.setOrigin(Align.center);

        screenTable.add(levelTitleLabel).fillX().expandX().padTop(30 * game.fontScale);
        screenTable.row();

        Table fillTable =  new Table(skin);
        fillTable.align(Align.bottom);
        ScreenElements.getGoalsElement(fillTable, game, after.currentLevel, skin);

        screenTable.add(fillTable).fill().expand();

        stage.addActor(screenTable);
    }

    @Override
    public void show() {
        touchTriggered = false;
        stage.addAction(Transitions.getFadeIn());
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (pendingTransition) {
            timePassed += delta;

            if (timePassed >= MAX_WAIT_TIME) {
                goToNextScreen(false);
                pendingTransition = false;
            }
        }

        stage.act();
        stage.draw();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (touchTriggered != true && timePassed > NO_INPUT_WAIT_TIME) {
            touchTriggered = true;
            goToNextScreen(true);
        }
        return true;
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
        gateScreen = null;
    }
}
