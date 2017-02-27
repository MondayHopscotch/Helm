package com.bitdecay.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.bitdecay.game.Helm;
import com.bitdecay.game.persist.JsonUtils;
import com.bitdecay.game.prefs.GamePrefs;
import com.bitdecay.game.time.TimerUtils;
import com.bitdecay.game.unlock.StatName;
import com.bitdecay.game.world.LevelDefinition;
import com.bitdecay.game.world.WorldDefinition;
import com.bitdecay.game.world.WorldInstance;

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

        Array<WorldInstance> worlds = new Array<>();

        FileHandle worldDirectory = Gdx.files.internal("level/worlds/");
        for (FileHandle worldFile : worldDirectory.list()) {
            worlds.add(buildWorldInstance(worldFile));
        }

        int totalHighScore = 0;
        float totalBestTime = 0;

        boolean allLevelsTimed = true;

        int levelsCompleted = Helm.prefs.getInteger(StatName.LEVELS_COMPLETED.preferenceID);

        for (WorldInstance world : worlds) {
            totalHighScore += world.getHighScore();

            if (world.getBestTime() == GamePrefs.TIME_NOT_SET) {
                allLevelsTimed = false;
            } else {
                totalBestTime += world.getBestTime();
            }

            if (levelsCompleted >= world.requiredLevelsForUnlock) {
                buildWorldRow(world, worldTable);
                worldTable.row().padTop(game.fontScale * 10);
            } else {
                // build some kind of "<x> more to unlock" row
                buildHintedUnlockRow(world, worldTable, levelsCompleted);
                worldTable.row().padTop(game.fontScale * 10);
                break;
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

    private WorldInstance buildWorldInstance(FileHandle worldFile) {
        WorldDefinition worldDef = JsonUtils.unmarshal(WorldDefinition.class, worldFile);
        WorldInstance worldInstance = new WorldInstance(worldDef.requiredLevelsForUnlock);
        worldInstance.name = worldDef.worldName;
        for (String levelPath : worldDef.levelList) {
            worldInstance.addLevelInstance(JsonUtils.unmarshal(LevelDefinition.class, Gdx.files.internal(levelPath)));
        }
        return worldInstance;
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

    private int buildWorldRow(final WorldInstance world, Table table) {
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
        return worldHighScore;
    }

    public void buildHintedUnlockRow(WorldInstance world, Table worldTable, int levelsCompleted) {
        int left = world.requiredLevelsForUnlock - levelsCompleted;
        String remainingText = "Beat " + left + " more " + (left > 1 ? "levels" : "level") + " to unlock next world";

        Label leftTillUnlockLabel = new Label(remainingText, skin);
        leftTillUnlockLabel.setColor(Color.GRAY);
        leftTillUnlockLabel.setAlignment(Align.center);
        leftTillUnlockLabel.setFontScale(game.fontScale);

        worldTable.add(leftTillUnlockLabel).colspan(4);
    }
}
