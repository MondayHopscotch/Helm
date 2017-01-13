package com.bitdecay.game.component;

/**
 * Created by Monday on 1/12/2017.
 */
public class ForcedRemoveComponent extends GameComponent {
    public Class<?> componentClazz;

    public ForcedRemoveComponent(Class<?> componentClazz) {
        this.componentClazz = componentClazz;
    }
}
