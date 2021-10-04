package deco.combatevolved.entities;

import deco.combatevolved.entities.dynamicentities.DynamicEntity;
import deco.combatevolved.entities.dynamicentities.PlayerPeon;
import deco.combatevolved.entities.enemyentities.BasicEnemy;
import deco.combatevolved.entities.staticentities.defensivetowers.Tower;
import deco.combatevolved.managers.EnemyManager;
import deco.combatevolved.managers.GameManager;
import deco.combatevolved.managers.TaskPool;
import deco.combatevolved.tasks.AbstractAttackTask;
import deco.combatevolved.tasks.AbstractTask;
import deco.combatevolved.tasks.MovementTask;
import deco.combatevolved.tasks.WaitTask;
import deco.combatevolved.util.HexVector;

/**
 * Artificial intelligence for enemies
 * Makes enemies target the player
 */
public class EnemyArtificialIntelligence <T extends DynamicEntity & CausesDamage>
        implements ArtificialIntelligence<T>, BossObserver {
    // the player character
    private PlayerPeon player;
    // the last entity that was targeted
    private AbstractEntity lastTarget;
    // the last position the entity moved towards
    private HexVector lastPosition;
    // the most recent task given
    private AbstractTask task;
    // the last time this was notified about a boss targeting and entity
    private long lastBossNotify;
    // the entity targeted by a boss
    private AbstractEntity bossTarget;

    /**
     * Constructs an AI that targets the player
     * @param player the player controlled character
     */
    public EnemyArtificialIntelligence(PlayerPeon player) {
        this.player = player;
        lastPosition = null;
        task = null;
        bossTarget = null;
        lastBossNotify = 0;

        GameManager.getManagerFromInstance(EnemyManager.class).addBossObserver(this);
    }

    /**
     * Gets a task for the given DynamicEntity
     * Returns a MovementTask with the player position as the destination
     * If the player has not moved more than 2 units since the last request
     * for a task, null is returned, indicating that the dynamic entity should
     * use the task it already has
     * @param entity The entity to perform the task
     * @return A MovementTask with the player's position as the destination
     */
    @Override
    public AbstractTask getTask(T entity) {
        AbstractEntity target = getTarget(entity);
        if (target == null) {
        	// player is dead and no tower left
        	return null;
        }

        HexVector destination = target.getPosition();
        // if the enemy is in range of a tower, then it should attack
        if (entity.getPosition().distance(destination) < entity.getRange()) {
            ((BasicEnemy)entity).setAngle(entity.getPosition(), destination);
            // if enough time has not passed since the last attack, the enemy
            // should wait
            if (System.currentTimeMillis() - entity.getLastAttack() < entity.getAttackDelay()) {
                // if the entity is already waiting it does not need to be
                // given a new wait task
                if (task instanceof WaitTask) {
                    return null;
                } else {
                    task = GameManager.getManagerFromInstance(TaskPool.class)
                            .getWaitTask(entity);
                    return task;
                }
            } else {
            	entity.setLastAttack();
            	task = attack(entity, (HasHealth) target, target.getPosition());
                return task;
            }
        }

        // if the entity is not in range of a target it should move to the
        // nearest target
        AbstractTask newTask = move(entity, target);
        if (newTask != null) {
            task = newTask;
        }
        return newTask;
    }

    /**
     * Gets a target for the given entity to move to or attack
     * @param entity the entity to find a target for
     * @return a target for the entity
     */
    public AbstractEntity getTarget(T entity) {
        if (bossTarget != null && System.currentTimeMillis() - lastBossNotify < 3000) {
            return bossTarget;
        }

        // find the closest tower or player
        AbstractEntity target = player;
        Tower closestTower = null;
        float distance = 9999f;
        for (AbstractEntity abstractEntity : GameManager.get().getWorld().getEntities()) {
            if (abstractEntity instanceof Tower &&
                    entity.distance(abstractEntity) < distance) {
            	distance = entity.distance(abstractEntity);
            	closestTower = (Tower) abstractEntity;
            }
        }
        if (distance < entity.distance(target) || !((PlayerPeon) target).isAlive()) {
        	target = closestTower;
        }
        return target;
    }

    /**
     * Gets a task to make the enemy attack a tower
     * @param entity the entity that should attack
     * @param target the tower the entity should attack
     * @return an attack task with the given target
     */
    protected AbstractAttackTask attack(T entity, HasHealth target, HexVector targetPosition) {
        return GameManager.getManagerFromInstance(TaskPool.class)
                .getMeleeAttackTask((BasicEnemy) entity, target, targetPosition);
    }

    /**
     * Gets a task to make the enemy move to a target
     * @param entity the enemy that should move
     * @param target the entity the enemy should move to
     * @return a movement task with the given destination or null if the
     * enemy should not change task
     */
    protected AbstractTask move(T entity, AbstractEntity target) {
        HexVector destination = target.getPosition();
        // a new task is only given if a task has not previously been given
        // or if the player has moved enough from the position they were at
        // when a task was last given
        if (lastPosition == null || target != lastTarget ||
                lastPosition.distance(destination) > 0.5f || task.isComplete() ||
                !(task instanceof MovementTask)) {
            lastTarget = target;
            lastPosition = new HexVector(destination);
            return GameManager.getManagerFromInstance(TaskPool.class)
                    .getMovementTask(entity, destination);
        } else {
            return null;
        }
    }

    /**
     * When notified that a boss is targeting an entity, this ai will make the
     * entity it controls attack the same target
     * @param boss the boss
     * @param target the entity that the boss is targeting
     */
    public void bossTargeted(BasicEnemy boss, AbstractEntity target) {
        bossTarget = target;
        lastBossNotify = System.currentTimeMillis();
    }
}
