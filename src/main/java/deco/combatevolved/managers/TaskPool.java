package deco.combatevolved.managers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import deco.combatevolved.entities.HasHealth;
import deco.combatevolved.entities.dynamicentities.DynamicEntity;
import deco.combatevolved.entities.enemyentities.BasicEnemy;
import deco.combatevolved.entities.enemyentities.RangeEnemy;
import deco.combatevolved.entities.staticentities.defensivetowers.Tower;
import deco.combatevolved.tasks.*;
import deco.combatevolved.util.HexVector;
import deco.combatevolved.worlds.AbstractWorld;
import deco.combatevolved.worlds.Tile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaskPool extends AbstractManager {
	private static final Logger LOGGER = LoggerFactory.getLogger(EnemyManager.class);

	private List<AbstractTask> taskPool;
	private List<MovementTask> movementTaskPool;
	private List<FlyingMovementTask> flyingMovementTaskPool;
	private List<MeleeAttackTask> meleeAttackTaskPool;
	private List<RangeAttackTask> rangeAttackTaskPool;
	private List<WaitTask> waitTaskPool;
	private List<SpawnTask> spawnTaskPool;
	private List<AbstractTask> towerTaskPool;
	private AbstractWorld world;
	private Random random;

	public TaskPool() {
		taskPool = new ArrayList<>();
		towerTaskPool = new ArrayList<>();
		movementTaskPool = new ArrayList<>();
		flyingMovementTaskPool = new ArrayList<>();
		meleeAttackTaskPool = new ArrayList<>();
		rangeAttackTaskPool = new ArrayList<>();
		waitTaskPool = new ArrayList<>();
		spawnTaskPool = new ArrayList<>();
		world = GameManager.get().getWorld();
		random = new Random();
	}
	
	public AbstractTask getTask(DynamicEntity entity) {
		if (taskPool.isEmpty()) {
			List<Tile> tiles = world.getTileListFromMap();
			if (tiles.size() == 0) {
				// There are no tiles
				return null;
			}
			Tile destination = tiles.get(random.nextInt(tiles.size()));
			return new MovementTask(entity, destination.getCoordinates());
		}
		return taskPool.remove(0);
	}

	/** Gets a tower task - target or attack depending on
	 * the parameters passed to it
	 * @param parameters - parameters required for tower tasks
	 * @return the task to be performed
	 */
    public AbstractTask getTowerTask(TowerTaskParameters parameters) {

        if (towerTaskPool.isEmpty()) {
        	LOGGER.info("New tower task created");

            //Attacks if target was given
            if (parameters.isTargetFound()) {
                return new TowerAttackTask((Tower) parameters.getEntity(), parameters.getTarget());
                //Tries to find target if none given
            } else {
                return new TowerTargetTask((Tower) parameters.getEntity());
            }
            //Returns task
        } else {
            return towerTaskPool.remove(0);
        }
    }

	/**
	 * Gets a movement task with the given parameters
	 * If there is an unused movement task in the pool, it is used, otherwise a
	 * new task is created
	 * @param entity the entity the task is for
	 * @param destination the destination the entity should move to
	 * @return a movement task with the given parameters
	 */
	public MovementTask getMovementTask(DynamicEntity entity, HexVector destination) {
		if (movementTaskPool.isEmpty()) {
			LOGGER.info("New movement task created");
			return new MovementTask(entity, destination);
		} else {
			MovementTask task = movementTaskPool.remove(0);
			task.reset(entity, destination);
			return task;
		}
	}

	/**
	 * Gets a flying movement task with the given parameters
	 * If there is an unused flying movement task in the pool, it is used,
	 * otherwise a new taskis created
	 * @param entity the entity the task is for
	 * @param destination the destination th entity should fly to
	 * @return a flying movement task with the given parameters
	 */
	public FlyingMovementTask getFlyingMovementTask(DynamicEntity entity, HexVector destination) {
		if (flyingMovementTaskPool.isEmpty()) {
			LOGGER.info("New flying movement task created");
			return new FlyingMovementTask(entity, destination);
		} else {
			FlyingMovementTask task = flyingMovementTaskPool.remove(0);
			task.reset(entity, destination);
			return task;
		}
	}

	/**
	 * Gets a melee attack task with the given parameters
	 * If there is an unused melee attack task in the pool, it is used,
	 * otherwise a new task is created
	 * @param entity the entity the task is for
	 * @param target the target the enemy should attack
	 * @return a melee attack task with the given parameters
	 */
	public MeleeAttackTask getMeleeAttackTask(BasicEnemy entity, HasHealth target, HexVector targetPosition) {
		if (meleeAttackTaskPool.isEmpty()) {
			LOGGER.info("New melee attack task created");
			return new MeleeAttackTask(entity, target, targetPosition);
		} else {
			MeleeAttackTask task = meleeAttackTaskPool.remove(0);
			task.reset(entity, target, targetPosition);
			return task;
		}
	}

	/**
	 * Gets a range attack task with the given parameters
	 * If there is an unused range attack task in the pool, it is used,
	 * otherwise a new task is created
	 * @param entity the entity the task is for
	 * @param target the target the enemy should attack
	 * @return a range attack task with the given parameters
	 */
	public RangeAttackTask getRangeAttackTask(RangeEnemy entity, HasHealth target, HexVector targetPosition) {
		if (rangeAttackTaskPool.isEmpty()) {
			LOGGER.info("New range attack task created");
			return new RangeAttackTask(entity, target, targetPosition);
		} else {
			RangeAttackTask task = rangeAttackTaskPool.remove(0);
			task.reset(entity, target, targetPosition);
			return task;
		}
	}

	/**
	 * Gets a wait task with the given parameters
	 * If there is an unused wait task in the pool, it is used, otherwise a new
	 * task is created
	 * @param entity the entity the task is for
	 * @return a wait task with the given parameters
	 */
	public WaitTask getWaitTask(DynamicEntity entity) {
		if (waitTaskPool.isEmpty()) {
			LOGGER.info("New wait task created");
			return new WaitTask(entity);
		} else {
			WaitTask task = waitTaskPool.remove(0);
			task.reset(entity);
			return task;
		}
	}

	public SpawnTask getSpawnTask(DynamicEntity entity, int amount, float radius, String type) {
		if (spawnTaskPool.isEmpty()) {
			LOGGER.info("New spawn task created");
			return new SpawnTask(entity, amount, radius, type);
		} else {
			SpawnTask task = spawnTaskPool.remove(0);
			task.reset(entity, amount, radius, type);
			return task;
		}
	}

	/**
	 * Adds a task that is no longer being used back to the task pool
	 * The task can then be reused later if a task of the same type is requested
	 * @param task the task to be returned to the task pool
	 */
	public void returnToTaskPool(AbstractTask task) {
		if (task instanceof MovementTask) {
			movementTaskPool.add((MovementTask) task);
		} else if (task instanceof FlyingMovementTask) {
			flyingMovementTaskPool.add((FlyingMovementTask) task);
		} else if (task instanceof MeleeAttackTask) {
			meleeAttackTaskPool.add((MeleeAttackTask) task);
		} else if (task instanceof RangeAttackTask) {
			rangeAttackTaskPool.add((RangeAttackTask) task);
		} else if (task instanceof WaitTask) {
			waitTaskPool.add((WaitTask) task);
		} else if (task instanceof TowerAttackTask || task instanceof TowerTargetTask) {
			towerTaskPool.add(task);
		} else if (task instanceof SpawnTask) {
			spawnTaskPool.add((SpawnTask) task);
		}
	}
}
