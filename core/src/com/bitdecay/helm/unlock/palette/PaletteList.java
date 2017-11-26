package com.bitdecay.helm.unlock.palette;

import com.bitdecay.helm.scoring.ScoreStamps;
import com.bitdecay.helm.system.render.GamePalette;
import com.bitdecay.helm.unlock.palette.types.CharcoalPalette;
import com.bitdecay.helm.unlock.palette.types.GuacamolePalette;
import com.bitdecay.helm.unlock.palette.types.MutedPalette;
import com.bitdecay.helm.unlock.palette.types.StandardPalette;
import com.bitdecay.helm.unlock.palette.types.VideoKidPalette;
import com.bitdecay.helm.unlock.palette.types.WhiteoutPalette;

/**
 * Created by Monday on 6/5/2017.
 */

public enum PaletteList {
    STANDARD("Helm", new StandardPalette(), 0),
    GUACAMOLE("Guacamole", new GuacamolePalette(), 100000),
    WHITEOUT("Whiteout", new WhiteoutPalette(), 200000),
    VIDEO_KID("Video Kid", new VideoKidPalette(), 300000),
    CHARCOAL("Charcoal", new CharcoalPalette(), 400000),
    MUTED("Muted", new MutedPalette(), 500000);

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
