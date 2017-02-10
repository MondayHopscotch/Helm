package com.bitdecay.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.bitdecay.game.Helm;
import com.bitdecay.game.prefs.GamePrefs;
import com.bitdecay.game.time.TimerUtils;
import com.bitdecay.game.world.LevelDefinition;
import com.bitdecay.game.world.LevelInstance;
import com.bitdecay.game.world.LevelWorld;
import com.bitdecay.game.world.World1;
import com.bitdecay.game.world.World2;
import com.bitdecay.game.world.World3;

/**
 * Created by Monday on 2/9/2017.
 */

public class LevelSelectScreen implements Screen {

    private final Helm game;
    Stage stage;
    Skin skin;

    public LevelSelectScreen(final Helm game, final LevelWorld world) {
        this.game = game;

        stage = new Stage();
        if (Helm.debug) {
            stage.setDebugAll(true);
        }
        skin = game.skin;

        Table mainTable = new Table();
        mainTable.setFillParent(true);

        int totalHighScore = 0;
        float totalBestTime = 0;

        Table levelTable = new Table();
        for (LevelInstance level : world.levels) {
            buildLevelRow(level, levelTable);
            totalHighScore += level.getHighScore();

            if (level.getBestTime() != GamePrefs.TIME_NOT_SET) {
                totalBestTime += level.getBestTime();
            }
        }

        Label totalHighScoreLabel = new Label("Totals: ", skin);
        totalHighScoreLabel.setFontScale(game.fontScale);
        totalHighScoreLabel.setAlignment(Align.right);

        Label totalHighScoreValue = new Label(Integer.toString(totalHighScore), skin);
        totalHighScoreValue.setFontScale(game.fontScale);
        totalHighScoreValue.setAlignment(Align.right);

        Label totalBestTimeValue = new Label(TimerUtils.getFormattedTime(totalBestTime), skin);
        totalBestTimeValue.setFontScale(game.fontScale);
        totalBestTimeValue.setAlignment(Align.right);

        levelTable.add(totalHighScoreLabel).colspan(2);
        levelTable.add(totalHighScoreValue);
        levelTable.add(totalBestTimeValue);

        mainTable.add(levelTable).width(Gdx.graphics.getWidth() * 0.8f);

        Table returnTable = new Table();
        returnTable.setFillParent(true);
        returnTable.align(Align.bottomRight);
        returnTable.setOrigin(Align.bottomRight);

        Label returnToTitleLabel = new Label("Return to Title Screen", skin);
        returnToTitleLabel.setFontScale(game.fontScale);
        returnToTitleLabel.setAlignment(Align.bottomRight);
        returnToTitleLabel.setOrigin(Align.bottomRight);
        returnToTitleLabel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new TitleScreen(game));
            }
        });

        returnTable.add(returnToTitleLabel);

        stage.addActor(mainTable);
        stage.addActor(returnTable);
    }

    private int buildLevelRow(final LevelInstance level, Table table) {
        ClickListener listener = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game, level.levelDef));
            }
        };

        TextButton goButton = new TextButton("Go!", skin);
        goButton.getLabel().setFontScale(game.fontScale);
        goButton.addListener(listener);

        Label worldNameLabel = new Label(level.getWorldName(), skin);
        worldNameLabel.setAlignment(Align.left);
        worldNameLabel.setFontScale(game.fontScale);

        worldNameLabel.addListener(listener);

        int worldHighScore = level.getHighScore();

        Label worldScoreLabel = new Label(Integer.toString(worldHighScore), skin);
        worldScoreLabel.setAlignment(Align.right);
        worldScoreLabel.setFontScale(game.fontScale);

        float worldTimeScore = level.getBestTime();

        Label worldTimeLabel = new Label(TimerUtils.getFormattedTime(worldTimeScore), skin);
        if (worldTimeScore == GamePrefs.TIME_NOT_SET) {
            worldTimeLabel.setText("--");
        }
        worldTimeLabel.setAlignment(Align.right);
        worldTimeLabel.setFontScale(game.fontScale);

        table.add(goButton).expand(false, false);
        table.add(worldNameLabel).expandX();
        table.add(worldScoreLabel).expandX();
        table.add(worldTimeLabel);
        table.row().padTop(game.fontScale * 10);
        return worldHighScore;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

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

    }
}
