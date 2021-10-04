package deco.combatevolved.entities.dynamicentities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import deco.combatevolved.entities.AbstractEntity;
import deco.combatevolved.entities.Poison;
import deco.combatevolved.entities.enemyentities.DesertEnemy;
import deco.combatevolved.entities.enemyentities.EnemyPoison;
import deco.combatevolved.entities.items.Inventory;
import deco.combatevolved.entities.items.Recycle;
import deco.combatevolved.entities.items.consumableitems.ActiveItem;
import deco.combatevolved.entities.items.resources.Items;
import deco.combatevolved.entities.staticentities.CraftingTable;
import deco.combatevolved.entities.HasHealth;
import deco.combatevolved.handlers.KeyboardManager;
import deco.combatevolved.managers.*;
import deco.combatevolved.observers.KeyDownObserver;
import deco.combatevolved.observers.KeyUpObserver;
import deco.combatevolved.observers.MouseMovedObserver;
import deco.combatevolved.tasks.MovementTask;
import deco.combatevolved.util.HexVector;
import deco.combatevolved.util.Keybindings;
import deco.combatevolved.util.WorldUtil;
import deco.combatevolved.worlds.AbstractWorld;
import deco.combatevolved.worlds.Tile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class PlayerPeon extends AgentEntity implements KeyDownObserver, KeyUpObserver, HasHealth, Poison, MouseMovedObserver, Combatant {

    private String entityAtlasString;

    private String nWalking = "N_Walking";
    private String neWalking = "NE_Walking";
    private String nwWalking = "NW_Walking";
    private String sWalking = "S_Walking";
    private String seWalking = "SE_Walking";
    private String swWalking = "SW_Walking";
    private String nRunning = "N_Running";
    private String neRunning = "NE_Running";
    private String nwRunning = "NW_Running";
    private String sRunning = "S_Running";
    private String seRunning = "SE_Running";
    private String swRunning = "SW_Running";
    private String nShooting = "N_Walking_Gun";
    private String neShooting = "NE_Walking_Gun";
    private String nwShooting = "NW_Walking_Gun";
    private String sShooting = "S_Walking_Gun";
    private String seShooting = "SE_Walking_Gun";
    private String swShooting = "SW_Walking_Gun";
    private String nIdle = "N_Idle";
    private String neIdle = "NE_Idle";
    private String nwIdle = "NW_Idle";
    private String sIdle = "S_Idle";
    private String seIdle = "SE_Idle";
    private String swIdle = "SW_Idle";
    private String dead = "Death";
    private String[] statesArray = {nWalking, neWalking, nwWalking, sWalking, seWalking, swWalking,
                                   nRunning, neRunning, nwRunning, sRunning, seRunning, swRunning,
                                   nShooting, neShooting, nwShooting, sShooting, seShooting, swShooting,
                                   nIdle, neIdle, nwIdle, sIdle, seIdle, swIdle, dead};

    private String currentState;
    private String previousState;

    private static final float ROW_VISUAL_OFFSET = 0.375f;

    private Map<String, Animation<TextureRegion>> animationMap;

    private float animationTimer = 0;

    // max health of the Player
    private int maxHealth;

    // the health of the player
    private int health = maxHealth;

    // the maximum amount of damage the player can unleash
    private int offenceMax = 10;

    // the maximum amount of damage the player can evade
    private int defenceMax = 5;

    // the minimum amount of damage the player can unleash
    private static final int OFFENCE_MIN = 1;

    // the minimum amount of damage the player can evade
    private static final int DEFENCE_MIN = 1;

    private boolean hasProjectile;

    private boolean projectileReady;

    private Projectile projectile;

    // stamina/energy of the player
    private int energy = MAX_ENERGY;

    // max energy of the player
    private static final int MAX_ENERGY = 100;

    // Setup a logger for the player
    private static final Logger logger = LoggerFactory.getLogger(PlayerPeon.class);

    private static boolean exists = false;

    // the inventory of the player
    private transient Inventory playerInventory;

    // the recycle of the player
    private transient Recycle playerRecycle;

    // the scrap of the player have
    private int scrap = 0;

    // player sprinting state
    private boolean sprinting = false;

    private List<DesertEnemy> poisonEnemies = new ArrayList<>();
    private List<EnemyPoison> poison = new ArrayList<>();

    // Player's keybindings
    private Keybindings controls;
    
    private SkillTreev2 playerSkills;

    // consumable items the player has active
    private List<ActiveItem> activeConsumables;

    // Random number
    private Random rand = new Random();

    private boolean alive = true;
    private float reviveCounter = 0;
    private boolean damaged = false;
    private float damagedCounter = 0;
    private boolean shooting = false;
    private float shootingTimer = 0;

    public PlayerPeon(float row, float column, float speed, String textureAtlas) {
        super(row, column, speed);

        animationMap = new HashMap<>();
        setCurrentState(sIdle);
        setPreviousState(sIdle);
        setUpAtlas(textureAtlas);

        this.hasProjectile = false;
        this.projectileReady = false;
        this.projectile = new Projectile(this.getCol(), this.getRow(), 3f);

        velocity = new HexVector();
        this.energy = MAX_ENERGY;
        this.hasProjectile = false;
        try {
            playerInventory = new Inventory(8);
        } catch (Exception e) {
            //
        }

        // Initialise input listeners for the player
        playerRecycle = new Recycle();

        // initialise active consumabes list

        this.activeConsumables = new ArrayList<>();

        Items items = new Items();
        playerInventory.addItem(items.getItems().get("potato"));
        playerInventory.addItem(items.getItems().get("roots"));
        playerInventory.addItem(items.getItems().get("wood"));
        playerInventory.addItem(items.getItems().get("wood"));
        playerInventory.addItem(items.getItems().get("wood"));

        playerSkills = new SkillTreev2();

        // Set controls only for first entity created
        if (!exists) {
            exists = true;

            // Sets the entityID in the game manager for when there is multiple players
            GameManager.get().setPlayerEntityID(getEntityID());

            GameManager.getManagerFromInstance(InputManager.class).addMouseMovedListener(this);
            GameManager.getManagerFromInstance(KeyboardManager.class).registerForKeyDown(this);
            GameManager.getManagerFromInstance(KeyboardManager.class).registerForKeyUp(this);

        }

        //initialise keybindings
        this.controls = new Keybindings();
    }

    /**
     * Sets up the player's texture atlas for setting animations.
     * This should only be called in the constructor.
     *
     * @param textureAtlas path to the texture atlas
     */
    protected void setUpAtlas(String textureAtlas) {
        // location of where the sprites are
        logger.info("Texture atlas: {}", textureAtlas);
        entityAtlasString = textureAtlas;
        TextureAtlas atlas = GameManager.getManagerFromInstance(TextureManager.class).getAtlas(textureAtlas);
        if (atlas == null) {
            logger.warn("Could not get atlas for {}", this);
            return;
        }
        TextureRegion initialState = new TextureRegion(atlas.findRegion(sIdle), 0, 0, 450, 450);
        // set sprite size limit by scaling it with the screen/game world size
        setBounds(0, 0, 320 * getColRenderLength() * WorldUtil.SCALE_X, 272 * getRowRenderLength() * WorldUtil.SCALE_Y);
        setRegion(initialState);
        setAnimations();
    }

    /**
     * Sets the player's animations.
     */
    public void setAnimations() {
        float frameDuration = 0.18f;
        int numOfFrames = 4;
        Array<TextureRegion> frames = new Array<>();

        for (String animation : getStatesArray()) {
            if (animation.contains("Idle") || animation.equals(dead)) {
                numOfFrames = 1;
            }

            setAnimation(frames, animation, numOfFrames);
            animationMap.put(animation, new Animation<>(frameDuration, frames));
            frames.clear();
        }
    }

    /**
     * Helper method for setAnimations(). Responsible for getting all the frames in a texture region into an array
     * for use in the Animation constructor.
     *
     * @param frames The array to hold the frames
     * @param regionName The texture region name of the animation
     * @param  numOfFrames The number of frames in the animation
     */
    public void setAnimation(Array<TextureRegion> frames, String regionName, int numOfFrames) {
        TextureAtlas atlas = GameManager.getManagerFromInstance(TextureManager.class).getAtlas(entityAtlasString);
        TextureRegion region = atlas.findRegion(regionName);
        int width = region.getRegionWidth() / numOfFrames;
        int height = region.getRegionHeight();
        for (int frame = 0; frame < numOfFrames; frame++) {
            frames.add(new TextureRegion(region, frame * width, 0, width, height));
        }
    }

    public void checkPoison() {
        for (EnemyPoison enemyPoison : poison) {
            if(enemyPoison.getTime() > 1000) {
                dealPoisonDamage(enemyPoison);
            }
        }
    }

    public void poison(DesertEnemy enemy) {
        if (!poisonEnemies.contains(enemy)) {
            poisonEnemies.add(enemy);
            EnemyPoison enemyPoison = new EnemyPoison(enemy);
            poison.add(enemyPoison);
        }
    }

    public void dealPoisonDamage(EnemyPoison enemyPoison) {
        this.loseHealth(enemyPoison.getPoisonDamage());
        enemyPoison.addCount();
        if (enemyPoison.getCount() == 5) {
            poisonEnemies.remove(enemyPoison.getEnemy());
            poison.remove(enemyPoison);
        }
    }

    public boolean hasProjectile() {
        return hasProjectile;
    }

    public boolean isProjectileReady() {
        return projectileReady;
    }

    public void setHasProjectile(boolean hasProjectile) {
        this.hasProjectile = hasProjectile;
    }

    public Animation<TextureRegion> getAnimation(String animation) {
        return animationMap.get(animation);
    }

    /**
     * Updates the player's column and row positions as well as the position of the texture.
     * It is also responsible for player animations (setting the correct animation based on the player's
     * state, as well as running through the animation sequence). It also calls the revive function if
     * the player is dead.
     *
     * @param dt Time since last update
     */
    public void update(float dt) {
        float[] entityWorldCoord = WorldUtil.colRowToWorldCords(getCol(), getRow());
        updateDamagedState(dt);
        updateShootingState(dt);
        setPosition(entityWorldCoord[0], entityWorldCoord[1]);
        setRegion(getFrame(dt));
        revive(dt);
    }

    /**
     * Updates the player's damaged state.
     *
     * @param delta Time since the last update
     */
    public void updateDamagedState(float delta) {
        if (isDamaged()) {
            incrementDamagedCounter(delta);
        }

        if (getDamagedCounter() >= 0.25f) {
            setDamaged(false);
            resetDamagedCounter();
        }
    }

    /**
     * Updates the player's shooting state.
     *
     * @param delta Time since the last update
     */
    public void updateShootingState(float delta) {
        if (isShooting()) {
            incrementShootingTimer(delta);
        }

        if (getShootingTimer() >= 0.25f) {
            setShooting(false);
            resetShootingTimer();
        }
    }

    /**
     * Gets the frame that the current animation is on.
     *
     * @param dt Time since last update
     * @return The current frame that is being played in the animation
     */
    public TextureRegion getFrame(float dt) {
        TextureRegion region;
        setState();
        region = getAnimation(getCurrentState()).getKeyFrame(getAnimationTimer(), true);

        // loop through the animation unless if we change states
        if (getCurrentState().equals(getPreviousState()) && isMoving()) {
            incrementAnimationTimer(dt);
        } else {
            resetAnimationTimer();
            if (!getCurrentState().equals(getPreviousState())) {
                logger.info("Current state is {}, previous state is {}", getCurrentState(), getPreviousState());
            }
        }

        setPreviousState(getCurrentState());

        return region;
    }

    /**
     * Set the player's state by checking where they are looking at, if they are sprinting and moving and if they are alive.
     */
    public void setState() {
        if (!isAlive()) {
            setCurrentState(dead);
            return;
        }

        boolean blocked;
        float velocityCol = getVelocity().getCol();
        float velocityRow = getVelocity().getRow();
        int index;

        if (rotation < 45 || rotation >= 315) { // north
            index = 0;
        } else if (rotation >= 45 && rotation < 90) { // north west
            index = 2;
        } else if (rotation >= 270 && rotation < 315) { // north east
            index = 1;
        } else if (rotation >= 90 && rotation < 135) { // south west
            index = 5;
        } else if (rotation >= 225 && rotation < 270) { // south east
            index = 4;
        } else { // south
            index = 3;
        }

        if (isMovingDiagonal()) {
            blocked = simpleTileCollisionCheck(velocityCol, 0)
                   && simpleTileCollisionCheck(0, velocityRow);
        } else if (velocityCol != 0) {
            blocked = simpleTileCollisionCheck(velocityCol, 0);
        } else {
            blocked = simpleTileCollisionCheck(0, velocityRow);
        }

        if (isMoving() && !blocked && !isInVehicle() && isAlive()) {
            if (isSprinting() && getPlayerEnergy() != 0 && !isShooting()) {
                index += 6; // set state to sprinting, otherwise set state to walking
            } else if (isShooting()) {
                index += 12; // set state to shooting walking
            }
        } else {
            index += 18; // set state to idle
        }

        setCurrentState(getStatesArray()[index]);
    }

    /**
     * Performs the revival process if the player is dead and another player is nearby.
     *
     * @param dt Time since last update.
     */
    public void revive(float dt) {
        AbstractEntity entity;
        Collection<Integer> players;

        if (!isAlive()) {
            players = GameManager.get().getManager(NetworkManager.class).getUserEntities().values();
            for (int player : players) {
                if (player != getEntityID()) {
                    entity = GameManager.get().getWorld().getEntityById(player);
                    if (entity.getPosition().isCloseEnoughToBeTheSame(getPosition(), 1)) {
                        incrementReviveCounter(dt);
                        break;
                    }
                }
            }
        }

        if (reviveCounter >= 5) {
            setAlive(true);
            gainFullHealth();
            resetReviveCounter();
        }
    }

    /**
     * Gets the player's revive counter.
     *
     * @return The player's revive counter
     */
    public float getReviveCounter() {
        return reviveCounter;
    }

    /**
     * Increments the revive counter by a specified amount.
     *
     * @param amount The amount to increment the revive counter by
     */
    public void incrementReviveCounter(float amount) {
        reviveCounter += amount;
    }

    /**
     * Resets the revive counter.
     */
    public void resetReviveCounter() {
        reviveCounter = 0;
    }

    /**
     * Sets the player's living status.
     *
     * @param state The state to set for the player's living status
     */
    public void setAlive(boolean state) {
        alive = state;
    }

    /**
     * Gets the player's damaged counter.
     *
     * @return The player's damaged counter
     */
    public float getDamagedCounter() {
        return damagedCounter;
    }

    /**
     * Increments the damaged counter by a specified amount.
     *
     * @param amount The amount to increment the damaged counter by
     */
    public void incrementDamagedCounter(float amount) {
        damagedCounter += amount;
    }

    /**
     * Resets the damaged counter.
     */
    public void resetDamagedCounter() {
        damagedCounter = 0;
    }

    /**
     * Sets the player's damaged status.
     *
     * @param state The state to set for the player's damaged status
     */
    public void setDamaged(boolean state) {
        damaged = state;
    }

    /**
     * Gets the player's current shooting timer.
     *
     * @return The player's shooting timer.
     */
    public float getShootingTimer() {
        return shootingTimer;
    }

    /**
     * Increments the shooting timer by a specified amount.
     *
     * @param amount The amount to increment the shooting timer by.
     */
    public void incrementShootingTimer(float amount) {
        shootingTimer += amount;
    }

    /**
     * Resets the shooting timer.
     */
    public void resetShootingTimer() {
        shootingTimer = 0;
    }

    /**
     * Sets the player's shooting state.
     *
     * @param state The state to set for the player's shooting status
     */
    public void setShooting(boolean state) {
        shooting = state;
    }

    /**
     * Gets the player's shooting status.
     *
     * @return True if the player is shooting, false otherwise
     */
    public boolean isShooting() {
        return shooting;
    }

    /**
     * Gets the array of possible states the player can be in.
     *
     * @return The states array
     */
    public String[] getStatesArray() {
        return statesArray;
    }

    /**
     * Returns the animation timer.
     *
     * @return the player's animation timer
     */
    public float getAnimationTimer() {
        return animationTimer;
    }

    /**
     * Increments the animations timer.
     *
     * @param time the amount of time to increment the animation timer by
     */
    public void incrementAnimationTimer(float time) {
        animationTimer += time;
    }

    /**
     * Resets the animations timer.
     */
    public void resetAnimationTimer() {
        animationTimer = 0;
    }

    /**
     * Set the player's current state.
     *
     * @param state The current state to be set for the player
     */
    public void setCurrentState(String state) {
        currentState = state;
    }

    /**
     * Set the player's previous state
     *
     * @param state The previous state to be for the player
     */
    public void setPreviousState(String state) {
        previousState = state;
    }

    /**
     * Gets the player's current state.
     *
     * @return The player's state
     */
    public String getCurrentState() {
        return currentState;
    }

    /**
     * Gets the player's previous state.
     *
     * @return The player's state.
     */
    public String getPreviousState() {
        return previousState;
    }

    /**
     * Gets the player's texture atlas
     *
     * @return The player's texture atlas
     */
    public TextureAtlas getEntityAtlas() {
        return GameManager.getManagerFromInstance(TextureManager.class).getAtlas(entityAtlasString);
    }

    public void setProjectileReady(boolean projectileReady) {
        this.projectileReady = projectileReady;
    }

    public int getOffenceMax() {
        return offenceMax;
    }

    public int getDefenceMax() {
        return defenceMax;
    }

    public Keybindings getControls(){
        return controls;
    }

    /**
     * Get the inventory of the player
     *
     * @return the inventory of the player
     */
    public Inventory getInventory() {
        return playerInventory;
    }

    /**
     * Get the recycle of the player
     *
     * @return the recycle of the player
     */
    public Recycle getRecycle() {
        return playerRecycle;
    }

    /**
     * Add a amount into the scrap,
     * the amount of scrap after change need equal or greater than 0
     *
     * @param amount the number that will add into the scrap
     * @return false if the scrap after change is lower than 0, the change will not take effect, otherwise true
     */
    public boolean changeScrap(int amount) {
        if ((scrap + amount) >= 0) {
            scrap += amount;
            return true;
        } else {
            return false;
        }
    }

    /**
     * get the number of the scrap that user have
     *
     * @return the number of the scrap that user have
     */
    public int getScrap() {
        return scrap;
    }

    /**
     * The ability for the player to attack the enemies
     *
     * @return the amount of the damage dealt
     */
    @Override
    public int attack() {
        return (int) (Math.random() * (offenceMax - OFFENCE_MIN + 1)) + OFFENCE_MIN;
    }

    /**
     * The ability for the player to defend themselves
     *
     * @return the amount of the damage fended off
     */
    @Override
    public int defend() {
        return (int) (Math.random() * (defenceMax - DEFENCE_MIN + 1)) + DEFENCE_MIN;
    }

    /**
     * The ability for the player to block assaults
     *
     * @return the amount of damage blocked
     */
    @Override
    public int dodge() {
        return Integer.MAX_VALUE;
    }

    /**
     * Applies a movement multiplier to the player.
     *
     * @param sprintMultiplier Multiplier to be applied to the player's movement speed
     */
    public void sprint(float sprintMultiplier) {
        float currentVelRow = velocity.getRow();
        float currentVelCol = velocity.getCol();
        float sprint = sprintMultiplier;

        // Check if player has energy, if not set sprint multiplier to 1
        if (getPlayerEnergy() <= 0) {
            sprint = 1;
        }

        // Check player's horizontal movement direction and apply sprint multiplier accordingly
        if (currentVelRow < 0) {
            velocity.setRow(sprint * -currentSpeed);
        } else if (currentVelRow > 0) {
            velocity.setRow(sprint * currentSpeed);
        }

        // Check player's vertical movement direction and apply sprint multiplier accordingly
        if (currentVelCol < 0) {
            velocity.setCol(sprint * -currentSpeed);
        } else if (currentVelCol > 0) {
            velocity.setCol(sprint * currentSpeed);
        }
    }

    /**
     * Checks if the player is moving.
     *
     * @return True if the player is moving, false otherwise
     */
    public boolean isMoving() {
        return getVelocity().getCol() != 0 || getVelocity().getRow() != 0;
    }

    /**
     * Checks if the player is moving diagonally.
     *
     * @return True if the player is moving diagonally, false otherwise
     */
    public boolean isMovingDiagonal() {
        return getVelocity().getCol() != 0 && getVelocity().getRow() != 0;
    }

    /**
     * Checks if the player is sprinting.
     *
     * @return True if the player is sprinting, false otherwise
     */
    public boolean isSprinting() {
        return sprinting;
    }

    /**
     * Moves the player.
     *
     * @param velocity The direction to move the player towards
     */
    public void move(HexVector velocity) {
        if (!isInVehicle()) {
            position = position.add(velocity);
        } else if (getVehicle() != null) {
            getVehicle().move(velocity);
        }
    }

    /**
     * Method that is responsible for the player's movement and collision handling
     */
    public void playerMovement() {
        if (isMoving()) {
            if (isSprinting() && !inVehicle && !isShooting()) {
                // Check if player is sprinting into a wall
                if (simpleTileCollisionCheck(getVelocity().getCol(), getVelocity().getRow())) {
                    increaseEnergy(1);
                } else {
                    // Applies efficiency skill to energy usage
                    if (playerSkills.hasLearnt(playerSkills.getSkill("utilityEfficiency"))) {
                        decreaseEnergy(1);
                    } else {
                        decreaseEnergy(2);
                    }
                }

                sprint(2);
            } else {
                sprint(1);
                // increase energy if not sprinting
                increaseEnergy(1);
            }

            // is the player walking diagonally? then reduce their speed by sqrt(2)
            if (isMovingDiagonal()) {
                getVelocity().setRow(getVelocity().getRow() / 1.414f);
                getVelocity().setCol(getVelocity().getCol() / 1.414f);
            }

            // check for collision in Y direction
            if (!simpleTileCollisionCheck(0, getVelocity().getRow())) { // move vertically if no collision
                HexVector velY = new HexVector(0, getVelocity().getRow());
                move(velY);
            }

            // check for collision in X direction
            if (!simpleTileCollisionCheck(getVelocity().getCol(), 0)) { // move horizontally if no collision
                HexVector velX = new HexVector(getVelocity().getCol(), 0);
                move(velX);
            }
        } else {
            // player not moving replenish energy
            increaseEnergy(1);
        }
    }

    /**
     * Checks and performs player movement on the grid every tick
     * using the player's velocity and coordinates.
     *
     * @param i the tick value
     */

    @Override
    public void onTick(long i) {
        if (isAlive()) {
            playerMovement();
            // Implementing unkillable skill here
            /*if (playerSkills.hasLearnt(playerSkills.getSkill("defenceUnkillable")) &&
                    (getHealth() < getMaxPlayerHealth())) {
                gainHealth((int) (getMaxPlayerHealth() * 0.01));
            }*/
        }

        if (this.getTask() != null && this.getTask().isAlive()) {
            this.getTask().onTick(i);
            if (this.getTask().isComplete()) {
                this.setTask(null);
            }
        }
        checkPoison();
    }

    /**
     * Gets coordinates from a hex vector position and converts to the
     * coordinates of the tile they relate to and determines if the
     * coordinates belong to a tile that cannot be walked on.
     *
     * @param position a hex vector containing a row and column value (the
     *                 coordinates the player is moving to)
     * @return true if the coordinates are of a non-walkable tile, false otherwise.
     */
    public boolean tileCollision(HexVector position){

        float[] nextCoords;

        //return true if next tile is null (player has reached the map border)
        nextCoords = getPosTile(position.getCol(), position.getRow(), 0);
        Tile nextTile1 = GameManager.get().getWorld().getTile(nextCoords[0], nextCoords[2]);
        Tile nextTile2 = GameManager.get().getWorld().getTile(nextCoords[0], nextCoords[3]);

        if (nextTile1 == null || nextTile2 == null){
            return true;
        }

        //if the next tile is not null, then round coords again with offset
        nextCoords= getPosTile(position.getCol(), position.getRow(), 0.25f);

        //return false if it is walkable (no collision), and true otherwise (there is a collision with a used tile)
        return !WorldUtil.isWalkable(nextCoords[0], nextCoords[2]) || !WorldUtil.isWalkable(nextCoords[0],
                nextCoords[3]);
    }

    /**
     * Gets the next tile that the player is going to walk on and checks if it is obstructed.
     *
     * @param velocityCol the player's horizontal velocity
     * @param velocityRow the player's vertical velocity
     * @return true if the next tile is obstructed or is null, false otherwise.
     */
    public boolean simpleTileCollisionCheck(float velocityCol, float velocityRow) {
        Tile nextTile = originalGetPosTile(velocityCol, velocityRow - ROW_VISUAL_OFFSET); // shift row offset to match collisions visually
        if (nextTile == null) {
            return true;
        } else {
            return nextTile.isObstructed();
        }
    }

    /**
     * Takes the row and column values of a position and rounds the column and row values
     * so that we can get an array of coordinates that can be used to identify the tile
     * that those coordinates link to and the upper and lower tile coordinate if the
     * original coordinate does not match the tile coordinate exactly for when the player
     * is moving between tiles.
     *
     * @param posRow a floating point row value from a position
     * @param posCol a floating point column value from a position
     * @param offSet a floating point that is used to determine how far a player can walk on an occupied tile
     *
     * @return An array of rounded coordinates
     */
    public float[] getPosTile(float posCol, float posRow, float offSet) {

        float roundedCol;
        float colLeft;
        float colRight;

        float roundedRow;
        float topRow;
        float bottomRow;

        float offSetOdd = offSet + 0.5f;

        //round the next column to col of tile
        roundedCol = (float) Math.round(posCol);
        colLeft = (float) Math.floor(posCol);
        colRight = (float) Math.ceil(posCol);

        roundedRow = (float) Math.round(posRow);

        //get top and bottom row boundaries of next coords and convert to tile coords
        if (roundedCol % 2 == 0) {
            topRow = (float) Math.ceil(posRow - offSet);
            bottomRow = (float) Math.floor(posRow + offSet);
        } else {
            topRow = (float) Math.ceil(posRow - offSetOdd);
            bottomRow = (float) Math.floor(posRow + offSetOdd);

            roundedRow+=0.5f;
            topRow += 0.5f;
            bottomRow -= 0.5f;

        }
        return new float[]{roundedCol, roundedRow, topRow, bottomRow, colLeft, colRight};
    }

    /**
     * Original version of the getPosTile method, with some adjustments to fix various issues not caught the first time.
     * Gets the current tile that is under the player's position. It gets the current player's position
     * and rounds the column and row values so that we can get the closest tile to the player. The column
     * and row offset values allow look-ahead tile getting.
     *
     * @param colOffset column offset
     * @param rowOffset row offset
     * @return The tile closest to the player, with regards to the offsets
     */
    public Tile originalGetPosTile(float colOffset, float rowOffset) {
        float roundedCol = Math.round(getCol() + colOffset);
        float roundedRow;

        // java can return negative numbers after modulus, hence the extra check
        // we need two different ways of rounding the row value due to the tile layout not allowing only one
        // rounding method
        if (roundedCol % 2 == 1 || roundedCol % 2 == -1) {
            roundedRow = (float) Math.floor(getRow() + rowOffset) + 0.5f;
        } else {
            roundedRow = Math.round(getRow() + rowOffset);
        }

        return GameManager.get().getWorld().getTile(roundedCol, roundedRow);
    }

    // Called by the multi-player client PlayerPeon as the Screen-to-World
    // coordinates calculation is done on the
    // client machine as it is done based on the position of the camera of the host.
    public void notifyTouchDown(float[] clickedPosition, int pointer, int button) {
        //
    }

    @Override
    public void notifyKeyDown(int keycode) {
        if (!GameManager.getManagerFromInstance(OnScreenMessageManager.class).isTyping() && isAlive()) {
            if (keycode == controls.getKey("MoveUp")) {
                this.setTask(null);
                velocity.setRow(currentSpeed);
            }

            if (keycode == controls.getKey("MoveDown")) {
                this.setTask(null);
                velocity.setRow(-currentSpeed);
            }

            if (keycode == controls.getKey("MoveLeft")) {
                this.setTask(null);
                velocity.setCol(-currentSpeed);
            }

            if (keycode == controls.getKey("MoveRight")) {
                this.setTask(null);
                velocity.setCol(currentSpeed);
            }

            if (keycode == controls.getKey("Sprint")) {
                sprinting = true;
            }
        }
    }

    @Override
    public void notifyKeyUp(int keycode) {
        if (!GameManager.getManagerFromInstance(OnScreenMessageManager.class).isTyping()) {
            if (keycode == controls.getKey("Sprint")) {
                sprinting = false;
            }

            if (keycode == controls.getKey("MoveUp") || keycode == controls.getKey("MoveDown")) {
                velocity.setRow(0);
            }

            if (keycode == controls.getKey("MoveLeft") || keycode == controls.getKey("MoveRight")) {
                velocity.setCol(0);
            }

            if (keycode == controls.getKey("Vehicle")) {
                if (!inVehicle) {
                    VehicleEntity vehicle = WorldUtil.findVehicle(this);
                    if (vehicle != null) {
                        inVehicle = vehicle.enterVehicle(this);
                    }
                } else {
                    if (this.vehicle != null) {
                        vehicle.driver.eject();
                    }
                }
            }
        }
    }

    @Override
    public void notifyMouseMoved(int screenX, int screenY) {
        setPlayerRotation(screenX, screenY);
    }

    /**
     * Sets the player's rotation to wherever the mouse cursor is on screen.
     *
     * @param screenX X-coordinate of the mouse cursor
     * @param screenY Y-coordinate of the mouse cursor
     */
    public void setPlayerRotation(int screenX, int screenY) {
        if (!isAlive()) {
            return;
        }

        float[] mouse = WorldUtil.screenToWorldCoordinates(screenX, screenY);
        float[] mousePosition = WorldUtil.worldCoordinatesToColRowUnrounded(mouse[0], mouse[1]);
        setPlayerRotation(mousePosition[0], mousePosition[1]);
    }

    public void setPlayerRotation(float col, float row) {
        float x = col - getCol();
        float y = row - getRow();
        float angle = (float) Math.toDegrees(Math.atan(y / x));

        if (y >= 0 && x >= 0) {
            angle = 360 - (90 - angle);
        } else if (y >= 0 && x <= 0 || y <= 0 && x <= 0) {
            angle += 90;
        } else if (y <= 0 && x >= 0) {
            angle += 270;
        }

        rotation = angle;
    }

    public void removeControls() {
        GameManager.getManagerFromInstance(InputManager.class).removeKeyUpListener(this);
        GameManager.getManagerFromInstance(InputManager.class).removeKeyDownListener(this);
        GameManager.getManagerFromInstance(InputManager.class).removeMouseMovedListener(this);
    }

    public void createInventory() {
        playerInventory = new Inventory(8);
        playerRecycle = new Recycle();
    }

    public void resetMovementTask() {
        if (this.getTask() instanceof MovementTask) {
            MovementTask resTask = (MovementTask)this.getTask();
            resTask.reset(this, new HexVector(0, 0));
        }
    }

    @Override
    public int getHealth() {
        return health;
    }

    @Override
    public void loseHealth(int lossHealth) {
        logger.info("Player {} lost {} health", getEntityID(), lossHealth);
        this.health -= lossHealth;
        setDamaged(true);
        if (health <= 0) {
            death();
        }
    }

    @Override
    public void gainHealth(int gainHealth) {
        this.health += gainHealth;
    }

    @Override
    public void gainFullHealth() {
        health = this.getMaxPlayerHealth();
    }

    @Override
    public void death() {
        health = 0;
        setAlive(false);
    }

    /**
     * Gets the player's living status.
     *
     * @return True if the player is alive, false othewise
     */
    public boolean isAlive() {
        return alive;
    }

    /**
     * Gets the player's damaged status.
     *
     * @return True if the player was recently damaged, false otherwise
     */
    public boolean isDamaged() {
        return damaged;
    }

    /***
     * Get the player's maximum health
     * @return player maximum health
     */
    public int getMaxPlayerHealth() {
        return maxHealth;
    }

    /***
     * Get the player's maximum energy
     * @return player maximum energy
     */
    public int getMaxPlayerEnergy() {
        return MAX_ENERGY;
    }

    /***
     * Get the player's energy
     * @return player energy
     */
    public int getPlayerEnergy() {
        return energy;
    }

    /***
     * Decrease the player's energy
     * @param amount - amount to decrease energy by
     */
    public void decreaseEnergy(int amount) {
        // Decrease energy level
        energy -= (float)amount;

        // Cap minimum energy level at 0
        if (energy <= 0) {
            energy = 0;
        }
    }

    /***
     * Increase the player's energy
     * @param amount - amount to increase the energy by
     */
    public void increaseEnergy(int amount) {
        // increase energy level
        energy += amount;

        // cap energy level at 100
        if (energy >= MAX_ENERGY) {
            energy = MAX_ENERGY;
        }
    }

    /**
     * Helper method for camera positioning to get a pointer to the crafting table.
     *
     * @return a pointer to the CraftingTable CraftingTable
     */
    private CraftingTable getCraftingTable() {

        AbstractWorld world = GameManager.get().getWorld();
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

    public void setAttributes(String textureAtlas, String classType){
        /*PlayerAttributes pa = new PlayerAttributes(getRow(),getCol(),
                getSpeed(),classType);*/
        PlayerAttributes pa = new PlayerAttributes(getRow(),getCol(),getSpeed(),textureAtlas,classType);
        this.maxHealth = pa.getStats("health");
        this.health = pa.getStats("health");
        this.defenceMax = pa.getStats("defence");
        this.offenceMax = pa.getStats("attack");
        this.setTexture(pa.getPlayerClassType());
        int speedVal = pa.getStats("speed");
        System.out.println(currentSpeed);
        if(speedVal > 10){
            float sprint = (float)speedVal/10f;
            float newSpeed = this.speed * sprint;
            System.out.println("new speed"+newSpeed);
            setSpeed(newSpeed);
            System.out.println("currentSpeed"+this.speed);
            this.currentSpeed = this.speed;
            System.out.println(currentSpeed);
        }
    }

    public void addProjectile() {
        projectile.setCol(this.position.getCol());
        projectile.setRow(this.position.getRow());
        GameManager.get().getWorld().addEntity(projectile);
        this.setHasProjectile(true);
    }

    public void shootProjectile(int screenX, int screenY) {
        float[] mouse = WorldUtil.screenToWorldCoordinates(screenX, screenY);
        float[] clickedPosition = WorldUtil.worldCoordinatesToColRow(mouse[0], mouse[1]);
        HexVector targetPosition = new HexVector(clickedPosition[0],clickedPosition[1]);
        projectile.setTask(new MovementTask(projectile, targetPosition));
    }

    public void explodeProjectile() {
        projectile.setTask(null);
        GameManager.get().getWorld().removeEntity(projectile);
        this.setHasProjectile(false);
    }

    public Projectile getProjectile() {
        return this.projectile;
    }

    public SkillTreev2 getPlayerSkills() {
        return this.playerSkills;
    }

    public void setOffenceMax(int offenceMax) {
        this.offenceMax = offenceMax;
    }

    public void setDefenceMax(int defenceMax) {
        this.defenceMax = defenceMax;
    }

    public void addConsumable(ActiveItem consumable) {
        activeConsumables.add(consumable);
    }

    /**
     * Removes consumable items when their time runs out.
     */
    private void checkConsumables() {
        for (ActiveItem item : activeConsumables) {
            if ((System.currentTimeMillis() - item.getTimeUsed()) / 1000 / 60 >= 5) {
                // If endless items skill is unlocked then has 25% chance to retain consumable item
                if (playerSkills.hasLearnt(playerSkills.getSkill("utilityEndlessItems"))) {
                    int random = rand.nextInt(4) + 1; // creates a number between 1 and 4 inclusive
                    if (random != 1) {
                        activeConsumables.remove(item);
                    }
                } else { // Otherwise just removes the item
                    activeConsumables.remove(item);
                }
                item.revertNormal();
            }
        }
    }

    public List<ActiveItem> getActiveConsumables() {
        return activeConsumables;
    }
}

