package com.bitdecay.helm.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Monday on 12/12/2016.
 */
public class TouchTracker {
    private int maxTouches;
    public int touchCount;
    public Array<ActiveTouch> activeTouches;

    public TouchTracker(int maxPointsToTrack) {
        maxTouches = maxPointsToTrack;
        activeTouches = new Array<>(maxPointsToTrack);
        touchCount = 0;
    }

    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
//        System.out.println("Touch Down: ID: " + pointer + " X: " + screenX + " Y: " + screenY + " Button: " + button);
        if (touchCount < maxTouches) {
            // workstation transform on the y
            activeTouches.add(new ActiveTouch(pointer, screenX, Gdx.graphics.getHeight() - screenY));
            touchCount++;
        }
        return false;
    }

    public boolean touchDragged(int screenX, int screenY, int pointer) {
//        System.out.println("Touch Move: ID: " + pointer + " X: " + screenX + " Y: " + screenY);

        Array.ArrayIterator<ActiveTouch> iter = new Array.ArrayIterator(activeTouches);
        ActiveTouch touch;
        while (iter.hasNext()) {
            touch = iter.next();
            if (touch.pointerNum == pointer) {
                touch.lastLocation.set(touch.currentLocation);
                touch.currentLocation.x = screenX;
                // workstation transform on the y
                touch.currentLocation.y = Gdx.graphics.getHeight() - screenY;
                touch.captureDelta();
            }
        }
        return false;
    }

    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
//        System.out.println("Touch Up: ID: " + pointer + " X: " + screenX + " Y: " + screenY + " Button: " + button);

        Array.ArrayIterator<ActiveTouch> iter = new Array.ArrayIterator(activeTouches);
        ActiveTouch touch;
        while (iter.hasNext()) {
            touch = iter.next();
            if (touch.pointerNum == pointer) {
                iter.remove();
                touchCount--;
            }
        }
        return false;
    }

}
