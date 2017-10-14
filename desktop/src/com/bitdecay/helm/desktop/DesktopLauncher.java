package com.bitdecay.helm.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.bitdecay.helm.Helm;
import com.bitdecay.helm.system.input.DesktopBoosterInputSystem;
import com.bitdecay.helm.system.input.DesktopSteeringInputSystem;
import com.bitdecay.helm.system.input.InputSystemFactory;

public class DesktopLauncher {
	public static void main (String[] arg) {
		if (arg.length > 0) {
			Helm.debug = true;
		}
		InputSystemFactory.setInputSystems(DesktopBoosterInputSystem.class, DesktopSteeringInputSystem.class);
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1600;
		config.height = 900;
		new LwjglApplication(new Helm(), config);
	}
}
