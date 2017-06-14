package com.bitdecay.game.unlock.stats;

import com.badlogic.gdx.Preferences;

/**
 * Created by Monday on 2/19/2017.
 */

public abstract class LiveStat {

    public StatName statName;

    public LiveStat(StatName statName, Preferences preferences) {
        this.statName = statName;
        load(preferences);
    }

    protected abstract void load(Preferences preferences);

    public abstract void save(Preferences preferences);

    public abstract boolean hasSetValue(Preferences preferences);

    public abstract String format();
}
