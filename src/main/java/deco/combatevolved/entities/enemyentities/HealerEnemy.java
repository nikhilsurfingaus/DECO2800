package deco.combatevolved.entities.enemyentities;

public class HealerEnemy extends BasicEnemy {

    private float range;
    private int healingAbility;
    private long lastHeal;
    private float healRange;

    /**
     *  Constructs a healer enemy that resembles a wizard lizard
     *  Abilities: Can heal other enemies in its range
     * @param row the row coordinate of the enemy
     * @param col the row coordinate of the enemy
     */
    public HealerEnemy(float col, float row) {
        super(col, row);
        this.range = 3f;
        this.healRange = 5f;
        this.speed = 0.035f;
        this.damage = 0;
        this.health = 75;
        this.maxHealth = this.health;
        this.armourHealth = 0;
        this.exp = 15;
        this.healingAbility = 5;
        this.lastHeal = System.currentTimeMillis();
        enemyType = "Wizard_Lizard";

        moveSound = "Lizard_Movement.mp3";
        attackSound = "Healer_Attack.mp3";
        damageSound = "Lizard_Damage.mp3";
        deathSound = "Lizard_Death.mp3";
    }

    /**
     * Gets the last time the enemy heals other enemies
     * @return long that is the time of the last heal
     */
    public long getLastHeal(){
        return this.lastHeal;
    }

    /**
     * Sets the last heal time
     * @param lastHeal - the heal time
     */
    public void setLastHeal(long lastHeal){
        this.lastHeal = lastHeal;
    }

    /**
     * Gets the heal ability amount
     * @return int that is the heal ability
     */
    public int getHealingAbility() {
        return this.healingAbility;
    }

    /**
     * Gets the range of the enemy
     * @return the range
     */
    public float getRange() {
        return this.range;
    }

    /**
     * Gets the healing range of the enemy
     * @return the healing range
     */
    public float getHealRange() {
        return this.healRange;
    }
    /**
     * checks if the healer has reacently healed
     * @return true if healer has healed.
     */
    public boolean hasHealed() {
        return System.currentTimeMillis() - this.lastHeal > 4000;
    }

    /**
     * check if the heal should be displayed.
     * @return
     */
    public boolean healDisplay() {
        return System.currentTimeMillis() - this.lastHeal < 150;
    }
}
