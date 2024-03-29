package com.bitdecay.helm.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.bitdecay.helm.Helm;
import com.bitdecay.helm.menu.MedalUtils;
import com.bitdecay.helm.menu.RotatingLabel;
import com.bitdecay.helm.sound.SFXLibrary;
import com.bitdecay.helm.sound.SoundMode;
import com.bitdecay.helm.unlock.StatName;
import com.bitdecay.helm.world.LevelInstance;
import com.bitdecay.helm.world.WorldInstance;
import com.bitdecay.helm.world.WorldOrderMarker;
import com.bitdecay.helm.world.WorldUtils;

/**
 * Created by Monday on 1/10/2017.
 */
public class WorldSelectScreen extends AbstractScrollingItemScreen {

    private static WorldSelectScreen instance;

    public static WorldSelectScreen get(Helm game) {
        if (instance == null) {
            instance = new WorldSelectScreen(game);
        }
        return instance;
    }

    public WorldSelectScreen(final Helm game) {
        super(game);
        requestAutoScroll = true;
        requestedScrollPercent = 1;
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

        Array<com.bitdecay.helm.world.WorldInstance> worlds = WorldUtils.getWorlds();

        int levelsCompleted = Helm.prefs.getInteger(StatName.LEVELS_COMPLETED.preferenceID);

        if (Helm.debug) {
            WorldOrderMarker testWorld = new WorldOrderMarker();
            testWorld.worldFile = "testWorld.json";
            testWorld.requiredLevelsForUnlock = 0;
            WorldInstance testInstance = WorldUtils.buildWorldInstance(testWorld);
            buildWorldRow(testInstance, worldTable);
            worldTable.row().padTop(game.fontScale * 10);
        }


        // print level count
        int levelCount = 0;
        for (WorldInstance world : worlds) {
            levelCount += world.levels.size;
            if (Helm.debug) {
                for (LevelInstance level : world.levels) {
                    if (level.getScoreNeededForMedal(MedalUtils.LevelRating.DEV) == Integer.MAX_VALUE) {
                        System.out.println(level.levelDef.name + " does not have a dev score");
                    }

                    if (level.getTimeNeededForMedal(MedalUtils.LevelRating.DEV) == 0) {
                        System.out.println(level.levelDef.name + " does not have a dev time");
                    }
                }
            }
        }

        game.totalLevels = levelCount;

        if (Helm.debug) {
            System.out.println(levelCount + " levels across all worlds");
        }

        buildTutorialRow(worldTable);
        worldTable.row().padTop(game.fontScale * 20);

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

    protected void addTotalsRow(Table worldTable, Array<com.bitdecay.helm.world.WorldInstance> worlds) {
        int totalHighScore = 0;
        float totalBestTime = 0;

        boolean allLevelsTimed = true;
        for (com.bitdecay.helm.world.WorldInstance world : worlds) {
            totalHighScore += world.getHighScore();

            if (world.getBestTime() == com.bitdecay.helm.prefs.GamePrefs.TIME_NOT_SET) {
                allLevelsTimed = false;
            } else {

                totalBestTime += world.getBestTime();
            }
        }

        if (!allLevelsTimed) {
            totalBestTime = com.bitdecay.helm.prefs.GamePrefs.TIME_NOT_SET;
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

        worldTable.add(totalHighScoreLabel).colspan(2).align(Align.right);
        worldTable.add(totalHighScoreValue);
        worldTable.add(totalBestTimeValue).colspan(2).align(Align.right);
    }

    @Override
    public ClickListener getReturnButtonAction() {
        return new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                AudioUtils.doSound(game, SoundMode.PLAY, SFXLibrary.MENU_BOOP);
                Gdx.input.vibrate(10);
                stage.addAction(Transitions.getQuickFadeOut(new Runnable() {
                    @Override
                    public void run() {
                        game.setScreen(TitleScreen.get(game));
                    }
                }));
            }
        };
    }

    private void buildTutorialRow(Table table) {
        ClickListener listener = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                AudioUtils.doSound(game, SoundMode.PLAY, SFXLibrary.MENU_BOOP);
                Gdx.input.vibrate(10);
                stage.addAction(Transitions.getQuickFadeOut(new Runnable() {
                    @Override
                    public void run() {
                        game.setScreen(new TutorialScreen(game));
                    }
                }));
            }
        };

        RotatingLabel tutorialLabel = new RotatingLabel("Tutorial", game.fontScale, skin, listener);
        table.add(tutorialLabel).colspan(2).expandX().fillX();
    }

    private int buildWorldRow(final com.bitdecay.helm.world.WorldInstance world, Table table) {
        ClickListener listener = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                AudioUtils.doSound(game, SoundMode.PLAY, SFXLibrary.MENU_BOOP);
                Gdx.input.vibrate(10);
                stage.addAction(Transitions.getQuickFadeOut(new Runnable() {
                    @Override
                    public void run() {
                        game.setScreen(LevelSelectScreen.get(game, world));
                    }
                }));
            }
        };

        RotatingLabel worldNameLabel = new RotatingLabel(world.getWorldName(), game.fontScale, skin, listener);

        int worldHighScore = world.getHighScore();

        Label worldScoreLabel = new Label(Integer.toString(worldHighScore), skin);
        worldScoreLabel.setAlignment(Align.right);
        worldScoreLabel.setFontScale(game.fontScale);

        float worldTimeScore = world.getBestTime();

        Label worldTimeLabel = new Label(com.bitdecay.helm.time.TimerUtils.getFormattedTime(worldTimeScore), skin);
        if (worldTimeScore == com.bitdecay.helm.prefs.GamePrefs.TIME_NOT_SET) {
            worldTimeLabel.setText("--");
        }
        worldTimeLabel.setAlignment(Align.right);
        worldTimeLabel.setFontScale(game.fontScale);

        table.add(worldNameLabel).colspan(2).expandX().fillX();
        table.add(worldScoreLabel);
        addScoreMedal(table, world);
        table.add(worldTimeLabel).padLeft(com.bitdecay.helm.menu.MedalUtils.imageSize / 2);
        addTimeMedal(table, world);
        return worldHighScore;
    }

    private void addScoreMedal(Table table, com.bitdecay.helm.world.WorldInstance world) {
        Image medalImage = com.bitdecay.helm.menu.MedalUtils.getRankImage(world.getBestScoreMedal());
        table.add(medalImage).size(com.bitdecay.helm.menu.MedalUtils.imageSize, com.bitdecay.helm.menu.MedalUtils.imageSize).expand(false, false).fill(false);
    }

    private void addTimeMedal(Table table, com.bitdecay.helm.world.WorldInstance world) {
        Image medalImage = com.bitdecay.helm.menu.MedalUtils.getRankImage(world.getBestTimeMedal());
        table.add(medalImage).size(com.bitdecay.helm.menu.MedalUtils.imageSize, com.bitdecay.helm.menu.MedalUtils.imageSize).expand(false, false).fill(false);
    }

    public void buildHintedUnlockRow(com.bitdecay.helm.world.WorldInstance world, Table worldTable, int levelsCompleted) {
        int left = world.requiredLevelsForUnlock - levelsCompleted;

        RotatingLabel worldNameLabel = new RotatingLabel(world.getWorldName(), game.fontScale, skin, null);
        worldNameLabel.innerLabel.setColor(Color.GRAY);
        worldTable.add(worldNameLabel).colspan(2);

        String simplifiedHint = "Complete " + left + " more " + (left > 1 ? "levels" : "level");
        Label hintLabel = new Label(simplifiedHint, skin);
        hintLabel.setColor(Color.GRAY);
        hintLabel.setAlignment(Align.center);
        hintLabel.setFontScale(game.fontScale);
        worldTable.add(hintLabel).colspan(4);

    }

    @Override
    public void hide() {
        super.hide();
        requestedScrollPercent = scroll.getScrollPercentY();
    }

    @Override
    public void show() {
        super.show();
        requestAutoScroll = true;
    }

    @Override
    public void dispose() {
        super.dispose();
        // if we dispose the screen, create a new instance next time we need it
        instance = null;
    }
}
