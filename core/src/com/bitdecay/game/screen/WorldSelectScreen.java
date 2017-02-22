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
import com.bitdecay.game.unlock.StatName;
import com.bitdecay.game.world.ChannelsWorld;
import com.bitdecay.game.world.GravityWorld;
import com.bitdecay.game.world.IslandsWorld;
import com.bitdecay.game.world.LevelWorld;
import com.bitdecay.game.world.World1;
import com.bitdecay.game.world.World2;
import com.bitdecay.game.world.ExtremeWorld;

/**
 * Created by Monday on 1/10/2017.
 */
public class WorldSelectScreen extends AbstractScrollingItemScreen {

    public WorldSelectScreen(final Helm game) {
        super(game);
        build();
    }

    @Override
    public void populateRows(Table worldTable) {
        itemTable.columnDefaults(1).expandX();
        itemTable.columnDefaults(2).width(game.fontScale * 50);
        itemTable.columnDefaults(3).width(game.fontScale * 50);

        LevelWorld[] worlds = new LevelWorld[] {
                new World1(),
                new World2(),
                new IslandsWorld(),
                new ChannelsWorld(),
                new GravityWorld(),
                new ExtremeWorld(),
        };

        int totalHighScore = 0;
        float totalBestTime = 0;

        boolean allLevelsTimed = true;

        int levelsCompleted = Helm.prefs.getInteger(StatName.LEVELS_COMPLETED.preferenceID);

        for (LevelWorld world : worlds) {
            if (Helm.debug || levelsCompleted >= world.requiredLevelsForUnlock) {
                buildWorldRow(world, worldTable);
            } else {
                // build some kind of "<x> more to unlock" row
            }
            totalHighScore += world.getHighScore();

            if (world.getBestTime() == GamePrefs.TIME_NOT_SET) {
                allLevelsTimed = false;
            } else {
                totalBestTime += world.getBestTime();
            }
        }

        if (!allLevelsTimed) {
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

        worldTable.add(totalHighScoreLabel).colspan(2).align(Align.right);
        worldTable.add(totalHighScoreValue);
        worldTable.add(totalBestTimeValue);
    }

    @Override
    public Actor getReturnButton() {
        TextButton returnButton = new TextButton("Return to Title Screen", skin);
        returnButton.getLabel().setFontScale(game.fontScale);
        returnButton.align(Align.bottomRight);
        returnButton.setOrigin(Align.bottomRight);
        returnButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new TitleScreen(game));
            }
        });
        return returnButton;
    }

    private int buildWorldRow(final LevelWorld world, Table table) {
        ClickListener listener = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new LevelSelectScreen(game, world));
            }
        };

        TextButton goButton = new TextButton("Go!", skin);
        goButton.getLabel().setFontScale(game.fontScale);
        goButton.addListener(listener);

        Label worldNameLabel = new Label(world.getWorldName(), skin);
        worldNameLabel.setAlignment(Align.center);
        worldNameLabel.setFontScale(game.fontScale);

        worldNameLabel.addListener(listener);

        int worldHighScore = world.getHighScore();

        Label worldScoreLabel = new Label(Integer.toString(worldHighScore), skin);
        worldScoreLabel.setAlignment(Align.right);
        worldScoreLabel.setFontScale(game.fontScale);

        float worldTimeScore = world.getBestTime();

        Label worldTimeLabel = new Label(TimerUtils.getFormattedTime(worldTimeScore), skin);
        if (worldTimeScore == GamePrefs.TIME_NOT_SET) {
            worldTimeLabel.setText("--");
        }
        worldTimeLabel.setAlignment(Align.right);
        worldTimeLabel.setFontScale(game.fontScale);

        table.add(goButton).expand(false, false);
        table.add(worldNameLabel);
        table.add(worldScoreLabel);
        table.add(worldTimeLabel);
        table.row().padTop(game.fontScale * 10);
        return worldHighScore;
    }
}
