package deco.combatevolved;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import deco.combatevolved.mainmenu.MainMenuScreen;
import deco.combatevolved.managers.GameManager;
import deco.combatevolved.managers.SoundManager;

/**
 * This class provides the game wrapper that different screens are plugged into
 */
public class CombatEvolvedGame extends Game {
    /**
     * The SpriteBatch for the game
     */
    private SpriteBatch batch;
    private static final String SAVE_ROOT_DIR = "combatevolved-saves";
    private FileHandle saveRootHandle;
    private MainMenuScreen mainMenuScreen;


	/**
	 * Creates the main menu screen
	 */
	public void create() {
		this.saveRootHandle = Gdx.files.local(SAVE_ROOT_DIR);
		this.batch = new SpriteBatch();
		initUISkin();
		initSoundVolume();
		this.mainMenuScreen = new MainMenuScreen(this);
		this.setScreen(mainMenuScreen);
		GameManager.get().setGame(this);
	}

    /**
     * Disposes of the game
     */
    @Override
    public void dispose() {
        this.mainMenuScreen.dispose();
        this.batch.dispose();
    }


    /**
     * Getter method to return the MainMenuScreen of the game
     * @return the MainMenuScreen of the game
     */
    public MainMenuScreen getMainMenuScreen() {
        return this.mainMenuScreen;
    }

    /**
     * Getter method to return the SaveRootHandle of the game
     * @return the SaveRootHandle of the game
     */
    public FileHandle getSaveRootHandle() {
        return this.saveRootHandle;
    }

    /**
     * Getter method to return the root directory to save for the game
     * @return the root directory for saving in the game
     */
    public static String getSaveRootDir() {
        return SAVE_ROOT_DIR;
    }

    public SpriteBatch getBatch() {
        return this.batch;
    }

	private void initUISkin() {
		GameManager.get().setSkin(new Skin(Gdx.files.internal("resources/uiskin.skin")));
	}

	private void initSoundVolume() {
		GameManager.get().getManager(SoundManager.class).setMusicVolume(1);
		GameManager.get().getManager(SoundManager.class).setSfxVolume(1);
	}
}
