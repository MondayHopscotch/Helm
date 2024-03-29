package com.bitdecay.helm.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.bitdecay.helm.Helm;
import com.bitdecay.helm.Version;
import com.bitdecay.helm.menu.RotatingLabel;
import com.bitdecay.helm.prefs.GamePrefs;
import com.bitdecay.helm.sound.MusicLibrary;
import com.bitdecay.helm.sound.SFXLibrary;
import com.bitdecay.helm.sound.SoundMode;

/**
 * Created by Monday on 12/21/2016.
 */
public class TitleScreen implements Screen {

    Texture backgroundImage;
    Stage stage;

    Skin skin;
    com.bitdecay.helm.Helm game;

    private Table mainMenu;
    private Table extraMenu;

    private boolean mainMenuActive = true;

    private float menuTransitionSpeed = .15f;

    private static TitleScreen instance;
    private boolean canTogglePause;

    public static TitleScreen get(Helm game) {
        if (instance == null) {
            instance = new TitleScreen(game);
        }
        return instance;
    }

    private TitleScreen(Helm game) {
        this.game = game;
    }

    private void buildScreen() {
        stage = new Stage();
        stage.setDebugAll(Helm.debug);

        skin = game.skin;

        backgroundImage = new Texture(Gdx.files.internal("splash/TitleScreen.png"));

        Image bgImage = new Image(backgroundImage);
        bgImage.setScaling(Scaling.fillY);
        bgImage.setFillParent(true);
        bgImage.setAlign(Align.left);

        stage.addActor(bgImage);

        Table mainTable = new Table();
        mainTable.setFillParent(true);

        mainMenu = buildMainMenu();
        mainTable.add(mainMenu).expand().align(Align.right);
        mainMenu.setVisible(mainMenuActive);

        Table extraTable = new Table();
        extraTable.setFillParent(true);

        extraMenu = buildExtraMenu();
        extraTable.add(extraMenu).expand().align(Align.right);
        extraMenu.setVisible(!mainMenuActive);

        Table versionTable = new Table();
        versionTable.setFillParent(true);
        versionTable.setOrigin(Align.bottomLeft);
        versionTable.align(Align.bottomLeft);

        Actor versionActor = buildVersionTag();
        versionTable.add(versionActor).align(Align.left).padLeft(game.fontScale * 10f).fill().expand();

        stage.addActor(mainTable);
        stage.addActor(extraTable);
        stage.addActor(versionTable);

        if (Helm.feedbackMode) {
            Table feedbackTable = new Table();
            feedbackTable.setFillParent(true);
            feedbackTable.setOrigin(Align.bottom);
            feedbackTable.align(Align.bottom);
            RotatingLabel feedbackLabel = new RotatingLabel("Give Feedback", game.fontScale * 1.2f, skin, new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    AudioUtils.doSound(game, SoundMode.PLAY, SFXLibrary.MENU_BOOP);
                    Gdx.input.vibrate(10);
                    Helm.urlOpener.open("https://goo.gl/forms/pT6n5bB9rlH30ecu2");
                }
            }, false);
            feedbackTable.add(feedbackLabel).padBottom(game.fontScale * 20).align(Align.bottom).fillX().expandX();

            stage.addActor(feedbackTable);
        }
    }

    private Table buildMainMenu() {
        final Table menu = new Table();
        menu.setTransform(true);
        menu.align(Align.center);
        menu.setOrigin(Align.center);

        RotatingLabel startLabel = new RotatingLabel("Start", game.fontScale * 1.8f, skin, new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                AudioUtils.doSound(game, SoundMode.PLAY, SFXLibrary.MENU_BOOP);
                Gdx.input.vibrate(10);
                finishLoadingAssets();
                stage.addAction(Transitions.getFadeOut(new Runnable() {
                            @Override
                            public void run() {
                                if (!Helm.prefs.getBoolean(GamePrefs.TUTORIAL_COMPLETE, GamePrefs.TUTORIAL_COMPLETE_DEFAULT)) {
                                    game.setScreen(new TutorialScreen(game));
                                } else {
                                    game.setScreen(WorldSelectScreen.get(game));
                                }
                            }
                        })
                );
            }
        });

        RotatingLabel optionsLabel = new RotatingLabel("Options", game.fontScale * 1.2f, skin, new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                AudioUtils.doSound(game, SoundMode.PLAY, SFXLibrary.MENU_BOOP);
                Gdx.input.vibrate(10);
                finishLoadingAssets();
                stage.addAction(Transitions.getQuickFadeOut(new Runnable() {
                    @Override
                    public void run() {
                        game.setScreen(OptionsScreen.get(game));
                    }
                }));
            }
        });

        final RotatingLabel extraMenuLabel = new RotatingLabel("Extras", game.fontScale * 1.2f, skin, new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                AudioUtils.doSound(game, SoundMode.PLAY, SFXLibrary.MENU_BOOP);
                Gdx.input.vibrate(10);
                transitionMenu(mainMenu, extraMenu);
            }
        });

        menu.add(startLabel).padRight(game.fontScale * 30).padBottom(game.fontScale * 30);
        menu.row();
        menu.add(optionsLabel).padRight(game.fontScale * 30).padBottom(game.fontScale * 30);
        menu.row();
        menu.add(extraMenuLabel).padRight(game.fontScale * 30);

        return menu;
    }

    private Table buildExtraMenu() {
        final Table menu = new Table();
        menu.setTransform(true);
        menu.align(Align.center);
        menu.setOrigin(Align.center);

        RotatingLabel paletteLabel = new RotatingLabel("Palette", game.fontScale * 1.2f, skin, new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                AudioUtils.doSound(game, SoundMode.PLAY, SFXLibrary.MENU_BOOP);
                Gdx.input.vibrate(10);
                finishLoadingAssets();
                stage.addAction(Transitions.getQuickFadeOut(new Runnable() {
                    @Override
                    public void run() {
                        game.setScreen(PaletteSelectScreen.get(game));
                    }
                }));
            }
        });

        RotatingLabel statsLabel = new RotatingLabel("Stats", game.fontScale * 1.2f, skin, new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                AudioUtils.doSound(game, SoundMode.PLAY, SFXLibrary.MENU_BOOP);
                Gdx.input.vibrate(10);
                finishLoadingAssets();
                stage.addAction(Transitions.getQuickFadeOut(new Runnable() {
                    @Override
                    public void run() {
                        game.setScreen(StatsScreen.get(game));
                    }
                }));
            }
        });

        RotatingLabel replayLabel = new RotatingLabel("Replays", game.fontScale * 1.2f, skin, new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                AudioUtils.doSound(game, SoundMode.PLAY, SFXLibrary.MENU_BOOP);
                Gdx.input.vibrate(10);
                finishLoadingAssets();
                stage.addAction(Transitions.getQuickFadeOut(new Runnable() {
                    @Override
                    public void run() {
                        game.setScreen(ReplaySelectScreen.get(game));
                    }
                }));
            }
        });

        RotatingLabel creditLabel = new RotatingLabel("Credits", game.fontScale * 1.2f, skin, new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                AudioUtils.doSound(game, SoundMode.PLAY, SFXLibrary.MENU_BOOP);
                Gdx.input.vibrate(10);
                finishLoadingAssets();
                stage.addAction(Transitions.getQuickFadeOut(new Runnable() {
                    @Override
                    public void run() {
                        game.setScreen(CreditsScreen.get(game));
                    }
                }));
            }
        });

        final RotatingLabel mainMenuLabel = new RotatingLabel("Main Menu", game.fontScale * 1.2f, skin, new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                AudioUtils.doSound(game, SoundMode.PLAY, SFXLibrary.MENU_BOOP);
                Gdx.input.vibrate(10);
                transitionMenu(extraMenu, mainMenu);
            }
        });

        menu.add(paletteLabel).padRight(game.fontScale * 30).padBottom(game.fontScale * 15);
        menu.row();
        menu.add(statsLabel).padRight(game.fontScale * 30).padBottom(game.fontScale * 15);
        menu.row();
        menu.add(replayLabel).padRight(game.fontScale * 30).padBottom(game.fontScale * 15);
        menu.row();
        menu.add(creditLabel).padRight(game.fontScale * 30).padBottom(game.fontScale * 15);
        menu.row();
        menu.add(mainMenuLabel).padRight(game.fontScale * 30);

        return menu;
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
                        ),
                        Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                mainMenuActive = to == mainMenu;
                            }
                        })
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
        System.out.println("GDXSTATE TITLE: SHOW");
        buildScreen();
        Gdx.input.setInputProcessor(stage);
        stage.addAction(Transitions.getFadeIn());
        Music music = game.assets.get(MusicLibrary.AMBIENT_MUSIC, Music.class);
        music.setLooping(true);

        if (game.prefs.getBoolean(GamePrefs.MUSIC_ENABLED, GamePrefs.MUSIC_ENABLED_DEFAULT)) {
            if (!music.isPlaying()) {
                music.play();
            }
        } else {
            if (music.isPlaying()) {
                music.pause();
            }
        }

        if (Version.isNewVersionSinceLastOpen(game)) { // check for if user has seen the setting
            Dialog changeDialog = Version.getChangeDialog(game);
            changeDialog.show(stage);
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.assets.update();

        stage.act();
        stage.draw();

        if (canTogglePause && Gdx.input.isKeyPressed(Input.Keys.BACK)) {
            canTogglePause = false;
            AudioUtils.doSound(game, SoundMode.PLAY, SFXLibrary.MENU_BOOP);
            Gdx.input.vibrate(10);
            if (!mainMenuActive) {
                transitionMenu(extraMenu, mainMenu);
            } else {
                Gdx.app.exit();
            }
        } else if (!Gdx.input.isKeyPressed(Input.Keys.BACK)) {
            // Force an explicit release of the key before we can pause again
            canTogglePause = true;
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
        backgroundImage.dispose();
        stage.dispose();
        instance = null;
    }
}
