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
        Table timeTable = new Table(skin);


        if (level.getBestTime() != com.bitdecay.helm.prefs.GamePrefs.TIME_NOT_SET &&
                level.getHighScore() != com.bitdecay.helm.prefs.GamePrefs.SCORE_NOT_SET) {
            buildCurrentScoreParts(game, level, scoreTable, skin);
            buildCurrentTimeParts(game, level, timeTable, skin);
        }

        buildTargetScoreParts(game, level, scoreTable, skin);
        buildTargetTimeParts(game, level, timeTable, skin);

        table.add(scoreTable).padRight(10 * game.fontScale);
        table.add(timeTable).padLeft(10 * game.fontScale);
        return table;
    }

    private static void buildCurrentScoreParts(Helm game, LevelInstance level, Table table, Skin skin) {
        if (level.getHighScore() == com.bitdecay.helm.prefs.GamePrefs.SCORE_NOT_SET) {
            return;
        }

        Label scoreTextLabel = new Label("Current Best Score", skin);
        scoreTextLabel.setFontScale(INFO_FONT_SCALE * game.fontScale);
        scoreTextLabel.setAlignment(Align.center);
        scoreTextLabel.setOrigin(Align.center);

        Image medalImage = MedalUtils.getIconForHighScore(level);
        Label scoreLabel = new Label(Integer.toString(level.getHighScore()), skin);
        scoreLabel.setFontScale(INFO_FONT_SCALE * game.fontScale);
        scoreLabel.setAlignment(Align.center);
        scoreLabel.setOrigin(Align.center);

        Table scoreTable = new Table(skin);
        scoreTable.add(medalImage).size(MedalUtils.imageSize, MedalUtils.imageSize).expand(false, false).fill(false);
        scoreTable.add(scoreLabel);

        table.add(scoreTextLabel).row();
        table.add(scoreTable).row();
    }

    private static void buildTargetScoreParts(Helm game, LevelInstance level, Table table, Skin skin) {
        MedalUtils.LevelRating scoreRank = MedalUtils.getScoreRank(level, level.getHighScore());
        if (MedalUtils.LevelRating.DEV.equals(scoreRank)) {
            return;
        }

        Label scoreTextLabel = new Label("Score for " + scoreRank.nextRank().medalName() + " medal", skin);
        scoreTextLabel.setFontScale(INFO_FONT_SCALE * game.fontScale);
        scoreTextLabel.setAlignment(Align.center);
        scoreTextLabel.setOrigin(Align.center);

        Image medalImage = MedalUtils.getRankImage(scoreRank.nextRank());
        Label scoreLabel = new Label(Integer.toString(level.getScoreNeededForMedal(scoreRank.nextRank())), skin);
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

    private static void buildCurrentTimeParts(Helm game, LevelInstance level, Table table, Skin skin) {
        if (level.getBestTime() == com.bitdecay.helm.prefs.GamePrefs.TIME_NOT_SET) {
            return;
        }

        Label timeTextLabel = new Label("Current Best Time", skin);
        timeTextLabel.setFontScale(INFO_FONT_SCALE * game.fontScale);
        timeTextLabel.setAlignment(Align.center);
        timeTextLabel.setOrigin(Align.center);

        Image medalImage = MedalUtils.getIconForBestTime(level);
        Label timeLabel = new Label(TimerUtils.getFormattedTime(level.getBestTime()), skin);
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

    private static void buildTargetTimeParts(Helm game, LevelInstance level, Table table, Skin skin) {
        MedalUtils.LevelRating timeRank = MedalUtils.getTimeRank(level, level.getBestTime());
        if (MedalUtils.LevelRating.DEV.equals(timeRank)) {
            return;
        }

        Label timeTextLabel = new Label("Time for " + timeRank.nextRank().medalName() + " medal", skin);
        timeTextLabel.setFontScale(INFO_FONT_SCALE * game.fontScale);
        timeTextLabel.setAlignment(Align.center);
        timeTextLabel.setOrigin(Align.center);

        Image medalImage = MedalUtils.getRankImage(timeRank.nextRank());
        Label scoreLabel = new Label(TimerUtils.getFormattedTime(level.getTimeNeededForMedal(timeRank.nextRank())), skin);
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
}
