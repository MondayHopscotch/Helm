package com.bitdecay.helm.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.bitdecay.helm.GameEntity;
import com.bitdecay.helm.GamePilot;
import com.bitdecay.helm.Helm;
import com.bitdecay.helm.component.CameraFollowComponent;
import com.bitdecay.helm.entities.LandingPlatformEntity;
import com.bitdecay.helm.menu.PauseMenu;
import com.bitdecay.helm.persist.JsonUtils;
import com.bitdecay.helm.prefs.GamePrefs;
import com.bitdecay.helm.scoring.LandingScore;
import com.bitdecay.helm.scoring.ScoreStamps;
import com.bitdecay.helm.sound.SoundMode;
import com.bitdecay.helm.tutorial.CrashPhase;
import com.bitdecay.helm.tutorial.FirstBoostPhase;
import com.bitdecay.helm.tutorial.FuelPhase;
import com.bitdecay.helm.tutorial.LandingAnglePhase;
import com.bitdecay.helm.tutorial.LandingInfoPhase;
import com.bitdecay.helm.tutorial.LandingSpeedPhase;
import com.bitdecay.helm.tutorial.LaunchPhase;
import com.bitdecay.helm.tutorial.StartPhase;
import com.bitdecay.helm.tutorial.SteeringPhase;
import com.bitdecay.helm.tutorial.TutorialPhase;
import com.bitdecay.helm.tutorial.TutorialUtils;
import com.bitdecay.helm.tutorial.WrapUpPhase;
import com.bitdecay.helm.unlock.palette.GameColors;
import com.bitdecay.helm.world.LevelDefinition;

/**
 * Created by Monday on 10/23/2017.
 */

public class TutorialScreen extends InputAdapter implements Screen, GamePilot {
    private final Helm game;
    private final LevelPlayer levelPlayer;
    private final InputMultiplexer combinedGameInput;

    private final ShapeRenderer tutorialShaper;
    private final Stage stage;

    private final PauseMenu examplePauseMenu;

    private boolean paused = false;

    public Array<TutorialPhase> phases;
    public int activePhase;

    // active 'pointer' or 'touch' on the screen
    private int activePointer;

    public TutorialScreen(Helm game) {
        this.game = game;

        examplePauseMenu = new PauseMenu(this);

        tutorialShaper = new ShapeRenderer();
        stage = new Stage();
        levelPlayer = new TutorialLevelPlayer(this);

        LevelDefinition tutorial1 = JsonUtils.unmarshal(LevelDefinition.class, Gdx.files.internal("level/tutorial/tut1_v2.json"));
        levelPlayer.loadLevel(tutorial1);

        TutorialUtils.removeLandingFocus(levelPlayer.allEntities);

        phases = new Array<>();
        phases.add(new StartPhase());
        phases.add(new LaunchPhase());
        phases.add(new SteeringPhase());
        phases.add(new FuelPhase());
        phases.add(new FirstBoostPhase());
        phases.add(new CrashPhase()); // TODO: Tell the player that they die if they touch anything
        phases.add(new LandingInfoPhase());
        phases.add(new LandingAnglePhase());
        phases.add(new LandingSpeedPhase());
        phases.add(new WrapUpPhase());
        activePhase = -1;
        nextPhase();

        combinedGameInput = new InputMultiplexer(this, stage, levelPlayer.getInput());
        Gdx.input.setInputProcessor(combinedGameInput);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (activePointer == -1) {
            activePointer = pointer;
            System.out.println("POINTER: new active: " + activePointer);
            return phases.get(activePhase).touchDown(screenX, screenY);
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (pointer != activePointer) {
            // so we only handle one touch at a time
            return false;
        }

        activePointer = -1;

        if (activePhase < phases.size) {
            return phases.get(activePhase).touchUp(screenX, screenY);
        } else {
            return false;
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if (delta > .5f) {
            delta = .5f;
        }
        Color clearColor = getHelm().palette.get(GameColors.BACKGROUND);
        Gdx.gl.glClearColor(clearColor.r, clearColor.g, clearColor.b, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        tutorialShaper.begin(ShapeRenderer.ShapeType.Filled);

        if (activePhase < phases.size) {
            if (phases.get(activePhase).update(tutorialShaper, delta)) {
                nextPhase();
            }
        } else {
            game.prefs.putBoolean(GamePrefs.TUTORIAL_COMPLETE, true);
            ScoreStamps.clearPendingStamps();
            game.setScreen(WorldSelectScreen.get(game));
        }

        tutorialShaper.end();

        if (!paused) {
            levelPlayer.update(delta);
        }

        levelPlayer.render(delta);

        stage.act();
        stage.draw();

        examplePauseMenu.updateAndDraw();
    }

    private void nextPhase() {
        activePhase++;
        if (activePhase < phases.size) {
            phases.get(activePhase).start(game, levelPlayer, stage);
        }
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

    @Override
    public void requestRestartLevel() {
        // just restart current phase
        if (activePhase < phases.size) {
            phases.get(activePhase).start(game, levelPlayer, stage);
        }
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
        return "Tutorial";
    }

    @Override
    public void finishLevel(LandingScore score) {
        nextPhase();
    }

    @Override
    public void returnToMenus(boolean isQuit) {

    }

    @Override
    public void goToNextLevel() {

    }

    @Override
    public Helm getHelm() {
        return game;
    }

    @Override
    public void togglePause() {
        paused = !paused;
    }

    @Override
    public void setTime(float secondsElapsed) {

    }

    @Override
    public void saveLastReplay(String name) {

    }
}
