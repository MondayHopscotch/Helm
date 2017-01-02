package com.bitdecay.game.desktop.editor;

public enum OptionsMode {
    // Modes that are a one-time action
    DRAW_LINE("Draw Line", EditorKeys.LINE_MODE, "icons/object.png"),
    DRAW_LANDING("Draw Landing", EditorKeys.LANDING_MODE, null),
    PLACE_START("Place Start", EditorKeys.START_MODE, "icons/trigger.png"),

    SAVE_LEVEL("Save", EditorKeys.SAVE, null),
    LOAD_LEVEL("Load", EditorKeys.LOAD, null);



    public final String label;
    public final EditorKeys hotkey;
    public final String icon;

    OptionsMode(String label, EditorKeys hotkey, String icon) {
        this.label = label;
        this.hotkey = hotkey;
        this.icon = icon;
    }
}