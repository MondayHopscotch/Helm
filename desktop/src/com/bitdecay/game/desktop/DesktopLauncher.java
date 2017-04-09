package com.bitdecay.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.bitdecay.game.Helm;
import com.bitdecay.game.desktop.packer.HelmPacker;
import com.bitdecay.game.system.input.DesktopBoosterInputSystem;
import com.bitdecay.game.system.input.DesktopSteeringInputSystem;
import com.bitdecay.game.system.input.InputSystemFactory;

public class DesktopLauncher {
	public static void main (String[] arg) {
		if (arg.length > 0) {
			Helm.debug = true;
			HelmPacker.packAllTextures("../../resources/img", "img");
		}
		InputSystemFactory.setInputSystems(DesktopBoosterInputSystem.class, DesktopSteeringInputSystem.class);
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1600;
		config.height = 900;
		new LwjglApplication(new Helm(), config);
	}
}
