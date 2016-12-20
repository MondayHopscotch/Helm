package com.bitdecay.game.component;

/**
 * Created by Monday on 12/19/2016.
 */
public class FuelComponent extends GameComponent {
    public float maxFuel;
    public float fuelRemaining;
    public float burnRate;

    public FuelComponent(int startingFuel) {
        maxFuel = startingFuel;
        fuelRemaining = startingFuel;
        burnRate = 50;
    }
}
