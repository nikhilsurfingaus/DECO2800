package deco.combatevolved;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import deco.combatevolved.entities.AbstractEntity;
import deco.combatevolved.entities.dynamicentities.*;
import deco.combatevolved.entities.enemyentities.BasicEnemy;
import deco.combatevolved.entities.staticentities.defensivetowers.*;
import deco.combatevolved.exceptions.EnemyValueException;
import deco.combatevolved.handlers.KeyboardManager;
import deco.combatevolved.mainmenu.InGameMenuWindow;
import deco.combatevolved.mainmenu.EndGamePopup;
import deco.combatevolved.mainmenu.InGameTutorialPopup;
import deco.combatevolved.managers.*;
import deco.combatevolved.observers.KeyDownObserver;
import deco.combatevolved.observers.TouchDownObserver;
import deco.combatevolved.renderers.*;
import deco.combatevolved.renderers.inventoryrenderer.*;
import deco.combatevolved.tasks.MovementTask;
import deco.combatevolved.util.WorldUtil;
import deco.combatevolved.worlds.*;
import deco.combatevolved.entities.staticentities.CraftingTable;
import deco.combatevolved.managers.DayNightCycle;
import deco.combatevolved.renderers.OverlayRenderer;
import deco.combatevolved.renderers.PotateCamera;
import deco.combatevolved.renderers.Renderer3D;
import deco.combatevolved.util.HexVector;
import org.slf4j.Logger;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;


import org.slf4j.LoggerFactory;

import javax.swing.Timer;


public class GameScreen implements Screen, KeyDownObserver, TouchDownObserver {
    private final Logger logger = LoggerFactory.getLogger(GameScreen.class);
    @SuppressWarnings("unused")
    private final CombatEvolvedGame game;

    /**
     * Set the renderer.
     * 3D is for Isometric worlds
     * Check the documentation for each renderer to see how it handles WorldEntity coordinates
     */
    private Renderer3D renderer = new Renderer3D();
    private OverlayRenderer rendererDebug = new OverlayRenderer();
    private InventoryRenderer rendererInventory = new InventoryRenderer();
    private CraftingWindowRenderer rendererCraftingWindow = new CraftingWindowRenderer();
    private TowerInventoryRenderer renderTowerInventory = new TowerInventoryRenderer();
    private TowerButtonRenderer renderTowerButton = new TowerButtonRenderer();
    private ConsumableRenderer rendererConsumable = new ConsumableRenderer();
	private GameplayInterfaceRenderer rendererGamePlayInterface =
			new GameplayInterfaceRenderer();
	private ComTowerConquestRenderer renderComTowerConquest = new ComTowerConquestRenderer();
	private DayNightRenderer dayNightRenderer = new DayNightRenderer();
	private SkillTreeRenderer skillTreeRenderer = new SkillTreeRenderer();

	private RecipeBookRenderer rendererRecipeBook = new RecipeBookRenderer();

	// no item renderer class found?
	//private ItemRenderer rendererItem = new ItemRenderer();

	private AbstractWorld world;

	private SpriteBatch batchDebug = new SpriteBatch();
	private SpriteBatch invBatch = new SpriteBatch();
	private SpriteBatch craftingBatch = new SpriteBatch();
	private SpriteBatch consumableBatch = new SpriteBatch();
	private SpriteBatch interfaceBatch = new SpriteBatch();
	private SpriteBatch batch = new SpriteBatch();
	private Texture bulletTexture = new Texture("resources/Bullet/bullet.png");

	// Do these need to be static? SonarQube is complaining about values
	// being assigned
	private static String hostname;
	private static String username;
	private static String password;
	private static Skin skin = GameManager.get().getSkin();

	private static final String PLACETOWERKEY = "PlaceTower";

	/**
	 * Create a camera for panning and zooming.
	 * Camera must be updated every render cycle.
	 */
	private PotateCamera camera;
	private PotateCamera cameraDebug;

	private Stage stage = new Stage(new ExtendViewport(1280, 720));

	private long lastGameTick = 0;

	/**
	 * Create an in-game menu window
	 */
	private InGameMenuWindow menu;
	private EndGamePopup popup;
	private InGameTutorialPopup tutorialPopup;

    private PlayerAttributes player;

    private Projectile projectile;

    private HexVector targetPosition;

    private List<Bullet> bullets;

    private List<Explosion> explosions;

	boolean isOpen = true;

    private TowerDefenseManager towerPlaced = new TowerDefenseManager();

    private boolean isSimpleTower = false;
    private boolean isSplashTower = false;
    private boolean isSniperTower = false;
    private boolean isZapTower = false;
    private boolean isSlowTower = false;
    private boolean isMultiTower = false;


    // Variable used to only allow one interface to open at a time.
    private boolean interfaceAlreadyOpen = false;
    private int whichInterfaceOpen = 0;

	public enum gameType {
		LOAD_GAME{
			@Override
			public AbstractWorld getWorld() {
				AbstractWorld world = new LoadGameWorld();
				DatabaseManager.loadWorld(world);
				GameManager.get().getManager(NetworkManager.class).startHosting(username, password);
				return world;
			}

            @Override
            public void network() {}
		},
		CONNECT_TO_SERVER{
			@Override
			public AbstractWorld getWorld() {
                return new ServerWorld();
			}

            @Override
            public void network() {
                GameManager.get().getManager(NetworkManager.class).connectToHost(hostname, username, password);
            }
		},
		NEW_GAME{
			@Override
			public CombatEvolvedWorld getWorld() {
                return new CombatEvolvedWorld();
			}

			@Override
            public void network() {
                GameManager.get().getManager(NetworkManager.class).startHosting(username, password);
            }
		};
		public abstract AbstractWorld getWorld();
		public abstract void network();
	}

    public GameScreen(final CombatEvolvedGame game, final gameType startType, String hostname, String username, String password, String textureAtlas, String classType) {
        /* Create an example world for the engine */
        this.game = game;

        GameScreen.hostname = hostname;
        GameScreen.username = username;
        GameScreen.password = password;

        GameManager gameManager = GameManager.get();

        world = startType.getWorld();
        gameManager.setWorld(world);

        GameManager.get().addManager(new UpdateEntityManager());

        popup = new EndGamePopup(game, skin, logger, stage, username);

//        System.out.println("PLACE PLAYER");
        for (Tile tile : GameManager.get().getWorld().getTileListFromMap()) {
//            System.out.println(!tile.isObstructed());
            if (!tile.isObstructed()) {		// prevent that the tile is at the edge of the map   && tile.getNeighbours().size() >= 4
                //player = new PlayerAttributes(tile.getRow(), tile.getCol(),
                player = new PlayerAttributes(0 , 0, // this is safe as rocks are placed later
                        0.05f,
                        textureAtlas, classType);
                break;
            }
        }
        player.setAttributes(textureAtlas, classType);
        logger.info("selected Class {}",classType);
        logger.info("Selected class attributes {}",player.getHealth());
        world.addEntity(player);
        float[] pos = player.getPosTile(player.getCol(), player.getRow() + 1, 0);
        world.addEntity(new Ferret(pos[0], pos[1], 0.14f));
        GameManager.get().setPlayerEntityID(player.getEntityID());

        startType.network();

        // Adds a delay to the client finishing its initialisation of GameScreen if all data hasn't been received
        // from the server
        while (!GameManager.get().getManager(NetworkManager.class).getClientLoadStatus()) {
            if (!GameManager.get().getManager(NetworkManager.class).getConnectionStatus()) {
                break;
            }
            logger.info("Waiting for tile data");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                logger.error("Error sleeping thread", e);
                Thread.currentThread().interrupt();
            }
        }

        camera = new PotateCamera(1920, 1080);
        cameraDebug = new PotateCamera(1920, 1080);

        // TODO: Maybe reintroduce to game
        // if (!GameManager.get().getManager(NetworkManager.class).getConnectionStatus()) {
        //     return;
        // }

        player = (PlayerAttributes) getPlayer(GameManager.get().getPlayerEntityID());
        projectile = new Projectile(player.getCol(), player.getRow(), 3f);
        explosions = new ArrayList<>();

        targetPosition = new HexVector(Integer.MAX_VALUE, Integer.MAX_VALUE);

        bullets = new ArrayList<>();

        GameManager.get().addManager(new CameraManager(player, camera));
        GameManager.getManagerFromInstance(InputManager.class).addTouchDownListener(this);
        GameManager.getManagerFromInstance(InputManager.class).addMouseMovedListener(this::notifyMouseMoved);

        /* Add the window to the stage */
        GameManager.get().setSkin(skin);
        GameManager.get().setStage(stage);
        GameManager.get().setCamera(camera);

        PathFindingService pathFindingService = new PathFindingService();
        GameManager.get().addManager(pathFindingService);

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(GameManager.get().getManager(KeyboardManager.class));
        multiplexer.addProcessor(GameManager.get().getManager(InputManager.class));
        Gdx.input.setInputProcessor(multiplexer);

        GameManager.get().getManager(KeyboardManager.class).registerForKeyDown(this);

        GameManager.get().addManager(new DayNightCycle(System.currentTimeMillis(), 20000, 35000));

        // Check to see if this is the client or the server, as many of the managers are not needed in the client
        // version and only work to crash the game
        if (GameManager.get().getManager(NetworkManager.class).isHost()) {

            // Add the EnemyManager to the managers (creates the enemy manager(spawnrate, enemytotal))
            GameManager.get().addManager(new EnemyManager(3, 10));

            // Add the ControlTowerManager to the managers
            GameManager.get().addManager(new ControlTowerManager());

            // Add the observers fro the ControlTower manager
            GameManager.get().getManager(ControlTowerManager.class).addObserver(renderComTowerConquest);
            GameManager.get().getManager(ControlTowerManager.class).addObserver(GameManager.get().
                    getManager(EnemyManager.class));

            // Add the observers from the DayNightCycle manager
            GameManager.get().getManager(DayNightCycle.class).addObserver(GameManager.get().
                    getManager(EnemyManager.class));
            GameManager.get().getManager(DayNightCycle.class).addObserver(GameManager.get().getWorld().getWorldState());
        }

        GameManager.get().getManager(DayNightCycle.class).addObserver(dayNightRenderer);

        rendererRecipeBook.create();
        rendererInventory.create();
        rendererCraftingWindow.create();
        rendererConsumable.create();
        rendererInventory.setCraftingWindowRenderer(rendererCraftingWindow);

        skillTreeRenderer.create();

        GameManager.get().addManager(new CommandsManager());
        GameManager.get().getManager(CommandsManager.class).createCommands();

        GameManager.get().setKeybindings(player.getControls());

        // Create in game window
        menu = new InGameMenuWindow(logger, stage, this.game);

        tutorialPopup = new InGameTutorialPopup(game, stage);

        GameManager.get().setDebugMode(false);

    }

    public void update() {
	    Bullet.keyUpdate(bullets, player);
        if (!menu.displayStatus() && menu.backButtonPressed()) {
            menu.setButtonPressed();
        }
    }

    /**
     * Renderer thread
     * Must update all displayed elements using a Renderer
     */
    @Override
    public void render(float delta) {
        if (!GameManager.get().getManager(NetworkManager.class).isHost() &&
                !GameManager.get().getManager(NetworkManager.class).getClientLoadStatus()) {
            return;
        }

        try {
            handleRenderables();
        } catch (Exception e) {
            //
        }

        GameManager.get().getManager(CameraManager.class).setMouseXY(Gdx.input.getX(), Gdx.input.getY());
        GameManager.get().getManager(CameraManager.class).moveCamera(camera);

        if (GameManager.get().getManager(CameraManager.class).isCameraLocked()) {
            GameManager.get().getManager(CameraManager.class).lockCamera(
                    GameManager.get().getWorld().getEntityById(GameManager.get().getPlayerEntityID()), camera);
        }

        if (GameManager.get().getManager(NetworkManager.class).getRecentDisconnect()) {
            return;
        }

        cameraDebug.position.set(camera.position);
        cameraDebug.update();
        camera.update();

        batchDebug.setProjectionMatrix(cameraDebug.combined);
        batch.setProjectionMatrix(camera.combined);
        invBatch.setProjectionMatrix(cameraDebug.combined);
        craftingBatch.setProjectionMatrix(cameraDebug.combined);
        interfaceBatch.setProjectionMatrix(cameraDebug.combined);

        // Clear the entire display as we are using lazy rendering
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // update connected players, including host
        Map<Integer, Integer> players = GameManager.get().getManager(NetworkManager.class).getUserEntities();
        for (int entity = 0; entity < players.size(); entity++) {
            ((PlayerPeon) (GameManager.get().getWorld().getEntityById(players.get(entity)))).update(delta);
        }

        rerenderMapObjects(batch, camera);
        dayNightRenderer.render(batchDebug, cameraDebug);
        renderComTowerConquest.render(batchDebug, cameraDebug);
        rendererRecipeBook.render(invBatch, cameraDebug);
        rendererDebug.render(batchDebug, cameraDebug);

        rendererInventory.render(invBatch, cameraDebug);
        rendererCraftingWindow.render(craftingBatch, cameraDebug);
        rendererCraftingWindow.displayItems(this.getCraftingTable(), craftingBatch, camera);
        rendererCraftingWindow.displayCraftedItem(this.getCraftingTable().getCraftedItem(), craftingBatch, camera);
        rendererDebug.render(batchDebug, cameraDebug);
        renderTowerButton.render(batchDebug, cameraDebug);
        rendererConsumable.render(consumableBatch, camera);

        List<Tower> towers = new LinkedList<>();
        List<BasicEnemy> enemies = new LinkedList<>();
        for (AbstractEntity entity: GameManager.get().getWorld().getEntities()) {
            if (entity instanceof BasicEnemy) {
                enemies.add((BasicEnemy) entity);
            } else if (entity instanceof Tower) {
                towers.add((Tower) entity);
            }
        }

        update();

        batch.begin();
        List<Bullet> hitBullets = new ArrayList<>();
        boolean isLaser = false;
        for (Bullet currentBullet : bullets) {
            currentBullet.update();
            batch.draw(bulletTexture, currentBullet.getBulletPosition().getCol(), currentBullet.getBulletPosition().getRow());
            SkillTreev2 playerSkills = player.getPlayerSkills();
            int bulletDistance = 40;
            if(playerSkills.hasLearnt(playerSkills.getSkill("attackExplosion"))){
                int increment = (int)Math.round(playerSkills.getSkill("attackExplosion").getValue() * bulletDistance);
                bulletDistance = bulletDistance + increment;
            }
            for (BasicEnemy enemy : enemies) {
                if (currentBullet.hitTarget(enemy, bulletDistance)) {
                    try {

                        // Enemies lose health based on their armour and player's attack stat
                        int attack = player.getStats("attack");
                        int armour = enemy.getArmourHealth();

                        if (playerSkills.hasLearnt(playerSkills.getSkill("attackMortalReminder"))) {
                            armour = (int) Math.round(armour * playerSkills.getSkill("attackMortalReminder").getValue());
                        }
                        if (playerSkills.hasLearnt(playerSkills.getSkill("attackMortalReminder"))) {
                            armour = (int) Math.round(armour * playerSkills.getSkill("attackMortalReminder").getValue());
                        }
                        if (playerSkills.hasLearnt(playerSkills.getSkill("utilityLaserBullets"))) {
                            isLaser = true;
                        }
                        // deal 5 damage every second for 5 second
                        if (playerSkills.hasLearnt(playerSkills.getSkill("attackFlameTrail"))) {
                            int constantDamage = 5;
                            if(playerSkills.hasLearnt(playerSkills.getSkill("attackMoreExplosion"))){
                                boolean chance = new Random().nextInt(5)==0;
                                if(chance){
                                    constantDamage = 15;
                                    System.out.println("Got the chance" + constantDamage);
                                }
                                System.out.println(constantDamage);
                            }
                            Timer timer = new Timer(1000,null);
                            int finalConstantDamage = constantDamage;
                            timer.addActionListener(new ActionListener() {
                                private int count = 1;

                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    if (enemy.getHealth() <= 0) {
                                        ((Timer)e.getSource()).stop();
                                    } else if (count == 5){
                                        ((Timer)e.getSource()).stop();
                                    } else {
                                        try {
                                            enemy.loseHealth(finalConstantDamage);
                                        } catch (EnemyValueException ex) {
                                            ex.printStackTrace();
                                        }
                                    }
                                    count++;

                                }
                            });
                            timer.start();
                        }

                        int damageAmount = attack * (100 / (100 + armour)); // Formula borrowed from League of Legends
                        if (damageAmount > 0) {
                            enemy.loseHealth(damageAmount);
                            if (playerSkills.hasLearnt(playerSkills.getSkill("attackFireTouch"))) {
                                // deal extra 20% damage to enemy if this skill exist
                                enemy.loseHealth((int)Math.round(damageAmount*0.2));
                            }
                        } else { // Loses 2 health if attack isn't high enough
                            enemy.loseHealth(2);
                        }

                        hitBullets.add(currentBullet);
                    } catch (EnemyValueException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        // remove bullets that have hit an enemy
        if(!isLaser){
            for (Bullet bullet : hitBullets) {
                bullets.remove(bullet);
            }
        }


        batch.end();
        GameManager.get().getManager(CameraManager.class).setScreenWidth(Gdx.graphics.getWidth());
        GameManager.get().getManager(CameraManager.class).setScreenHeight(Gdx.graphics.getHeight());

        List<Explosion> explosionsToRemove = new ArrayList<>();
        for (Explosion explosion : explosions) {
            explosion.update(delta);
            if (explosion.getRemove()) {
                explosionsToRemove.add(explosion);
            }
        }
        explosions.removeAll(explosionsToRemove);

        if (Gdx.input.isKeyJustPressed(Input.Keys.X)) {
            if (player.getAttackPattern().equals("bullet")) {
                player.setAttackPattern("projectile");
            } else {
                player.setAttackPattern("bullet");
            }
        }

        if (player.hasProjectile() && projectile.getCol() == targetPosition.getCol() && projectile.getRow() == targetPosition.getRow()) {
            explosions.add(
                    new Explosion(WorldUtil.colRowToWorldCords(projectile.getCol(),
                            projectile.getRow())[0], WorldUtil.colRowToWorldCords(projectile.getCol(), projectile.getRow())[1]));
            projectile.setTask(null);
            GameManager.get().getManager(SoundManager.class).playAnimationSound("projectile-explosion.wav");
            world.removeEntity(projectile);
            for (BasicEnemy enemy : enemies) {
                if (projectile.hitTarget(enemy) == 1) {
                    try {
                        enemy.loseHealth(200);
                    } catch (EnemyValueException e) {
                        //
                    }
                } else if (projectile.hitTarget(enemy) == 2) {
                    try {
                        enemy.loseHealth(100);
                    } catch (EnemyValueException e) {
                        //
                    }
                } else if (projectile.hitTarget(enemy) == 3) {
                    try {
                        enemy.loseHealth(50);
                    } catch (EnemyValueException e) {
                        //
                    }
                }
            }
            player.setHasProjectile(false);
        }

        if (!player.getAttackPattern().equals("projectile")) {
            projectile.setTask(null);
            world.removeEntity(projectile);
            player.setHasProjectile(false);
            //player.explodeProjectile();
        }

        batch.begin();
        for (Explosion explosion : explosions) {
            explosion.render(batch);
        }
        batch.end();

        batch.begin();
        try {
            for (BasicEnemy enemy : enemies) {
                enemy.getHealthBar().update();
                enemy.getHealthBar().render(batch);
            }
        } catch (Exception e) {
            //
        }
        try {
            for (Tower tower : towers) {
                tower.getHealthBar().towerUpdate();
                tower.getHealthBar().render(batch);
            }
        } catch (Exception e) {
            //
        }

        batch.end();

        rendererGamePlayInterface.render(interfaceBatch, cameraDebug);

        // Raid bar covering skill tree so putting it here
        skillTreeRenderer.render(batchDebug, cameraDebug);

        /* Refresh the experience UI for if information was updated */
        stage.act(delta);
        stage.draw();
    }

    /**
     * Notifies the observers of the mouse button being pushed down
     *
     * @param screenX the x position the mouse was pressed at
     * @param screenY the y position the mouse was pressed at
     * @param pointer
     * @param button  the button which was pressed
     */
    @Override
    public void notifyTouchDown(int screenX, int screenY, int pointer, int button) {
        // TODO - add these keybindings to the keybindings class so that all keys/buttons can be defined and stored in
        //  one place.

        Rectangle buttonBoundaries;

        //GameManager.get().getManager(SoundManager.class).playSound("Sound Effect/EFFECT_gunshot_01_Inactive.wav");
        //if the tower Health is zero then remove the tower
        if (this.towerPlaced.getTowers().size() > 1) {
            LinkedList<Tower> towersToDelete = this.towerPlaced.removeAllTowers();
            int deadTowers = towersToDelete.size();
            for (int i = 0; i < deadTowers; i++) {
                world.removeEntity(towersToDelete.get(i));
            }
        }


        if (!GameManager.get().getManager(NetworkManager.class).isHost()) {
            player = (PlayerAttributes) getPlayer(GameManager.get().getPlayerEntityID());
        }
        if (!rendererInventory.getDisplayStash() &&
        		!rendererInventory.playerInvClicked() && !rendererRecipeBook.isBookOpen() &&
        		(player.getAttackPattern().equals("bullet") && button == player.getControls().getKey("FireBullet")) &&
                Bullet.canFire() && player.isAlive()) {
            world.removeEntity(projectile);
            float[] mouse = WorldUtil.screenToWorldCoordinates(screenX, screenY);
            float[] mousePosition = WorldUtil.worldCoordinatesToColRowUnrounded(mouse[0], mouse[1]);
            float x = mousePosition[0] - player.getCol();
            float y = mousePosition[1] - player.getRow();
            HexVector velocity = new HexVector(x, y);
            velocity = velocity.normalise().multiply(Bullet.VELOCITY);
            float[] playerPosition = WorldUtil.colRowToWorldCords(player.getCol(), player.getRow());
            HexVector bulletPosition = new HexVector(playerPosition[0], playerPosition[1]);
            Bullet bullet = new Bullet(bulletPosition, velocity, 10, 10);
            GameManager.get().getManager(SoundManager.class).playAnimationSound("fireball.wav");
            bullets.add(bullet);
            Bullet.updateFire();
            player.setShooting(true);
        }

        //for opening tower inventory through button on screen
        buttonBoundaries = new Rectangle(camera.position.x + (float) (camera.viewportWidth / 3.2),
                (float) (camera.position.y - camera.viewportHeight / 2.01),
                Gdx.graphics.getWidth()/5, (float) (Gdx.graphics.getHeight()/10.285));
        Vector3 coords = this.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
        float[] mouse2 = new float[] { coords.x, coords.y };
        Vector2 cameraPos = new Vector2(mouse2[0], mouse2[1]);
        if (button == 0 && (buttonBoundaries.contains(cameraPos) && !renderTowerInventory.isDisplayed())) {
            getTowerInventory();
        }

        float[] mouse = WorldUtil.screenToWorldCoordinates(screenX, screenY);
        float[] clickedPosition = WorldUtil.worldCoordinatesToColRow(mouse[0], mouse[1]);
        HexVector h = new HexVector(clickedPosition[0],clickedPosition[1]);
        float[] tilePos = player.getPosTile(clickedPosition[0], clickedPosition[1] - 0.5f, 0);

        if ((button == player.getControls().getKey(PLACETOWERKEY) && isSimpleTower)) {
            if (!world.positionHasTower(tilePos[0], tilePos[1])) {
                world.addEntityTower(new SimpleTower(tilePos[0] + 0.2f, tilePos[1] + 0.1f), tilePos[0], tilePos[1]);
                Tile t = world.getTile(h);
                t.setObstructed(true);
                isSimpleTower = false;
            }
        }

        if (button == player.getControls().getKey(PLACETOWERKEY) && isSniperTower) {
            if (!world.positionHasTower(tilePos[0], tilePos[1])) {
                world.addEntityTower(new SniperTower(tilePos[0] + 0.2f, tilePos[1] + 0.1f), tilePos[0], tilePos[1]);
                Tile t = world.getTile(h);
                t.setObstructed(true);
                isSniperTower = false;
            }
        }

        if (button == player.getControls().getKey(PLACETOWERKEY) && isSplashTower) {
            if (!world.positionHasTower(tilePos[0], tilePos[1])) {
                world.addEntityTower(new SplashTower(tilePos[0], tilePos[1]), tilePos[0], tilePos[1]);
                Tile t = world.getTile(h);
                t.setObstructed(true);
                isSplashTower = false;
            }
        }

        if (button == player.getControls().getKey(PLACETOWERKEY) && isSlowTower) {
            if (!world.positionHasTower(tilePos[0], tilePos[1])) {
                world.addEntityTower(new SlowTower(tilePos[0] + 0.2f, tilePos[1] + 0.1f), tilePos[0], tilePos[1]);
                Tile t = world.getTile(h);
                t.setObstructed(true);
                isSlowTower = false;
            }
        }

        if (button == player.getControls().getKey(PLACETOWERKEY) && isZapTower) {
            if (!world.positionHasTower(tilePos[0], tilePos[1])) {
                world.addEntityTower(new ZapTower(tilePos[0] + 0.2f, tilePos[1] + 0.1f), tilePos[0], tilePos[1]);
                Tile t = world.getTile(h);
                t.setObstructed(true);
                isSlowTower = false;
                isZapTower = false;
            }
        }

        if (button == player.getControls().getKey(PLACETOWERKEY) && isMultiTower) {
            if (!world.positionHasTower(tilePos[0], tilePos[1])) {
                world.addEntityTower(new MultiTower(tilePos[0], tilePos[1]), tilePos[0], tilePos[1]);
                Tile t = world.getTile(h);
                t.setObstructed(true);
                isMultiTower = false;
            }
        }

        if(!rendererInventory.getDisplayStash() &&
        		!rendererInventory.playerInvClicked() && !rendererRecipeBook.isBookOpen() &&
        		player.getAttackPattern().equals("projectile")) {
            if (button == player.getControls().getKey("FireProjectile") && !player.hasProjectile()) {
                projectile.setCol(player.getCol());
                projectile.setRow(player.getRow());
                world.addEntity(projectile);
                player.setHasProjectile(true);
                //GameManager.get().getManager(NetworkManager.class).sendProjectileUpdate(GameManager.get().getPlayerEntityID(), projectile, ProjectileMessage.ProjectileMsgType.ADD, screenX, screenY);
                //player.addProjectile();
                //projectile = player.getProjectile();
            } else if (button == player.getControls().getKey("FireProjectile") && player.hasProjectile()) {
                targetPosition = new HexVector(clickedPosition[0], clickedPosition[1]);
                projectile.setTask(new MovementTask(projectile, targetPosition));
                //GameManager.get().getManager(NetworkManager.class).sendProjectileUpdate(GameManager.get().getPlayerEntityID(), projectile, ProjectileMessage.ProjectileMsgType.SHOOT, screenX, screenY);
                //player.shootProjectile(screenX, screenY);
                //projectile = player.getProjectile();
            }
        }

        if (button == player.getControls().getKey("SwitchAttack")) {
            if (player.getAttackPattern().equals("bullet")) {
                player.setAttackPattern("projectile");
            }else {
                player.setAttackPattern("bullet");
            }
        }

    }

    /**
     * Notifies the observers of the mouse button being moved
     *
     * @param screenX the x position the mouse was pressed at
     * @param screenY the y position the mouse was pressed at
     */
    public void notifyMouseMoved(int screenX, int screenY) {
        float[] mouse = WorldUtil.screenToWorldCoordinates(screenX, screenY);
        float[] mousePosition = WorldUtil.worldCoordinatesToColRowUnrounded(mouse[0], mouse[1]);
        float x = mousePosition[0] - projectile.getCol();
        float y = mousePosition[1] - projectile.getRow();

        double rotation = Math.toDegrees(Math.atan(y / x));

        if(y >= 0 && x >= 0) {
            rotation = 360 - (90 - rotation);
        } else if(y >= 0 && x <= 0) {
            rotation = 90 + rotation;
        } else if(y <= 0 && x >= 0) {
            rotation = 270 + rotation;
        } else if(y <= 0 && x <= 0) {
            rotation = 90 + rotation;
        }
        projectile.setRotation((float) rotation);
    }

    /**
     * Sets the time to move forwards in the game in time
     */
    private void handleRenderables() {
        if (System.currentTimeMillis() - lastGameTick > 20) {
            lastGameTick = System.currentTimeMillis();
            GameManager.get().onTick(0);
        }
    }

    /**
     * Use the selected renderer to render objects onto the map
     */
    private void rerenderMapObjects(SpriteBatch batch, OrthographicCamera camera) {
        renderer.render(batch, camera);
    }


    @Override
    public void show() {
        // Inherited method
    }

    /**
     * Resize the viewport
     *
     * @param width  the new width of the viewport
     * @param height the new height of the viewport
     */
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, false);
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();

        cameraDebug.viewportWidth = width;
        cameraDebug.viewportHeight = height;
        cameraDebug.update();

        tutorialPopup.repositionElements(stage);
    }

    @Override
    public void pause() {
        //do nothing
    }

    @Override
    public void resume() {
        //do nothing
    }

    @Override
    public void hide() {
        //do nothing
    }

    /**
     * Disposes of assets etc when the rendering system is stopped.
     */
    @Override
    public void dispose() {
        // Don't need this at the moment
        batchDebug.dispose();
        invBatch.dispose();
        craftingBatch.dispose();
        batch.dispose();
        System.exit(0);
    }

    /**
     * key handler for observer an key down input
     *
     * @param keycode the input keystroke
     */
    @Override
    public void notifyKeyDown(int keycode) {
        // TODO - when adding a key, please add it to the Keybindings class so that every key/button is referenced in
        //  one place.
        if (keycode == player.getControls().getKey("DebugInfo")) {
            GameManager.get().setDebugMode(!GameManager.get().getDebugMode());
        }
        if (keycode == player.getControls().getKey("ReloadWorld")) {
             world = new CombatEvolvedWorld();  //GameWorld
             AbstractEntity.resetID();
             Tile.resetID();
             GameManager gameManager = GameManager.get();
             gameManager.setWorld(world);
             // Add first peon to the world
             world.addEntity(new AgentEntity(0f, 0f, 0.05f));
        }
        if (keycode == player.getControls().getKey("TileCoords")) {
            GameManager.get().setShowCoords(!GameManager.get().getShowCoords());
            logger.info("Show coords is now {}", GameManager.get().getShowCoords());
        }
        if (keycode == player.getControls().getKey("ShowPath")) {
            GameManager.get().setShowPath(!GameManager.get().getShowPath());
            logger.info("Show Path is now {}", GameManager.get().getShowPath());
        }
        if (keycode == player.getControls().getKey("SaveWorld")) {
            // Save the world to the DB
            DatabaseManager.saveWorld(null);
        }
        if (keycode == player.getControls().getKey("LoadWorld")) {
            // Load the world to the DB
            DatabaseManager.loadWorld(null);
        }


        if (keycode == player.getControls().getKey("Inventory") &&
                !GameManager.get().getManager(OnScreenMessageManager.class).isTyping()
                && vehicleExists()) {
            if (whichInterfaceOpen == 1) {
                // Close the vehicle inventory stash
                rendererInventory.toggleStash();
                whichInterfaceOpen = 0;
            }else if (whichInterfaceOpen == 0) {
                // Display the vehicle inventory stash
                rendererInventory.toggleStash();
                tutorialPopup.show4();
                whichInterfaceOpen = 1;

            }
        } else if (!vehicleExists()){
            logger.warn("Vehicle does not exist in world");
        }

        if (keycode == player.getControls().getKey("EndGame")) {
            popup.render();
        }
        if (keycode == player.getControls().getKey("GameMenu")) {
            if (!GameManager.get().getManager(OnScreenMessageManager.class).isTyping()) {

            }
            // Render in game menu
            menu.render();
        }
        if (keycode == player.getControls().getKey("RecipeBook") &&
                !GameManager.get().getManager(OnScreenMessageManager.class).isTyping()) {
            if (whichInterfaceOpen == 2) {
                // Close recipe book
                rendererRecipeBook.toggleRecipe();
                whichInterfaceOpen = 0;
            } else if (whichInterfaceOpen == 0) {
                // Open recipe book
                rendererRecipeBook.toggleRecipe();
                tutorialPopup.show3();
                whichInterfaceOpen = 2;
            }
        }

        if (keycode == player.getControls().getKey("TowerInventory") &&
                !GameManager.get().getManager(OnScreenMessageManager.class).isTyping()) {
            if (whichInterfaceOpen == 3) {
                // Close tower inventory
                getTowerInventory();
                whichInterfaceOpen = 0;
            } else if (whichInterfaceOpen == 0) {
                // Open tower inventory
                getTowerInventory();
                whichInterfaceOpen = 3;
            }
        }


        if (keycode == player.getControls().getKey("Crafting") &&
                !GameManager.get().getManager(OnScreenMessageManager.class).isTyping()) {
            if (whichInterfaceOpen == 4) {
                // Close crafting table
                rendererCraftingWindow.toggleCraft();
                whichInterfaceOpen = 0;
            } else if (whichInterfaceOpen == 0) {
                // Open crafting table
                rendererCraftingWindow.toggleCraft();
                tutorialPopup.show2();
                whichInterfaceOpen = 4;
            }
        }

        if (keycode == player.getControls().getKey("CraftItem") &&
                rendererCraftingWindow.isCraftingOn()) {
            this.getCraftingTable().setCraftedItem(this.getCraftingTable().craftItem());
        }

        if (keycode == player.getControls().getKey("SkillTree") &&
                !GameManager.get().getManager(OnScreenMessageManager.class).isTyping()) {
            if (whichInterfaceOpen == 5) {
                // Close skill tree
                skillTreeRenderer.toggleSkillTree();
                whichInterfaceOpen = 0;
            } else if (whichInterfaceOpen == 0) {
                // Open skill tree
                skillTreeRenderer.toggleSkillTree();
                whichInterfaceOpen = 5;
            }
        }
    }

    /**
     * Helper method for camera positioning to get a pointer to the player.
     *
     * @return a pointer to the PlayerPeon player
     * @throws NullPointerException if player is null
     */
    private PlayerPeon getPlayer(int playerID) {
        PlayerPeon playerPeon = null;
        for (AbstractEntity entity : world.getEntities()) {
            if (entity instanceof PlayerPeon && (entity.getEntityID() == playerID)) {
                playerPeon = (PlayerPeon) entity;
            }
        }
        if (playerPeon == null) {
            throw new NullPointerException("Player is null");
        }
        return playerPeon;
    }


    /**
     * Helper method for camera positioning to get a pointer to the crafting table.
     *
     * @return a pointer to the CraftingTable CraftingTable
     */
    private CraftingTable getCraftingTable() {
        CraftingTable craftingTable = null;
        for (AbstractEntity entity : world.getEntities()) {
            if (entity instanceof CraftingTable) {
                craftingTable = (CraftingTable) entity;
            }
        }
        if (craftingTable == null) {
            throw new NullPointerException("Crafting table is null");
        }
        return craftingTable;
    }

    /**
     * Check wether the vehicle to travel exists in game
     *
     * @return true if the vehicle exists on the map
     */
    private boolean vehicleExists() {
        List<AbstractEntity> entities = GameManager.get().getWorld().getSortedEntities();
        for (AbstractEntity entity : entities) {
            if (entity instanceof VehicleEntity) {
                return true;
            }
        }
        return false;
    }

    /**
     * Helper method to open/close tower inventory and placing down towers.
     */
    private void getTowerInventory() {
        renderTowerInventory.toggleTowerInventory();

        renderTowerInventory.getSimpleBut().addActionListener(e -> isSimpleTower = true);
        renderTowerInventory.getSplashBut().addActionListener(e -> isSplashTower = true);
        renderTowerInventory.getSniperBut().addActionListener(e -> isSniperTower = true);
        renderTowerInventory.getZapBut().addActionListener(e -> isZapTower = true);
        renderTowerInventory.getMultiBut().addActionListener(e -> isMultiTower = true);
        renderTowerInventory.getSlowBut().addActionListener(e -> isSlowTower = true);
    }
}