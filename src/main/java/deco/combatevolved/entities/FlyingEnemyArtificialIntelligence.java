package deco.combatevolved.entities;

import deco.combatevolved.entities.enemyentities.BasicEnemy;
import deco.combatevolved.entities.dynamicentities.PlayerPeon;
import deco.combatevolved.managers.GameManager;
import deco.combatevolved.tasks.AbstractTask;
import deco.combatevolved.managers.TaskPool;

/**
 * Artificial intelligence for flying enemies
 * Flying enemies move straight towards targets and ignore obstructions
 */
public class FlyingEnemyArtificialIntelligence extends EnemyArtificialIntelligence<BasicEnemy> {
    /**
     * Construct artificial intelligence for a flying enemy
     * @param player the player the enemy should track
     */
    public FlyingEnemyArtificialIntelligence(PlayerPeon player) {
        super(player);
    }

    /**
     * Get a task to make the enemy move in a straight line to the target
     * @param entity the enemy that should move
     * @param target the entity the enemy should move to
     * @return
     */
    @Override
    protected AbstractTask move(BasicEnemy entity, AbstractEntity target) {
        return GameManager.getManagerFromInstance(TaskPool.class)
                .getFlyingMovementTask(entity, target.getPosition());
    }
}
