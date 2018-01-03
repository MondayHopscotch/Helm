package com.bitdecay.helm.unlock.palette.types;

import com.badlogic.gdx.graphics.Color;
import com.bitdecay.helm.system.render.GamePalette;
import com.bitdecay.helm.unlock.palette.GameColors;

/**
 * Created by Monday on 9/14/2017.
 */

public class MutedPalette extends GamePalette {
    public MutedPalette() {
        colors.put(GameColors.LEVEL_SEGMENT, new Color(0x3D2645FF));
        colors.put(GameColors.EXPLOSION, new Color(0xD16666FF));
        colors.put(GameColors.SHIP_BODY, new Color(0x6E447CFF));
        colors.put(GameColors.LAUNCH_SMOKE, Color.WHITE);
        colors.put(GameColors.FUEL_METER, new Color(0x1DA58CFF));
        colors.put(GameColors.REPULSION_FIELD, new Color(0x136A5AFF));
        colors.put(GameColors.GRAVITY_WELL, new Color(0x064956FF));
        colors.put(GameColors.WORMHOLE, new Color(0x774736FF));
        colors.put(GameColors.BOOST, new Color(0xAC5454FF));
        colors.put(GameColors.LANDING_PLATFORM, new Color(0xB6C649FF));
        colors.put(GameColors.BACKGROUND, new Color(0x140D17FF));
    }
}
