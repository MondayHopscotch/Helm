package com.bitdecay.helm.unlock.palette;

import com.bitdecay.helm.scoring.ScoreStamps;
import com.bitdecay.helm.system.render.GamePalette;
import com.bitdecay.helm.unlock.palette.types.*;

/**
 * Created by Monday on 6/5/2017.
 */

public enum PaletteList {
    STANDARD("Helm", new StandardPalette(), 0),
    MIDNIGHT("Midnight", new MidnightPalette(), 225000),
    VIDEO_KID("Video Kid", new VideoKidPalette(), 450000),
    BRIMSTONE("Brimstone", new BrimstonePalette(), 675000),
    MUTED("Muted", new MutedPalette(), 900000),
    OCEAN("Ocean", new OceanPalette(), 1125000),
    AUTUMN("Autumn", new AutumnPalette(), 1350000);

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
