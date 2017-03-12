package com.bitdecay.game;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.bitdecay.game.system.input.InputSystemFactory;
import com.bitdecay.game.system.input.TouchScreenBoosterInputSystem;
import com.bitdecay.game.system.input.TouchScreenSteeringInputSystem;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		InputSystemFactory.setInputSystems(TouchScreenBoosterInputSystem.class, TouchScreenSteeringInputSystem.class);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new Helm(), config);
	}
}
