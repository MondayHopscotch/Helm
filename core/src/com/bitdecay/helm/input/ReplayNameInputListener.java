package com.bitdecay.helm.input;

import com.badlogic.gdx.Input;

/**
 * Created by Monday on 10/19/2017.
 */

public class ReplayNameInputListener implements Input.TextInputListener {

    public boolean valid;
    public String lastInput;

    @Override
    public void input (String text) {
        valid = true;
        lastInput = text;
    }

    @Override
    public void canceled () {
        valid = false;
        lastInput = null;
    }
}