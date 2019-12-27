package com.climb.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.climb.utils.Constants;
import com.climb.Application;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.title = Constants.TITLE;
		config.width = Constants.V_WIDTH * (int)Constants.SCALE;
		config.height = Constants.V_HEIGHT * (int)Constants.SCALE;
		config.backgroundFPS = Constants.FPS;
		config.foregroundFPS = Constants.FPS;
		config.resizable = false;

		new LwjglApplication(new Application(), config);
	}
}
