package deco.combatevolved;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import deco.combatevolved.managers.GameManager;
import deco.combatevolved.managers.ScreenManager;
import org.lwjgl.opengl.Display;

import java.awt.*;

/**
 * DesktopLauncher
 */
public class GameLauncher {

	/**
	 * Private constructor to hide the implicit constructor
	 */
	private GameLauncher () {
		//Private constructor to hide the implicit one.
	}

	/**
	 * Main function for the game
	 * @param arg Command line arguments (we wont use these)
	 */
	@SuppressWarnings("unused") //app
	public static void main (String[] arg) throws InterruptedException {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		int fps = 60;
		config.width = 1280;
		config.height = 720;
		config.fullscreen = false;
		config.foregroundFPS = fps;
		config.backgroundFPS = fps;
		config.vSyncEnabled = false;
		config.title = "Deco: Combat Evolved";
		config.addIcon("resources/icon_128.png", Files.FileType.Internal);
		config.addIcon("resources/icon_32.png", Files.FileType.Internal);
		config.addIcon("resources/icon_16.png", Files.FileType.Internal);
		Graphics.DisplayMode desktop =
				LwjglApplicationConfiguration.getDesktopDisplayMode();
		GameManager.get().getManager(ScreenManager.class).setDesktopMode(desktop);
		config.forceExit = true;
		new LwjglApplication(new CombatEvolvedGame(), config);

	}
}




