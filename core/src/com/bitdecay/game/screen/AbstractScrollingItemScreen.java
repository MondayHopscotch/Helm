package com.bitdecay.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.bitdecay.game.Helm;

/**
 * Created by Monday on 2/12/2017.
 */

public abstract class AbstractScrollingItemScreen implements Screen {

    protected final Helm game;
    Stage stage;
    Skin skin;
    private final Table itemTable;
    private Table returnTable;

    public AbstractScrollingItemScreen(final Helm game) {
        this.game = game;

        stage = new Stage();
        if (Helm.debug) {
            stage.setDebugAll(true);
    }
        skin = game.skin;

        Table container = new Table();
        container.setFillParent(true);

        Table mainTable = new Table();

        ScrollPane scroll = new ScrollPane(mainTable, skin);

        itemTable = new Table();
        mainTable.add(itemTable).width(Gdx.graphics.getWidth() * 0.8f).padTop(game.fontScale * 10).padBottom(game.fontScale * 30);

        returnTable = new Table();
        returnTable.setFillParent(true);
        returnTable.align(Align.bottomRight);
        returnTable.setOrigin(Align.bottomRight);

        stage.addActor(container);
        stage.addActor(returnTable);

        container.add(scroll).expand().fill();
    }

    protected void build() {
        populateRows(itemTable);
        returnTable.add(getReturnButton());
    }

    public abstract void populateRows(Table mainTable);

    public abstract Actor getReturnButton();

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
        stage.dispose();
    }

    @Override
    public void dispose() {

    }
}
