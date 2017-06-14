package com.bitdecay.game.unlock.achievement;

/**
 * Created by Monday on 6/13/2017.
 */

// TODO: We need the notion of, at least, Score properties, and Stat properties. May need others.
public class Property {

    public final String name;
    public final String value;
    public final int triggerGate;
    public final TriggerRule rule;

    public Property(String name, String value, int triggerGate, TriggerRule rule) {
        this.name = name;
        this.value = value;
        this.triggerGate = triggerGate;
        this.rule = rule;
    }
}
