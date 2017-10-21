package com.bitdecay.helm.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.bitdecay.helm.Helm;
import com.bitdecay.helm.prefs.GamePrefs;
import com.bitdecay.helm.sound.MusicLibrary;

/**
 * Created by Monday on 12/21/2016.
 */
public class TitleScreen implements Screen {

    Texture backgroundImage;
    Stage stage;

    Skin skin;
    com.bitdecay.helm.Helm game;

    private final Table mainMenu;
    private final Table extraMenu;

    private float menuTransitionSpeed = .15f;

    private boolean spinningRight = true;
    private float maxRotation = 10;
    private float rotation = maxRotation;
    private float spinSpeed = 0;
    private float spinAccel = .005f;

    private static TitleScreen instance;

    public static TitleScreen get(Helm game) {
        if (instance == null) {
            instance = new TitleScreen(game);
        }
        return instance;
    }

    private TitleScreen(Helm game) {
        this.game = game;
        stage = new Stage();
        stage.setDebugAll(Helm.debug);

        skin = game.skin;

        backgroundImage = new Texture(Gdx.files.internal("splash/TitleScreen.png"));

        Image bgImage = new Image(backgroundImage);
        bgImage.setScaling(Scaling.fillY);
        bgImage.setFillParent(true);

        stage.addActor(bgImage);

        Table mainTable = new Table();
        mainTable.setFillParent(true);

        mainMenu = buildMainMenu();
        mainTable.add(mainMenu).expand().align(Align.right);

        Table extraTable = new Table();
        extraTable.setFillParent(true);

        extraMenu = buildExtraMenu();
        extraTable.add(extraMenu).expand().align(Align.right);

        extraMenu.setVisible(false);
        System.out.println(extraMenu.getWidth());


        Table versionTable = new Table();
        versionTable.setFillParent(true);
        versionTable.setOrigin(Align.bottomLeft);
        versionTable.align(Align.bottomLeft);

        Actor versionActor = buildVersionTag();
        versionTable.add(versionActor).align(Align.left).fill().expand();

        stage.addActor(mainTable);
        stage.addActor(extraTable);
        stage.addActor(versionTable);
    }

    private Table buildMainMenu() {
        final Table menu = new Table();
        menu.setTransform(true);
        menu.align(Align.center);
        menu.setOrigin(Align.center);

        Label startLabel = new Label("Start", skin);
        startLabel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                finishLoadingAssets();
                stage.addAction(Transitions.getFadeOut(new Runnable() {
                            @Override
                            public void run() {
                                game.setScreen(WorldSelectScreen.get(game));
                            }
                        })
                );
            }
        });
        startLabel.setFontScale(game.fontScale * 1.8f);

        Label optionsLabel = new Label("Options", skin);
        optionsLabel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                finishLoadingAssets();
                stage.addAction(Transitions.getQuickFadeOut(new Runnable() {
                    @Override
                    public void run() {
                        game.setScreen(new OptionsScreen(game));
                    }
                }));
            }
        });
        optionsLabel.setFontScale(game.fontScale * 1.2f);

        final Label extraMenuLabel = new Label("Extras", skin);
        extraMenuLabel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                transitionMenu(mainMenu, extraMenu);
            }
        });
        extraMenuLabel.setFontScale(game.fontScale * 1.2f);

        menu.add(wrapLabel(startLabel)).padRight(game.fontScale * 30).padBottom(game.fontScale * 30);
        menu.row();
        menu.add(wrapLabel(optionsLabel)).padRight(game.fontScale * 30).padBottom(game.fontScale * 30);
        menu.row();
        menu.add(wrapLabel(extraMenuLabel)).padRight(game.fontScale * 30);

        return menu;
    }

    private Table buildExtraMenu() {
        final Table menu = new Table();
        menu.setTransform(true);
        menu.align(Align.center);
        menu.setOrigin(Align.center);

        Label paletteLabel = new Label("Palette", skin);
        paletteLabel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                finishLoadingAssets();
                stage.addAction(Transitions.getQuickFadeOut(new Runnable() {
                    @Override
                    public void run() {
                        game.setScreen(new PaletteSelectScreen(game));
                    }
                }));
            }
        });
        paletteLabel.setFontScale(game.fontScale * 1.2f);

        Label statsLabel = new Label("Stats", skin);
        statsLabel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                finishLoadingAssets();
                stage.addAction(Transitions.getQuickFadeOut(new Runnable() {
                    @Override
                    public void run() {
                        game.setScreen(new StatsScreen(game));
                    }
                }));
            }
        });
        statsLabel.setFontScale(game.fontScale * 1.2f);

        Label replayLabel = new Label("Replays", skin);
        replayLabel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                finishLoadingAssets();
                stage.addAction(Transitions.getQuickFadeOut(new Runnable() {
                    @Override
                    public void run() {
                        game.setScreen(new ReplaySelectScreen(game));
                    }
                }));
            }
        });
        replayLabel.setFontScale(game.fontScale * 1.2f);

        Label creditLabel = new Label("Credits", skin);
        creditLabel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                finishLoadingAssets();
                stage.addAction(Transitions.getQuickFadeOut(new Runnable() {
                    @Override
                    public void run() {
                        game.setScreen(new CreditsScreen(game));
                    }
                }));
            }
        });
        creditLabel.setFontScale(game.fontScale * 1.2f);

        final Label mainMenuLabel = new Label("Main Menu", skin);
        mainMenuLabel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                transitionMenu(extraMenu, mainMenu);
            }
        });
        mainMenuLabel.setFontScale(game.fontScale * 1.2f);

        menu.add(wrapLabel(paletteLabel)).padRight(game.fontScale * 30).padBottom(game.fontScale * 15);
        menu.row();
        menu.add(wrapLabel(statsLabel)).padRight(game.fontScale * 30).padBottom(game.fontScale * 15);
        menu.row();
        menu.add(wrapLabel(replayLabel)).padRight(game.fontScale * 30).padBottom(game.fontScale * 15);
        menu.row();
        menu.add(wrapLabel(creditLabel)).padRight(game.fontScale * 30).padBottom(game.fontScale * 15);
        menu.row();
        menu.add(wrapLabel(mainMenuLabel)).padRight(game.fontScale * 30);

        return menu;
    }

    private Actor wrapLabel(Label label) {
        Table labelParent = new Table();
        labelParent.setTransform(true);
        labelParent.setFillParent(false);
        labelParent.add(label);
        labelParent.setOrigin(labelParent.getMinWidth() / 2, labelParent.getMinHeight() / 2);
        return labelParent;
    }

    private void transitionMenu(final Actor from, final Actor to) {
        from.addAction(
                Actions.sequence(
                        Actions.moveBy(from.getWidth(), 0, menuTransitionSpeed),
                        Actions.run(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        from.setVisible(false);
                                    }
                                }
                        ),
                        Actions.moveBy(-from.getWidth(), 0, menuTransitionSpeed),
                        Actions.run(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        to.addAction(
                                                Actions.sequence(
                                                        Actions.moveBy(to.getWidth(), 0),
                                                        Actions.run(
                                                                new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        to.setVisible(true);
                                                                    }
                                                                }
                                                        ),
                                                        Actions.moveBy(-to.getWidth(), 0, menuTransitionSpeed)
                                                )
                                        );
                                    }
                                }
                        )
                )
        );
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
        stage.addAction(Transitions.getFadeIn());
        Music music = game.assets.get(MusicLibrary.AMBIENT_MUSIC, Music.class);
        music.setLooping(true);

        if (game.prefs.getBoolean(GamePrefs.MUTE_MUSIC)) {
            if (music.isPlaying()) {
                music.pause();
            }
        } else {
            if (!music.isPlaying()) {
                music.play();
            }
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.assets.update();

        if (spinningRight) {
            if (rotation > 0) {
                spinSpeed -= spinAccel;
            } else {
                spinSpeed += spinAccel;
            }
        } else {
            if (rotation > 0) {
                spinSpeed += spinAccel;
            } else {
                spinSpeed -= spinAccel;
            }
        }

        rotation += spinSpeed;

        if (rotation > maxRotation || rotation < -maxRotation) {
            spinningRight = !spinningRight;
            spinSpeed = 0;
            rotation = Math.max(rotation, -maxRotation);
            rotation = Math.min(rotation, maxRotation);
        }

        for (Actor actor : mainMenu.getChildren()) {
            actor.setRotation(rotation);
        }

        for (Actor actor : extraMenu.getChildren()) {
            actor.setRotation(rotation);
        }


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
        stage.dispose();
        instance = null;
    }
}
