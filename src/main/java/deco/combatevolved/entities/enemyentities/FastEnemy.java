package deco.combatevolved.entities.enemyentities;

public class FastEnemy extends BasicEnemy {

    /**
     *  Constructs a fast enemy that resembles a spider
     *  Abilities: Increased speed
     * @param row the row coordinate of the enemy
     * @param col the row coordinate of the enemy
     */
    public FastEnemy(float col, float row) {
        super(col, row);
        this.speed = 0.07f;
        this.damage = 2;
        this.health = 50;
        this.maxHealth = this.health;
        this.armourHealth = 0;
        this.exp = 5;
        enemyType = "Spider";

        moveSound = "Spider_Movement_3.mp3";
        attackSound = "Spider_Attack.mp3";
        damageSound = "Spider_Damage.mp3";
        deathSound = "Spider_Death.mp3";
    }
}
