package deco.combatevolved.managers;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import deco.combatevolved.CombatEvolvedGame;
import deco.combatevolved.renderers.PotateCamera;
import deco.combatevolved.util.Keybindings;
import deco.combatevolved.worlds.AbstractWorld;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class GameManager {
	//debug values stored here
	private int entitiesRendered, entitiesCount, tilesRendered, tilesCount;

	private static final Logger LOGGER = LoggerFactory.getLogger(GameManager.class);

	private CombatEvolvedGame game = null;

	private static GameManager instance = null;

	// The list of all instantiated managers classes.
	private List<AbstractManager> managers = new ArrayList<>();

	// The game world currently being played on.
	private AbstractWorld combatEvolvedWorld;

	// The camera being used by the Game Screen to project the game world.
	private PotateCamera camera;

	// The stage the game world is being rendered on to.
	private Stage stage;

	// The UI skin being used by the game for libGDX elements.
	private Skin skin;

	//tracker of paused status, checked by control functions
	private boolean paused = false;


	private float fps = 0;

	private boolean debugMode = true;

	/**
	 * Whether or not we render info over the tiles.
	 */
	// Whether or not we render the movement path for Players.
	private boolean showCoords = false;
	
	// The game screen for a game that's currently running.
	private boolean showPath = true;

	/**
	 * Whether or not we render info over the entities
	 */
	private boolean showCoordsEntity = false;

	// The ID of entity which the player can control
	private int playerEntityID;

	private int projectileID;

	private Keybindings keybindings;

	/**
	 * Returns an instance of the GM
	 *
	 * @return GameManager
	 */
	public static GameManager get() {
		if (instance == null) {
			instance = new GameManager();
		}
		return instance;
	}

	/**
	 * Private constructor to inforce use of get()
	 */
	GameManager() {

	}

	/**
	 * Add a manager to the current instance, making a new instance if none
	 * exist
	 *
	 * @param manager
	 */
	public static void addManagerToInstance(AbstractManager manager) {
		get().addManager(manager);
	}

	/**
	 * Adds a manager component to the GM
	 *
	 * @param manager
	 */
	public void addManager(AbstractManager manager) {
		managers.add(manager);
	}

	/**
	 * Retrieves a manager from the list.
	 * If the manager does not exist one will be created, added to the list and returned
	 *
	 * @param type The class type (ie SoundManager.class)
	 * @return A Manager component of the requested type
	 */
	@SuppressWarnings("unchecked")
	public <T extends AbstractManager> T getManager(Class<T> type) {
		/* Check if the manager exists */
		for (AbstractManager m : managers) {
			if (m.getClass() == type) {
				return (T) m;
			}
		}
		LOGGER.info("creating new manager instance: " + type.toString());
		/* Otherwise create one */
		AbstractManager newInstance;
		try {
			Constructor<?> ctor = type.getConstructor();
			newInstance = (AbstractManager) ctor.newInstance();
			this.addManager(newInstance);
			return (T) newInstance;
		} catch (Exception e) {
			// Gotta catch 'em all
			LOGGER.error("Exception occurred when adding Manager.\n" + e.toString());
		}

		LOGGER.warn("GameManager.getManager returned null! It shouldn't have!");
		return null;
	}

	/**
	 * Retrieve a manager from the current GameManager instance, making a new
	 * instance when none are available.
	 *
	 * @param type The class type (ie SoundManager.class)
	 * @return A Manager component of the requested type
	 */
	public static <T extends AbstractManager> T getManagerFromInstance(Class<T> type) {
		return get().getManager(type);
	}

	
	/* ------------------------------------------------------------------------
	 * 				GETTERS AND SETTERS BELOW THIS COMMENT.
	 * ------------------------------------------------------------------------ */

	/**Get entities rendered count
	 * @return entities rendered count
	 */
	public int getEntitiesRendered() {
		return this.entitiesRendered;
	}

	/** Set entities rendered to new amount
	 * @param entitiesRendered the new amount
	 */
	public void setEntitiesRendered(int entitiesRendered) {
		this.entitiesRendered = entitiesRendered;
	}
	/**Get number of entities
	 * @return entities count
	 */
	public int getEntitiesCount() {
		return this.entitiesCount;
	}

	/** Set entities count to new amount
	 * @param entitiesCount the new amount
	 */
	public void setEntitiesCount(int entitiesCount) {
		this.entitiesCount = entitiesCount;
	}

	/**Get tiles rendered count
	 * @return tiles rendered count
	 */
	public int getTilesRendered() {
		return this.tilesRendered;
	}

	/** Set tiles rendered to new amount
	 * @param tilesRendered the new amount
	 */
	public void setTilesRendered(int tilesRendered) {
		this.tilesRendered = tilesRendered;
	}

	/**Get number of tiles
	 * @return tiles count
	 */
	public int getTilesCount() {
		return this.tilesCount;
	}

	/** Set tiles count to new amount
	 * @param tilesCount the new amount
	 */
	public void setTilesCount(int tilesCount) {
		this.tilesCount = tilesCount;
	}
	
	/**
	 * Sets the current game world
	 *
	 * @param world
	 */
	public void setWorld(AbstractWorld world) {
		this.combatEvolvedWorld = world;
	}

	/**
	 * Gets the current game world
	 *
	 * @return gameWorld
	 */
	public AbstractWorld getWorld() {
		return combatEvolvedWorld;
	}


	public void setCamera(PotateCamera camera) {
		this.camera = camera;
	}

	/**
	 * @return current game's stage
	 */
	public Stage getStage() {
		return stage;
	}

	/**
	 * @param stage - the current game's stage
	 */
	public void setStage(Stage stage) {
		this.stage = stage;
	}

	/**
	 * @return current game's skin
	 */
	public Skin getSkin() {
		return skin;
	}

	/**
	 * @param skin - the current game's skin
	 */
	public void setSkin(Skin skin) {
		this.skin = skin;
	}

	public PotateCamera getCamera() {
		return camera;
	}

	/**
	 * On tick method for ticking managers with the TickableManager interface
	 *
	 * @param i
	 */
	public void onTick(long i) {
		for (AbstractManager m : managers) {
			if (m instanceof TickableManager) {
				((TickableManager) m).onTick(0);
			}
		}
		combatEvolvedWorld.onTick(0);
	}
	
	public int getPlayerEntityID() {
		return playerEntityID;
	}

	public int getProjectileID() {
		return projectileID;
	}

	public void setPaused(boolean state) {
		this.paused = state;
	}

	public boolean getPaused() {
		return this.paused;
	}

	public void setPlayerEntityID(int newID) {
		playerEntityID = newID;
		LOGGER.info("Set player id to {}", newID);
		if (getManager(NetworkManager.class).isHost()) {
			getManager(NetworkManager.class).addConnectionPlayerEntity(0, newID);
		}
	}

	public void setProjectileID(int projectileID) {
		this.projectileID = projectileID;
	}

	/**
	 * Sets game in GameManager.
	 * @param game - The game to set the GameManager to.
	 */
	public void setGame(CombatEvolvedGame game) {
		this.game = game;
	}

	/**
	 * Gets CombatEvolvedGame
	 * @return CombatEvolvedGame if set otherwise null/
	 */
	public CombatEvolvedGame getGame() {
		return game;
	}

	/**
	 * Gets whether the game manager is in debug mode.
	 * @return 	true : In DebugMode
	 * 			false : Not in DebugMode
	 */
	public boolean getDebugMode() {
		return debugMode;
	}

	/**
	 * Sets whether the game manager is in debug mode.
	 * @param debugMode true : In DebugMode
	 *                  false : Not in DebugMode
	 */
	public void setDebugMode(boolean debugMode) {
		this.debugMode = debugMode;
	}

	/**
	 * Gets whether the debugger overlay is showing the coordinates of the player.
	 * @return 	true : Showing coordinates
	 * 			false : Not showing coordinates
	 */
	public boolean getShowCoords() {
		return this.showCoords;
	}

	/**
	 * Sets whether the debugger overlay is showing the coordinates of the player.
	 * @param showCoords 	true : Showing coordinates
	 * 						false : Not showing coordinates
	 */
	public void setShowCoords(boolean showCoords) {
		this.showCoords = showCoords;
	}

	/**
	 * Gets whether the path is showing.
	 * @return 	true : Path is showing
	 * 			false : Path is not showing
	 */
	public boolean getShowPath() {
		return this.showPath;
	}

	/**
	 * Sets whether the path is showing.
	 * @param showPath	true : Path is showing
	 *                  false : Path is not showing
	 */
	public void setShowPath(boolean showPath) {
		this.showPath = showPath;
	}

	/**
	 * Gets whether the game is rendering info over the entities.
	 * @return	true : Rendering info over the entities
	 * 			false : Not rendering info over the entities
	 */
	public boolean getShowCoordsEntity() {
		return this.showCoordsEntity;
	}

	/**
	 * Sets whether the game is rendering info over the entities.
	 * @param showCoordsEntity	true : Rendering info over entities
	 *                          false : Not rendering info over entities
	 */
	public void setShowCoordsEntity(boolean showCoordsEntity) {
		this.showCoordsEntity = showCoordsEntity;
	}


	/** Sets keybindings so that it can be used by settings screen in game
	 *
	 * @param keybindings
	 */
	public void setKeybindings(Keybindings keybindings) {
		this.keybindings = keybindings;
	}

	public Keybindings getKeybindings() {
		return this.keybindings;
	}
}