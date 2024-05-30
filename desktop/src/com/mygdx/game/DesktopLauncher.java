package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.mygdx.game.database.DatabaseHelper;
import com.mygdx.game.screens.LoginScreen;

import javax.xml.crypto.Data;
import java.awt.*;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setTitle("Overtime!");
		config.setWindowIcon("serato.png");
//		config.setFullscreenMode(Lwjgl3ApplicationConfiguration.getDisplayMode());
//		config.setResizable(false);
//		config.setWindowSizeLimits(1920, 1080, 1920, 1080);

		config.setMaximized(true);

		DatabaseHelper databaseHelper = DatabaseHelper.getInstance();
		new Lwjgl3Application(new Application(), config);
	}
}
