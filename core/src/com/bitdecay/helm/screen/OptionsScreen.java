package com.bitdecay.helm.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.bitdecay.helm.Helm;
import com.bitdecay.helm.prefs.GamePrefs;
import com.bitdecay.helm.sound.SFXLibrary;
import com.bitdecay.helm.sound.SoundMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Monday on 1/4/2017.
 */
public class OptionsScreen extends AbstractScrollingItemScreen {
    private float baseFontScale;

    private Map<String, Actor> labelMap = new HashMap<>();
    private Map<String, Actor> inputMap = new HashMap<>();

    private List<Runnable> closingActions = new ArrayList<>();
    private final Table prefsTable = new Table();

    private static OptionsScreen instance;

    public static OptionsScreen get(Helm game) {
        if (instance == null) {
            instance = new OptionsScreen(game);
        }
        return instance;
    }

    public OptionsScreen(final Helm game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();
        scroll.setCancelTouchFocus(false);
    }

    @Override
    public void populateRows(Table mainTable) {
        baseFontScale = game.fontScale * 0.8f;

        prefsTable.clear();

        prefsTable.align(Align.left);

        generateSliderIntSetting("Steering Sensitivity", GamePrefs.SENSITIVITY, GamePrefs.SENSITIVITY_DEFAULT, GamePrefs.SENSITIVITY_MIN, GamePrefs.SENSITIVITY_MAX);
        generateCheckBoxSetting("Left-handed Controls", GamePrefs.USE_LEFT_HANDED_CONTROLS, GamePrefs.USE_LEFT_HANDED_CONTROLS_DEFAULT);

        generateCheckBoxSetting("Ambient Music", GamePrefs.MUSIC_ENABLED, GamePrefs.MUSIC_ENABLED_DEFAULT);
        inputMap.get(GamePrefs.MUSIC_ENABLED).addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                updateMusicMute();
            }
        });

        mainTable.add(prefsTable).fillX().expandX();
    }

    @Override
    public String getTitle() {
        return "Options";
    }

    @Override
    public ClickListener getReturnButtonAction() {
        return new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                AudioUtils.doSound(game, SoundMode.PLAY, SFXLibrary.MENU_BOOP);
                Gdx.input.vibrate(10);
                stage.addAction(Transitions.getQuickFadeOut(new Runnable() {
                    @Override
                    public void run() {
                        game.setScreen(TitleScreen.get(game));
                    }
                }));
            }
        };
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

        settingInput.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                AudioUtils.doSound(game, SoundMode.PLAY, SFXLibrary.MENU_BOOP);
                Gdx.input.vibrate(10);
            }
        });

        int screenHeight = Gdx.graphics.getHeight();

        prefsTable.add(settingLabel).align(Align.left).expandX().padBottom(screenHeight / 10);
        prefsTable.add(settingInput).size(
                settingInput.getImage().getWidth() * settingInput.getImage().getScaleX(),
                settingInput.getImage().getHeight() * settingInput.getImage().getScaleY())
                .align(Align.center)
                .padBottom(screenHeight / 10);
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

    private void setSliderKnobHeight(Slider settingInput) {
        // this affects all slider knobs
        int screenHeight = Gdx.graphics.getHeight();
        int knobHeight = screenHeight / 7;
        int knobWidth = knobHeight / 3;
        settingInput.getStyle().knob.setMinHeight(screenHeight / 7);
        settingInput.getStyle().knob.setMinWidth(knobWidth);
    }

    private void addToOptions(String settingName, Label settingLabel, Slider settingInput) {
        labelMap.put(settingName, settingLabel);
        inputMap.put(settingName, settingInput);

        int screenHeight = Gdx.graphics.getHeight();
        prefsTable.add(settingLabel).align(Align.left).padBottom(screenHeight / 10);
        prefsTable.add(settingInput).width(screenHeight / 3).padBottom(screenHeight / 10);
        prefsTable.row();
    }

    private void updateMusicMute() {
        boolean enabled = ((CheckBox) inputMap.get(GamePrefs.MUSIC_ENABLED)).isChecked();
        if (enabled) {
            Music music = game.assets.get(com.bitdecay.helm.sound.MusicLibrary.AMBIENT_MUSIC, Music.class);
            music.setLooping(true);
            if (!music.isPlaying()) {
                music.play();
            }
        } else {
            Music music = game.assets.get(com.bitdecay.helm.sound.MusicLibrary.AMBIENT_MUSIC, Music.class);
            if (music.isPlaying()) {
                music.pause();
            }
        }
    }

    @Override
    public void hide() {
        super.hide();
        for (Runnable closingAction : closingActions) {
            closingAction.run();
        }
        Helm.prefs.flush();
    }
}
