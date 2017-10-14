package com.bitdecay.helm.unlock;

/**
 * Created by Monday on 2/18/2017.
 */

public enum StatName {
    FLIGHT_TIME("Total Flight Time", "flightTime", StatType.TIME, StatDisplayType.ALWAYS_SHOW),
    LEVELS_COMPLETED("Levels Completed", "levelsCompleted", StatType.COUNT, StatDisplayType.ALWAYS_SHOW),

    LAUNCHES("Launches", "playerLaunches", StatType.COUNT, StatDisplayType.ALWAYS_SHOW),
    LANDINGS("Total Landings", "landings", StatType.COUNT, StatDisplayType.ALWAYS_SHOW),
    PERFECT_LANDINGS("Perfect Landings", "perfectLandings", StatType.COUNT, StatDisplayType.ALWAYS_SHOW),
    ZERO_FUEL_LANDINGS("Zero Fuel Landings", "zeroFuelLandings", StatType.COUNT, StatDisplayType.ALWAYS_SHOW),

    DEV_MEDALS(com.bitdecay.helm.menu.MedalUtils.LevelRating.DEV.medalName() + " Medals", "developerMedalCount", StatType.COUNT, StatDisplayType.ONLY_SHOW_WHEN_PRESENT),
    GOLD_MEDALS(com.bitdecay.helm.menu.MedalUtils.LevelRating.GOLD.medalName() + " Medals", "goldMedalCount", StatType.COUNT, StatDisplayType.ALWAYS_SHOW),
    SILVER_MEDALS(com.bitdecay.helm.menu.MedalUtils.LevelRating.SILVER.medalName() + " Medals", "silverMedalCount", StatType.COUNT, StatDisplayType.ALWAYS_SHOW),
    BRONZE_MEDALS(com.bitdecay.helm.menu.MedalUtils.LevelRating.BRONZE.medalName() + " Medals", "bronzeMedalCount", StatType.COUNT, StatDisplayType.ALWAYS_SHOW),

    WALL_CRASHES("Terrain Crashes", "wallCrashes", StatType.COUNT, StatDisplayType.ALWAYS_SHOW),
    GRAV_WELL_CRASHES("Gravity Well Crashes", "gravWellCrashes", StatType.COUNT, StatDisplayType.ALWAYS_SHOW),
    OOB_CRASHES("Out Of Bounds Crashes", "OOBCrashes", StatType.COUNT, StatDisplayType.ALWAYS_SHOW),
    LANDING_PLAT_CRASHES("Landing Platform Crashes", "platformCrashes", StatType.COUNT, StatDisplayType.ALWAYS_SHOW),

    ABANDONS("Abandoned Runs", "abandons", StatType.COUNT, StatDisplayType.ALWAYS_SHOW),

    NO_STAT("NOSTAT", "NOSTAT", StatType.NONE, StatDisplayType.HIDE);

    public final String displayName;
    public final String preferenceID;
    public StatType type;
    public StatDisplayType displayType;

    StatName(String display, String preference, StatType type, StatDisplayType displayType) {

        this.displayName = display;
        this.preferenceID = preference;
        this.type = type;
        this.displayType = displayType;
    }
}
