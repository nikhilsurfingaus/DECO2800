package deco.combatevolved.entities;

import deco.combatevolved.entities.enemyentities.BasicEnemy;

/**
 * Interface used to notify a class that a boss is targeting another entity
 */
public interface BossObserver {
    /**
     * Notifies the observer that a boss is targeting another entity
     * @param boss the boss
     * @param target the entity that the boss is targeting
     */
    void bossTargeted(BasicEnemy boss, AbstractEntity target);
}
