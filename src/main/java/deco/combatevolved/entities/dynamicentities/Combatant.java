package deco.combatevolved.entities.dynamicentities;

/**
 * The combatant interface
 */
public interface Combatant {
    /**
     * The ability for the player to attack the enemies
     * @return the amount of the damage dealt
     */
    int attack();

    /**
     * The ability for the player to defend themselves
     * @return the amount of the damage fended off
     */
    int defend();

    /**
     * The ability for the player to block assaults
     * @return the amount of damage blocked
     */
    int dodge();

}
