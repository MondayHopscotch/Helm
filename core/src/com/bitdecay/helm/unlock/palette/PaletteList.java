package com.bitdecay.helm.unlock.palette;

import com.bitdecay.helm.scoring.ScoreStamps;
import com.bitdecay.helm.system.render.GamePalette;

/**
 * Created by Monday on 6/5/2017.
 */

public enum PaletteList {
    STANDARD("Helm", new com.bitdecay.helm.unlock.palette.types.StandardPalette(), 0),
    GUACAMOLE("Guacamole", new com.bitdecay.helm.unlock.palette.types.GuacamolePalette(), 100000),
    WHITEOUT("Whiteout", new com.bitdecay.helm.unlock.palette.types.WhiteoutPalette(), 200000),
    VIDEO_KID("Video Kid", new com.bitdecay.helm.unlock.palette.types.VideoKidPalette(), 300000),
    CHARCOAL("Charcoal", new com.bitdecay.helm.unlock.palette.types.CharcoalPalette(), 400000),
    MUTED("Muted", new com.bitdecay.helm.unlock.palette.types.MutedPalette(), 500000);

    public final String name;
    public final int pointsForUnlock;
    public final GamePalette palette;

    PaletteList(String name, GamePalette palette, int pointsForUnlock) {
        this.name = name;
        this.pointsForUnlock = pointsForUnlock;
        this.palette = palette;
    }

    public static void checkUnlocks(int totalHighScore, int pointsImprovement) {
        for (PaletteList unlock : PaletteList.values()) {
            if (unlock.pointsForUnlock > totalHighScore - pointsImprovement && unlock.pointsForUnlock < totalHighScore) {
                // we just crossed the unlock for this palette
                ScoreStamps.addPendingStamp(unlock.name + " palette unlocked!");
            }
        }

    }
}
