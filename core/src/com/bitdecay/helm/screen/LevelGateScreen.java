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
import com.bitdecay.helm.time.TimerUtils;
import com.bitdecay.helm.world.LevelInstance;

/**
 * Created by Monday on 9/4/2017.
 */

public class LevelGateScreen extends InputAdapter implements Screen {

    private static final float NO_INPUT_WAIT_TIME = 0.25f;
    private static final float MAX_WAIT_TIME = 5f;

    public static LevelGateScreen gateScreen;

    public static LevelGateScreen get(Helm helm, GameScreen after) {
        if (gateScreen == null) {
            gateScreen = new LevelGateScreen(helm);
        }

        gateScreen.timePassed = 0;
        gateScreen.after = after;
        gateScreen.build();
        return gateScreen;
    }

    protected final Helm game;
    Stage stage;
    Skin skin;

    private float timePassed = 0;
    private boolean touchTriggered = false;
    private GameScreen after;

    private LevelGateScreen(Helm game) {
        this.game = game;

        stage = new Stage();
        if (Helm.debug) {
            stage.setDebugAll(true);
        }
        skin = game.skin;
    }

    private void goToNextScreen() {
        game.setScreen(TransitionColorScreen.get(game, game.palette.get(com.bitdecay.helm.unlock.palette.GameColors.BACKGROUND), after));
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
        ScreenElements.getGoalsElement(fillTable, game, after.currentLevel, skin);

        screenTable.add(fillTable).fill().expand();

        stage.addActor(screenTable);
    }

    @Override
    public void show() {
        touchTriggered = false;
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        timePassed += delta;

        if (timePassed >= MAX_WAIT_TIME) {
            goToNextScreen();
        }

        stage.act();
        stage.draw();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (touchTriggered != true && timePassed > NO_INPUT_WAIT_TIME) {
            touchTriggered = true;
            goToNextScreen();
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
