package deco.combatevolved.entities;

import deco.combatevolved.entities.dynamicentities.PlayerPeon;
import deco.combatevolved.entities.enemyentities.BasicEnemy;
import deco.combatevolved.managers.EnemyManager;
import deco.combatevolved.managers.GameManager;
import deco.combatevolved.managers.TaskPool;
import deco.combatevolved.tasks.AbstractTask;

import java.util.List;

/**
 * Artificial intelligence for boss enemies
 * Boss enemies spawn other enemies near them
 * @param <T> The type of boss enemy this ai is for
 */
public class BossEnemyArtificialIntelligence<T extends BasicEnemy> extends EnemyArtificialIntelligence<T> {
    private long lastSpawn;
    private long spawnDelay;
    private String type;

    public BossEnemyArtificialIntelligence(PlayerPeon player, String type) {
        super(player);
        lastSpawn = 0;
        spawnDelay = 5000;
        this.type = type;
        GameManager.getManagerFromInstance(EnemyManager.class).removeBossObserver(this);
    }


    /**
     * Get a task for the boss enemy
     * If there are not enough enemies nearby, the task returned makes more
     * enemies spawn
     * Otherwise the task returned is the same as for a normal enemy
     * @param entity The entity to perform the task
     * @return The task for the boss enemy to do
     */
    @Override
    public AbstractTask getTask(T entity) {
        List<BasicEnemy> enemies = GameManager.getManagerFromInstance(EnemyManager.class)
                .getEnemyList();
        int count = 0;
        // count how many enemies are near the boss
        for (BasicEnemy enemy : enemies) {
            if (enemy.distance(entity) < 10) {
                count++;
            }
        }

        // if there are not many enemies near the boss, spawn more
        if (count < 10 && System.currentTimeMillis() - lastSpawn > spawnDelay) {
            lastSpawn = System.currentTimeMillis();
            return GameManager.getManagerFromInstance(TaskPool.class)
                    .getSpawnTask(entity, 3, 5, type);
        }

        return super.getTask(entity);
    }

    @Override
    public AbstractEntity getTarget(T entity) {
        // get a target
        AbstractEntity target = super.getTarget(entity);

        // notify observers that this boss is targeting an entity
        GameManager.getManagerFromInstance(EnemyManager.class).notifyBossObservers(entity, target);

        return target;
    }
}
