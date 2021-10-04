package deco.combatevolved.entities.enemyentities;

import deco.combatevolved.entities.RangeEnemyArtificialIntelligence;

public class RangeEnemy extends BasicEnemy {

    private float range;

    /**
     *  Constructs a ranged enemy that resembles a lizard
     *  Abilities: Shoots from a distance
     * @param row the row coordinate of the enemy
     * @param col the row coordinate of the enemy
     */
    public RangeEnemy(float col, float row) {
        super(col, row);
        this.setZ(1);
        this.range = 10f;
        this.speed = 0.05f;
        this.damage = 2;
        this.health = 50;
        this.maxHealth = this.health;
        this.armourHealth = 0;
        this.exp = 7;
        this.ai = new RangeEnemyArtificialIntelligence(getPlayer());
        enemyType = "Gun_Lizard";

        moveSound = "Lizard_Movement.mp3";
        attackSound = "Ranged_Attack.mp3";
        damageSound = "Lizard_Damage.mp3";
        deathSound = "Lizard_Death.mp3";
    }

    /**
     * Sets the range of the enemy
     * @param newRange - the range of the enemy
     */
    public void setRange(int newRange) {
        this.range = newRange;
    }

    /**
     * Gets the range of the enemy
     * @return the range
     */
    public float getRange() {
    	return this.range;
    }
}
