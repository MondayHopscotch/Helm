package com.bitdecay.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.StringBuilder;
import com.bitdecay.game.Helm;

/**
 * Created by Monday on 4/8/2017.
 */

public class SplashScreen implements Screen {
    private Helm helm;
    private final Stage stage;

    private final Image bitDecaySplash;
    private final Label animatedText;

    private StringBuilder ellipsisText = new StringBuilder(4);
    private final Label loadingText;

    private boolean transitioningAway = false;

    private final String[] loadingSayings = new String[] {
            "Preparing cockpit",
            "Cleaning windshield",
            "Fueling the ship",
            "Checking turn signals"
    };

    public SplashScreen(Helm helm) {
        this.helm = helm;
        stage = new Stage();
        stage.setDebugAll(Helm.debug);

        bitDecaySplash = new Image(new TextureRegion(new Texture(Gdx.files.internal("splash/bitDecay_splash.png"))));
        bitDecaySplash.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        stage.addActor(bitDecaySplash);

        loadingText = new Label(loadingSayings[MathUtils.random(0, loadingSayings.length-1)], helm.skin);
        loadingText.setFontScale(helm.fontScale * 1.5f);

        animatedText = new Label("", helm.skin);
        animatedText.setFontScale(helm.fontScale * 1.5f);

        Table textTable = new Table();
        textTable.setOrigin(Align.center);
        textTable.align(Align.bottom);
        textTable.add(loadingText).align(Align.bottom);
        textTable.add(animatedText).width(100).align(Align.bottom);

        Table mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.add(textTable).expand().fill().expand().fill().padBottom(Gdx.graphics.getHeight() / 20);

        stage.addActor(mainTable);
    }

    @Override
    public void show() {

        loadingText.addAction(
                Actions.sequence(
                        Actions.alpha(0),
                        Actions.delay(0.25f),
                        Actions.fadeIn(1f)
                )
        );

        animatedText.addAction(
                Actions.sequence(
                        Actions.alpha(0),
                        Actions.delay(0.25f),
                        Actions.fadeIn(1f),
                        Actions.repeat(-1, Actions.sequence(
                                Actions.delay(0.2f),
                                Actions.run(new Runnable() {
                                    @Override
                                    public void run() {
                                        ellipsisText.append(".");
                                        if (ellipsisText.length() > 3) {
                                            ellipsisText.delete(0, 4);
                                        }
                                        animatedText.setText(ellipsisText.toString());
                                    }
                                })
                        ))
                )
        );

        bitDecaySplash.addAction(
                Actions.sequence(
                        Actions.alpha(0),
                        Actions.delay(0.25f),
                        Actions.fadeIn(1f),
                        Actions.delay(2f) // this will ensure we are on this screen for at least 2 seconds
                )
        );
    }

    private void nextScreen() {
        loadingText.addAction(Actions.fadeOut(1));
        animatedText.setText("");
        animatedText.clearActions();
        animatedText.addAction(Actions.fadeOut(1));
        bitDecaySplash.addAction(
                Actions.sequence(
                        Actions.fadeOut(1),
                        Actions.run(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        helm.setScreen(new TitleScreen(helm));
                                    }
                                }
                        )
                )
        );
    }

    @Override
    public void render(float delta) {
        if (helm.assets.update() && !transitioningAway) {
            if (bitDecaySplash.getActions().size == 0) {
                transitioningAway = true;
                nextScreen();
            }
        }
        Gdx.gl.glClearColor(0, 0, 0, 1);
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
        dispose();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
