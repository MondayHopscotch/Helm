package com.bitdecay.helm.component;

/**
 * Created by Monday on 2/26/2017.
 */

public class ReplayActiveComponent extends GameComponent {
    public int nextInput = 0;
    public com.bitdecay.helm.input.InputReplay input;

    public ReplayActiveComponent(com.bitdecay.helm.input.InputReplay replay) {
        input = replay;
    }
}
