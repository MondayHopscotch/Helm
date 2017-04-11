package com.bitdecay.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.bitdecay.game.Helm;
import com.bitdecay.game.credits.CreditsData;
import com.bitdecay.game.desktop.editor.file.FileUtils;
import com.bitdecay.game.desktop.packer.HelmPacker;
import com.bitdecay.game.persist.JsonUtils;
import com.bitdecay.game.system.input.DesktopBoosterInputSystem;
import com.bitdecay.game.system.input.DesktopSteeringInputSystem;
import com.bitdecay.game.system.input.InputSystemFactory;

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
