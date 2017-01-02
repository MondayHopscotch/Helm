package com.bitdecay.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.bitdecay.game.GamePilot;
import com.bitdecay.game.Helm;
import com.bitdecay.game.menu.ScoreMenu;
import com.bitdecay.game.scoring.LandingScore;
import com.bitdecay.game.sound.MusicLibrary;
import com.bitdecay.game.sound.SFXLibrary;
import com.bitdecay.game.sound.SoundMode;
import com.bitdecay.game.world.LevelDefinition;
import com.bitdecay.game.world.LevelWorld;
import com.bitdecay.game.world.World1;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Monday on 12/15/2016.
 */
public class GameScreen implements Screen, GamePilot {
    private Helm game;

    LevelPlayer levelPlayer;

    private LevelWorld currentWorld;

    private boolean reloadQueued;

    private ScoreMenu scoreMenu;

    public GameScreen(Helm game) {
        this.game = game;

        levelPlayer = new LevelPlayer(this);

        initMenus();

        currentWorld = new World1();
        requestRestartLevel();
    }

    private void initMenus() {
        scoreMenu = new ScoreMenu(this);
    }

    private void setLevel(LevelDefinition level) {
        levelPlayer.loadLevel(level);
        Gdx.input.setInputProcessor(levelPlayer.getInput());
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

    Map<String, Sound> soundMap = new HashMap<>();

    @Override
    public void doSound(SoundMode mode, String soundName) {
        Sound sfx;
        if (!soundMap.containsKey(soundName)) {
            soundMap.put(soundName, Gdx.audio.newSound(Gdx.files.internal(SFXLibrary.SFX_DIR + soundName)));
        }

        sfx = soundMap.get(soundName);
        if (SoundMode.PLAY.equals(mode)) {
            sfx.play();
        } else if (SoundMode.STOP.equals(mode)) {
            sfx.stop();
        }
    }

    Map<String, Music> musicMap = new HashMap<>();

    @Override
    public void doMusic(SoundMode mode, String soundName) {
        Music music;
        if (!musicMap.containsKey(soundName)) {
            musicMap.put(soundName, Gdx.audio.newMusic(Gdx.files.internal(MusicLibrary.MUSIC_DIR + soundName)));
        }

        music = musicMap.get(soundName);
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
        // temporary
        int levelScore = score.angleScore + score.speedScore;
        System.out.println("SCORE: " + levelScore);
        currentWorld.setLevelScore(currentWorld.getCurrentLevel(), levelScore);
        // end temp
        scoreMenu.setScore(score, currentWorld.getTotalScore());
        scoreMenu.visible = true;
        if (currentWorld.hasNextLevel()) {
            scoreMenu.setNextLevelOption();
        } else {
            scoreMenu.setReturnToMenuOption();
        }
        Gdx.input.setInputProcessor(scoreMenu.stage);
        levelPlayer.resetInputSystems();
    }

    @Override
    public void nextLevel() {
        LevelDefinition nextLevel = currentWorld.getNextLevel();
        if (nextLevel == null) {
            returnToTitle();
        } else {
            setLevel(nextLevel);
            reloadQueued = true;
        }
    }

    @Override
    public void returnToTitle() {

        int total = currentWorld.getTotalScore();

        int oldHighScore = 0;
        if (game.prefs.contains(Helm.HIGH_SCORE)) {
            oldHighScore = game.prefs.getInteger(Helm.HIGH_SCORE);
        }

        if (total > oldHighScore) {
            game.prefs.putInteger(Helm.HIGH_SCORE, total);
            game.prefs.flush();
            System.out.println("Scorer: SAVING NEW SCORE: " + total);
        }

        game.setScreen(new TitleScreen(game));
    }

    @Override
    public void render(float delta) {
        if (delta > .5f) {
            delta = .5f;
        }
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        levelPlayer.update(delta);

        levelPlayer.render(delta);

        if (reloadQueued) {
            setLevel(currentWorld.getCurrentLevel());
            reloadQueued = false;
        }

        scoreMenu.updateAndDraw();
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
