package com.bitdecay.helm.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.bitdecay.helm.system.input.DesktopSteeringInputSystem;

/**
 * Created by Monday on 1/2/2017.
 */
public class EditorLauncher {
    public static void main (String[] arg) {
        com.bitdecay.helm.system.input.InputSystemFactory.setInputSystems(com.bitdecay.helm.system.input.DesktopBoosterInputSystem.class, DesktopSteeringInputSystem.class);
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 1600;
        config.height = 900;
        new LwjglApplication(new com.bitdecay.helm.desktop.editor.HelmEditor(), config);
    }
}
