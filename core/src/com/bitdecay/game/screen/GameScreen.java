package com.bitdecay.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.bitdecay.game.GamePilot;
import com.bitdecay.game.input.InputReplay;
import com.bitdecay.game.menu.Overlay;
import com.bitdecay.game.menu.PauseMenu;
import com.bitdecay.game.persist.ReplayUtils;
import com.bitdecay.game.prefs.GamePrefs;
import com.bitdecay.game.Helm;
import com.bitdecay.game.menu.ScoreMenu;
import com.bitdecay.game.scoring.LandingScore;
import com.bitdecay.game.sound.SoundMode;
import com.bitdecay.game.unlock.StatName;
import com.bitdecay.game.world.LevelDefinition;
import com.bitdecay.game.world.LevelInstance;
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

    private enum PlayMode {
        PLAY_MODE,
        REPLAY_MODE
    }

    private PlayMode currentMode;

    public GameScreen(Helm game, WorldInstance world, LevelInstance level) {
        this.game = game;

        currentMode = PlayMode.PLAY_MODE;

        levelPlayer = new LevelPlayer(this);
        initMenus();
        combinedGameInput = new InputMultiplexer(pauseMenu.stage, levelPlayer.getInput());

        currentLevel = level;
        activeWorld = world;

        requestRestartLevel();
    }

    public GameScreen(Helm game, InputReplay replay) {
        this.game = game;

        currentMode = PlayMode.REPLAY_MODE;

        levelPlayer = new LevelPlayer(this);
        initMenus();
        combinedGameInput = new InputMultiplexer(pauseMenu.stage, levelPlayer.getInput());
        currentReplay = replay;
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
        String replayName = currentLevel.levelDef.name + "_" + System.currentTimeMillis();
        System.out.println("Saving replay: " + replayName);
        ReplayUtils.saveReplay(replayName, levelPlayer.inputReplay);
    }

    @Override
    public void finishLevel(LandingScore score) {
        switch(currentMode){
            case PLAY_MODE:
                scoreRun(score);
                break;
            case REPLAY_MODE:
                game.setScreen(new TitleScreen(game));
                break;
        }
    }

    private void scoreRun(LandingScore score) {
//        String replayName = "replay_" + currentLevel.levelDef.name + "_" + System.currentTimeMillis();
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
        scoreMenu.setScore(score);
        scoreMenu.visible = true;
        Gdx.input.setInputProcessor(scoreMenu.stage);
        levelPlayer.resetInputSystems();
    }

    @Override
    public void returnToMenus() {
        if (activeWorld == null) {
            game.setScreen(new TitleScreen(game));
        } else {
            game.setScreen(new LevelSelectScreen(game, activeWorld));
        }
    }

    @Override
    public Helm getHelm() {
        return game;
    }

    @Override
    public void togglePause() {
        System.out.println("PAUSE");
        paused = !paused;
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
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (!paused) {
            levelPlayer.update(delta);
        }

        levelPlayer.render(delta);

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
