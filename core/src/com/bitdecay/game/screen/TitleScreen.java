package com.bitdecay.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.bitdecay.game.prefs.GamePrefs;
import com.bitdecay.game.Helm;
import com.bitdecay.game.Version;

/**
 * Created by Monday on 12/21/2016.
 */
public class TitleScreen implements Screen {

    SpriteBatch batch;
    Texture backgroundImage;

    Stage stage;
    Skin skin;

    Helm game;

    public TitleScreen(Helm game) {
        this.game = game;
        batch = new SpriteBatch();
        backgroundImage = new Texture(Gdx.files.internal("img/TitleScreen.png"));

        stage = new Stage();
        skin = game.skin;

        Table mainTable = new Table();
        mainTable.setFillParent(true);

        Actor highScoreActor = buildHighScoreDisplay();
        mainTable.add(highScoreActor).align(Align.right).expandX();
        mainTable.row();

        Actor mainMenu = buildMainMenu();
        mainTable.add(mainMenu).expand();
        mainTable.row();

        Actor versionActor = buildVersionTag();
        mainTable.add(versionActor).align(Align.left).expandX();

        stage.addActor(mainTable);
    }

    private Actor buildHighScoreDisplay() {
        Table highScoreTable = new Table();

        Label scoreLabel = new Label("High Score: " + Integer.toString(Helm.prefs.getInteger(GamePrefs.HIGH_SCORE)), skin);
        scoreLabel.setFontScale(5);
        highScoreTable.add(scoreLabel);
        return highScoreTable;
    }

    private Actor buildMainMenu() {
        Table mainMenu = new Table();
//        mainMenu.setFillParent(true);
        mainMenu.align(Align.center);
        mainMenu.setOrigin(Align.center);

        Label startLabel = new Label("Start", skin);
        startLabel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                finishLoadingAssets();
                game.setScreen(new GameScreen(game));
            }
        });
        startLabel.setFontScale(10);

        Label optionsLabel = new Label("Options", skin);
        optionsLabel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                finishLoadingAssets();
                game.setScreen(new OptionsScreen(game));
            }
        });
        optionsLabel.setFontScale(10);

        Label creditLabel = new Label("Credits", skin);
        creditLabel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                finishLoadingAssets();
                game.setScreen(new CreditsScreen(game));
            }
        });
        creditLabel.setFontScale(10);


        mainMenu.add(startLabel);
        mainMenu.row();
        mainMenu.add(optionsLabel);
        mainMenu.row();
        mainMenu.add(creditLabel);

        return mainMenu;
    }

    private void finishLoadingAssets() {
        game.assets.finishLoading();
    }

    private Actor buildVersionTag() {
        Table versionTable = new Table();
        versionTable.align(Align.bottomLeft);
        versionTable.setOrigin(Align.bottomLeft);

        Label versionLabel = new Label("Version " + Version.CURRENT_VERSION, skin);
        versionLabel.setFontScale(3);

        versionTable.add(versionLabel);

        return versionTable;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.assets.update();

        batch.begin();
        batch.draw(backgroundImage, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

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
        backgroundImage.dispose();
        batch.dispose();
        stage.dispose();
    }
}
