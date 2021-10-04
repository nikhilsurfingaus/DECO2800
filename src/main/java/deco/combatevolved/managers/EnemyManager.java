package deco.combatevolved.managers;



import deco.combatevolved.Tickable;
import deco.combatevolved.entities.AbstractEntity;
import deco.combatevolved.entities.BossObserver;
import deco.combatevolved.entities.enemyentities.BasicEnemy;
import deco.combatevolved.entities.enemyentities.HealerEnemy;
import deco.combatevolved.entities.dynamicentities.PlayerPeon;
import deco.combatevolved.entities.items.Item;
import deco.combatevolved.entities.items.resources.Items;
import deco.combatevolved.entities.staticentities.Death;
import deco.combatevolved.entities.staticentities.ItemEntity;
import deco.combatevolved.exceptions.EnemyValueException;
import deco.combatevolved.mainmenu.EndGamePopup;
import deco.combatevolved.observers.ComTowerObserver;
import deco.combatevolved.util.BFSPathfinder;
import deco.combatevolved.util.HexVector;
import deco.combatevolved.worlds.CombatEvolvedWorld;
import deco.combatevolved.worlds.Tile;
import deco.combatevolved.factories.EnemyEntityFactory;
import deco.combatevolved.observers.CycleObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * The EnemyManager manages the spawning of enemies and group properties
 */
public class EnemyManager extends TickableManager implements Tickable, CycleObserver, ComTowerObserver {
    private static final Logger LOGGER = LoggerFactory.getLogger(EnemyManager.class);
    protected int enemyNumber; // current number of enemies
    protected static int enemySpawnTotal; // rate at which enemies spawn
    protected static int enemySpawnNumber; // total amount of enemies that can exist
    private long enemySpawnRate = 5000;
    protected HexVector enemyPosition;
    protected BasicEnemy enemy;
    protected List<BasicEnemy> enemyList = new CopyOnWriteArrayList<>(); //list of enemies
    private long lastEnemySpawn;
    private int currentCycle;
    private List<String> spawnList = new CopyOnWriteArrayList<>(); //list of strings indicating enemies to spawn
    private List<BasicEnemy> slowedList = new CopyOnWriteArrayList<>();
    private boolean stopSpawning;
    private Tile tile;

    private int conquestCompletion = 0;
    private boolean conquestWaveActive = false;

    private int numberOfNights = 0;
    private int numberOfConquests = 0;

    private int exp;

    private EndGamePopup popup;

    private boolean bossWave = false;

    private List<BossObserver> bossObservers;


    /**
     *Constructs the enemy manager for managing enemies
     * @param spawnRate The number of enemies that spawn at a single spawn
     * @param spawnTotal The total number of enemies that exist in the game
     */
    public EnemyManager(int spawnRate, int spawnTotal){
        enemyNumber = 0;
        this.enemySpawnTotal = spawnTotal;
        this.enemySpawnNumber = spawnRate;
        lastEnemySpawn = System.currentTimeMillis();
        currentCycle = 1;
        bossObservers = new CopyOnWriteArrayList<>();
    }

    /**
     * Constructs an enemy manager for managing enemies
     * has a default spawn rate of 3 and spawn total of 10
     */
    public EnemyManager() {
        this(3, 10);
    }

    /**
     * spawns a basic enemy at a random location on the map that is not near the player
     */
    public void spawn() {
        float playerRow = 0;
        float playerCol = 0;
        for (AbstractEntity entity : GameManager.get().getWorld().getEntities()) {
            if (entity instanceof PlayerPeon) {
                playerRow = entity.getCol();
                playerCol = entity.getRow();
            }
        }
        int currentSpawn = 0;
        int count =  0;
        int tileCount = 0;
        int mapRadius = GameManager.get().getWorld().getWorldGenParamBag().getWorldSize() / 2;
        while (currentSpawn < enemySpawnNumber && enemyNumber < enemySpawnTotal) {
            float enemyRow = ((float)(Math.random() * mapRadius * 2) - mapRadius);
            float enemyCol = ((float)(Math.random() * mapRadius * 2) - mapRadius);
            while (enemyRow < playerRow - 4 && enemyRow > playerRow + 4 && enemyCol < playerCol - 4 &&
                    enemyCol > playerCol + 4) {
                enemyRow = ((float)(Math.random() * mapRadius * 2) - mapRadius);
                enemyCol = ((float)(Math.random() * mapRadius * 2) - mapRadius);
            }
            enemyPosition = new HexVector(enemyCol, enemyRow);
            if (GameManager.get().getManager(NetworkManager.class).isHost()) {
                tile = GameManager.get().getWorld().getTile(enemyPosition, 0.7f);
                if (tile == null){
                    tileCount++;
                    if(tileCount > 1000) {      //attempt to keep the game from breaking
                        LOGGER.warn("can't find a position to place the enemy");
                        break;
                    }
                    continue;
                }
                if (tile.isObstructed()) {  // If map is bad may be trapped in endless loop
                    count++;
                    if(count > 1000) {      //attempt to keep the game from breaking
                        LOGGER.warn("can't find a position to place the enemy");
                        break;
                    }
                    continue;
                }
            }
            try {
                enemy = EnemyEntityFactory.createEnemy("basic", enemyCol, enemyRow);
                if (enemy == null){
                    LOGGER.warn("Enemy factory returned null");
                    return;
                }
            }
            catch (IndexOutOfBoundsException e) {
                this.stopSpawning = true;
                LOGGER.info("reached end of spawn list");
                return;
            }
            enemy.setLastAttack();
            currentSpawn++;
            addEnemyToGame(enemy);
        }
    }
    /**
     * Spawns enemies from the spawnlist into the game.
     * enemies are added with a start position within a range of the player
     */
    public void waveSpawn() {
        float playerRow = 0;
        float playerCol = 0;
        for (AbstractEntity entity : GameManager.get().getWorld().getEntities()) {
            if (entity instanceof PlayerPeon) {
                playerRow = entity.getRow();
                playerCol = entity.getCol();
            }
        }
        int currentSpawn = 0;
        int count =  0;
        int tileCount = 0;
        while (currentSpawn < enemySpawnNumber && enemyNumber < enemySpawnTotal) {
            float spawndirection = Math.round((Math.random())) - 0.5f; //dictates if the spawn is up or down
            float enemyRow = playerRow + spawndirection * ((float)(Math.random() * 7) + 5); //sets spawn row
            spawndirection = Math.round((Math.random())) - 0.5f; //dictates if the spawn is left or right
            float enemyCol = playerCol + spawndirection * ((float)(Math.random() * 7) + 5); //sets spawn column
            enemyPosition = new HexVector(enemyCol, enemyRow);
            if (GameManager.get().getManager(NetworkManager.class).isHost()) {
                tile = GameManager.get().getWorld().getTile(enemyPosition, 0.8f);//BFSPathfinder.getTileByHexVector(GameManager.get().getWorld(), enemyPosition);
                if (tile == null){
                    tileCount++;
                    if(tileCount > 1000) {      //attempt to keep the game from breaking
                        LOGGER.warn("can only find null tiles");
                        break;
                    }
                    continue;
                }
                LOGGER.trace("tile {}", tile.getBiome());
                if (tile.isObstructed()) {  // If map is bad may be trapped in endless loop
                    count++;
                    if(count > 1000) {      //attempt to keep the game from breaking
                        LOGGER.warn("can't find a position to place the enemy");
                        break;
                    }
                    continue;
                }
            }
            try {
                enemy = EnemyEntityFactory.createEnemy(spawnList.remove(0), enemyCol, enemyRow);
                if (enemy == null){
                    LOGGER.warn("Enemy factory returned null");
                    return;
                }
            }
            catch (IndexOutOfBoundsException e) {
                this.stopSpawning = true;
                LOGGER.info("reached end of spawn list");
                break;
            }
            LOGGER.trace("Enemy Row: {}, Col: {}", enemyRow, enemyCol);
            enemy.setLastAttack();
            currentSpawn++;
            addEnemyToGame(enemy);
        }
    }

    /**
     * adds the enemy to the game so that it can be interacted in other classes
     * @param enemy the enemy that is being added to the game
     */
    public void addEnemyToGame(BasicEnemy enemy){
        enemyNumber++;
        GameManager.get().getWorld().addEntity(enemy);
        enemyList.add(enemy);
    }

    /**
     * removes a enemy from the game
     * @param enemy the enemy to be removed.
     */
    public void removeEnemyFromGame(BasicEnemy enemy){
        LOGGER.info("removed a enemy");
        Death deadEnemy = new Death(enemy.getCol(), enemy.getRow(), System.currentTimeMillis());
        GameManager.get().getWorld().addEntity(deadEnemy);
        int rarity;
        int itemQuantity = ThreadLocalRandom.current().nextInt(1, 3);
        int isSpawn = ThreadLocalRandom.current().nextInt(0,2);
        Item[] droppedItems = new Item[itemQuantity];

        // isSpawn decreases the chance of enemies spawning loot to 1/2 to improve playability
        if (isSpawn > 0) {
            for (int i = 0; i < itemQuantity; i++) {
                rarity = ThreadLocalRandom.current().nextInt(1, 3);
                droppedItems[i] = new Items().getRandomSpawnableItem(rarity);
            }
            HexVector position = enemy.getPosition();
            Tile enemyTile = BFSPathfinder.getTileByHexVector(GameManager.get().getWorld(), position);
            if (tile != null) {
                if (enemyTile.hasParent() && enemyTile.getParent() instanceof ItemEntity) {
                    ((ItemEntity) enemyTile.getParent()).addItemArray(droppedItems);
                } else {
                    GameManager.get().getWorld().addEntity(new ItemEntity(enemyTile, droppedItems));
                }
            }
        }

        enemyNumber--;
        GameManager.get().getWorld().removeEntity(enemy);
        GameManager.get().getManager(NetworkManager.class).deleteEntity(enemy);
        enemyList.remove(enemy);
    }

    /**
     * function for damaging enemies
     * @param enemy the enemy that is being damaged
     * @param damage the amount of damage that is occuring
     */
    public void damageEnemy(BasicEnemy enemy, int damage) { //at the moment returns void comment on ticket #86 if return value is needed or implemented
        try  {
            enemy.loseHealth(damage);
        } catch (EnemyValueException e){
            LOGGER.warn("Couldn't damage enemy, threw EnemyValueException");
        }
    }

    /**
     * slows a given enemy by half its speed
     * @param enemy the enemy that is being slowed
     */
    public void slowEnemy(BasicEnemy enemy){
        float enemySpeed = enemy.getSpeed();
        LOGGER.trace("EnemySpeed: {}", enemySpeed);
        if (!slowedList.contains(enemy)) {
            try {
                slowedList.add(enemy);
                enemy.loseSpeed(enemySpeed / 2);
                enemy.setLastSlow(System.currentTimeMillis());
            } catch (EnemyValueException e) {
                LOGGER.warn("can't slow enemy");
            }
        }
    }

    /***
     * resets a slowed enemy back to its initial speed (doubles current speed) is handled in this class
     * @param enemy the enemy that is being sped back up
     */
    private void resetSpeed(BasicEnemy enemy) {
        slowedList.remove(enemy);
        try {
            enemy.gainSpeed(enemy.getSpeed());
        }
        catch (EnemyValueException e){
            LOGGER.warn("could not reset enemy speed");
        }
    }

    /**
     * Controls the difficulty of the game depending on number of Nights and number
     * of Conquests.
     * @return The difficulty of the game.
     */
    public int getDifficulty() {
        int progress = this.numberOfNights + this.numberOfConquests * 2;
        int difficulty = 0;
        if (progress < 4) {
            difficulty = 1 + numberOfNights/2;
        } else if (progress < 9) {
            difficulty = 2 + numberOfNights/5;
        } else if (progress < 15) {
            difficulty = 3 + this.numberOfNights/5;
        } else {
            difficulty = 2 + this.numberOfNights/3;
        }
        LOGGER.trace("Difficulty: {}", difficulty);
        return difficulty;
    }


    /**
     * draft method for producing a wave to be spawned
     * @param difficulty the difficulty of the wave being spawned.
     */
    public void creatWave(int difficulty) {
        int basicNumber = 0;
        int fastNumber = 0;
        int healerNumber = 0;
        int flyingNumber = 0;
        int heavyNumber = 0;
        int vehicleNumber = 0;
        int explosiveNumber = 0;
        int rangedNumber = 0;
        int rangerNumber = 0;
        int bossNumber = 0;
        int biomeNumber = 0;
        if (difficulty <= 1) {
            basicNumber = 2 + this.numberOfNights;
            fastNumber = 2 + this.numberOfNights;
            flyingNumber = 2;
        } else if (difficulty == 2) {
            basicNumber = 1;
            fastNumber = 2;
            healerNumber = 1;
            flyingNumber = 2;
            heavyNumber = this.numberOfNights;
            rangedNumber = this.numberOfNights/2;
            biomeNumber = 2;
        } else if (difficulty == 3) {
            basicNumber = 1;
            fastNumber = 2;
            healerNumber = 2;
            flyingNumber = this.numberOfNights/2;
            heavyNumber = this.numberOfNights/2;
            vehicleNumber = 1;
            explosiveNumber = 2;
            rangedNumber = 3;
            rangerNumber = this.numberOfNights;
            biomeNumber = 3;
        } else if (difficulty == 4) {
            fastNumber = 1;
            healerNumber = 2;
            flyingNumber = this.numberOfNights/2;
            heavyNumber = this.numberOfNights/2;
            vehicleNumber = 2;
            explosiveNumber = 3;
            rangedNumber = this.numberOfNights/2;
            rangerNumber = this.numberOfNights/3;
            biomeNumber = 4;
        } else {
            fastNumber = 2;
            healerNumber = 2;
            flyingNumber = 1 + this.numberOfNights/4;
            heavyNumber = 1 + this.numberOfNights/4;
            vehicleNumber = 1 + this.numberOfNights/5;
            explosiveNumber = 1 + this.numberOfNights/4;
            rangedNumber = 1 + this.numberOfNights/3;
            rangerNumber = 3;
            biomeNumber = 1 + this.numberOfNights/2;


            //setEnemySpawnTotal(4 + this.numberOfNights/10);
        }
        if (difficulty == 5) {
            GameManager.get().getManager(DayNightCycle.class).setNightLength(60000);
        }
        if (bossWave) {
            bossNumber = 1 + (this.numberOfNights/10);
        }
        enemySpawnTotal = basicNumber + fastNumber + healerNumber + flyingNumber +
            heavyNumber + vehicleNumber + explosiveNumber + rangedNumber + rangerNumber +
            bossNumber + biomeNumber;
        enemySpawnRate = GameManager.get().getManager(DayNightCycle.class).getNightLength() /
                ((enemySpawnTotal / enemySpawnNumber) + 1);
        addToSpawnList(basicNumber, "basic");
        addToSpawnList(fastNumber, "fast");
        addToSpawnList(healerNumber, "healer");
        addToSpawnList(flyingNumber, "flying");
        addToSpawnList(heavyNumber, "heavy");
        addToSpawnList(vehicleNumber, "vehicle");
        addToSpawnList(explosiveNumber, "explosive");
        addToSpawnList(rangedNumber, "ranged");
        addToSpawnList(rangerNumber, "ranger");
        addToSpawnList(biomeNumber, "biome");
        addToSpawnList(bossNumber, "boss");
        stopSpawning = false;
    }

    /**
     * clears the list of enemies to spawn
     */
    public void emptyWave(){
        this.spawnList.clear();
    }

    /**
     * adds a number of enemies enemy to the list of enemies to be spawned (unused)
     * @param number the number of enemies being added
     * @param type the type of enemy
     */
    public void addToSpawnList(int number, String type){
        int count = 0;
        while (count < number) {
            int insertPosition = ThreadLocalRandom.current().nextInt(spawnList.size() + 1);
            this.spawnList.add(insertPosition, type);
            count++;
        }
    }

    /**
     * heals enemies within range of a healer enemy
     */
    public void enemyHeal(){
        for (BasicEnemy healer : enemyList){
            if (healer instanceof HealerEnemy && ((HealerEnemy) healer).hasHealed()){
                for (BasicEnemy enemies : enemyList) {
                    if (!(enemies instanceof HealerEnemy) && (healer.distance(enemies) < ((HealerEnemy) healer).getHealRange())) {
                        try {
                            enemies.gainHealth(((HealerEnemy) healer).getHealingAbility());
                        }
                        catch (EnemyValueException e){
                            LOGGER.warn("was unable to heal enemies");
                        }
                    }
                }
                ((HealerEnemy)healer).setLastHeal(System.currentTimeMillis());
            }
        }
    }

    @Override
    public void onTick(long i){
        if (!this.stopSpawning && (this.currentCycle == 2 || this.conquestWaveActive) &&
                System.currentTimeMillis() - lastEnemySpawn >  this.enemySpawnRate) {
            this.waveSpawn();
            lastEnemySpawn = System.currentTimeMillis();
        }
        for (BasicEnemy enemies : slowedList){
            if(System.currentTimeMillis() - enemies.getLastSlow() > 1000){
                resetSpeed(enemies);
                slowedList.remove(enemies);
            }
        }
        enemyHeal();
    }

    /**
     * gets the list of enemies currently active
     * @return list of active enemies
     */
    public List<BasicEnemy> getEnemyList() {
        return new CopyOnWriteArrayList<>(this.enemyList);
    }

    /**
     * Get the current number of enemies
     *
     * @return the number of enemies
     */
    public int getEnemyNumber() {
        return enemyList.size();
    }

    /**
     * Get the rate at which enemies spawn
     *
     * @return rate at which enemies spawn
     */
    public int getenemySpawnNumber() {
        return enemySpawnNumber;
    }

    /**
     * Get the maximum number of spawnable enemies
     *
     * @return the maximum number of spawnable enemies
     */
    public int getEnemySpawnTotal() {
        return enemySpawnTotal;
    }

    /**
     * sets the spawn rate
     * @param spawnNumber the new spawn rate
     */
    public static void setEnemySpawnNumber(int spawnNumber) {
        enemySpawnNumber = spawnNumber;
    }

    /**
     * sets the total number of enemies that can be spawned
     * @param spawnTotal the new total number of enemies
     */
    public static void setEnemySpawnTotal(int spawnTotal) {
        enemySpawnTotal = spawnTotal;
    }

    @Override
    public void notifyCycleChange(int cycleCode){
        LOGGER.info("Day night cycle changed to code: " + cycleCode);
        this.numberOfNights++;
        this.currentCycle = cycleCode;
        if (this.conquestCompletion <= 0) {
            if (cycleCode == 1){
                emptyWave();
            }
            if (this.numberOfNights % 4 == 0) {
                bossWave = true;
            }
            creatWave(getDifficulty());
        }
    }

    @Override
    public void notifyComConquest(int conquestCompletion) {
        if(conquestCompletion < 0) {
            LOGGER.info("All towers captured.");
            popup.render();
        } if(conquestCompletion <= 0) {
            if (conquestWaveActive) {
                this.emptyWave();
                this.conquestWaveActive = false;
                if (currentCycle == 2) {
                    this.creatWave(1);
                }
            }
            this.conquestCompletion = conquestCompletion;
            this.conquestWaveActive = false;
        } if (conquestCompletion > this.conquestCompletion) {
            if (!conquestWaveActive && conquestCompletion == 10) { //starts the attack from enemies at 10% completion
                emptyWave();
                creatWave(getDifficulty());
                this.conquestWaveActive = true;
                this.conquestCompletion = conquestCompletion;
                if (this.numberOfConquests >= 1) {
                    this.bossWave = true;
                }
            } else if (conquestCompletion == 100) { //runs when the conquest is complete
                emptyWave();
                this.conquestWaveActive = false;
                this.numberOfConquests++;
            }
        }
    }

    public int getExp() {
        return this.exp;
    }

    public void addExp(int exp) {
        this.exp += exp;
        LOGGER.info(String.format("Total Experience: %s",this.exp));
    }

    public void resetExp() {
        this.exp = 0;
    }


    public int getCurrentCycle() {
        return this.currentCycle;
    }

    public void resetDificulty() {
        this.numberOfNights = 0;
    }

    /**
     * Adds an object to the list of objects that are notified when a boss
     * targets an entity
     * @param observer the observer to add
     */
    public void addBossObserver(BossObserver observer) {
        bossObservers.add(observer);
    }

    /**
     * Removes an object from the list of objects that are notified when a boss
     * targets an entity
     * @param observer the observer to remove
     */
    public void removeBossObserver(BossObserver observer) {
        bossObservers.remove(observer);
    }

    /**
     * Notifies all boss observers that the given boss has targeted the given entity
     * @param boss the boss to notify observers about
     * @param target the entity the boss is targeting
     */
    public void notifyBossObservers(BasicEnemy boss, AbstractEntity target) {
        for (BossObserver observer : bossObservers) {
            observer.bossTargeted(boss, target);
        }
    }
}



