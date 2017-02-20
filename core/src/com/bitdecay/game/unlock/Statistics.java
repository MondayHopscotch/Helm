package com.bitdecay.game.unlock;

import com.badlogic.gdx.Preferences;
import com.bitdecay.game.Helm;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Monday on 2/18/2017.
 */

public class Statistics {
    Map<StatName, LiveStat> statistics = new HashMap<>();
    private Preferences preferences;

    public void init(Preferences preferences) {
        this.preferences = preferences;
        for (StatName statName : StatName.values()) {
            initStat(statName, preferences);
        }
    }

    public void save() {
        for (StatName statName : statistics.keySet()) {
            LiveStat stat = statistics.get(statName);
            stat.save(preferences);
        }
        Helm.prefs.flush();
    }

    private void initStat(StatName statName, Preferences preferences) {
        for (StatName mapStat : statistics.keySet()) {
            if (mapStat.preferenceID == statName.preferenceID) {
                throw new RuntimeException("STAT WITH PREF ID '" + statName.preferenceID + "' is already initialized");
            }
        }

        switch(statName.type) {
            case COUNT:
                statistics.put(statName, new LiveCountStat(statName, preferences));
                break;
            case TIME:
                statistics.put(statName, new LiveTimeStat(statName, preferences));
                break;
        }
    }

    public void count(StatName statName, int count) {
        LiveStat stat = statistics.get(statName);
        if (stat != null) {
            if (StatType.COUNT != stat.statName.type) {
                System.out.println("Adding count to wrong stat type. Bailing");
                return;
            }
            System.out.println("Adding '" + count + "' to stat '" + statName.preferenceID + "'");
            ((LiveCountStat)stat).count += count;
            stat.save(preferences);
            preferences.flush();
        }
    }

    public void roll(StatName statName, float time) {
        LiveStat stat = statistics.get(statName);
        if (stat != null) {
            if (StatType.TIME != stat.statName.type) {
                System.out.println("Adding time to wrong stat type. Bailing");
                return;
            }
            System.out.println("Adding '" + time + "' to stat '" + statName.preferenceID + "'");
            ((LiveTimeStat)stat).amount += time;
            stat.save(preferences);
            preferences.flush();
        }
    }

    public LiveStat getLiveStat(StatName statName) {
        if (statistics.containsKey(statName)) {
            return statistics.get(statName);
        } else {
            return new StatNotFound(statName, preferences);
        }
    }
}
