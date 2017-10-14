package com.bitdecay.helm.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

/**
 * Created by Monday on 12/21/2016.
 */
public class TitleScreen implements Screen {

    SpriteBatch batch;
    Texture backgroundImage;

    Stage stage;
    Skin skin;

    com.bitdecay.helm.Helm game;

    public TitleScreen(com.bitdecay.helm.Helm game) {
        this.game = game;
        batch = new SpriteBatch();
        backgroundImage = new Texture(Gdx.files.internal("splash/TitleScreen.png"));

        stage = new Stage();
        skin = game.skin;

        Table mainTable = new Table();
        mainTable.setFillParent(true);

        Actor mainMenu = buildMainMenu();
        mainTable.add(mainMenu).expand().align(Align.bottom);
        mainTable.row();

        Actor versionActor = buildVersionTag();
        mainTable.add(versionActor).align(Align.left).expandX();

        stage.addActor(mainTable);
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
                game.setScreen(WorldSelectScreen.get(game));
            }
        });
        startLabel.setFontScale(game.fontScale * 1.8f);

        Label paletteLabel = new Label("Palette", skin);
        paletteLabel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                finishLoadingAssets();
                game.setScreen(new PaletteSelectScreen(game));
            }
        });
        paletteLabel.setFontScale(game.fontScale * 1.2f);


        Label optionsLabel = new Label("Options", skin);
        optionsLabel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                finishLoadingAssets();
                game.setScreen(new OptionsScreen(game));
            }
        });
        optionsLabel.setFontScale(game.fontScale * 1.2f);

        Label statsLabel = new Label("Stats", skin);
        statsLabel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                finishLoadingAssets();
                game.setScreen(new StatsScreen(game));
            }
        });
        statsLabel.setFontScale(game.fontScale * 1.2f);

        Label replayLabel = new Label("Replays", skin);
        replayLabel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                finishLoadingAssets();
                game.setScreen(new ReplaySelectScreen(game));
            }
        });
        replayLabel.setFontScale(game.fontScale * 1.2f);

        Label creditLabel = new Label("Credits", skin);
        creditLabel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                finishLoadingAssets();
                game.setScreen(new CreditsScreen(game));
            }
        });
        creditLabel.setFontScale(game.fontScale * 1.2f);


        mainMenu.add(startLabel);
        mainMenu.row();
        mainMenu.add(paletteLabel);
        mainMenu.row();
        mainMenu.add(optionsLabel);
        mainMenu.row();
        mainMenu.add(statsLabel);
        mainMenu.row();
        mainMenu.add(replayLabel);
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

        Label versionLabel = new Label("Version " + com.bitdecay.helm.Version.CURRENT_VERSION, skin);
        versionLabel.setFontScale(game.fontScale * 0.5f);

        versionTable.add(versionLabel);

        return versionTable;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        Music music = game.assets.get(com.bitdecay.helm.sound.MusicLibrary.AMBIENT_MUSIC, Music.class);
        music.setLooping(true);
        if (!music.isPlaying()) {
            music.play();
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
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
