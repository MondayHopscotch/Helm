package com.bitdecay.game.system.input;

import com.bitdecay.game.GamePilot;
import com.bitdecay.game.system.input.AbstractInputSystem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Monday on 3/12/2017.
 */

public class InputSystemFactory {

    static Class<? extends AbstractInputSystem>[] inputSystemClasses = new Class[0];

    public static void setInputSystems(Class<? extends AbstractInputSystem> ...clazzes) {
        inputSystemClasses = clazzes;
    }

    public static List<AbstractInputSystem> getInputSystems(GamePilot pilot) {
        List instances = new ArrayList(inputSystemClasses.length);
        try {
            for (Class<? extends AbstractInputSystem> inputSystemClass : inputSystemClasses) {
                instances.add(inputSystemClass.getConstructors()[0].newInstance(pilot));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return instances;
    }
}
