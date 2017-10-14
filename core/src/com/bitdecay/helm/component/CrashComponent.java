package com.bitdecay.helm.component;

/**
 * Created by Monday on 12/18/2016.
 */
public class CrashComponent extends GameComponent {
    public final com.bitdecay.helm.collision.CollisionKind with;

    public CrashComponent(com.bitdecay.helm.collision.CollisionKind with) {
        this.with = with;
    }
}
