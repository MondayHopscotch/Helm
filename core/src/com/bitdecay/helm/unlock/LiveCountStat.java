package com.bitdecay.helm.unlock;

import com.badlogic.gdx.Preferences;
import com.bitdecay.helm.Helm;

/**
 * Created by Monday on 2/19/2017.
 */

public class LiveCountStat extends LiveStat {
    public int count;

    public LiveCountStat(com.bitdecay.helm.unlock.StatName statName, Preferences preferences) {
        super(statName, preferences);
    }

    @Override
    protected void load(Preferences preferences) {
        count = preferences.getInteger(statName.preferenceID, 0);
        if (Helm.debug) {
            System.out.println("Initializing stat '" + statName.preferenceID + "' with value '" + count + "'");
        }
    }

    @Override
    public void save(Preferences preferences) {
        if (Helm.debug) {
            System.out.println("Saving stat '" + statName.preferenceID + "' with value '" + count + "'");
        }
        preferences.putInteger(statName.preferenceID, count);
    }

    @Override
    public boolean hasSetValue(Preferences preferences) {
        return Integer.MIN_VALUE != preferences.getInteger(statName.preferenceID, Integer.MIN_VALUE);
    }

    @Override
    public String format() {
        return Integer.toString(count);
    }
}
