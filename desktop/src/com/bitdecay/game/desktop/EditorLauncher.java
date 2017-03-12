package com.bitdecay.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.bitdecay.game.desktop.editor.HelmEditor;
import com.bitdecay.game.system.input.DesktopBoosterInputSystem;
import com.bitdecay.game.system.input.DesktopSteeringInputSystem;
import com.bitdecay.game.system.input.InputSystemFactory;

/**
 * Created by Monday on 1/2/2017.
 */
public class EditorLauncher {
    public static void main (String[] arg) {
        InputSystemFactory.setInputSystems(DesktopBoosterInputSystem.class, DesktopSteeringInputSystem.class);
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 1600;
        config.height = 900;
        new LwjglApplication(new HelmEditor(), config);
    }
}
