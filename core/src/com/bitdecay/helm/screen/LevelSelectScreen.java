package com.bitdecay.helm.screen;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.bitdecay.helm.menu.MedalUtils;
import com.bitdecay.helm.menu.RotatingLabel;
import com.bitdecay.helm.prefs.GamePrefs;
import com.bitdecay.helm.time.TimerUtils;
import com.bitdecay.helm.world.LevelInstance;

/**
 * Created by Monday on 2/9/2017.
 */

public class LevelSelectScreen extends AbstractScrollingItemScreen {

    private final com.bitdecay.helm.world.WorldInstance world;

    public LevelSelectScreen(final com.bitdecay.helm.Helm game, final com.bitdecay.helm.world.WorldInstance world) {
        super(game);
        this.world = world;

        build(false);
    }

    @Override
    public String getTitle() {
        return "Level Select - " + world.getWorldName();
    }

    @Override
    public void populateRows(Table levelTable) {
        itemTable.columnDefaults(1).expandX();
        itemTable.columnDefaults(2).width(game.fontScale * 50);
        itemTable.columnDefaults(3).width(MedalUtils.imageSize);
        itemTable.columnDefaults(4).width(game.fontScale * 50);
        itemTable.columnDefaults(5).width(MedalUtils.imageSize);

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
        if (totalBestTime == com.bitdecay.helm.prefs.GamePrefs.TIME_NOT_SET) {
            displayTime = "--";
        } else {
            displayTime = com.bitdecay.helm.time.TimerUtils.getFormattedTime(totalBestTime);
        }
        Label totalBestTimeValue = new Label(displayTime, skin);
        totalBestTimeValue.setFontScale(game.fontScale);
        totalBestTimeValue.setAlignment(Align.right);

        levelTable.add(totalHighScoreLabel).colspan(2).align(Align.right);
        levelTable.add(totalHighScoreValue);
        levelTable.add(totalBestTimeValue).colspan(2).align(Align.right);
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
                stage.addAction(Transitions.getQuickFadeOut(new Runnable() {
                    @Override
                    public void run() {
                        game.setScreen(WorldSelectScreen.get(game));
                    }
                }));
            }
        });
        return returnButton;
    }

    private int buildLevelRow(final LevelInstance level, Table table) {
        ClickListener listener = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                stage.addAction(Transitions.getQuickFadeOut(new Runnable() {
                    @Override
                    public void run() {
                        game.setScreen(LevelGateScreen.get(game, new GameScreen(game, world, level)));
                    }
                }));
            }
        };

        RotatingLabel levelNameLabel = new RotatingLabel(level.levelDef.name, game.fontScale, skin, listener);

        int levelHighScore = level.getHighScore();

        int score = levelHighScore;
        if (score == com.bitdecay.helm.prefs.GamePrefs.SCORE_NOT_SET) {
            score = 0;
        }

        Label levelScoreLabel = new Label(Integer.toString(score), skin);
        levelScoreLabel.setAlignment(Align.right);
        levelScoreLabel.setFontScale(game.fontScale);

        float levelTimeScore = level.getBestTime();

        Label levelTimeLabel = new Label(TimerUtils.getFormattedTime(levelTimeScore), skin);
        if (levelTimeScore == com.bitdecay.helm.prefs.GamePrefs.TIME_NOT_SET) {
            levelTimeLabel.setText("--");
        }
        levelTimeLabel.setAlignment(Align.right);
        levelTimeLabel.setFontScale(game.fontScale);

        table.add(levelNameLabel).colspan(2).expandX().fillX();
        table.add(levelScoreLabel);
        addScoreMedal(table, level);
        table.add(levelTimeLabel).padLeft(com.bitdecay.helm.menu.MedalUtils.imageSize / 2);
        addTimeMedal(table, level);
        table.row().padTop(game.fontScale * 10);
        return levelHighScore;
    }

    private void addScoreMedal(Table table, com.bitdecay.helm.world.LevelInstance level) {
        Image medalImage = MedalUtils.getIconForHighScore(level);
        table.add(medalImage).size(MedalUtils.imageSize, MedalUtils.imageSize).expand(false, false).fill(false);
    }

    private void addTimeMedal(Table table, com.bitdecay.helm.world.LevelInstance level) {
        Image medalImage = MedalUtils.getIconForBestTime(level);
        table.add(medalImage).size(MedalUtils.imageSize, MedalUtils.imageSize).expand(false, false).fill(false);
    }
}
