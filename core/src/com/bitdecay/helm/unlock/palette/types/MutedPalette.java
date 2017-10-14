package com.bitdecay.helm.unlock.palette.types;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by Monday on 9/14/2017.
 */

public class MutedPalette extends com.bitdecay.helm.system.render.GamePalette {
    public MutedPalette() {
        colors.put(com.bitdecay.helm.unlock.palette.GameColors.LEVEL_SEGMENT, new Color(0x3D2645FF));
        colors.put(com.bitdecay.helm.unlock.palette.GameColors.EXPLOSION, new Color(0xD16666FF));
        colors.put(com.bitdecay.helm.unlock.palette.GameColors.SHIP_BODY, new Color(0x6E447CFF));
        colors.put(com.bitdecay.helm.unlock.palette.GameColors.LAUNCH_SMOKE, Color.WHITE);
        colors.put(com.bitdecay.helm.unlock.palette.GameColors.FUEL_METER, new Color(0x1DA58CFF));
        colors.put(com.bitdecay.helm.unlock.palette.GameColors.REPULSION_FIELD, new Color(0x136A5AFF));
        colors.put(com.bitdecay.helm.unlock.palette.GameColors.GRAVITY_WELL, new Color(0x064956FF));
        colors.put(com.bitdecay.helm.unlock.palette.GameColors.WORMHOLE, new Color(0x774736FF));
        colors.put(com.bitdecay.helm.unlock.palette.GameColors.BOOST, new Color(0xAC5454FF));
        colors.put(com.bitdecay.helm.unlock.palette.GameColors.LANDING_PLATFORM, new Color(0xB6C649FF));
        colors.put(com.bitdecay.helm.unlock.palette.GameColors.BACKGROUND, new Color(0x140D17FF));
    }
}
