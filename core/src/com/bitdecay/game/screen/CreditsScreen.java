package com.bitdecay.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.bitdecay.game.Helm;

public class CreditsScreen implements Screen {

    private static String SPACE_AFTER_TITLE = "\n\n\n";
    private static String SPACE_AFTER_NAME = "\n\n";

    private Helm game;
    private Stage stage = new Stage();

    private Label lblTitle;
    private Label lblCredits;


    public CreditsScreen(final Helm game) {
        this.game = game;

        Skin skin = game.skin;

        lblTitle = new Label("Credits", skin);
        lblTitle.setFontScale(10);
        lblTitle.setFillParent(true);
        lblTitle.setAlignment(Align.topLeft);
        lblTitle.setColor(Color.SKY);

        lblCredits = new Label("Programming and Design: " + SPACE_AFTER_TITLE +
                "Logan Moore" + SPACE_AFTER_TITLE +
                "All sounds created using BFXR" + SPACE_AFTER_TITLE +
                "Music:" + SPACE_AFTER_TITLE +
                "None yet" + SPACE_AFTER_NAME,
                skin);
        lblCredits.setFontScale(6);
        lblCredits.setFillParent(true);
        lblCredits.setAlignment(Align.top);
        lblCredits.setColor(Color.WHITE);

        stage.addActor(lblTitle);
        stage.addActor(lblCredits);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {
        lblCredits.addAction(Actions.sequence(
                Actions.moveBy(0, -Gdx.graphics.getHeight()),
                Actions.moveBy(0, lblCredits.getHeight() * lblCredits.getFontScaleY() + Gdx.graphics.getHeight(), 30),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        nextScreen();
                    }
                })
        ));
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();

        if (Gdx.input.isTouched()) {
            nextScreen();
        }
    }

    public void nextScreen(){
        game.setScreen(new TitleScreen(game));
    }


    public void update(float delta){

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
