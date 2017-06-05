package com.bitdecay.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.bitdecay.game.Helm;
import com.bitdecay.game.menu.MedalUtils;
import com.bitdecay.game.persist.JsonUtils;
import com.bitdecay.game.prefs.GamePrefs;
import com.bitdecay.game.time.TimerUtils;
import com.bitdecay.game.unlock.StatName;
import com.bitdecay.game.world.LevelDefinition;
import com.bitdecay.game.world.WorldDefinition;
import com.bitdecay.game.world.WorldInstance;
import com.bitdecay.game.world.WorldOrderMarker;

import static com.bitdecay.game.persist.JsonUtils.json;

/**
 * Created by Monday on 1/10/2017.
 */
public class WorldSelectScreen extends AbstractScrollingItemScreen {

    private static WorldSelectScreen instance;

    public static WorldSelectScreen get(Helm game) {
        if (instance == null) {
            instance = new WorldSelectScreen(game);
        } else {
            instance.build(false);
        }
        return instance;
    }

    public WorldSelectScreen(final Helm game) {
        super(game);
        build(true);
    }

    @Override
    public String getTitle() {
        return "World Select";
    }

    @Override
    public void populateRows(Table worldTable) {
        itemTable.columnDefaults(1).expandX();
        itemTable.columnDefaults(2).width(game.fontScale * 50);
        itemTable.columnDefaults(3).width(MedalUtils.imageSize);
        itemTable.columnDefaults(4).width(game.fontScale * 50);
        itemTable.columnDefaults(5).width(MedalUtils.imageSize);

        Array<WorldInstance> worlds = new Array<>();

        FileHandle worldDirectory = Gdx.files.internal("level/world_defs/worldOrder.json");
        WorldOrderMarker[] worldsInOrder = JsonUtils.unmarshal(WorldOrderMarker[].class, worldDirectory);
        for (WorldOrderMarker worldMarker : worldsInOrder) {
            worlds.add(buildWorldInstance(worldMarker));
        }

        int levelsCompleted = Helm.prefs.getInteger(StatName.LEVELS_COMPLETED.preferenceID);

        boolean allWorldsUnlocked = true;
        for (WorldInstance world : worlds) {
            if (levelsCompleted >= world.requiredLevelsForUnlock) {
                buildWorldRow(world, worldTable);
                worldTable.row().padTop(game.fontScale * 10);
            } else {
                allWorldsUnlocked = false;
                buildHintedUnlockRow(world, worldTable, levelsCompleted);
                worldTable.row().padTop(game.fontScale * 10);
                break;
            }

        }

        if (allWorldsUnlocked) {
            addTotalsRow(worldTable, worlds);
        }
    }

    protected void addTotalsRow(Table worldTable, Array<WorldInstance> worlds) {
        int totalHighScore = 0;
        float totalBestTime = 0;

        boolean allLevelsTimed = true;
        for (WorldInstance world : worlds) {
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
        worldTable.add(totalBestTimeValue).colspan(2).align(Align.right);
    }

    private WorldInstance buildWorldInstance(WorldOrderMarker marker) {
        FileHandle worldFile = Gdx.files.internal("level/world_defs/" + marker.worldFile);
        WorldDefinition worldDef = JsonUtils.unmarshal(WorldDefinition.class, worldFile);
        WorldInstance worldInstance = new WorldInstance(marker.requiredLevelsForUnlock);
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
        addScoreMedal(table, world);
        table.add(worldTimeLabel).padLeft(MedalUtils.imageSize / 2);
        addTimeMedal(table, world);
        return worldHighScore;
    }

    private void addScoreMedal(Table table, WorldInstance world) {
        Image medalImage = MedalUtils.getRankImage(world.getBestScoreMedal());
        table.add(medalImage).size(MedalUtils.imageSize, MedalUtils.imageSize).expand(false, false).fill(false);
    }

    private void addTimeMedal(Table table, WorldInstance world) {
        Image medalImage = MedalUtils.getRankImage(world.getBestTimeMedal());
        table.add(medalImage).size(MedalUtils.imageSize, MedalUtils.imageSize).expand(false, false).fill(false);
    }

    public void buildHintedUnlockRow(WorldInstance world, Table worldTable, int levelsCompleted) {
        int left = world.requiredLevelsForUnlock - levelsCompleted;
        String remainingText = "Beat " + left + " more " + (left > 1 ? "levels" : "level") + " to unlock '" + world.getWorldName() + "'";

        Label leftTillUnlockLabel = new Label(remainingText, skin);
        leftTillUnlockLabel.setColor(Color.GRAY);
        leftTillUnlockLabel.setAlignment(Align.center);
        leftTillUnlockLabel.setFontScale(game.fontScale);

        worldTable.add(leftTillUnlockLabel).colspan(6);
    }

    @Override
    public void dispose() {
        super.dispose();
        // if we dispose the screen, create a new instance next time we need it
        instance = null;
    }
}
