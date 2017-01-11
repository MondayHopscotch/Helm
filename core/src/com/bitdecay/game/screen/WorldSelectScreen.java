package com.bitdecay.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.bitdecay.game.Helm;
import com.bitdecay.game.world.LevelWorld;
import com.bitdecay.game.world.World1;
import com.bitdecay.game.world.World2;
import com.bitdecay.game.world.World3;

/**
 * Created by Monday on 1/10/2017.
 */
public class WorldSelectScreen implements Screen {

    private final Helm game;
    Stage stage;
    Skin skin;

    public WorldSelectScreen(Helm game) {
        this.game = game;

        stage = new Stage();
        skin = game.skin;

        Table mainTable = new Table();
        mainTable.setFillParent(true);

        buildWorldRow(new World1(), mainTable);
        buildWorldRow(new World2(), mainTable);
        buildWorldRow(new World3(), mainTable);

        stage.addActor(mainTable);
    }

    private void buildWorldRow(final LevelWorld world, Table table) {
        Label worldNameLabel = new Label(world.getWorldName(), skin);
        worldNameLabel.setFontScale(game.fontScale);
        worldNameLabel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game, world));
            }
        });

        table.add(worldNameLabel);
        table.row();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();
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
        stage.dispose();
    }
}
