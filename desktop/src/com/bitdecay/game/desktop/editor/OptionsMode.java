package com.bitdecay.game.desktop.editor;

public enum OptionsMode {
    // Modes that are a one-time action
    DRAW_LINE("Draw Line", EditorKeys.LINE_MODE),
    DELETE_LINE("Delete Line", EditorKeys.DELETE_LINE),
    DRAW_LANDING("Draw Landing", EditorKeys.LANDING_MODE),
    PLACE_START("Place Start", EditorKeys.START_MODE),
    SET_FUEL("Set Fuel", EditorKeys.SET_FUEL),

    SAVE_LEVEL("Save", EditorKeys.SAVE),
    LOAD_LEVEL("Load", EditorKeys.LOAD);



    public final String label;
    public final EditorKeys hotkey;

    OptionsMode(String label, EditorKeys hotkey) {
        this.label = label;
        this.hotkey = hotkey;
    }
}