package com.bitdecay.helm.screen;

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
 * Created by Monday on 10/16/2017.
 */

public class ScreenElements {

    private static float INFO_FONT_SCALE = 1.0f;

    public static  Table getGoalsElement(Table table, Helm game, LevelInstance level, Skin skin) {
        table.clear();

        Table scoreTable = new Table(skin);
        scoreTable.setOrigin(Align.bottomLeft);

        Table timeTable = new Table(skin);
        timeTable.setOrigin(Align.bottomRight);

        buildCurrentScoreParts(game, level, scoreTable, skin);
        scoreTable.add(getBlankLabel(skin)).padBottom(game.fontScale * 10).row();

        buildCurrentTimeParts(game, level, timeTable, skin);
        timeTable.add(getBlankLabel(skin)).padBottom(game.fontScale * 10).row();

        buildScoreTable(game, level, scoreTable, skin);
        buildTimeTable(game, level, timeTable, skin);

        table.add(scoreTable).align(Align.bottomLeft)
                .padLeft(10 * game.fontScale)
                .padBottom(10 * game.fontScale)
                .expandX();
        table.add(timeTable).align(Align.bottomRight)
                .padRight(10 * game.fontScale)
                .padBottom(10 * game.fontScale)
                .expandX();
        return table;
    }

    private static Label getBlankLabel(Skin skin) {
        return new Label("", skin);
    }

    private static void buildCurrentScoreParts(Helm game, LevelInstance level, Table table, Skin skin) {
        Label scoreTextLabel = new Label("Current Best Score", skin);
        scoreTextLabel.setFontScale(INFO_FONT_SCALE * game.fontScale);
        scoreTextLabel.setAlignment(Align.center);
        scoreTextLabel.setOrigin(Align.center);

        Image medalImage = MedalUtils.getIconForHighScore(level);
        Label scoreLabel = new Label(Integer.toString(level.getHighScore()), skin);
        if (level.getHighScore() == com.bitdecay.helm.prefs.GamePrefs.SCORE_NOT_SET) {
            scoreLabel.setText("-----");
        }
        scoreLabel.setFontScale(INFO_FONT_SCALE * game.fontScale);
        scoreLabel.setAlignment(Align.center);
        scoreLabel.setOrigin(Align.center);

        Table scoreTable = new Table(skin);
        scoreTable.add(medalImage).size(MedalUtils.imageSize, MedalUtils.imageSize).expand(false, false).fill(false);
        scoreTable.add(scoreLabel);

        table.add(scoreTextLabel).row();
        table.add(scoreTable).row();
    }

    private static void buildScoreTable(Helm game, LevelInstance level, Table table, Skin skin) {
        Table innerScoreTable = new Table(skin);
        for (int i = MedalUtils.LevelRating.values().length - 1; i > 0; i--) {
            MedalUtils.LevelRating medal = MedalUtils.LevelRating.values()[i];

            Image medalImage = MedalUtils.getRankImage(medal);
            Label scoreLabel = new Label(Integer.toString(level.getScoreNeededForMedal(medal)), skin);
            scoreLabel.setFontScale(INFO_FONT_SCALE * game.fontScale);
            scoreLabel.setAlignment(Align.center);
            scoreLabel.setOrigin(Align.center);

            Table scoreMedalTable = new Table(skin);
            innerScoreTable.add(medalImage).size(MedalUtils.imageSize, MedalUtils.imageSize).align(Align.left).expand(false, false).fill(false);
            innerScoreTable.add(scoreLabel).expandX();
            innerScoreTable.add(scoreMedalTable).row();
        }
        table.add(innerScoreTable).row();
    }

    private static void buildCurrentTimeParts(Helm game, LevelInstance level, Table table, Skin skin) {
        Label timeTextLabel = new Label("Current Best Time", skin);
        timeTextLabel.setFontScale(INFO_FONT_SCALE * game.fontScale);
        timeTextLabel.setAlignment(Align.center);
        timeTextLabel.setOrigin(Align.center);

        Image medalImage = MedalUtils.getIconForBestTime(level);
        Label timeLabel = new Label(TimerUtils.getFormattedTime(level.getBestTime()), skin);
        if (level.getBestTime() == com.bitdecay.helm.prefs.GamePrefs.TIME_NOT_SET) {
            timeLabel.setText("--.---");
        }
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

    private static void buildTimeTable(Helm game, LevelInstance level, Table table, Skin skin) {
        Table innerTimeTable = new Table(skin);
        for (int i = MedalUtils.LevelRating.values().length - 1; i > 0; i--) {
            MedalUtils.LevelRating medal = MedalUtils.LevelRating.values()[i];

            Image medalImage = MedalUtils.getRankImage(medal);
            Label timeLabel = new Label(TimerUtils.getFormattedTime(level.getTimeNeededForMedal(medal)), skin);
            timeLabel.setFontScale(INFO_FONT_SCALE * game.fontScale);
            timeLabel.setAlignment(Align.center);
            timeLabel.setOrigin(Align.center);

            Table timeMedalTable = new Table(skin);
            innerTimeTable.add(medalImage).size(MedalUtils.imageSize, MedalUtils.imageSize).align(Align.left).expand(false, false).fill(false);
            innerTimeTable.add(timeLabel).expandX();
            innerTimeTable.add(timeMedalTable).row();
        }
        table.add(innerTimeTable).row();
    }
}
