package com.bitdecay.helm.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.bitdecay.helm.GamePilot;
import com.bitdecay.helm.Helm;
import com.bitdecay.helm.persist.JsonUtils;
import com.bitdecay.helm.scoring.LandingScore;
import com.bitdecay.helm.sound.SoundMode;
import com.bitdecay.helm.tutorial.FirstBoostPhase;
import com.bitdecay.helm.tutorial.FuelPhase;
import com.bitdecay.helm.tutorial.LandingPhase;
import com.bitdecay.helm.tutorial.LaunchPhase;
import com.bitdecay.helm.tutorial.StartPhase;
import com.bitdecay.helm.tutorial.SteeringPhase;
import com.bitdecay.helm.tutorial.TutorialPhase;
import com.bitdecay.helm.unlock.palette.GameColors;
import com.bitdecay.helm.world.LevelDefinition;

/**
 * Created by Monday on 10/23/2017.
 */

public class TutorialScreen implements Screen, GamePilot {
    private final Helm game;
    private final LevelPlayer levelPlayer;
    private final InputMultiplexer combinedGameInput;

    private final ShapeRenderer tutorialShaper;
    private final Stage stage;

    private boolean paused = false;

    public Array<TutorialPhase> phases;
    public int activePhase;

    public TutorialScreen(Helm game) {
        this.game = game;

        tutorialShaper = new ShapeRenderer();
        stage = new Stage();
        levelPlayer = new TutorialLevelPlayer(this);

        LevelDefinition tutorial1 = JsonUtils.unmarshal(LevelDefinition.class, Gdx.files.internal("level/tutorial/tut1.json"));
        levelPlayer.loadLevel(tutorial1);

        phases = new Array<>();
        phases.add(new StartPhase());
        phases.add(new LaunchPhase());
        phases.add(new SteeringPhase());
        phases.add(new FirstBoostPhase());
        phases.add(new FuelPhase());
        phases.add(new LandingPhase());
        activePhase = -1;
        nextPhase();


        combinedGameInput = new InputMultiplexer(stage, levelPlayer.getInput());
        Gdx.input.setInputProcessor(combinedGameInput);
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
            if (phases.get(activePhase).update(tutorialShaper)) {
                nextPhase();
            }
        } else {
            System.out.println("WE DONE");
        }

        tutorialShaper.end();

        if (!paused) {
            levelPlayer.update(delta);
        }

        levelPlayer.render(delta);

        stage.act();
        stage.draw();
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

    }

    @Override
    public void doSound(SoundMode mode, String soundName) {

    }

    @Override
    public void doMusic(SoundMode mode, String soundName) {

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
