package com.bitdecay.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.bitdecay.game.GamePilot;
import com.bitdecay.game.input.InputReplay;
import com.bitdecay.game.menu.Overlay;
import com.bitdecay.game.menu.PauseMenu;
import com.bitdecay.game.persist.ReplayUtils;
import com.bitdecay.game.prefs.GamePrefs;
import com.bitdecay.game.Helm;
import com.bitdecay.game.menu.ScoreMenu;
import com.bitdecay.game.scoring.LandingScore;
import com.bitdecay.game.sound.MusicLibrary;
import com.bitdecay.game.sound.SoundMode;
import com.bitdecay.game.time.TimerUtils;
import com.bitdecay.game.unlock.stats.StatName;
import com.bitdecay.game.unlock.palette.GameColors;
import com.bitdecay.game.world.LevelDefinition;
import com.bitdecay.game.world.LevelInstance;
import com.bitdecay.game.world.ReadOnlyLevelInstance;
import com.bitdecay.game.world.WorldInstance;

/**
 * Created by Monday on 12/15/2016.
 */
public class GameScreen implements Screen, GamePilot {
    private Helm game;

    LevelPlayer levelPlayer;

    private WorldInstance activeWorld;
    private LevelInstance currentLevel;
    private InputReplay currentReplay;

    private boolean reloadQueued;

    private ScoreMenu scoreMenu;
    private PauseMenu pauseMenu;
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

    private void setLevel(LevelDefinition level) {
        levelPlayer.loadLevel(level);
        Gdx.input.setInputProcessor(combinedGameInput);
        scoreMenu.visible = false;
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

        Sound sfx = game.assets.get(soundName, Sound.class);

        if (SoundMode.PLAY.equals(mode)) {
            sfx.play();
        } else if (SoundMode.STOP.equals(mode)) {
            sfx.stop();
        }
    }

    @Override
    public void doMusic(SoundMode mode, String musicName) {

        Music music = game.assets.get(musicName, Music.class);

        switch(mode) {
            case START:
            case PLAY:
            case RESUME:
                music.setLooping(true);
                if (!music.isPlaying()) {
                    music.play();
                }
                break;
            case PAUSE:
                if (music.isPlaying()) {
                    music.pause();
                }
                break;
            case STOP:
                music.stop();
                break;
        }
        if (SoundMode.PLAY.equals(mode) && !music.isPlaying()) {
            music.play();
        } else if (SoundMode.STOP.equals(mode) && music.isPlaying()) {
            music.stop();
        }
    }

    @Override
    public void saveLastReplay() {
        String replayName = TimerUtils.getDateAsString() + " - " + currentLevel.levelDef.name;
        System.out.println("Saving replay: " + replayName);
        ReplayUtils.saveReplay(replayName, levelPlayer.recordedInput);
    }

    @Override
    public void finishLevel(LandingScore score) {
        switch(currentMode){
            case PLAY_MODE:
                scoreRun(score);
                break;
            case REPLAY_MODE:
                showScoreMenu(score, true);
                break;
        }
    }

    private void scoreRun(LandingScore score) {
        levelPlayer.stopReplayCapture();
        System.out.println("ANGLE: " + score.angleScore + " SPEED: " + score.speedScore);
        int levelScore = score.total();
        System.out.println("SCORE: " + levelScore);
        if (currentLevel.getHighScore() == GamePrefs.SCORE_NOT_SET) {
            // first time beating the level!
            Helm.stats.count(StatName.LEVELS_COMPLETED, 1);
        }
        score.newHighScore = currentLevel.maybeSetNewHighScore(levelScore);
        score.newBestTime = currentLevel.maybeSetNewBestTime(score.timeTaken);
        showScoreMenu(score, false);
    }

    private void showScoreMenu(LandingScore score, boolean isReplay) {
        scoreMenu.rebuildNextTable(isReplay);
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
    public boolean isDebug() {
        return Helm.debug;
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

                    double average = 1.0 * totalTime / updateFrameTimes.size;
                    System.out.println("Average update time: " + average);
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

                double average = 1.0 * totalTime / renderFrameTimes.size;
                System.out.println("Average render time: " + average);
                renderFrameTimes.clear();
            }
        }

        if (reloadQueued) {
            switch(currentMode) {
                case PLAY_MODE:
                    setLevel(currentLevel.levelDef);
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
    }
}
