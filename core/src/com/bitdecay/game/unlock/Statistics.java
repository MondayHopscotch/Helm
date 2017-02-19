package com.bitdecay.game.unlock;

import com.badlogic.gdx.Preferences;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Monday on 2/18/2017.
 */

public class Statistics {
    Map<StatName, SingleCountStat> statistics = new HashMap<>();

    public void init(Preferences preferences) {
        for (StatName statName : StatName.values()) {
            initStat(statName, preferences);
        }
    }

    public void save(Preferences preferences) {
        for (StatName statName : statistics.keySet()) {
            SingleCountStat stat = statistics.get(statName);
            System.out.println("Saving stat '" + statName.preferenceID + "' with value '" + stat.count + "'");
            preferences.putInteger(statName.preferenceID, stat.count);
        }
    }

    private void initStat(StatName statName, Preferences preferences) {
        for (StatName mapStat : statistics.keySet()) {
            if (mapStat.preferenceID == statName.preferenceID) {
                throw new RuntimeException("STAT WITH PREF ID '" + statName.preferenceID + "' is already initialized");
            }
        }

        statistics.put(statName, new SingleCountStat(statName, preferences));
    }

    public void add(StatName statName, int count) {
        SingleCountStat stat = statistics.get(statName);
        if (stat != null) {
            System.out.println("Adding '" + count + "' to stat '" + statName.preferenceID + "'");
            stat.count += count;
        }
    }

    public int getCount(StatName statName) {
        SingleCountStat stat = statistics.get(statName);
        if (stat != null) {
            return stat.count;
        } else {
            return Integer.MIN_VALUE;
        }
    }

    private class SingleCountStat {
        int count;
        private StatName statName;

        public SingleCountStat(StatName statName, Preferences preferences) {
            this.statName = statName;
            count = preferences.getInteger(statName.preferenceID, 0);
            System.out.println("Initializing stat '" + statName.preferenceID + "' with value '" + count + "'");
        }
    }
}
