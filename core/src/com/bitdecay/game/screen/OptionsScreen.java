package com.bitdecay.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.bitdecay.game.Helm;
import com.bitdecay.game.prefs.GamePrefs;

/**
 * Created by Monday on 1/4/2017.
 */
public class OptionsScreen implements Screen {

    Helm game;

    Stage stage;
    Skin skin;
    private final CheckBox dynamicSteeringInput;

    public OptionsScreen(final Helm game) {
        this.game = game;

        stage = new Stage();
        skin = game.skin;


        Table mainTable = new Table();
        mainTable.setFillParent(true);

        Table prefsTable = new Table();
        prefsTable.align(Align.left);
        prefsTable.pad(200);

        Label dynamicSteeringLabel = new Label("Use Dynamic Steering", skin);
        dynamicSteeringLabel.setFontScale(3);
        dynamicSteeringInput = new CheckBox(null, skin);
        dynamicSteeringInput.setChecked(Helm.prefs.getBoolean(GamePrefs.USE_DYNAMIC_STEERING_CONTROLS, GamePrefs.USE_DYNAMIC_STEERING_CONTROLS_DEFAULT));
        dynamicSteeringInput.getImage().scaleBy(6);
        dynamicSteeringInput.align(Align.bottomLeft);
        dynamicSteeringInput.setOrigin(Align.bottomLeft);

        prefsTable.add(dynamicSteeringLabel).align(Align.left).expandX();
        prefsTable.add(dynamicSteeringInput).size(
                dynamicSteeringInput.getImage().getWidth() * dynamicSteeringInput.getImage().getScaleX(),
                dynamicSteeringInput.getImage().getHeight() * dynamicSteeringInput.getImage().getScaleY()).align(Align.center);
        prefsTable.row();


        TextButton doneLabel = new TextButton("Done", skin);
        doneLabel.getLabel().setFontScale(7);

        doneLabel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new TitleScreen(game));
            }
        });

        mainTable.add(prefsTable).expand().fill().align(Align.left);
        mainTable.row();
        mainTable.add(doneLabel);

        stage.addActor(mainTable);
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
        Helm.prefs.putBoolean(GamePrefs.USE_DYNAMIC_STEERING_CONTROLS, dynamicSteeringInput.isChecked());
        Helm.prefs.flush();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
