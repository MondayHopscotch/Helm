package com.bitdecay.game.unlock;

/**
 * Created by Monday on 2/18/2017.
 */

public enum StatName {
    FLIGHT_TIME("Total Flight Time", "flightTime", StatType.TIME),
    LEVELS_COMPLETED("Levels Completed", "levelsCompleted", StatType.COUNT),

    LAUNCHES("Launches", "playerLaunches", StatType.COUNT),
    LANDINGS("Successful Landings", "landings", StatType.COUNT),
    WALL_CRASHES("Crashes", "wallCrashes", StatType.COUNT),
    GRAV_WELL_CRASHES("Gravity Well Crashes", "gravWellCrashes", StatType.COUNT),
    OOB_CRASHES("Out Of Bounds Crashes", "OOBCrashes", StatType.COUNT),
    LANDING_PLAT_CRASHES("Landing Platform Crashes", "platformCrashes", StatType.COUNT),

    ABANDONS("Abandoned Runs", "abandons", StatType.COUNT);

    public final String displayName;
    public final String preferenceID;
    public StatType type;

    StatName(String display, String preference, StatType type) {

        this.displayName = display;
        this.preferenceID = preference;
        this.type = type;
    }
}
