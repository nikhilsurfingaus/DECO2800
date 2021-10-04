package deco.combatevolved.entities.staticentities.defensivetowers;
import com.badlogic.gdx.graphics.Texture;
import deco.combatevolved.entities.Poison;
import deco.combatevolved.entities.RenderConstants;
import deco.combatevolved.entities.dynamicentities.DynamicEntity;
import deco.combatevolved.entities.enemyentities.DesertEnemy;
import deco.combatevolved.entities.enemyentities.EnemyPoison;
import deco.combatevolved.managers.GameManager;
import deco.combatevolved.managers.TaskPool;
import deco.combatevolved.tasks.AbstractTask;
import deco.combatevolved.tasks.TowerTargetTask;
import deco.combatevolved.tasks.TowerTaskParameters;
import deco.combatevolved.worlds.Tile;
import deco.combatevolved.entities.enemyentities.BasicEnemy;
import deco.combatevolved.entities.HasHealth;
import deco.combatevolved.entities.dynamicentities.HealthBar;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Defensive Tower in the game.
 *
 * <p>A Tower has at least a name, health, range, attack speed and does damage.
 */
public abstract class Tower extends DynamicEntity implements HasHealth, Poison {
    protected transient AbstractTask task;
    //Name of tower, damage dealt by tower and towers health
    private int health;
    private int maxHealth;
    private int damage;
    private int range;
    private float attackSpeed;
    private String towerName;
    private int targetId = -1;
    private boolean rotate;
    private boolean destroyed = false;
    private boolean targetFound = false;
    private long lastShot;
    private List<DesertEnemy> poisonEnemies = new ArrayList<>();
    private List<EnemyPoison> poison = new ArrayList<>();
    private long timeFrozen;
    private HealthBar healthBar;
    private String suffix;

    /**
     * @param towerName Name of the tower
     * @param health health the tower has
     * @param damage damage the tower does to enemies, if none then
     * @param range range of the tower
     * @param attackSpeed attacks of tower per second
     * @param tile tile that tower is being placed
     * @param obstructed whether it is visible
     * @param rotate is the tower is a rotatable tower
     */
    public Tower(String towerName, int health, int damage, int range, float attackSpeed, Tile tile, boolean obstructed, boolean rotate){

        this.towerName = towerName;
        this.health = health;
        this.maxHealth = health;
        this.damage = damage;
        this.range = range;
        this.attackSpeed = attackSpeed;
        this.rotate = rotate;
        this.suffix = "_D";

        //Sets the initial rotation to downward
        if (this.rotate){
            this.setTexture(towerName + suffix);

        } else {
            this.setTexture(towerName);
        }



        checkNegatives();

        try {
            this.healthBar = new HealthBar(this, new Texture("resources/enemies/enemyhealthbg.png"),
                    new Texture("resources/enemies/enemyhealthfg.png"));
        } catch (Exception ignored) {

        }
    }

    /** Gets the health bar
     *
     * @return healthbar
     */
    public HealthBar getHealthBar() {
        return healthBar;
    }

    /**Getter for the suffix
     *
     * @return suffix -  at string of the direction the tower is pointing
     */
    public String getSuffix() {return suffix;}

    /**Sets the suffix
     *
     * @param suffix
     */
    public void setSuffix(String suffix) {this.suffix = suffix;}

    /**
     * @param towerName Name of the tower
     * @param health health the tower has
     * @param damage damage the tower does to enemies, if none then
     * @param range range of the tower
     * @param attackSpeed attacks of tower per second
     * @param col column index of tile
     * @param row row index of tile
     * @param rotate is the tower is a rotatable tower
     */
    public Tower(String towerName, int health, int damage, int range, float attackSpeed,
                 float col, float row, boolean rotate) {

        super(col, row, RenderConstants.TOWER_RENDER, 0);

        this.towerName = towerName;
        this.health = health;
        this.maxHealth = health;
        this.damage = damage;
        this.range = range;
        this.attackSpeed = attackSpeed;
        this.rotate = rotate;

        //Sets the initial rotation to point downward
        if (this.rotate){
            this.setTexture(towerName+ "_D");
        } else {
            this.setTexture(towerName);
        }

        try {
            this.healthBar = new HealthBar(this, new Texture("resources/enemies/enemyhealthbg.png"),
                    new Texture("resources/enemies/enemyhealthfg.png"));
        } catch (Exception ignored) {

        }

        checkNegatives();
    }

    public void checkPoison() {
        for (EnemyPoison enemyPoison : poison) {
            if(enemyPoison.getTime() > 1000) {
                dealPoisonDamage(enemyPoison);
            }
        }
    }

    public int getMaxHealth() {
        return maxHealth;
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

    public void checkNegatives() {
        //Cant have negative values for stats, sets them to the minimum allowed
        if (this.damage < 0){
            this.damage = 0;
        }
        if (this.health < 0){
            this.health = 0;
        }

        if (this.range <= 0){
            this.range = 1;
        }
        if (this.attackSpeed <= 0){
            this.attackSpeed = 0.1f;
        }
    }

    /**
    *Getter method to return name of the tower
    *
    * @return String name of the tower
    */
    public String getTowerName(){
        return  this.towerName;
    }

    /**
     *Getter method to return health of the tower
     *
     * @return int health of tower
     */
    public int getHealth(){
        return this.health;
    }

    /**
     *Getter method to return the damage dealt by tower, can be zero if no
     * damage or negative damage.
     *
     * @return int damage done by tower
     */
    public int getDamage(){
        return this.damage;
    }

    /**
     *Getter method to attack range of the tower.
     *
     * @return int range of tower (in tiles)
     */
    public int getRange(){
        return this.range;
    }

    /**
     * Getter method for attack speed in attacks per second
     * @return float of attackspeed
     */
    public float getAttackSpeed(){
        return  this.attackSpeed;
    }


    /**
     * Getter method
     * @return whether a target has been found or not
     */
    public boolean wasTargetFound() {
        return targetFound;
    }

    /**
     * Getter method
     * @return whether a the tower is a rotatable tower
     */
    public boolean isRotatable() {
        return rotate;
    }

    /**
     *Setter method to set the health of tower
     */
    public void setHealth(int health){
        this.health = health;
        updateImage();
        if (health <= 0) {
            death();
        }
    }

    /**
     *  Update the texture based on how damaged the tower is
     */
    protected void updateImage() {
        // Work out how damaged this tower is as a percentage of maxHealth
        float p = 1 - (float) this.health / this.maxHealth;
        String suffix = "";
        if (p > 0.9) {
            suffix = "_damage2";
        } else if (p >0.7) {
            suffix = "_damage1";
        } else {
            suffix = "";
        }
        setTexture(this.towerName+suffix);
//        healthBar.update();
    }

    /**
     *Setter method to set the damage dealt by tower
     */
     public void setDamage(int damage){
        this.damage = damage;
        checkNegatives();
    }

    /**
     *Setter method to set the range of the tower
     */
    public void setRange(int range){
        this.range = range;
        checkNegatives();
    }

    /**
     *Setter method to set the attacks per second of the tower
     */
    public void setAttackSpeed(float attackSpeed){
        this.attackSpeed = attackSpeed;
        checkNegatives();
    }

    /**
     *Update the towers health after being hit by an enemy
     *
     * @param enemyDamage damage done by the enemy
     */
    public void takeDamage(int enemyDamage) {
        //need the damage to be non-negative
        if (enemyDamage >= 0) {
            this.health = this.health - enemyDamage;
        }
        if (this.health < 0) {
            this.health  = 0;
            death();
        }
        // damage images not yet available
        // updateImage();
    }

    /**Deals an amount of damage to the tower
     *
     * @param lossHealth Amount the enemy loses health
     */
    public void loseHealth(int lossHealth) {
        takeDamage(lossHealth);
    }

    /**Heals the tower a certain amount
     *
     * @param gainHealth Amount the entity loses health
     */
    public void gainHealth(int gainHealth) {
        health += gainHealth;
        updateImage();
    }

    /**Freezes the tower - making it not attack
     *
     */
    public void setFreezeTime() {
        this.timeFrozen = System.currentTimeMillis();
    }

    /**Gets when the tower was last frozen
     *
     * @return long of freeze time in milliseconds
     */
    public long getFreezeTime() {
        return this.timeFrozen;
    }

    /**Resets a towers health to full
     *
     */
    public void gainFullHealth() {
        setHealth(maxHealth);
    }

    /**Death method simply removes the tower entity from the world
     *
     */
    public void death() {
        //Removes from the manager list of living towers
        GameManager.get().getManager(TowerDefenseManager.class).removeTower(this);
        //Removes from entity list in world
        GameManager.get().getWorld().removeEntity(this);

    }

    /**A method used to check if the tower is dead
     *
     * @return true if dead, false if alive
     */
    public boolean towerDestroyed() {
        if (this.health <= 0) {
            this.destroyed = true;
            return true;
        }
        return this.destroyed;
    }

    /**
     * checks if a tower is frozen
     * @return true if frozen
     */
    public boolean isFrozen() {
        if ((System.currentTimeMillis() < this.timeFrozen + 4000)) {
            return true;
        }
        return false;
    }

    /** Method currently finds a target (enemy) and then attacks it until it has 0 health
     *
     * @param i
     */
    @Override
    //TODO - reset targetting and attacks each time rather than creating them again - more efficient
    public void onTick(long i) {
        checkPoison();
        if (!isFrozen()) {
            //Uses aim method to rotate to aim at whatever enemy it is attacking
            if (targetId != -1 && rotate && (GameManager.get().getWorld().getEntityById(targetId) != null)) {
                TowerTargetTask.aim((BasicEnemy) GameManager.get().getWorld().getEntityById(targetId), this);
            }

            if (System.currentTimeMillis() - lastShot > this.getAttackSpeed()*1000) {

                lastShot = System.currentTimeMillis();

                //Targets an enemy if no other tasks are set
                if (task == null) {
                    task = GameManager.getManagerFromInstance(TaskPool.class).getTowerTask(new TowerTaskParameters(this));
                }

                //Continues if task is not finished
                if (task.isAlive()) {
                    task.onTick(i);

                    if (task.isComplete()) {

                        //Attacks an enemy if already targeted one
                        if (task instanceof TowerTargetTask) {
                            targetId = ((TowerTargetTask) task).getTarget().getEntityID();
                            if (targetId == -1) {
                                return;
                            }
                            if (GameManager.get().getWorld().getEntityById(targetId) == null) {
                                return;
                            }
                            if (rotate) {
                                TowerTargetTask.aim((BasicEnemy) GameManager.get().getWorld().getEntityById(targetId),
                                        this);
                            }
                            task = GameManager.getManagerFromInstance(TaskPool.class).getTowerTask(
                                    new TowerTaskParameters(this,
                                            (BasicEnemy) GameManager.get().getWorld().getEntityById(targetId)));

                            //Finds a new enemy if just killed one
                        } else {
                            task = GameManager.getManagerFromInstance(TaskPool.class).getTowerTask(new TowerTaskParameters(this));
                            if (targetId == -1) {
                                return;
                            }
                            if (GameManager.get().getWorld().getEntityById(targetId) == null) {
                                return;
                            }
                            if (rotate) {
                                TowerTargetTask.aim((BasicEnemy) GameManager.get().getWorld().getEntityById(targetId),
                                        this);
                            }
                        }
                    }
                }
            }
        }
    }

}