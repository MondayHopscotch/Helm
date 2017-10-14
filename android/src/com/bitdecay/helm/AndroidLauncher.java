package com.bitdecay.helm;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.bitdecay.helm.system.input.TouchScreenBoosterInputSystem;
import com.bitdecay.helm.system.input.TouchScreenSteeringInputSystem;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		com.bitdecay.helm.system.input.InputSystemFactory.setInputSystems(TouchScreenBoosterInputSystem.class, TouchScreenSteeringInputSystem.class);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useWakelock = true;
		initialize(new Helm(), config);
	}
}
