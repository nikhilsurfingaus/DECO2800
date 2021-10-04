package deco.combatevolved.entities;

import deco.combatevolved.entities.dynamicentities.PlayerPeon;
import deco.combatevolved.entities.enemyentities.RangeEnemy;
import deco.combatevolved.managers.GameManager;
import deco.combatevolved.managers.TaskPool;
import deco.combatevolved.tasks.AbstractAttackTask;
import deco.combatevolved.util.HexVector;

/**
 * Artificial intelligence for ranged enemies
 * when they attack, ranged enemies create a projectile that moves toward the
 * target and damages it
 */
public class RangeEnemyArtificialIntelligence extends EnemyArtificialIntelligence<RangeEnemy> {
    /**
     * Constructs an artificial intelligence for a ranged enemy
     * @param player the player the enemy should track
     */
    public RangeEnemyArtificialIntelligence(PlayerPeon player) {
        super(player);
    }

    /**
     * Gets a task to make the entity range attack the tower
     * @param entity the entity that should attack
     * @param target the tower the entity should attack
     * @return
     */
    @Override
    protected AbstractAttackTask attack(RangeEnemy entity, HasHealth target, HexVector targetPosition) {
        return GameManager.getManagerFromInstance(TaskPool.class)
                .getRangeAttackTask(entity, target, targetPosition);
    }
}
