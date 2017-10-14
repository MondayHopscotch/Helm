package com.bitdecay.helm.unlock;

import com.badlogic.gdx.Preferences;
import com.bitdecay.helm.Helm;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Monday on 2/18/2017.
 */

public class Statistics {
    Map<com.bitdecay.helm.unlock.StatName, com.bitdecay.helm.unlock.LiveStat> statistics = new HashMap<>();
    private Preferences preferences;

    public void init(Preferences preferences) {
        this.preferences = preferences;
        for (com.bitdecay.helm.unlock.StatName statName : com.bitdecay.helm.unlock.StatName.values()) {
            initStat(statName, preferences);
        }
    }

    public void save() {
        for (com.bitdecay.helm.unlock.StatName statName : statistics.keySet()) {
            com.bitdecay.helm.unlock.LiveStat stat = statistics.get(statName);
            stat.save(preferences);
        }
        Helm.prefs.flush();
    }

    private void initStat(com.bitdecay.helm.unlock.StatName statName, Preferences preferences) {
        for (com.bitdecay.helm.unlock.StatName mapStat : statistics.keySet()) {
            if (mapStat.preferenceID == statName.preferenceID) {
                throw new RuntimeException("STAT WITH PREF ID '" + statName.preferenceID + "' is already initialized");
            }
        }

        switch(statName.type) {
            case COUNT:
                statistics.put(statName, new LiveCountStat(statName, preferences));
                break;
            case TIME:
                statistics.put(statName, new com.bitdecay.helm.unlock.LiveTimeStat(statName, preferences));
                break;
        }
    }

    public void count(com.bitdecay.helm.unlock.StatName statName, int count) {
        com.bitdecay.helm.unlock.LiveStat stat = statistics.get(statName);
        if (stat != null) {
            if (com.bitdecay.helm.unlock.StatType.COUNT != stat.statName.type) {
                System.out.println("Adding count to wrong stat type. Bailing");
                return;
            }
            System.out.println("Adding '" + count + "' to stat '" + statName.preferenceID + "'");
            ((LiveCountStat)stat).count += count;
            stat.save(preferences);
            preferences.flush();
        }
    }

    public void roll(com.bitdecay.helm.unlock.StatName statName, float time) {
        com.bitdecay.helm.unlock.LiveStat stat = statistics.get(statName);
        if (stat != null) {
            if (com.bitdecay.helm.unlock.StatType.TIME != stat.statName.type) {
                System.out.println("Adding time to wrong stat type. Bailing");
                return;
            }
            System.out.println("Adding '" + time + "' to stat '" + statName.preferenceID + "'");
            ((com.bitdecay.helm.unlock.LiveTimeStat)stat).amount += time;
            stat.save(preferences);
            preferences.flush();
        }
    }

    public com.bitdecay.helm.unlock.LiveStat getLiveStat(com.bitdecay.helm.unlock.StatName statName) {
        if (statistics.containsKey(statName)) {
            return statistics.get(statName);
        } else {
            return new com.bitdecay.helm.unlock.StatNotFound(statName, preferences);
        }
    }
}
