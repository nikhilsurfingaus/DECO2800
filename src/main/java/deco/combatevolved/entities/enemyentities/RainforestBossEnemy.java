package deco.combatevolved.entities.enemyentities;

import deco.combatevolved.entities.BossEnemyArtificialIntelligence;
import deco.combatevolved.factories.EnemyEntityFactory;
import deco.combatevolved.managers.EnemyManager;
import deco.combatevolved.managers.GameManager;
import deco.combatevolved.tasks.AbstractTask;

public class RainforestBossEnemy extends BasicEnemy {

    // Time the enemy was spawned
    private long spawnTime;

    /**
     *  Constructs a rainforest boss enemy that resembles a large lizard
     *  Abilities: Spawns other enemies
     * @param row the row coordinate of the enemy
     * @param col the row coordinate of the enemy
     */
    public RainforestBossEnemy(float col, float row) {
        super(col, row);
        this.speed = 0.035f;
        this.damage = 0;
        this.health = 600;
        this.maxHealth = this.health;
        this.armourHealth = 40;
        this.exp = 150;
        spawnForestEnemies();
        this.spawnTime = System.currentTimeMillis();
        this.ai = new BossEnemyArtificialIntelligence(getPlayer(), "rainforest");
        enemyType = "Guardian_Lizard";
        includeBiome = false;

        moveSound = "Lizard_Movement_2.mp3";
        attackSound = "Lizard_Attack_2.mp3";
        damageSound = "Lizard_Damage_2.mp3";
        deathSound = "Lizard_Death_2.mp3";
    }

    /**
     *  Determines what the enemy will do on every frame
     * @param i
     */
    @Override
    public void onTick(long i) {
        AbstractTask newTask = getTask(i);
        spawnForestEnemies();
        play_move_sound(newTask);
        setEnemyTexture(newTask);
    }

    /**
     * Spawns forest enemies
     */
    public void spawnForestEnemies() {
        if (System.currentTimeMillis() > spawnTime + 5000) {
            float row = this.getPosition().getRow();
            float col = this.getPosition().getCol();
            for(int i = 0; i < 2; i++) {
                float spawndirection = Math.round((Math.random())) - 0.5f; //dictates if the spawn is up or down
                float enemyRow = row + spawndirection * ((float)(Math.random() * 8) + 4); //sets spawn row
                spawndirection = Math.round((Math.random())) - 0.5f; //dictates if the spawn is left or right
                float enemyCol = col + spawndirection * ((float)(Math.random() * 8) + 4); //sets spawn column
                BasicEnemy enemy = EnemyEntityFactory.createEnemy("rainforest", enemyCol, enemyRow);
                GameManager.get().getManager(EnemyManager.class).addEnemyToGame(enemy);
            }
            spawnTime = System.currentTimeMillis();
        }
    }
}