package deco.combatevolved.entities.enemyentities;

import deco.combatevolved.entities.BossEnemyArtificialIntelligence;

public class BasicBossEnemy extends BasicEnemy {

    /**
     *  Constructs a basic boss enemy that resembles a large spider
     *  Abilities: None
     * @param row the row coordinate of the enemy
     * @param col the row coordinate of the enemy
     */
    public BasicBossEnemy(float col, float row) {
        super(col, row);
        this.speed = 0.035f;
        this.damage = 30;
        this.health = 450;
        this.maxHealth = this.health;
        this.armourHealth = 25;
        this.exp = 150;
        this.ai = new BossEnemyArtificialIntelligence(getPlayer(), "basic");
        enemyType = "Spider_Queen";
        includeBiome = false;

        moveSound = "Spider_Movement_2.mp3";
        attackSound = "Spider_Attack_2.mp3";
        damageSound = "Spider_Damage_2.mp3";
        deathSound = "Spider_Death_2.mp3";
    }
}
