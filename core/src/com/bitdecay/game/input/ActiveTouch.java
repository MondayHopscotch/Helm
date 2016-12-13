package com.bitdecay.game.input;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by Monday on 12/12/2016.
 */
public class ActiveTouch {

    public int pointerNum;
    public Vector2 startingLocation = new Vector2();
    public Vector2 currentLocation = new Vector2();

    public ActiveTouch(int pointerNum, int screenX, int screenY) {
        this.pointerNum = pointerNum;
        startingLocation.x = screenX;
        startingLocation.y = screenY;
        currentLocation.x = screenX;
        currentLocation.y = screenY;
    }
}
