package deco.combatevolved.entities.enemyentities;

import deco.combatevolved.managers.GameManager;
import deco.combatevolved.factories.EnemyEntityFactory;
import deco.combatevolved.managers.EnemyManager;
import deco.combatevolved.tasks.AbstractTask;

public class VehicleEnemy extends BasicEnemy {
    private long lastSpawnTime;

    /**
     *  Constructs a vehicle enemy that resembles a spider in a vehicle
     *  Abilities: Can spawn other spiders
     * @param row the row coordinate of the enemy
     * @param col the row coordinate of the enemy
     */
    public VehicleEnemy(float col, float row) {
        super(col, row);
        this.speed = 0.03f;
        this.damage = 3;
        this.health = 150;
        this.maxHealth = this.health;
        this.armourHealth = 0;
        this.exp = 15;
        this.lastSpawnTime = System.currentTimeMillis();
        enemyType = "Spider_Vehicle";
        includeBiome = false;

        moveSound = "Vehicle_Movement.mp3";
        attackSound = "Vehicle_Attack.mp3";
        damageSound = "Vehicle_Damage.mp3";
        deathSound = "Vehicle_Death.mp3";
    }

    /**
     *  Determines what the enemy will do on every frame
     * @param i
     */
    @Override
    public void onTick(long i) {
        AbstractTask newTask = getTask(i);
        spawnSpiders();
        play_move_sound(newTask);
        setEnemyTexture(newTask);
    }

    /**
     * Spawns spiders around the enemy
     */
    public void spawnSpiders() {
        BasicEnemy newEnemy = EnemyEntityFactory.createEnemy("small", this.getCol(), this.getRow());
        if (System.currentTimeMillis() - lastSpawnTime > 5000) {
            GameManager.get().getManager(EnemyManager.class).addEnemyToGame(newEnemy);
            this.lastSpawnTime = System.currentTimeMillis();
        }
    }
}