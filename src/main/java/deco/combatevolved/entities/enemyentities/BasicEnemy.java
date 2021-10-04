package deco.combatevolved.entities.enemyentities;

import com.badlogic.gdx.graphics.Texture;
import deco.combatevolved.Tickable;
import deco.combatevolved.entities.*;
import deco.combatevolved.entities.dynamicentities.DynamicEntity;
import deco.combatevolved.entities.dynamicentities.HealthBar;
import deco.combatevolved.entities.dynamicentities.PlayerAttributes;
import deco.combatevolved.entities.dynamicentities.PlayerPeon;
import deco.combatevolved.exceptions.EnemyValueException;
import deco.combatevolved.factories.EnemyEntityFactory;
import deco.combatevolved.managers.EnemyManager;
import deco.combatevolved.managers.GameManager;

import deco.combatevolved.managers.SoundManager;

import deco.combatevolved.tasks.AbstractTask;
import deco.combatevolved.tasks.MovementTask;
import deco.combatevolved.util.HexVector;
import deco.combatevolved.worlds.biomes.BiomeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An BasicEnemy is an entity that tracks towards and damages the player and
 * has health.
 */
public class BasicEnemy extends DynamicEntity implements Tickable, HasHealth,
        CausesDamage {
    // Logger for the enemy
    private final Logger logger = LoggerFactory.getLogger(BasicEnemy.class);

    // Task of the enemy
    protected transient AbstractTask task;

    // All the enemies stats
    protected int damage;
    protected int health;
    protected int armourHealth;
    protected int maxHealth;
    private final int maxArmourHealth;
    protected int exp;

    // AI of the enemy
    protected transient ArtificialIntelligence ai;
    // Time of the last attack
    protected long lastAttack;
    // Delay before the next attack
    private long attackDelay;
    // The last time the enemy is slow
    private long lastSlow;
    // The home biome of the enemy
    protected String homeBiome;
    // whether the biome should be included in the texture
    protected boolean includeBiome;
    // The type of enemy
    protected String enemyType;
    // The angle of the enemy
    protected double angle;
    // The last time the enemies were damaged
    private long lastDamaged;

    // The health bar
    private HealthBar healthBar;

    // The sound effects
    protected String moveSound;
    protected String attackSound;
    protected String damageSound;
    protected String deathSound;

    protected AbstractTask oldMoveTask;
    protected long lastMoveSound;


    /**
     *  Constructs a basic enemy that resembles a lizard
     *  Abilities: None
     * @param row the row coordinate of the enemy
     * @param col the row coordinate of the enemy
     */
    public BasicEnemy(float col, float row) {
        super(col, row, RenderConstants.PEON_RENDER, 0.05f);
        this.setZ(1);
        this.speed = 0.05f;
        this.damage = 5;
        this.health = 70;
        this.maxHealth = this.health;
        this.armourHealth = 0;
        this.maxArmourHealth = this.armourHealth;
        this.exp = 5;

        this.ai = new EnemyArtificialIntelligence(getPlayer());

        this.lastAttack = 0;
        attackDelay = 3000;
        this.homeBiome = getHomeBiome();
        enemyType = "Basic_Lizard";
        includeBiome = true;


        moveSound = "Lizard_Movement.mp3";
        attackSound = "Lizard_Attack.mp3";
        damageSound = "Lizard_Damage.mp3";
        deathSound = "Lizard_Death.mp3";

        oldMoveTask = null;
        lastMoveSound = System.currentTimeMillis() - 2000;


        try {
            this.healthBar = new HealthBar(this, new Texture("resources/enemy_health_bar_bg.png"),
                    new Texture("resources/enemy_health_bar_fg.png"));
        } catch (Exception e) {
            logger.error("ENEMY HELTHBAR NOT SET!", e);
        }
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    /**
     *  Get the health bar
     * @return The HealthBar
     */
    public HealthBar getHealthBar() {
        return healthBar;
    }

    /**
     * Determines what the enemy will do on every frame
     * @param i
     */
    @Override
    public void onTick(long i) {
        AbstractTask newTask = getTask(i);
        setEnemyTexture(newTask);
    }

    /**
     *  Plays the move sound for the enemy
     * @param task
     */
    public void play_move_sound(AbstractTask task) {
        if(task instanceof MovementTask && !task.equals(oldMoveTask)
                && ((lastMoveSound + 2000) < System.currentTimeMillis())) {
            lastMoveSound = System.currentTimeMillis();
            GameManager.get().getManager(SoundManager.class).playEnemySound(moveSound);
            oldMoveTask = task;
        }
    }

    /**
     * Plays the attack sound of the enemy
     */
    public void play_attack_sound() {
        GameManager.get().getManager(SoundManager.class).playEnemySound(attackSound);
    }

    /**
     * Sets the texture of the enemy
     * @param task
     */
    public void setEnemyTexture(AbstractTask task) {
        if(task instanceof MovementTask) {
            try {
                setAngle(getPosition(),((MovementTask) task).getPath().get(0).getCoordinates());
            } catch (NullPointerException | IndexOutOfBoundsException e) {
                //Keep the same angle as before if there is no path to the target
            }
        }
        setTextureByAngle(this.angle);
    }

    /**
     *  Gets the task of the enemy for that tick
     * @param i
     * @return AbstractTask
     */
    public AbstractTask getTask(long i) {
        AbstractTask newTask = ai.getTask(this);
        if (newTask != null) {
            if (task != null) {
                task.returnToTaskPool();
            }
            task = newTask;
        }

        if (task != null && task.isAlive()) {
            task.onTick(i);
            if (task.isComplete()) {
                task.returnToTaskPool();
                task = null;
            }
        }
        return task;
    }

    /**
     *  Returns the health of the enemy
     * @return The health of the enemy
     */
    public int getHealth() {
        return this.health;
    }

    /**
     *  Allows the health of the enemy to decrease
     * @param loseHealth Amount the enemy loses health
     * @throws EnemyValueException when negative value is entered
     */
    public void loseHealth(int loseHealth) throws EnemyValueException {
        this.lastDamaged = System.currentTimeMillis();
        if (loseHealth > 0) {
            GameManager.get().getManager(SoundManager.class).playEnemySound(damageSound);
            if (loseHealth > this.armourHealth) {
                loseHealth -= this.armourHealth;
                this.armourHealth = 0;
                if(loseHealth > this.health) {
                    this.health = 0;
                } else {
                    this.health -= loseHealth;
                }
            } else {
                this.armourHealth -= loseHealth;
            }
        } else {
            throw new EnemyValueException();
        }
        if (this.health == 0) {
            GameManager.get().getManager(SoundManager.class).playEnemySound(deathSound);
            this.death();
        }
    }

    /**
     *  Allows the health of the enemy to increase
     * @param gainHealth Amount the enemy loses health
     * @throws EnemyValueException when negative value is entered
     */
    public void gainHealth(int gainHealth) throws EnemyValueException {
        if (gainHealth > 0) {
            if((gainHealth + this.health) > this.maxHealth) {
                gainHealth -= this.maxHealth - this.health;
                this.health = this.maxHealth;
                if ((gainHealth + this.armourHealth) > this.maxArmourHealth) {
                    this.armourHealth = this.maxArmourHealth;
                } else {
                    this.armourHealth += gainHealth;
                }
            } else {
                this.health += gainHealth;
            }
        } else {
            throw new EnemyValueException();
        }
    }

    /**
     *  Allows the health of the enemy to increase to its maximum amount
     */
    public void gainFullHealth() {
        this.health = this.maxHealth;
        this.armourHealth = this.maxArmourHealth;
    }

    /**
     * Removes Enemy from the manager and the world
     */
    public void death() {
        GameManager.get().getManager(EnemyManager.class).removeEnemyFromGame(this);
        GameManager.get().getManager(EnemyManager.class).addExp(this.exp);
        PlayerAttributes player = getPlayer();
        player.increaseExp(this.exp); // Gives player the exp
    }

    /**
     * Displays the hit animation for 0.1 of a second
     * @return a boolean that if true the animation is displayed
     */
    public boolean isDamaged() {
        return System.currentTimeMillis() - lastDamaged <= 100;
    }

    /**
     * Displays the hit animation for 0.1 of a second
     * @return a boolean that if true the animation is displayed
     */
    public boolean hasAttacked() {
        return System.currentTimeMillis() - lastAttack <= 100;
    }

    /**
     *  Returns the amount of damage the enemy can cause
     * @return the damage the enemy can return
     */
    public int getDamage() { return this.damage; }

    /**
     * Returns the range the enemy can attack from
     * @return the range the enemy can attack from
     */
    public float getRange() {
        return 1.5f;
    }

    /**
     *  Returns the health of the enemy armour
     * @return The health of the enemy armour
     */
    public int getArmourHealth() { return this.armourHealth; }

    /**
     * Sets the time of the last attack
     */
    public void setLastAttack() {
        this.lastAttack = System.currentTimeMillis();
    }

    /**
     * Gets the time of the last attack
     * @return long that is the time of the last attack
     */
    public long getLastAttack() {
        return this.lastAttack;
    }

    /**
     * Get the time the enemy waits between each attack
     * @return the delay between attacks for the enemy
     */
    public long getAttackDelay() {
        return attackDelay;
    }

    /**
     * Gets the speed of the enemy
     * @return float that is the speed
     */
    public float getSpeed() { return this.speed; }

    /**
     * Gets the exp of the enemy
     * @return float that is the exp
     */
    public float getExp() { return this.exp; }

    /**
     * Decreases the speed of the enemy
     * @param loseSpeed - amount the enemies speed will decrease
     * @throws EnemyValueException
     */
    public void loseSpeed(float loseSpeed) throws EnemyValueException {
        if (loseSpeed < 0) {
            throw new EnemyValueException();
        }
        this.speed -= loseSpeed;
    }

    /**
     * Increases the speed of the enemy
     * @param gainSpeed - amount the enemies speed will increase
     * @throws EnemyValueException
     */
    public void gainSpeed(float gainSpeed) throws EnemyValueException {
        if (gainSpeed < 0) {
            throw new EnemyValueException();
        }
        this.speed += gainSpeed;
    }

    /**
     * Gets the last time the enemy was slow
     * @return long
     */
    public long getLastSlow(){
        return this.lastSlow;
    }

    /**
     * Sets the time the enemy was slow
     * @param lastSlow - time the enemy is slow
     */
    public void setLastSlow(long lastSlow) {
        this.lastSlow = lastSlow;
    }

    /**
     * Returns the player entity
     * @return player entity
     */

    public PlayerAttributes getPlayer() {
        PlayerAttributes playerAttributes = null;

        for (AbstractEntity entity : GameManager.get().getWorld().getEntities()) {
            if (entity instanceof PlayerPeon) {
                playerAttributes = (PlayerAttributes)entity;
            }
        }
        return playerAttributes;
    }

    /**
     * Gets the biome the enemy spawns in
     * @return A string of the home biome
     */
    public String getHomeBiome() {
        float error = 1.1f;
        return GameManager.get().getWorld().getTile(position, error).getTextureName();
    }

    /**
     * Sets the angle of the direction the enemy will travel in
     * @param position - current position
     * @param oldPosition - next position
     */
    public void setAngle(HexVector position, HexVector oldPosition) {
        this.angle = Math.toDegrees(Math.atan2(position.getRow() - oldPosition.getRow(), position.getCol() - oldPosition.getCol()));
    }

    /**
     * Gets the angle of the enemy
     * @return double that is the angle
     */
    public double getAngle() {
//        logger.trace("direction: {}", this.angle);
        return this.angle;
    }

    /**
     *  Takes an angle and sets the appropriate texture
     * @param angle - direction that enemy will move
     */
    public void setTextureByAngle(double angle){
        String biomeSuffix = getBiomeSuffix();
        String directionSuffix = getDirectionSuffix(angle);

        if (includeBiome) {
            setTexture(enemyType + "_" + biomeSuffix + directionSuffix);
        } else {
            setTexture(enemyType + directionSuffix);
        }
    }

    /**
     * Uses the home biome of the enemy to determine what the biome suffix for
     * its texture should be
     * @return biome suffix for the enemy's texture
     */
    public String getBiomeSuffix() {
        if (homeBiome.contains(BiomeType.SNOW.asLowerCase())
                || homeBiome.contains(BiomeType.ICE.asLowerCase())
                || homeBiome.contains(BiomeType.TUNDRA.asLowerCase())) {
            return "Snow";
        } else if (homeBiome.contains(BiomeType.TEMPERATE_DESERT.asLowerCase())
                || homeBiome.contains(BiomeType.SUBTROPICAL_DESERT.asLowerCase())) {
            return "Desert";
        } else if (homeBiome.contains(BiomeType.GRASSLAND.asLowerCase())) {
            return "Plains";
        } else if (homeBiome.contains(BiomeType.TROPICAL_RAINFOREST.asLowerCase())
                || homeBiome.contains(BiomeType.TEMPERATE_RAINFOREST.asLowerCase())
                || homeBiome.contains(BiomeType.SHRUBLAND.asLowerCase())) {
            return "Forest";
        } else {
            return "Plains";
        }
    }

    /**
     * Uses the angle of the enemy to determine what the direction suffix for
     * its texture should be
     * @param angle the angle the enemy is facing
     * @return the direction suffix for the enemy's texture
     */
    private String getDirectionSuffix(double angle) {
        if (angle >= -180 && angle < -120) {
            return "BR";
        } else if (angle >= -120 && angle < -60) {
            return "BW";
        } else if (angle >= -60 && angle < 0) {
           return "BL";
        } else if (angle >= 0 && angle < 60) {
            return "FL";
        } else if (angle >= 60 && angle < 120) {
            return "FW";
        } else if (angle >= 120 && angle < 180) {
            return "FR";
        }

        return "FR";
    }
}
