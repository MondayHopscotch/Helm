package com.bitdecay.helm.unlock;

import com.badlogic.gdx.Preferences;
import com.bitdecay.helm.Helm;
import com.bitdecay.helm.time.TimerUtils;

/**
 * Created by Monday on 2/19/2017.
 */

public class LiveTimeStat extends LiveStat {
    public float amount;

    public LiveTimeStat(com.bitdecay.helm.unlock.StatName statName, Preferences preferences) {
        super(statName, preferences);
    }

    @Override
    protected void load(Preferences preferences) {
        amount = preferences.getFloat(statName.preferenceID, 0);
        if (Helm.debug) {
            System.out.println("Initializing stat '" + statName.preferenceID + "' with value '" + amount + "'");
        }
    }

    @Override
    public void save(Preferences preferences) {
        if (Helm.debug) {
            System.out.println("Saving stat '" + statName.preferenceID + "' with value '" + amount + "'");
        }
        preferences.putFloat(statName.preferenceID, amount);
    }

    @Override
    public boolean hasSetValue(Preferences preferences) {
        return Float.NEGATIVE_INFINITY != preferences.getFloat(statName.preferenceID, Float.NEGATIVE_INFINITY);
    }

    @Override
    public String format() {
        return TimerUtils.getFormattedTime(amount);
    }
}
