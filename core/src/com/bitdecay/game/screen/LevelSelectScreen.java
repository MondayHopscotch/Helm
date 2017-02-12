package com.bitdecay.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
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

    private final LevelWorld world;

    public LevelSelectScreen(final Helm game, final LevelWorld world) {
        this.game = game;

        stage = new Stage();
        if (Helm.debug) {
            stage.setDebugAll(true);
        }
        skin = game.skin;

        this.world = world;

        Table mainTable = new Table();
        mainTable.setFillParent(true);

        int totalHighScore = 0;
        float totalBestTime = 0;

        boolean allLevelsTimed = true;
        Table levelTable = new Table();
        for (LevelInstance level : world.levels) {
            buildLevelRow(level, levelTable);
            totalHighScore += level.getHighScore();

            if (level.getBestTime() == GamePrefs.TIME_NOT_SET) {
                allLevelsTimed = false;
            } else {
                totalBestTime += level.getBestTime();
            }
        }

        if (!allLevelsTimed) {
            totalBestTime = GamePrefs.TIME_NOT_SET;
        }

        Label totalHighScoreLabel = new Label("Totals: ", skin);
        totalHighScoreLabel.setFontScale(game.fontScale);
        totalHighScoreLabel.setAlignment(Align.right);

        Label totalHighScoreValue = new Label(Integer.toString(totalHighScore), skin);
        totalHighScoreValue.setFontScale(game.fontScale);
        totalHighScoreValue.setAlignment(Align.right);

        String displayTime;
        if (totalBestTime == GamePrefs.TIME_NOT_SET) {
            displayTime = "--";
        } else {
            displayTime = TimerUtils.getFormattedTime(totalBestTime);
        }
        Label totalBestTimeValue = new Label(displayTime, skin);
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

        Label returnToWorldSelectLabel = new Label("Return to World Select", skin);
        returnToWorldSelectLabel.setFontScale(game.fontScale);
        returnToWorldSelectLabel.setAlignment(Align.bottomRight);
        returnToWorldSelectLabel.setOrigin(Align.bottomRight);
        returnToWorldSelectLabel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new WorldSelectScreen(game));
            }
        });

        returnTable.add(returnToWorldSelectLabel);

        stage.addActor(mainTable);
        stage.addActor(returnTable);
    }

    private int buildLevelRow(final LevelInstance level, Table table) {
        ClickListener listener = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game, world, level));
            }
        };

        TextButton goButton = new TextButton("Go!", skin);
        goButton.getLabel().setFontScale(game.fontScale);
        goButton.addListener(listener);

        Label levelNameLabel = new Label(level.levelDef.name, skin);
        levelNameLabel.setAlignment(Align.left);
        levelNameLabel.setFontScale(game.fontScale);

        levelNameLabel.addListener(listener);

        int levelHighScore = level.getHighScore();

        Label levelScoreLabel = new Label(Integer.toString(levelHighScore), skin);
        levelScoreLabel.setAlignment(Align.right);
        levelScoreLabel.setFontScale(game.fontScale);

        float levelTimeScore = level.getBestTime();

        Label levelTimeLabel = new Label(TimerUtils.getFormattedTime(levelTimeScore), skin);
        if (levelTimeScore == GamePrefs.TIME_NOT_SET) {
            levelTimeLabel.setText("--");
        }
        levelTimeLabel.setAlignment(Align.right);
        levelTimeLabel.setFontScale(game.fontScale);

        table.add(goButton).expand(false, false);
        table.add(levelNameLabel).expandX();
        table.add(levelScoreLabel).expandX();
        table.add(levelTimeLabel);
        table.row().padTop(game.fontScale * 10);
        return levelHighScore;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

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
    }
}
