package deco.combatevolved.managers;

import com.badlogic.gdx.Graphics;
import deco.combatevolved.GameScreen;

public class ScreenManager extends AbstractManager {

    /* Represents the current screen displayed in the game */
    private GameScreen currentScreen;
    private Graphics.DisplayMode desktopMode;
    private boolean fullscreen;
    private boolean settingsOpen = false;
    /**
     * @return the current screen
     */
    public GameScreen getCurrentScreen() {
        return currentScreen;
    }

    /**
     * Sets the current screen
     * @param screen to set
     */
    public void setCurrentScreen(GameScreen screen) {
        currentScreen = screen;
    }

    public void setDesktopMode(Graphics.DisplayMode desktopMode) {
        this.desktopMode = desktopMode;
    }

    public Graphics.DisplayMode getDesktopMode() {
        return this.desktopMode;
    }

    public void setFullscreen(boolean status) {
        this.fullscreen = status;
    }

    public boolean getFullscreen() {
        return this.fullscreen;
    }

    public void setSettingsOpen(boolean status) {
        this.settingsOpen = status;
    }

    public boolean getSettingsOpen() {
        return this.settingsOpen;
    }
}
