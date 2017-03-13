package com.bitdecay.game.desktop.editor;

public enum OptionsMode {
    // Modes that are a one-time action
    DRAW_LINE("Draw Line", EditorKeys.LINE_MODE),
    DELETE_LINE("Delete Line", EditorKeys.DELETE_LINE),
    DRAW_LANDING("Draw Landing", EditorKeys.LANDING_MODE),
    ROTATE_LANDING("Rotate Landing", EditorKeys.ROTATE_LANDING),
    PLACE_START("Place Start", EditorKeys.START_MODE),
    SET_FUEL("Set Fuel", EditorKeys.SET_FUEL),
    ADD_FOCUS("Add Focal Point", EditorKeys.ADD_FOCUS),
    REMOVE_FOCUS("Remove Focal Point", EditorKeys.REMOVE_FOCUS),
    ADD_GRAV_WELL("Add Gravity Well", EditorKeys.ADD_GRAV_WELL),
    REMOVE_GRAV_WELL("Remove Gravity Well", EditorKeys.REMOVE_GRAV_WELL),
    ADD_REPULSION_FIELD("Add Repulsion Field", EditorKeys.ADD_REPULSION_FIELD),
    REMOVE_REPULSION_FIELD("Remove Repulsion Field", EditorKeys.REMOVE_REPULSION_FIELD),

    SET_NAME("Set Level Name", EditorKeys.SET_NAME),

    SAVE_LEVEL("Save", EditorKeys.SAVE),
    LOAD_LEVEL("Load", EditorKeys.LOAD);



    public final String label;
    public final EditorKeys hotkey;

    OptionsMode(String label, EditorKeys hotkey) {
        this.label = label;
        this.hotkey = hotkey;
    }
}