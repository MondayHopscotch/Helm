package com.bitdecay.helm.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.bitdecay.helm.Helm;
import com.bitdecay.helm.menu.BitImageButton;

/**
 * Created by Monday on 2/12/2017.
 */

public abstract class AbstractScrollingItemScreen implements Screen {

    protected final Helm game;
    Stage stage;
    Skin skin;
    private Table titleTable;
    protected Table itemTable;
    private Table returnTable;
    protected ScrollPane scroll;

    private boolean backPressed = false;

    public boolean requestAutoScroll = false;
    public float requestedScrollPercent = 0;
    private float iconSize;
    private TextureAtlas.AtlasRegion exitIcon;

    public AbstractScrollingItemScreen(final com.bitdecay.helm.Helm game) {
        this.game = game;
    }

    private void init() {
        if (stage == null) {
            stage = new Stage();
        }
        stage.clear();

        if (Helm.debug) {
            stage.setDebugAll(true);
        }
        skin = game.skin;

        iconSize = game.fontScale * 0.5f;
        exitIcon = game.assets.get("img/icons.atlas", TextureAtlas.class).findRegion("exit_icon");

        Table container = new Table();
        container.setFillParent(true);

        Table mainTable = new Table();

        scroll = new ScrollPane(mainTable, skin);

        itemTable = new Table();
        mainTable.add(itemTable).width(Gdx.graphics.getWidth() * 0.8f).padTop(game.fontScale * 10).padBottom(game.fontScale * 30);

        titleTable = new Table();
        titleTable.align(Align.topLeft);
        titleTable.setOrigin(Align.topLeft);

        returnTable = new Table();
        returnTable.align(Align.bottomRight);
        returnTable.setOrigin(Align.bottomRight);


        stage.addActor(container);

        container.add(titleTable).expandX().fillX();
        container.row();
        container.add(scroll).expand().fill();
        container.row();
        container.add(returnTable).expandX().fillX();
    }

    protected void build() {
        init();
        backPressed = false;
        Label titleLabel = new Label(getTitle(), skin);
        titleLabel.setFontScale(game.fontScale * 2);
        titleLabel.setAlignment(Align.topLeft);
        titleLabel.setOrigin(Align.topLeft);
        titleTable.clear();
        titleTable.add(titleLabel).padLeft(game.fontScale);
        itemTable.clear();
        populateRows(itemTable);
        returnTable.clear();

        TextureRegionDrawable levelSelectDrawable = new TextureRegionDrawable(exitIcon);
        BitImageButton returnButton = new BitImageButton(levelSelectDrawable, levelSelectDrawable, iconSize, skin);
        returnButton.addListener(getReturnButtonAction());

        returnTable.add(returnButton).padRight(game.fontScale).padBottom(game.fontScale);
    }

    public abstract void populateRows(Table mainTable);

    public abstract String getTitle();

    public abstract ClickListener getReturnButtonAction();

    @Override
    public void show() {
        build();
        stage.addAction(Transitions.getFadeIn());
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();

        if (!backPressed && Gdx.input.isKeyPressed(Input.Keys.BACK)) {
            backPressed = true;
            getReturnButtonAction().clicked(null, 0, 0);
        }

        if (requestAutoScroll) {
            requestAutoScroll = false;
            scroll.setScrollPercentY(requestedScrollPercent);
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
        stage.dispose();
    }
}
