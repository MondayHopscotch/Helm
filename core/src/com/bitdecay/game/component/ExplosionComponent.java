package com.bitdecay.game.component;

import com.badlogic.gdx.graphics.Color;
import com.bitdecay.game.unlock.palette.GameColors;

/**
 * Created by Monday on 2/16/2017.
 */
public class ExplosionComponent extends GameComponent {

    public GameColors color = GameColors.EXPLOSION;

    public float distance = 0;

    public float speed = 10;
    public float particleSize = 50;
    public float decay = 1f;

    public int spreadCount = 16;

    // offset rotation to start explosion particles
    public float rotationalOffset = 0;

    public ExplosionComponent(GameColors color) {
        this.color = color;
    }
}
