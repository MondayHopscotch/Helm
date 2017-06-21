package com.bitdecay.game.unlock.palette;

import com.bitdecay.game.scoring.ScoreStamps;
import com.bitdecay.game.system.render.GamePalette;
import com.bitdecay.game.unlock.palette.types.*;

/**
 * Created by Monday on 6/5/2017.
 */

public enum PaletteList {
    STANDARD("Helm", new StandardPalette(), 0),
    GUACAMOLE("Guacamole", new GuacamolePalette(), 100000),
    WHITEOUT("Whiteout", new WhiteoutPalette(), 200000),
    VIDEO_KID("Video Kid", new VideoKidPalette(), 300000),
    CHARCOAL("Charcoal", new CharcoalPalette(), 400000);

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
