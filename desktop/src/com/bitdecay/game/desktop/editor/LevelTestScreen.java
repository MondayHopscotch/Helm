package com.bitdecay.game.desktop.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.bitdecay.game.GamePilot;
import com.bitdecay.game.Helm;
import com.bitdecay.game.scoring.LandingScore;
import com.bitdecay.game.screen.LevelPlayer;
import com.bitdecay.game.sound.SoundMode;
import com.bitdecay.game.world.LevelDefinition;

/**
 * Created by Monday on 1/2/2017.
 */
public class LevelTestScreen implements Screen, GamePilot {
    private final LevelPlayer levelPlayer;
    private HelmEditor editor;
    private LevelDefinition level;

    public LevelTestScreen(HelmEditor editor) {
        this.editor = editor;
        levelPlayer = new LevelPlayer(this);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(levelPlayer.getInput());
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        levelPlayer.update(delta);
        levelPlayer.render(delta);
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
        editor.showEditor();
    }

    @Override
    public void doSound(SoundMode mode, String soundName) {

    }

    @Override
    public void doMusic(SoundMode mode, String soundName) {

    }

    @Override
    public void finishLevel(LandingScore score) {
        editor.showEditor();

    }

    @Override
    public void nextLevel() {
        editor.showEditor();

    }

    @Override
    public void returnToTitle() {
        editor.showEditor();

    }

    @Override
    public Helm getHelm() {
        return null;
    }

    @Override
    public void togglePause() {

    }

    @Override
    public void setTime(float secondsElapsed) {

    }

    public void setLevel(LevelDefinition level) {
        levelPlayer.loadLevel(level);
    }
}
