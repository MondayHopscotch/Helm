package com.bitdecay.game.desktop.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

/**
 * Created by Monday on 1/1/2017.
 */
public enum EditorKeys {
    // Camera controls
    PAN_LEFT("Camera Left", false, Input.Keys.LEFT),
    PAN_RIGHT("Camera Right", false, Input.Keys.RIGHT),
    PAN_UP("Camera Up", false, Input.Keys.UP),
    PAN_DOWN("Camera Down", false, Input.Keys.DOWN),
    ZOOM_IN("Camera Zoom In", false, Input.Keys.EQUALS),
    ZOOM_OUT("Camera Zoom Out", false, Input.Keys.MINUS),

    SAVE("Save", false, Input.Keys.S),
    LOAD("Load", false, Input.Keys.O),

    LINE_MODE("Draw Line", false, Input.Keys.NUM_1),
    DELETE_LINE("Delete Line", false, Input.Keys.NUM_2),
    LANDING_MODE("Draw Landing", false, Input.Keys.NUM_3),
    START_MODE("Place Start", false, Input.Keys.NUM_4),
    ADD_FOCUS("Add Focal Point", false, Input.Keys.NUM_5),
    REMOVE_FOCUS("Remove Focal Point", false, Input.Keys.NUM_6),
    ADD_GRAV_WELL("Add Gravity Well", false, Input.Keys.NUM_7),
    REMOVE_GRAV_WELL("Remove Gravity Well", false, Input.Keys.NUM_8),

    SET_FUEL("Set Fuel Level", false, Input.Keys.F),

    SET_NAME("Set Level Name", false, Input.Keys.N),

    TEST_LEVEL("Test Level", false, Input.Keys.T),

    HELP("Help", false, Input.Keys.F1);

    private final String name;
    private final int[] keys;
    private final boolean isKeyCombo;
    private final String keyHelp;

    EditorKeys(String name, boolean isKeyCombo, int... keyList) {
        this.name = name;
        this.isKeyCombo = isKeyCombo;
        this.keys = keyList;

        StringBuilder builder = new StringBuilder();

        for (int key : keys) {
            if (builder.length() > 0) {
                if (isKeyCombo) {
                    builder.append(" and ");
                } else {
                    builder.append(" or ");
                }
            }
            builder.append(Input.Keys.toString(key));
        }
        keyHelp = builder.toString();
    }

    public boolean isPressed() {
        if (isKeyCombo) {
            return allKeysPressed();
        } else {
            return atLeastOneKeyPressed();
        }

    }

    private boolean allKeysPressed() {
        for (int key : keys) {
            if (!Gdx.input.isKeyPressed(key)) {
                return false;
            }
        }
        return true;
    }

    private boolean atLeastOneKeyPressed() {
        for (int key : keys) {
            if (Gdx.input.isKeyPressed(key)) {
                if (isKeyAModifer(key) || !isAnyModiferPressed()) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isJustPressed() {
        if (isKeyCombo) {
            return comboJustPressed();
        } else {
            return atLeastOneKeyJustPressed();
        }
    }

    private boolean comboJustPressed() {
        boolean atLeastOneJustPressed = false;
        for (int key : keys) {
            if (!Gdx.input.isKeyPressed(key)) {
                // some part of our combo isn't pressed.
                return false;
            } else if (Gdx.input.isKeyJustPressed(key)) {
                atLeastOneJustPressed = true;
            }
        }
        // combo is all pressed and at least one key was just pressed
        return atLeastOneJustPressed;
    }

    private boolean atLeastOneKeyJustPressed() {
        for (int key : keys) {
            if (Gdx.input.isKeyJustPressed(key)) {
                if (isKeyAModifer(key) || !isAnyModiferPressed()) {
                    return true;
                }
            }
        }
        return false;
    }

    public String getHelp() {
        return name + ": " + keyHelp;
    }

    private static boolean isKeyAModifer(int key) {
        if (Input.Keys.SHIFT_LEFT == key) {
            return true;
        }
        if (Input.Keys.SHIFT_RIGHT == key) {
            return true;
        }
        if (Input.Keys.CONTROL_LEFT == key) {
            return true;
        }
        if (Input.Keys.CONTROL_RIGHT == key) {
            return true;
        }
        if (Input.Keys.ALT_LEFT == key) {
            return true;
        }
        if (Input.Keys.ALT_RIGHT == key) {
            return true;
        }
        return false;
    }

    private static boolean isAnyModiferPressed() {
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
            return true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT)) {
            return true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
            return true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT)) {
            return true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.ALT_LEFT)) {
            return true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.ALT_RIGHT)) {
            return true;
        }
        return false;
    }
}
