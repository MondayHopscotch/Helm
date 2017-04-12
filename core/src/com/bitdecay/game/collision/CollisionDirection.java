package com.bitdecay.game.collision;

/**
 * Created by Monday on 1/2/2017.
 */
public class CollisionDirection {
        public static final int RECEIVES = 1;
        public static final int DELIVERS = 1<<1;
        public static final int BOTH = RECEIVES | DELIVERS;
        public static final int PLAYER = 1<<3;
}
