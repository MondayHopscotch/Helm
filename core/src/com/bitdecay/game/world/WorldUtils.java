package com.bitdecay.game.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.bitdecay.game.persist.JsonUtils;

/**
 * Created by Monday on 6/7/2017.
 */

public class WorldUtils {
    private static Array<WorldInstance> cachedWorlds;

    public static Array<WorldInstance> getWorlds() {
        if (cachedWorlds == null) {
            cachedWorlds = new Array<>();
            FileHandle worldDirectory = Gdx.files.internal("level/world_defs/worldOrder.json");
            WorldOrderMarker[] worldsInOrder = JsonUtils.unmarshal(WorldOrderMarker[].class, worldDirectory);
            for (WorldOrderMarker worldMarker : worldsInOrder) {
                cachedWorlds.add(buildWorldInstance(worldMarker));
            }
        }
        return cachedWorlds;
    }

    public static WorldInstance buildWorldInstance(WorldOrderMarker marker) {
        FileHandle worldFile = Gdx.files.internal("level/world_defs/" + marker.worldFile);
        WorldDefinition worldDef = JsonUtils.unmarshal(WorldDefinition.class, worldFile);
        WorldInstance worldInstance = new WorldInstance(marker.requiredLevelsForUnlock);
        worldInstance.name = worldDef.worldName;
        for (String levelPath : worldDef.levelList) {
            worldInstance.addLevelInstance(JsonUtils.unmarshal(LevelDefinition.class, Gdx.files.internal(levelPath)));
        }
        return worldInstance;
    }
}
