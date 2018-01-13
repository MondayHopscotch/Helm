package com.bitdecay.helm.unlock.palette.types;

import com.badlogic.gdx.graphics.Color;
import com.bitdecay.helm.system.render.GamePalette;

/**
 * Created by Monday on 6/19/2017.
 */

public class MutedPalette extends GamePalette {
    public MutedPalette() {
        colors.put(com.bitdecay.helm.unlock.palette.GameColors.LEVEL_SEGMENT, Color.GRAY);
        colors.put(com.bitdecay.helm.unlock.palette.GameColors.EXPLOSION, Color.LIGHT_GRAY);
        colors.put(com.bitdecay.helm.unlock.palette.GameColors.SHIP_BODY, Color.GRAY);
        colors.put(com.bitdecay.helm.unlock.palette.GameColors.LAUNCH_SMOKE, Color.GRAY);
        colors.put(com.bitdecay.helm.unlock.palette.GameColors.FUEL_METER, Color.LIGHT_GRAY);
        colors.put(com.bitdecay.helm.unlock.palette.GameColors.REPULSION_FIELD, Color.GRAY);
        colors.put(com.bitdecay.helm.unlock.palette.GameColors.GRAVITY_WELL, Color.GRAY);
        colors.put(com.bitdecay.helm.unlock.palette.GameColors.WORMHOLE, Color.GRAY);
        colors.put(com.bitdecay.helm.unlock.palette.GameColors.BOOST, Color.WHITE);
        colors.put(com.bitdecay.helm.unlock.palette.GameColors.LANDING_PLATFORM, Color.WHITE);
        colors.put(com.bitdecay.helm.unlock.palette.GameColors.BACKGROUND, Color.DARK_GRAY);
    }
}
