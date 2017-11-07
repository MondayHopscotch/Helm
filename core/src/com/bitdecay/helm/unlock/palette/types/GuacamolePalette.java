package com.bitdecay.helm.unlock.palette.types;

import com.badlogic.gdx.graphics.Color;
import com.bitdecay.helm.system.render.GamePalette;

/**
 * Created by Monday on 6/6/2017.
 */

public class GuacamolePalette extends GamePalette {
    public GuacamolePalette() {
        colors.put(com.bitdecay.helm.unlock.palette.GameColors.LEVEL_SEGMENT, Color.GREEN);
        colors.put(com.bitdecay.helm.unlock.palette.GameColors.EXPLOSION, Color.ORANGE);
        colors.put(com.bitdecay.helm.unlock.palette.GameColors.SHIP_BODY, Color.FOREST);
        colors.put(com.bitdecay.helm.unlock.palette.GameColors.FUEL_METER, Color.YELLOW);
        colors.put(com.bitdecay.helm.unlock.palette.GameColors.REPULSION_FIELD, Color.GRAY);
        colors.put(com.bitdecay.helm.unlock.palette.GameColors.GRAVITY_WELL, Color.RED);
        colors.put(com.bitdecay.helm.unlock.palette.GameColors.WORMHOLE, Color.DARK_GRAY);
        colors.put(com.bitdecay.helm.unlock.palette.GameColors.BOOST, Color.GOLD);
        colors.put(com.bitdecay.helm.unlock.palette.GameColors.LANDING_PLATFORM, Color.OLIVE);
        colors.put(com.bitdecay.helm.unlock.palette.GameColors.BACKGROUND, Color.BROWN.mul(0.25f).add(0,0,0,1));
    }
}
