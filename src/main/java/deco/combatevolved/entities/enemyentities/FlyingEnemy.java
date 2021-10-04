package deco.combatevolved.entities.enemyentities;

import deco.combatevolved.entities.FlyingEnemyArtificialIntelligence;

public class FlyingEnemy extends BasicEnemy {

    /**
     *  Constructs a flying enemy that resembles a wasp
     *  Abilities: Can fly over obstacles
     * @param row the row coordinate of the enemy
     * @param col the row coordinate of the enemy
     */
    public FlyingEnemy(float col, float row) {
        super(col, row);
        this.speed = 0.05f;
        this.damage = 2;
        this.health = 70;
        this.maxHealth = this.health;
        this.armourHealth = 0;
        this.exp = 10;
        this.ai = new FlyingEnemyArtificialIntelligence(getPlayer());
        enemyType = "Wasp";

        moveSound = "Wasp_Movement.mp3";
        attackSound = "Wasp_Attack.mp3";
        damageSound = "Wasp_Damage.mp3";
        deathSound = "Wasp_Death.mp3";
    }
}
