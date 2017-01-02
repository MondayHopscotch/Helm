package com.bitdecay.game.desktop.editor;

/**
 * Created by Monday on 10/11/2015.
 */
public enum MouseButton {
    LEFT,
    RIGHT,
    MIDDLE;

    public static MouseButton getButton(int button) {
        switch (button) {
            case 0:
                return LEFT;
            case 1:
                return RIGHT;
            case 2:
                return MIDDLE;
            default:
                return null;
        }
    }
}