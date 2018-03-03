package com.bitdecay.helm.input;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by Monday on 12/12/2016.
 */
public class ActiveTouch {

    public int pointerNum;
    public Vector2 startingLocation = new Vector2();
    public Vector2 lastLocation = new Vector2();
    public Vector2 currentLocation = new Vector2();
    public Vector2 lastDelta = new Vector2();

    private Vector2 workingVec = new Vector2();

    public ActiveTouch(int pointerNum, int screenX, int screenY) {
        this.pointerNum = pointerNum;
        startingLocation.x = screenX;
        startingLocation.y = screenY;
        lastLocation.x = screenX;
        lastLocation.y = screenY;
        currentLocation.x = screenX;
        currentLocation.y = screenY;
    }

    public void captureDelta() {
        // Always track based on our starting position to avoid rounding errors when
        // the touch point is barely moving
        lastDelta.set(startingLocation).sub(lastLocation);
        workingVec.set(startingLocation).sub(currentLocation);
        lastDelta.sub(workingVec);
    }

    public void consumeDeltaInto(Vector2 into) {
        into.x = lastDelta.x;
        into.y = lastDelta.y;
        lastDelta.set(Vector2.Zero);
    }
}
