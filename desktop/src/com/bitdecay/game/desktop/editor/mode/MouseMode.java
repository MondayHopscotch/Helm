package com.bitdecay.game.desktop.editor.mode;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.bitdecay.game.desktop.editor.MouseButton;

public interface MouseMode {
    void mouseDown(Vector2 point, MouseButton button);

    /**
     * mouse moved while button is down
     *
     * @param point
     */
    void mouseDragged(Vector2 point);

    void mouseUp(Vector2 point, MouseButton button);

    /**
     * Mouse moved without button down
     *
     * @param point
     */
    void mouseMoved(Vector2 point);

    void render(ShapeRenderer shaper);
}
