package deco.combatevolved.entities.enemyentities;

import deco.combatevolved.entities.BossEnemyArtificialIntelligence;
import deco.combatevolved.factories.EnemyEntityFactory;
import deco.combatevolved.managers.EnemyManager;
import deco.combatevolved.managers.GameManager;
import deco.combatevolved.tasks.AbstractTask;

public class SnowBossEnemy extends SnowEnemy {

    private long spawnTime;

    /**
     *  Constructs a snow boss enemy that resembles a yeti
     *  Abilities: Can spawn snowmen
     * @param row the row coordinate of the enemy
     * @param col the row coordinate of the enemy
     */
    public SnowBossEnemy(float col, float row) {
        super(col, row);
        this.speed = 0.035f;
        this.damage = 40;
        this.health = 650;
        this.maxHealth = this.health;
        this.armourHealth = 55;
        this.exp = 175;
        spawnSnowman();
        this.spawnTime = System.currentTimeMillis();
        this.ai = new BossEnemyArtificialIntelligence(getPlayer(), "snowman");
        enemyType = "Snow_Monster";
        includeBiome = false;

        moveSound = "Yeti_Movement.mp3";
        attackSound = "Yeti_Attack.mp3";
        damageSound = "Yeti_Damage.mp3";
        deathSound = "Yeti_Death.mp3";
    }

    /**
     *  Determines what the enemy will do on every frame
     * @param i
     */
    @Override
    public void onTick(long i) {
        AbstractTask newTask = getTask(i);
        spawnSnowman();
        play_move_sound(newTask);
        freezeAbility(newTask);
        setEnemyTexture(newTask);
    }

    /**
     * Spawns snowmen around the enemy
     */
    public void spawnSnowman() {
        if (System.currentTimeMillis() > spawnTime + 5000) {
            float row = this.getPosition().getRow();
            float col = this.getPosition().getCol();

            for(int i = 0; i < 2; i++) {
                float spawndirection = Math.round((Math.random())) - 0.5f; //dictates if the spawn is up or down
                float enemyRow = row + spawndirection * ((float)(Math.random() * 8) + 4); //sets spawn row
                spawndirection = Math.round((Math.random())) - 0.5f; //dictates if the spawn is left or right
                float enemyCol = col + spawndirection * ((float)(Math.random() * 8) + 4); //sets spawn column
                BasicEnemy enemy = EnemyEntityFactory.createEnemy("biome", enemyCol, enemyRow);
                GameManager.get().getManager(EnemyManager.class).addEnemyToGame(enemy);
            }
            spawnTime = System.currentTimeMillis();
        }
    }
}