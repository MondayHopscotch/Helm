package com.bitdecay.helm.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.bitdecay.helm.Helm;
import com.bitdecay.helm.prefs.GamePrefs;
import com.bitdecay.helm.sound.MusicLibrary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Monday on 1/4/2017.
 */
public class OptionsScreen implements Screen {

    Helm game;

    Stage stage;
    Skin skin;
    private float baseFontScale;

    private Map<String, Actor> labelMap = new HashMap<>();
    private Map<String, Actor> inputMap = new HashMap<>();

    private List<Runnable> closingActions = new ArrayList<>();
    private final Table prefsTable;

    public OptionsScreen(final Helm game) {
        this.game = game;

        stage = new Stage();
        skin = game.skin;

        baseFontScale = game.fontScale * 0.8f;

        Table mainTable = new Table();
        mainTable.setFillParent(true);

        prefsTable = new Table();
        prefsTable.align(Align.left);
        prefsTable.pad(200);

        generateSliderIntSetting("Steering Sensitivity", GamePrefs.SENSITIVITY, GamePrefs.SENSITIVITY_DEFAULT, GamePrefs.SENSITIVITY_MIN, GamePrefs.SENSITIVITY_MAX);
        generateCheckBoxSetting("Use Joystick Steering", GamePrefs.USE_JOYSTICK_STEERING, GamePrefs.USE_JOYSTICK_STEERING_DEFAULT);

        inputMap.get(GamePrefs.USE_JOYSTICK_STEERING).addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                updateSteeringSettingVisibility();
            }
        });

        generateSliderFloatSetting("Steering Joystick X", GamePrefs.JOYSTICK_STEERING_WIDTH, GamePrefs.JOYSTICK_STEERING_WIDTH_DEFAULT, GamePrefs.JOYSTICK_STEERING_MIN, GamePrefs.JOYSTICK_STEERING_MAX);
        generateSliderFloatSetting("Steering Joystick Y", GamePrefs.JOYSTICK_STEERING_HEIGHT, GamePrefs.JOYSTICK_STEERING_HEIGHT_DEFAULT, GamePrefs.JOYSTICK_STEERING_MIN, GamePrefs.JOYSTICK_STEERING_MAX);
        generateCheckBoxSetting("Use Lefty Controls", GamePrefs.USE_LEFT_HANDED_CONTROLS, GamePrefs.USE_LEFT_HANDED_CONTROLS_DEFAULT);

        generateCheckBoxSetting("Mute Music", GamePrefs.MUTE_MUSIC, GamePrefs.MUTE_MUSIC_DEFAULT);
        inputMap.get(GamePrefs.MUTE_MUSIC).addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                updateMusicMute();
            }
        });

        TextButton doneLabel = new TextButton("Save", skin);
        doneLabel.getLabel().setFontScale(baseFontScale * 1.5f);

        doneLabel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(TitleScreen.get(game));
            }
        });

        mainTable.add(prefsTable).expand().fill().align(Align.left);
        mainTable.row();
        mainTable.add(doneLabel);

        stage.addActor(mainTable);

        // call this to make sure our settings are appearing correctly right after opening the screen
        updateSteeringSettingVisibility();
    }

    private void generateCheckBoxSetting(String displayName, final String settingName, boolean defaultValue) {
        Label settingLabel = new Label(displayName, skin);
        settingLabel.setFontScale(baseFontScale);
        final CheckBox settingInput = new CheckBox(null, skin);
        settingInput.setChecked(Helm.prefs.getBoolean(settingName, defaultValue));
        // this isn't a font, but we can scale it the same
        settingInput.getImage().scaleBy(game.fontScale);
        settingInput.align(Align.bottomLeft);
        settingInput.setOrigin(Align.bottomLeft);

        prefsTable.add(settingLabel).align(Align.left).expandX();
        prefsTable.add(settingInput).size(
                settingInput.getImage().getWidth() * settingInput.getImage().getScaleX(),
                settingInput.getImage().getHeight() * settingInput.getImage().getScaleY()).align(Align.center);
        prefsTable.row();

        labelMap.put(settingName, settingLabel);
        inputMap.put(settingName, settingInput);

        closingActions.add(new Runnable() {
            @Override
            public void run() {
                Helm.prefs.putBoolean(settingName, settingInput.isChecked());
            }
        });
    }

    private void generateSliderIntSetting(String displayName, final String settingName, int defaultValue, int minValue, int maxValue) {
        Label settingLabel = new Label(displayName, skin);
        settingLabel.setFontScale(baseFontScale);
        final Slider settingInput = new Slider(minValue, maxValue, 1, false, skin);
        settingInput.setAnimateDuration(0.1f);
        settingInput.setValue(Helm.prefs.getInteger(settingName, defaultValue));
        setSliderKnobHeight(settingInput);

        addToOptions(settingName, settingLabel, settingInput);

        closingActions.add(new Runnable() {
            @Override
            public void run() {
                Helm.prefs.putInteger(settingName, (int) settingInput.getValue());
            }
        });
    }

    private void generateSliderFloatSetting(String displayName, final String settingName, float defaultValue, float minValue, float maxValue) {
        Label settingLabel = new Label(displayName, skin);
        settingLabel.setFontScale(baseFontScale);
        final Slider settingInput = new Slider(minValue, maxValue, .05f, false, skin);
        settingInput.setAnimateDuration(0.1f);
        settingInput.setValue(Helm.prefs.getFloat(settingName, defaultValue));

        setSliderKnobHeight(settingInput);
        addToOptions(settingName, settingLabel, settingInput);

        closingActions.add(new Runnable() {
            @Override
            public void run() {
                Helm.prefs.putFloat(settingName, settingInput.getValue());
            }
        });
    }

    private void setSliderKnobHeight(Slider settingInput) {
        // this affects all slider knobs
        int screenHeight = Gdx.graphics.getHeight();
        settingInput.getStyle().knob.setMinHeight(screenHeight / 15);
    }

    private void addToOptions(String settingName, Label settingLabel, Slider settingInput) {
        labelMap.put(settingName, settingLabel);
        inputMap.put(settingName, settingInput);

        prefsTable.add(settingLabel).align(Align.left);
        prefsTable.add(settingInput).width(300);
        prefsTable.row();
    }

    private void updateMusicMute() {
        boolean mute = ((CheckBox) inputMap.get(GamePrefs.MUTE_MUSIC)).isChecked();
        if (mute) {
            Music music = game.assets.get(com.bitdecay.helm.sound.MusicLibrary.AMBIENT_MUSIC, Music.class);
            if (music.isPlaying()) {
                music.pause();
            }
        } else {
            Music music = game.assets.get(com.bitdecay.helm.sound.MusicLibrary.AMBIENT_MUSIC, Music.class);
            music.setLooping(true);
            if (!music.isPlaying()) {
                music.play();
            }
        }
    }

    private void updateSteeringSettingVisibility() {
        boolean usingJoystickSteering = ((CheckBox) inputMap.get(GamePrefs.USE_JOYSTICK_STEERING)).isChecked();

        if (usingJoystickSteering) {
            disableSetting(GamePrefs.SENSITIVITY);
            enableSetting(GamePrefs.JOYSTICK_STEERING_WIDTH);
            enableSetting(GamePrefs.JOYSTICK_STEERING_HEIGHT);
        } else {
            enableSetting(GamePrefs.SENSITIVITY);
            disableSetting(GamePrefs.JOYSTICK_STEERING_WIDTH);
            disableSetting(GamePrefs.JOYSTICK_STEERING_HEIGHT);
        }
    }

    private void enableSetting(String settingName) {
        labelMap.get(settingName).setTouchable(Touchable.enabled);
        inputMap.get(settingName).setTouchable(Touchable.enabled);
        labelMap.get(settingName).setColor(Color.WHITE);
        inputMap.get(settingName).setColor(Color.WHITE);
    }

    private void disableSetting(String settingName) {
        labelMap.get(settingName).setTouchable(Touchable.disabled);
        inputMap.get(settingName).setTouchable(Touchable.disabled);
        labelMap.get(settingName).setColor(Color.DARK_GRAY);
        inputMap.get(settingName).setColor(Color.DARK_GRAY);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
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
        for (Runnable closingAction : closingActions) {
            closingAction.run();
        }
        Helm.prefs.flush();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
