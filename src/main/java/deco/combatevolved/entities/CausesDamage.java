package deco.combatevolved.entities;

public interface CausesDamage {

    /**
     *  Returns the amount of damage the enemy can cause
     * @return the damage the enemy can return
     */
    int getDamage();

    /**
     * Returns the range the enemy can attack from
     * @return the range the enemy can attack from
     */
    float getRange();

    /**
     * Gets the last time that this object caused damage
     * @return the last tie this object caused damage
     */
    long getLastAttack();

    /**
     * Sets the last time that this object caused damage as the current time
     */
    void setLastAttack();

    /**
     * Gets the time between each attack
     * @return the time this object has to wait between attacks
     */
    long getAttackDelay();
}
