package com.bitdecay.game.unlock;

/**
 * Created by Monday on 2/18/2017.
 */

public enum StatName {
    LAUNCHES("Launches", "playerLaunches"),
    LANDINGS("Successful Landings", "landings"),
    WALL_CRASHES("Crashes", "wallCrashes"),
    GRAV_WELL_CRASHES("Gravity Well Crashes", "gravWellCrashes"),
    OOB_CRASHES("Out Of Bounds Crashes", "OOBCrashes"),
    LANDING_PLAT_CRASHES("Landing Platform Crashes", "platformCrashes");


    public final String displayName;
    public final String preferenceID;

    StatName(String display, String preference) {

        this.displayName = display;
        this.preferenceID = preference;
    }
}
