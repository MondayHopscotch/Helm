package com.bitdecay.helm.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

/**
 * Created by Monday on 12/12/2016.
 */
public class TouchTracker {
    private int maxTouches;
    public ObjectMap activeTouchMap;

    public TouchTracker(int maxPointsToTrack) {
        maxTouches = maxPointsToTrack;
        activeTouchMap = new ObjectMap(maxTouches);
    }

    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (activeTouchMap.size < maxTouches) {
            // workstation transform on the y
            activeTouchMap.put(pointer, new ActiveTouch(pointer, screenX, Gdx.graphics.getHeight() - screenY));
        }
        return false;
    }

    public boolean touchDragged(int screenX, int screenY, int pointer) {
        ActiveTouch touch;
        if (activeTouchMap.containsKey(pointer)) {
            touch = (ActiveTouch) activeTouchMap.get(pointer);
            touch.currentLocation.x = screenX;
            // workstation transform on the y
            touch.currentLocation.y = Gdx.graphics.getHeight() - screenY;
        }
        return false;
    }

    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (activeTouchMap.containsKey(pointer)) {
            activeTouchMap.remove(pointer);
        }

        return false;
    }

    public Array<ActiveTouch> getTouches() {
        return activeTouchMap.values().toArray();
    }
}
