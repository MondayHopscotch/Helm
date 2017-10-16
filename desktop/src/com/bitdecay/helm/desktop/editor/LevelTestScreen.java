package com.bitdecay.helm.desktop.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

/**
 * Created by Monday on 1/2/2017.
 */
public class LevelTestScreen implements Screen, com.bitdecay.helm.GamePilot {
    private final com.bitdecay.helm.screen.LevelPlayer levelPlayer;
    private final com.bitdecay.helm.Helm helmGame;
    private HelmEditor editor;
    private com.bitdecay.helm.world.LevelDefinition level;

    public LevelTestScreen(HelmEditor editor) {
        this.helmGame = new com.bitdecay.helm.Helm();
        helmGame.palette = new com.bitdecay.helm.unlock.palette.types.StandardPalette();
        this.editor = editor;
        levelPlayer = new com.bitdecay.helm.screen.LevelPlayer(this, false);
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
    public void doSound(com.bitdecay.helm.sound.SoundMode mode, String soundName) {

    }

    @Override
    public void doMusic(com.bitdecay.helm.sound.SoundMode mode, String soundName) {

    }

    @Override
    public void finishLevel(com.bitdecay.helm.scoring.LandingScore score) {
        editor.showEditor();

    }

    @Override
    public void saveLastReplay() {

    }

    @Override
    public void returnToMenus(boolean isQuit) {
        editor.showEditor();
    }

    @Override
    public void goToNextLevel() {
        editor.showEditor();
    }

    @Override
    public com.bitdecay.helm.Helm getHelm() {
        return helmGame;
    }

    @Override
    public void togglePause() {

    }

    @Override
    public void setTime(float secondsElapsed) {

    }

    @Override
    public boolean isDebug() {
        return true;
    }

    public void setLevel(com.bitdecay.helm.world.LevelDefinition level) {
        levelPlayer.loadLevel(level);
    }
}
