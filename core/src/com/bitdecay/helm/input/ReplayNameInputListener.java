package com.bitdecay.helm.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.bitdecay.helm.GamePilot;
import com.bitdecay.helm.menu.BitImageButton;

/**
 * Created by Monday on 10/19/2017.
 */

public class ReplayNameInputListener implements Input.TextInputListener {

    public boolean valid;
    public String lastInput;
    private GamePilot saver;
    private BitImageButton button;

    public ReplayNameInputListener(GamePilot saver, BitImageButton button) {
        this.saver = saver;
        this.button = button;
    }

    @Override
    public void input (String text) {
        if (text == null || text.length() <= 0) {
            return;
        }

        text = text.replaceAll("[^A-Za-z0-9_-] ", "");
        if (text.length() > 0) {
            saver.saveLastReplay(text);
            button.clearListeners();
            button.down();
            button.setDisabled(true);
            button.setTouchable(Touchable.disabled);
        }
    }

    @Override
    public void canceled () {
        valid = false;
        lastInput = null;
    }
}