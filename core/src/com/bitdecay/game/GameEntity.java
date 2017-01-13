package com.bitdecay.game;

import com.bitdecay.game.component.GameComponent;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Monday on 12/8/2016.
 */
public class GameEntity {
    private Map<Class<? extends GameComponent>,GameComponent> components = new HashMap<>();

    public boolean hasComponent(Class<?> componentClazz) {
        return components.get(componentClazz) != null;
    }

    public boolean hasComponents(Class<?> ...componentClazzes) {
        for (Class<?> clazz : componentClazzes) {
            if (!hasComponent(clazz)) {
                return false;
            }
        }
        return true;
    }

    public void addComponent(GameComponent component) {
        components.put(component.getClass(), component);
    }

    public <T> T getComponent(Class<T> componentClazz) {
        return componentClazz.cast(components.get(componentClazz));
    }

    public <T> void removeComponent(Class<T> componentClazz) {
        components.remove(componentClazz);
    }
}

