package com.bitdecay.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.bitdecay.game.GamePilot;
import com.bitdecay.game.menu.Overlay;
import com.bitdecay.game.menu.PauseMenu;
import com.bitdecay.game.prefs.GamePrefs;
import com.bitdecay.game.Helm;
import com.bitdecay.game.menu.ScoreMenu;
import com.bitdecay.game.scoring.LandingScore;
import com.bitdecay.game.sound.SoundMode;
import com.bitdecay.game.unlock.StatName;
import com.bitdecay.game.world.LevelDefinition;
import com.bitdecay.game.world.LevelInstance;
import com.bitdecay.game.world.LevelWorld;

/**
 * Created by Monday on 12/15/2016.
 */
public class GameScreen implements Screen, GamePilot {
    private Helm game;

    LevelPlayer levelPlayer;

    private LevelWorld activeWorld;
    private LevelInstance currentLevel;

    private boolean reloadQueued;

    private ScoreMenu scoreMenu;
    private PauseMenu pauseMenu;
    private boolean paused = false;
    private Overlay overlay;

    private InputMultiplexer combinedGameInput;

    public GameScreen(Helm game, LevelWorld world, LevelInstance level) {
        this.game = game;

        levelPlayer = new LevelPlayer(this);

        initMenus();

        combinedGameInput = new InputMultiplexer(pauseMenu.stage, levelPlayer.getInput());

        currentLevel = level;
        activeWorld = world;

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
    public void finishLevel(LandingScore score) {
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
        game.setScreen(new LevelSelectScreen(game, activeWorld));
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
            setLevel(currentLevel.levelDef);
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
