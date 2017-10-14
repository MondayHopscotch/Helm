package com.bitdecay.helm.unlock;

import com.badlogic.gdx.Preferences;

/**
 * Created by Monday on 2/19/2017.
 */

public class StatNotFound extends LiveStat {
    public StatNotFound(com.bitdecay.helm.unlock.StatName statName, Preferences preferences) {
        super(statName, preferences);
    }

    @Override
    protected void load(Preferences preferences) {
        // No-Op
    }

    @Override
    public void save(Preferences preferences) {
        // No-Op
    }

    @Override
    public boolean hasSetValue(Preferences preferences) {
        return false;
    }

    @Override
    public String format() {
        return "Stat '" + statName.preferenceID + "' not found";
    }
}
