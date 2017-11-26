package com.bitdecay.helm;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.bitdecay.helm.external.URLOpener;
import com.bitdecay.helm.system.input.InputSystemFactory;
import com.bitdecay.helm.system.input.TouchScreenBoosterInputSystem;
import com.bitdecay.helm.system.input.TouchScreenSteeringInputSystem;

public class AndroidLauncher extends AndroidApplication implements URLOpener {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		InputSystemFactory.setInputSystems(TouchScreenBoosterInputSystem.class, TouchScreenSteeringInputSystem.class);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useWakelock = true;
		Helm.debug = true;
		Helm.urlOpener = this;

		initialize(new Helm(), config);
	}

	@Override
	public void open(String url) {
		Uri marketUri = Uri.parse(url);
		Intent intent = new Intent( Intent.ACTION_VIEW, marketUri );
		getContext().startActivity( intent );
	}
}
