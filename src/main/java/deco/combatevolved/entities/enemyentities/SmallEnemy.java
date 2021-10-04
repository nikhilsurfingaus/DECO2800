package deco.combatevolved.entities.enemyentities;

public class SmallEnemy extends BasicEnemy {

    /**
     *  Constructs a small enemy that resembles a small spider
     *  Abilities: None
     * @param row the row coordinate of the enemy
     * @param col the row coordinate of the enemy
     */
    public SmallEnemy(float col, float row) {
        super(col, row);
        this.speed = 0.05f;
        this.damage = 1;
        this.health = 30;
        this.maxHealth = this.health;
        this.armourHealth = 0;
        this.exp = 3;
        enemyType = "Spider_Small";

        moveSound = "Spider_Movement.mp3";
        attackSound = "Spider_Attack.mp3";
        damageSound = "Spider_Damage.mp3";
        deathSound = "Spider_Death.mp3";
    }
}

