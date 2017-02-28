package com.bitdecay.game.screen;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.bitdecay.game.Helm;
import com.bitdecay.game.prefs.GamePrefs;
import com.bitdecay.game.time.TimerUtils;
import com.bitdecay.game.world.LevelInstance;
import com.bitdecay.game.world.WorldInstance;

/**
 * Created by Monday on 2/9/2017.
 */

public class LevelSelectScreen extends AbstractScrollingItemScreen {

    private final WorldInstance world;

    public LevelSelectScreen(final Helm game, final WorldInstance world) {
        super(game);
        this.world = world;
        build();
    }

    @Override
    public String getTitle() {
        return "Level Select - " + world.getWorldName();
    }

    @Override
    public void populateRows(Table levelTable) {
        itemTable.columnDefaults(1).expandX();
        itemTable.columnDefaults(2).width(game.fontScale * 50);
        itemTable.columnDefaults(3).width(game.fontScale * 50);

        int totalHighScore = 0;
        float totalBestTime = 0;

        boolean allLevelsCompleted = true;
        for (LevelInstance level : world.levels) {
            buildLevelRow(level, levelTable);
            int score = level.getHighScore();
            if (score != GamePrefs.SCORE_NOT_SET) {
                totalHighScore += score;
            }

            if (level.getBestTime() == GamePrefs.TIME_NOT_SET) {
                allLevelsCompleted = false;
            } else {
                totalBestTime += level.getBestTime();
            }
        }

        if (!allLevelsCompleted) {
            totalBestTime = GamePrefs.TIME_NOT_SET;
        }

        Label totalHighScoreLabel = new Label("Total: ", skin);
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

        levelTable.add(totalHighScoreLabel).colspan(2).align(Align.right);
        levelTable.add(totalHighScoreValue);
        levelTable.add(totalBestTimeValue);
    }

    @Override
    public Actor getReturnButton() {
        TextButton returnButton = new TextButton("Return to World Select", skin);
        returnButton.getLabel().setFontScale(game.fontScale);
        returnButton.align(Align.bottomRight);
        returnButton.setOrigin(Align.bottomRight);
        returnButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new WorldSelectScreen(game));
            }
        });
        return returnButton;
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
        levelNameLabel.setAlignment(Align.center);
        levelNameLabel.setFontScale(game.fontScale);

        levelNameLabel.addListener(listener);

        int levelHighScore = level.getHighScore();

        int score = levelHighScore;
        if (score == GamePrefs.SCORE_NOT_SET) {
            score = 0;
        }

        Label levelScoreLabel = new Label(Integer.toString(score), skin);
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
        table.add(levelNameLabel);
        table.add(levelScoreLabel);
        table.add(levelTimeLabel);
        table.row().padTop(game.fontScale * 10);
        return levelHighScore;
    }
}
