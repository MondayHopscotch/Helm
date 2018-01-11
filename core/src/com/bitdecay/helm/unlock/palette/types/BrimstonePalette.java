package com.bitdecay.helm.unlock.palette.types;

import com.badlogic.gdx.graphics.Color;
import com.bitdecay.helm.system.render.GamePalette;

/**
 * Created by Monday on 6/7/2017.
 */

public class BrimstonePalette extends GamePalette {
    public BrimstonePalette() {
        colors.put(com.bitdecay.helm.unlock.palette.GameColors.LEVEL_SEGMENT, new Color(0xfad99aff));
        colors.put(com.bitdecay.helm.unlock.palette.GameColors.EXPLOSION, new Color(0xfad99aff));
        colors.put(com.bitdecay.helm.unlock.palette.GameColors.SHIP_BODY, new Color(0x965d63ff));
        colors.put(com.bitdecay.helm.unlock.palette.GameColors.FUEL_METER, new Color(0xf4b642ff));
        colors.put(com.bitdecay.helm.unlock.palette.GameColors.REPULSION_FIELD, new Color(0x842402ff));
        colors.put(com.bitdecay.helm.unlock.palette.GameColors.GRAVITY_WELL, new Color(0x842402ff));
        colors.put(com.bitdecay.helm.unlock.palette.GameColors.WORMHOLE, new Color(0x842402ff));
        colors.put(com.bitdecay.helm.unlock.palette.GameColors.BOOST, new Color(0xc5270fff));
        colors.put(com.bitdecay.helm.unlock.palette.GameColors.LANDING_PLATFORM, new Color(0xf4b642ff));
        colors.put(com.bitdecay.helm.unlock.palette.GameColors.BACKGROUND, new Color(0x330303ff));
    }
}
