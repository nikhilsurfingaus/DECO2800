package deco.combatevolved.entities;

import deco.combatevolved.exceptions.EnemyValueException;

public interface HasHealth {

    /**
     *  Returns the health of the entity
     * @return The health of the entity
     */
    int getHealth();

    /**
     *  Allows the health of the entity to decrease
     * @param lossHealth Amount the enemy loses health
     * @throws EnemyValueException when negative value is entered
     */
    void loseHealth(int lossHealth) throws EnemyValueException;

    /**
     *  Allows the health of the entity to increase
     * @param gainHealth Amount the entity loses health
     * @throws EnemyValueException when negative value is entered
     */
    void gainHealth(int gainHealth) throws EnemyValueException;

    /**
     *  Allows the health of the entity to increase to its maximum amount
     */
    void gainFullHealth();

    /**
     * Gives the entity health of 0
     */
    void death();
}