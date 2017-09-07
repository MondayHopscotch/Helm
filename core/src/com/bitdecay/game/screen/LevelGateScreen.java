package com.bitdecay.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.bitdecay.game.Helm;
import com.bitdecay.game.menu.MedalUtils;
import com.bitdecay.game.prefs.GamePrefs;
import com.bitdecay.game.time.TimerUtils;
import com.bitdecay.game.unlock.palette.GameColors;

/**
 * Created by Monday on 9/4/2017.
 */

public class LevelGateScreen implements Screen {

    private static final float NO_INPUT_WAIT_TIME = 0.25f;
    private static final float MAX_WAIT_TIME = 5f;

    public static LevelGateScreen gateScreen;

    private static float INFO_FONT_SCALE = 1.0f;

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
        game.setScreen(TransitionColorScreen.get(game, game.palette.get(GameColors.BACKGROUND), after));
    }

    private void build() {
        stage.clear();

        ClickListener skipListener = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if (timePassed > NO_INPUT_WAIT_TIME) {
                    goToNextScreen();
                }
            }
        };

        stage.addListener(skipListener);

        Table screenTable = new Table(skin);
        screenTable.setFillParent(true);
        screenTable.addListener(skipListener);

        Label levelTitleLabel = new Label(after.currentLevel.levelDef.name, skin);
        levelTitleLabel.setFontScale(2 * game.fontScale);
        levelTitleLabel.setAlignment(Align.center);
        levelTitleLabel.setOrigin(Align.center);

        screenTable.add(levelTitleLabel).fillX().expandX().padTop(30 * game.fontScale);
        screenTable.row();

        Table fillTable = new Table(skin);

        Table scoreTable = new Table(skin);
        Table timeTable = new Table(skin);



        if (after.currentLevel.getBestTime() != GamePrefs.TIME_NOT_SET &&
                after.currentLevel.getHighScore() != GamePrefs.SCORE_NOT_SET) {
            buildCurrentScoreParts(scoreTable);
            buildCurrentTimeParts(timeTable);
        }

        buildTargetScoreParts(scoreTable);
        buildTargetTimeParts(timeTable);

        fillTable.add(scoreTable).padRight(10 * game.fontScale);
        fillTable.add(timeTable).padLeft(10 * game.fontScale);

        screenTable.add(fillTable).fill().expand();

        stage.addActor(screenTable);
    }

    private void buildCurrentScoreParts(Table table) {
        if (after.currentLevel.getHighScore() == GamePrefs.SCORE_NOT_SET) {
            return;
        }

        Label scoreTextLabel = new Label("Current Best Score", skin);
        scoreTextLabel.setFontScale(INFO_FONT_SCALE * game.fontScale);
        scoreTextLabel.setAlignment(Align.center);
        scoreTextLabel.setOrigin(Align.center);

        Image medalImage = MedalUtils.getIconForHighScore(after.currentLevel);
        Label scoreLabel = new Label(Integer.toString(after.currentLevel.getHighScore()), skin);
        scoreLabel.setFontScale(INFO_FONT_SCALE * game.fontScale);
        scoreLabel.setAlignment(Align.center);
        scoreLabel.setOrigin(Align.center);

        Table scoreTable = new Table(skin);
        scoreTable.add(medalImage).size(MedalUtils.imageSize, MedalUtils.imageSize).expand(false, false).fill(false);
        scoreTable.add(scoreLabel);

        table.add(scoreTextLabel).row();
        table.add(scoreTable).row();
    }

    private void buildTargetScoreParts(Table table) {
        MedalUtils.LevelRating scoreRank = MedalUtils.getScoreRank(after.currentLevel, after.currentLevel.getHighScore());
        if (MedalUtils.LevelRating.DEV.equals(scoreRank)) {
            return;
        }

        Label scoreTextLabel = new Label("Score for " + scoreRank.nextRank().medalName() + " medal", skin);
        scoreTextLabel.setFontScale(INFO_FONT_SCALE * game.fontScale);
        scoreTextLabel.setAlignment(Align.center);
        scoreTextLabel.setOrigin(Align.center);

        Image medalImage = MedalUtils.getRankImage(scoreRank.nextRank());
        Label scoreLabel = new Label(Integer.toString(after.currentLevel.getScoreNeededForMedal(scoreRank.nextRank())), skin);
        scoreLabel.setFontScale(INFO_FONT_SCALE * game.fontScale);
        scoreLabel.setAlignment(Align.center);
        scoreLabel.setOrigin(Align.center);

        Table scoreTable = new Table(skin);
        scoreTable.add(medalImage).size(MedalUtils.imageSize, MedalUtils.imageSize).expand(false, false).fill(false);
        scoreTable.add(scoreLabel);

        table.add(scoreTextLabel).row();
        table.add(scoreTable);
        table.row();
    }

    private void buildCurrentTimeParts(Table table) {
        if (after.currentLevel.getBestTime() == GamePrefs.TIME_NOT_SET) {
            return;
        }

        Label timeTextLabel = new Label("Current Best Time", skin);
        timeTextLabel.setFontScale(INFO_FONT_SCALE * game.fontScale);
        timeTextLabel.setAlignment(Align.center);
        timeTextLabel.setOrigin(Align.center);

        Image medalImage = MedalUtils.getIconForBestTime(after.currentLevel);
        Label timeLabel = new Label(TimerUtils.getFormattedTime(after.currentLevel.getBestTime()), skin);
        timeLabel.setFontScale(INFO_FONT_SCALE * game.fontScale);
        timeLabel.setAlignment(Align.center);
        timeLabel.setOrigin(Align.center);

        Table scoreTable = new Table(skin);
        scoreTable.add(medalImage).size(MedalUtils.imageSize, MedalUtils.imageSize).expand(false, false).fill(false);
        scoreTable.add(timeLabel);

        table.add(timeTextLabel).row();
        table.add(scoreTable);
        table.row();
    }

    private void buildTargetTimeParts(Table table) {
        MedalUtils.LevelRating timeRank = MedalUtils.getTimeRank(after.currentLevel, after.currentLevel.getBestTime());
        if (MedalUtils.LevelRating.DEV.equals(timeRank)) {
            return;
        }

        Label timeTextLabel = new Label("Time for " + timeRank.nextRank().medalName() + " medal", skin);
        timeTextLabel.setFontScale(INFO_FONT_SCALE * game.fontScale);
        timeTextLabel.setAlignment(Align.center);
        timeTextLabel.setOrigin(Align.center);

        Image medalImage = MedalUtils.getRankImage(timeRank.nextRank());
        Label scoreLabel = new Label(TimerUtils.getFormattedTime(after.currentLevel.getTimeNeededForMedal(timeRank.nextRank())), skin);
        scoreLabel.setFontScale(INFO_FONT_SCALE * game.fontScale);
        scoreLabel.setAlignment(Align.center);
        scoreLabel.setOrigin(Align.center);

        Table scoreTable = new Table(skin);
        scoreTable.add(medalImage).size(MedalUtils.imageSize, MedalUtils.imageSize).expand(false, false).fill(false);
        scoreTable.add(scoreLabel);

        table.add(timeTextLabel).row();
        table.add(scoreTable);
        table.row();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
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
