package com.bitdecay.helm.desktop.editor.mode;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.bitdecay.helm.desktop.editor.LevelBuilder;
import com.bitdecay.helm.world.WormholePair;

/**
 * Created by Monday on 3/21/2017.
 */

public class DeleteWormholeMouseMode extends BaseMouseMode {

    private WormholePair selectedWormhole;

    public DeleteWormholeMouseMode(LevelBuilder builder) {
        super(builder);
    }

    @Override
    public void mouseMoved(Vector2 point) {
        for (WormholePair pair : builder.wormholes) {
            if (point.cpy().sub(pair.entrance.x, pair.entrance.y).len() < pair.entrance.radius + 10) {
                selectedWormhole = pair;
                return;
            } else if (point.cpy().sub(pair.exit.x, pair.exit.y).len() < pair.exit.radius + 10) {
                selectedWormhole = pair;
                return;
            }
        }

        selectedWormhole = null;
    }

    @Override
    protected void mouseUpLogic(Vector2 point, com.bitdecay.helm.desktop.editor.MouseButton button) {
        if (selectedWormhole != null) {
            builder.removeWormhole(selectedWormhole);
            selectedWormhole = null;
        }
    }

    @Override
    public void render(ShapeRenderer shaper) {
        if (selectedWormhole != null) {
            shaper.setColor(Color.TAN);
            shaper.circle(selectedWormhole.entrance.x, selectedWormhole.entrance.y, selectedWormhole.entrance.radius);
            shaper.circle(selectedWormhole.entrance.x, selectedWormhole.entrance.y, selectedWormhole.entrance.radius + 10);
            shaper.circle(selectedWormhole.entrance.x, selectedWormhole.entrance.y, selectedWormhole.entrance.radius - 10);
            shaper.circle(selectedWormhole.exit.x, selectedWormhole.exit.y, selectedWormhole.exit.radius);
            shaper.circle(selectedWormhole.exit.x, selectedWormhole.exit.y, selectedWormhole.exit.radius + 10);
            shaper.circle(selectedWormhole.exit.x, selectedWormhole.exit.y, selectedWormhole.exit.radius - 10);
        }
    }
}
