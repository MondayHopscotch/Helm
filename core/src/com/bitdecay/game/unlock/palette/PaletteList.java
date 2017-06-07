package com.bitdecay.game.unlock.palette;

import com.bitdecay.game.system.render.GamePalette;

/**
 * Created by Monday on 6/5/2017.
 */

public enum PaletteList {
    STANDARD("Helm", new StandardPalette(), 0),
    GUACAMOLE("Guacamole", new GuacamolePalette(), 100000);

    public final String name;
    public final int pointsForUnlock;
    public final GamePalette palette;

    PaletteList(String name, GamePalette palette, int pointsForUnlock) {
        this.name = name;
        this.pointsForUnlock = pointsForUnlock;
        this.palette = palette;
    }
}
