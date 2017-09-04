package com.bitdecay.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.bitdecay.game.Helm;

/**
 * Created by Monday on 6/13/2017.
 */

public class TransitionColorScreen implements Screen {

    public static TransitionColorScreen transitionScreen;

    public static TransitionColorScreen get(Helm helm, Color to, Screen after) {
        if (transitionScreen == null) {
            transitionScreen = new TransitionColorScreen();
        }
        transitionScreen.helm = helm;
        // reset our timer
        transitionScreen.secondsBeforeTransfer = TRANSITION_SECONDS;
        // save our last color
        transitionScreen.transitionFrom = transitionScreen.transitionTo;
        // set our new color
        transitionScreen.transitionTo = to;
        transitionScreen.showAfter = after;

        return transitionScreen;
    }

    private static final float TRANSITION_SECONDS = 0.5f;

    private Helm helm;

    private float secondsBeforeTransfer = TRANSITION_SECONDS;
    private Color transitionFrom = Color.BLACK;
    private Color transitionTo = Color.BLACK;


    private Color currentColor = new Color(Color.BLACK);


    private Screen showAfter = null;

    @Override
    public void show() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void render(float delta) {
        secondsBeforeTransfer -= delta;
        currentColor.set(transitionFrom);
        currentColor.lerp(transitionTo, 1 - (secondsBeforeTransfer / TRANSITION_SECONDS));
        Gdx.gl.glClearColor(currentColor.r, currentColor.g, currentColor.b, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (secondsBeforeTransfer <= 0) {
            helm.setScreen(showAfter);
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
        // if we dispose the screen, create a new instance next time we need it
        transitionScreen = null;
    }
}
