package com.bitdecay.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.bitdecay.game.GameEntity;
import com.bitdecay.game.GamePilot;
import com.bitdecay.game.Helm;
import com.bitdecay.game.camera.FollowOrthoCamera;
import com.bitdecay.game.entities.LandingPlatformEntity;
import com.bitdecay.game.entities.LineSegmentEntity;
import com.bitdecay.game.entities.ShipEntity;
import com.bitdecay.game.menu.ScoreMenu;
import com.bitdecay.game.scoring.LandingScore;
import com.bitdecay.game.sound.MusicLibrary;
import com.bitdecay.game.sound.SFXLibrary;
import com.bitdecay.game.sound.SoundMode;
import com.bitdecay.game.system.BoostSystem;
import com.bitdecay.game.system.BoosterInputSystem;
import com.bitdecay.game.system.CameraUpdateSystem;
import com.bitdecay.game.system.CollisionAlignmentSystem;
import com.bitdecay.game.system.CollisionSystem;
import com.bitdecay.game.system.DelayedAddSystem;
import com.bitdecay.game.system.GameSystem;
import com.bitdecay.game.system.GravitySystem;
import com.bitdecay.game.system.LandingSystem;
import com.bitdecay.game.system.MovementSystem;
import com.bitdecay.game.system.PlayerCollisionHandlerSystem;
import com.bitdecay.game.system.PlayerStartLevelSystem;
import com.bitdecay.game.system.RenderBodySystem;
import com.bitdecay.game.system.RenderBoostSystem;
import com.bitdecay.game.system.SteeringInputSystem;
import com.bitdecay.game.system.SteeringSystem;
import com.bitdecay.game.world.LevelDefinition;
import com.bitdecay.game.world.LineSegment;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Monday on 12/15/2016.
 */
public class GameScreen implements Screen, GamePilot {
    private Helm game;
    FollowOrthoCamera cam;

    LevelPlayer levelPlayer;

    ShapeRenderer shapeRenderer;

    Array<GameSystem> allSystems = new Array<>(1);
    Array<GameSystem> inputSystems = new Array<>(1);

    private final LevelDefinition currentLevel;
    Array<GameEntity> allEntities = new Array<>(1000);

    private boolean reloadQueued;
    private InputMultiplexer inputMux;
    private ScoreMenu scoreMenu;

    public GameScreen(Helm game) {
        this.game = game;

        shapeRenderer = new ShapeRenderer();

        levelPlayer = new LevelPlayer(this);

        initMenus();

        currentLevel = getTestLevel();
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

    private LevelDefinition getTestLevel() {
        LevelDefinition testLevel = new LevelDefinition();


        Array<LineSegment> testLines = new Array<>(10);
        testLines.add(new LineSegment(new Vector2(-100, 1500), new Vector2(-850, 800)));
        testLines.add(new LineSegment(new Vector2(-850, 800), new Vector2(-800, 300)));
        testLines.add(new LineSegment(new Vector2(-800, 300), new Vector2(-100, -101)));
        testLines.add(new LineSegment(new Vector2(-100, -101), new Vector2(100, -101)));
        testLines.add(new LineSegment(new Vector2(100, -101), new Vector2(300, -600)));
        testLevel.finishPlatform.set(new Rectangle(300, -650, 150, 50));
        testLines.add(new LineSegment(new Vector2(450, -600), new Vector2(800, -300)));
        testLines.add(new LineSegment(new Vector2(800, -300), new Vector2(1000, 300)));
        testLines.add(new LineSegment(new Vector2(1000, 300), new Vector2(1100, 1100)));
        testLines.add(new LineSegment(new Vector2(1100, 1100), new Vector2(1050, 1800)));
        testLines.add(new LineSegment(new Vector2(1050, 1800), new Vector2(400, 1800)));
        testLines.add(new LineSegment(new Vector2(400, 1800), new Vector2(-100, 1500)));

        testLines.add(new LineSegment(new Vector2(300, 900), new Vector2(700, 900)));
        testLines.add(new LineSegment(new Vector2(700, 900), new Vector2(750, 1250)));
        testLines.add(new LineSegment(new Vector2(750, 1250), new Vector2(500, 1100)));
        testLines.add(new LineSegment(new Vector2(500, 1100), new Vector2(300, 900)));

        testLevel.levelLines = testLines;

        testLevel.startPosition =  Vector2.Zero;
        testLevel.startingFuel = 300;


        return testLevel;
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
        scoreMenu.setScore(score);
        scoreMenu.visible = true;
        Gdx.input.setInputProcessor(scoreMenu.stage);
        levelPlayer.resetInputSystems();
    }

    @Override
    public void nextLevel() {
        setLevel(getTestLevel());
        reloadQueued = true;
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
            setLevel(getTestLevel());
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
        shapeRenderer.dispose();
    }
}
