package com.bitdecay.helm.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.bitdecay.helm.GamePilot;
import com.bitdecay.helm.input.InputReplay;
import com.bitdecay.helm.menu.MedalUtils;
import com.bitdecay.helm.menu.Overlay;
import com.bitdecay.helm.menu.PauseMenu;
import com.bitdecay.helm.persist.ReplayUtils;
import com.bitdecay.helm.prefs.GamePrefs;
import com.bitdecay.helm.Helm;
import com.bitdecay.helm.menu.ScoreMenu;
import com.bitdecay.helm.scoring.LandingScore;
import com.bitdecay.helm.scoring.ScoreStamps;
import com.bitdecay.helm.sound.MusicLibrary;
import com.bitdecay.helm.sound.SoundMode;
import com.bitdecay.helm.time.TimerUtils;
import com.bitdecay.helm.unlock.StatName;
import com.bitdecay.helm.unlock.palette.GameColors;
import com.bitdecay.helm.world.LevelInstance;
import com.bitdecay.helm.world.ReadOnlyLevelInstance;
import com.bitdecay.helm.world.WorldInstance;

/**
 * Created by Monday on 12/15/2016.
 */
public class GameScreen implements Screen, GamePilot {
    private Helm game;

    LevelPlayer levelPlayer;

    private WorldInstance activeWorld;
    public LevelInstance currentLevel;
    private InputReplay currentReplay;

    private boolean reloadQueued;

    private ScoreMenu scoreMenu;
    private PauseMenu pauseMenu;
    private boolean canTogglePause = true;
    private boolean paused = false;
    private Overlay overlay;

    private InputMultiplexer combinedGameInput;

    private Array<Long> updateFrameTimes;
    private Array<Long> renderFrameTimes;

    private enum PlayMode {
        PLAY_MODE,
        REPLAY_MODE
    }

    private PlayMode currentMode;

    public GameScreen(Helm game, WorldInstance world, LevelInstance level) {
        this.game = game;

        currentMode = PlayMode.PLAY_MODE;

        levelPlayer = new LevelPlayer(this, false);
        initMenus();
        combinedGameInput = new InputMultiplexer(pauseMenu.stage, levelPlayer.getInput());

        currentLevel = level;
        activeWorld = world;
        currentReplay = null;


        requestRestartLevel();
    }

    public GameScreen(Helm game, InputReplay replay) {
        this.game = game;

        currentMode = PlayMode.REPLAY_MODE;

        levelPlayer = new LevelPlayer(this, true);
        initMenus();
        combinedGameInput = new InputMultiplexer(pauseMenu.stage, levelPlayer.getInput());
        currentReplay = replay;
        currentLevel = null;
        activeWorld = null;
        requestRestartLevel();
    }

    private void initMenus() {
        scoreMenu = new ScoreMenu(this);
        pauseMenu = new PauseMenu(this);
        overlay = new Overlay(this);
    }

    private void setLevel(LevelInstance level) {
        levelPlayer.loadLevel(level.levelDef);
        Gdx.input.setInputProcessor(combinedGameInput);
        scoreMenu.visible = false;
        pauseMenu.setLevel(level);
    }

    private void setReplay(InputReplay replay) {
        levelPlayer.loadReplay(replay);
        Gdx.input.setInputProcessor(combinedGameInput);
        scoreMenu.visible = false;
    }

    @Override
    public void show() {

    }

    @Override
    public void requestRestartLevel() {
        reloadQueued = true;
        scoreMenu.visible = false;
    }

    @Override
    public void doSound(SoundMode mode, String soundName) {
        AudioUtils.doSound(game, mode, soundName);
    }

    @Override
    public void doMusic(SoundMode mode, String musicName) {
        AudioUtils.doMusic(game, mode, musicName);
    }

    @Override
    public String getLevelName() {
        return currentLevel.levelDef.name;
    }

    @Override
    public void saveLastReplay(String replayName) {
        if (replayName == null) {
            replayName = TimerUtils.getDateAsString() + " - " + currentLevel.levelDef.name;
        }
        System.out.println("Saving replay: " + replayName);
        ReplayUtils.saveReplay(replayName, levelPlayer.recordedInput);
    }

    @Override
    public void finishLevel(LandingScore score) {
        levelPlayer.stopTickCapture();
        switch (currentMode) {
            case PLAY_MODE:
                scoreRun(score);
                break;
            case REPLAY_MODE:
                showScoreMenu(score, true);
                break;
        }
    }

    private void scoreRun(LandingScore score) {
        int levelScore = score.total();
        if (Helm.debug) {
            System.out.println("ANGLE: " + score.angleScore + " SPEED: " + score.speedScore);
            System.out.println("SCORE: " + levelScore);
        }

        if (currentLevel.getHighScore() == GamePrefs.SCORE_NOT_SET) {
            // first time beating the level!
            Helm.stats.count(StatName.LEVELS_COMPLETED, 1);

            int levelsCompleted = Helm.prefs.getInteger(StatName.LEVELS_COMPLETED.preferenceID, Integer.MIN_VALUE);
            if (levelsCompleted >= game.totalLevels) {
                // player finished all levels
                if (!Helm.prefs.getBoolean(GamePrefs.ALERTED_OF_DEV_MEDALS)) {
                    Helm.prefs.putBoolean(GamePrefs.SHOW_DEV_MEDAL_DIALOG, true);
                }
            }
        }

        if (score.total() >= currentLevel.getScoreNeededForMedal(MedalUtils.LevelRating.DEV)) {
            ScoreStamps.addPendingStamp("Dev score beaten!");
        }

        if (score.timeTaken <= currentLevel.getTimeNeededForMedal(MedalUtils.LevelRating.DEV)) {
            ScoreStamps.addPendingStamp("Dev time beaten!");
        }

        score.pointsImprovement = currentLevel.maybeSetNewHighScore(levelScore);
        score.secondsImprovement = currentLevel.maybeSetNewBestTime(score.timeTaken);
        showScoreMenu(score, false);
    }

    private void showScoreMenu(LandingScore score, boolean isReplay) {
        scoreMenu.rebuildButtonTable(isReplay);
        scoreMenu.visible = true;
        if (PlayMode.REPLAY_MODE.equals(currentMode)) {
            scoreMenu.setScore(new ReadOnlyLevelInstance(currentReplay.levelDef), score);
        } else {
            scoreMenu.setScore(currentLevel, score);
        }
        Gdx.input.setInputProcessor(scoreMenu.stage);
        levelPlayer.resetInputSystems();
    }

    @Override
    public void returnToMenus(boolean isQuit) {
        if (activeWorld == null) {
            // we are in a replay (probably want to have a more explicit way of handling this
            game.setScreen(TransitionColorScreen.get(game, Color.BLACK, new ReplaySelectScreen(game)));
        } else {
            if (isQuit) {
                Helm.stats.count(StatName.ABANDONS, 1);
            }
            game.setScreen(TransitionColorScreen.get(game, Color.BLACK, new LevelSelectScreen(game, activeWorld)));
        }
    }

    @Override
    public void goToNextLevel() {
        if (activeWorld == null) {
            // not sure if this is a reasonable check or not
            returnToMenus(false);
        }

        int levelPos = -1;
        for (int i = 0; i < activeWorld.levels.size; i++) {
            if (activeWorld.levels.get(i) == currentLevel) {
                levelPos = i;
                break;
            }
        }

        if (levelPos == -1 || levelPos + 1 >= activeWorld.levels.size) {
            returnToMenus(false);
        } else {
            LevelInstance nextLevel = activeWorld.levels.get(levelPos + 1);
            game.setScreen(LevelGateScreen.get(game, new GameScreen(game, activeWorld, nextLevel)));
        }
    }

    @Override
    public Helm getHelm() {
        return game;
    }

    @Override
    public void togglePause() {
        paused = !paused;
        if (paused) {
            // since the player boost is a 'music' we have to pause it when the player pauses the game
            doMusic(SoundMode.PAUSE, MusicLibrary.SHIP_BOOST);
        }
    }

    @Override
    public void setTime(float secondsElapsed) {
        overlay.setTime(secondsElapsed);
    }

    @Override
    public void render(float delta) {
        if (delta > .5f) {
            delta = .5f;
        }
        Color clearColor = getHelm().palette.get(GameColors.BACKGROUND);
        Gdx.gl.glClearColor(clearColor.r, clearColor.g, clearColor.b, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        long startTime = TimeUtils.millis();

        if (!scoreMenu.visible) {
            if (canTogglePause && Gdx.input.isKeyPressed(Input.Keys.BACK)) {
                canTogglePause = false;
                pauseMenu.doPause();
            } else if (!Gdx.input.isKeyPressed(Input.Keys.BACK)) {
                // Force an explicit release of the key before we can pause again
                canTogglePause = true;
            }
        }

        if (!paused) {
            levelPlayer.update(delta);
            if (Helm.debug) {
                if (updateFrameTimes == null) {
                    updateFrameTimes = new Array<>(120);
                }
                updateFrameTimes.add(TimeUtils.millis() - startTime);
                if (updateFrameTimes.size >= 120) {
                    long totalTime = 0;
                    for (Long frameTime : updateFrameTimes) {
                        totalTime += frameTime;
                    }

                    if (Helm.debug) {
                        double average = 1.0 * totalTime / updateFrameTimes.size;
                        System.out.println("Average update time: " + average);
                    }
                    updateFrameTimes.clear();
                }
            }
        }

        startTime = TimeUtils.millis();
        levelPlayer.render(delta);
        if (Helm.debug) {
            if (renderFrameTimes == null) {
                renderFrameTimes = new Array<>(120);
            }
            renderFrameTimes.add(TimeUtils.millis() - startTime);
            if (renderFrameTimes.size >= 120) {
                long totalTime = 0;
                for (Long frameTime : renderFrameTimes) {
                    totalTime += frameTime;
                }

                if (Helm.debug) {
                    double average = 1.0 * totalTime / renderFrameTimes.size;
                    System.out.println("Average render time: " + average);
                }
                renderFrameTimes.clear();
            }
        }

        if (reloadQueued) {
            switch (currentMode) {
                case PLAY_MODE:
                    setLevel(currentLevel);
                    break;
                case REPLAY_MODE:
                    setReplay(currentReplay);
                    break;
            }
            reloadQueued = false;
        }

        scoreMenu.updateAndDraw();
        pauseMenu.updateAndDraw();
        overlay.updateAndDraw();
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
        levelPlayer.dispose();
    }
}
