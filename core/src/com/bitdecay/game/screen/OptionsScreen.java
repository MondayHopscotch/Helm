package com.bitdecay.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
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
    private final CheckBox joystickSteeringInput;
    private final Slider steeringXInput;
    private final Slider steeringYInput;
    private final Label steeringPositionYLabel;
    private final Label steeringPositionXLabel;

    private final Label steeringSensitivityLabel;
    private final Slider steeringSensitivityInput;

    private float baseFontScale;

    public OptionsScreen(final Helm game) {
        this.game = game;

        stage = new Stage();
        skin = game.skin;

        baseFontScale = game.fontScale * 0.8f;

        Table mainTable = new Table();
        mainTable.setFillParent(true);

        Table prefsTable = new Table();
        prefsTable.align(Align.left);
        prefsTable.pad(200);

        steeringSensitivityLabel = new Label("Sensitivity", skin);
        steeringSensitivityLabel.setFontScale(baseFontScale);
        steeringSensitivityInput = new Slider(GamePrefs.SENSITIVITY_MIN, GamePrefs.SENSITIVITY_MAX, 1, false, skin);
        steeringSensitivityInput.setAnimateDuration(0.1f);
        steeringSensitivityInput.setValue(Helm.prefs.getInteger(GamePrefs.SENSITIVITY, GamePrefs.SENSITIVITY_DEFAULT));

        Label joystickSteeringLabel = new Label("Use Joystick Steering", skin);
        joystickSteeringLabel.setFontScale(baseFontScale);
        joystickSteeringInput = new CheckBox(null, skin);
        joystickSteeringInput.setChecked(Helm.prefs.getBoolean(GamePrefs.USE_JOYSTICK_STEERING, GamePrefs.USE_JOYSTICK_STEERING_DEFAULT));
        // this isn't a font, but we can scale it the same
        joystickSteeringInput.getImage().scaleBy(game.fontScale);
        joystickSteeringInput.align(Align.bottomLeft);
        joystickSteeringInput.setOrigin(Align.bottomLeft);

        joystickSteeringInput.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                updateSteeringSettingVisibility();
            }
        });

        steeringPositionXLabel = new Label("Steering Joystick X", skin);
        steeringPositionXLabel.setFontScale(baseFontScale);
        steeringXInput = new Slider(.1f, .5f, .05f, false, skin);
        steeringXInput.setAnimateDuration(0.1f);
        // this affects all slider knobs

        int screenHeight = Gdx.graphics.getHeight();
        steeringXInput.getStyle().knob.setMinHeight(screenHeight / 25);
        steeringXInput.setValue(Helm.prefs.getFloat(GamePrefs.SIMPLE_STEERING_WIDTH, GamePrefs.SIMPLE_STEERING_WIDTH_DEFAULT));

        steeringPositionYLabel = new Label("Steering Joystick Y", skin);
        steeringPositionYLabel.setFontScale(baseFontScale);
        steeringYInput = new Slider(.1f, .5f, .05f, false, skin);
        steeringYInput.setAnimateDuration(0.1f);
        steeringYInput.setValue(Helm.prefs.getFloat(GamePrefs.SIMPLE_STEERING_HEIGHT, GamePrefs.SIMPLE_STEERING_HEIGHT_DEFAULT));

        prefsTable.add(steeringSensitivityLabel).align(Align.left);
        prefsTable.add(steeringSensitivityInput).width(300);
        prefsTable.row();

        prefsTable.add(joystickSteeringLabel).align(Align.left).expandX();
        prefsTable.add(joystickSteeringInput).size(
                joystickSteeringInput.getImage().getWidth() * joystickSteeringInput.getImage().getScaleX(),
                joystickSteeringInput.getImage().getHeight() * joystickSteeringInput.getImage().getScaleY()).align(Align.center);
        prefsTable.row();

        prefsTable.add(steeringPositionXLabel).align(Align.left);
        prefsTable.add(steeringXInput).width(300);
        prefsTable.row();

        prefsTable.add(steeringPositionYLabel).align(Align.left);
        prefsTable.add(steeringYInput).width(300);
        prefsTable.row();


        TextButton doneLabel = new TextButton("Save", skin);
        doneLabel.getLabel().setFontScale(baseFontScale * 1.5f);

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

        updateSteeringSettingVisibility();
    }

    private void updateSteeringSettingVisibility() {
        boolean usingJoystickSteering = joystickSteeringInput.isChecked();
        steeringPositionXLabel.setVisible(usingJoystickSteering);
        steeringXInput.setVisible(usingJoystickSteering);
        steeringPositionYLabel.setVisible(usingJoystickSteering);
        steeringYInput.setVisible(usingJoystickSteering);
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
        Helm.prefs.putInteger(GamePrefs.SENSITIVITY, (int) steeringSensitivityInput.getValue());
        Helm.prefs.putBoolean(GamePrefs.USE_JOYSTICK_STEERING, joystickSteeringInput.isChecked());
        Helm.prefs.putFloat(GamePrefs.SIMPLE_STEERING_WIDTH, steeringXInput.getValue());
        Helm.prefs.putFloat(GamePrefs.SIMPLE_STEERING_HEIGHT, steeringYInput.getValue());
        Helm.prefs.flush();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
