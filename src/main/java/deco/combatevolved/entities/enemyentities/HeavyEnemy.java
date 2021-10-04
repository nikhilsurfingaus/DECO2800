package deco.combatevolved.entities.enemyentities;

public class HeavyEnemy extends BasicEnemy {

    /**
     *  Constructs a heavy enemy that resembles a lizard
     *  Abilities: Increased health
     * @param row the row coordinate of the enemy
     * @param col the row coordinate of the enemy
     */
    public HeavyEnemy(float col, float row) {
        super(col, row);
        this.setTexture("Heavy_Lizard_PlainsFL");
        this.speed = 0.035f;
        this.damage = 5;
        this.health = 200;
        this.maxHealth = this.health;
        this.armourHealth = 15;
        this.exp = 20;
        enemyType = "Heavy_Lizard";

        moveSound = "Lizard_Movement.mp3";
        attackSound = "Lizard_Attack.mp3";
        damageSound = "Lizard_Damage.mp3";
        deathSound = "Lizard_Death.mp3";
    }
}
