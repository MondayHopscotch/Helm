package com.bitdecay.helm.system.render;

import com.badlogic.gdx.graphics.Color;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Monday on 6/5/2017.
 */

public class GamePalette {
    public Map<com.bitdecay.helm.unlock.palette.GameColors, Color> colors = new HashMap<>();

    public GamePalette() {
        // Here for JSON
    }

    public Color get(com.bitdecay.helm.unlock.palette.GameColors selector) {
        if (colors.containsKey(selector)) {
            return colors.get(selector);
        } else {
            return Color.PINK;
        }
    }
}
