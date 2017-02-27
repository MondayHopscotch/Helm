package com.bitdecay.game.component;

import com.bitdecay.game.input.InputReplay;

/**
 * Created by Monday on 2/26/2017.
 */

public class ReplayActiveComponent extends GameComponent {
    public int nextInput = 0;
    public InputReplay input;

    public ReplayActiveComponent(InputReplay replay) {
        input = replay;
    }
}
