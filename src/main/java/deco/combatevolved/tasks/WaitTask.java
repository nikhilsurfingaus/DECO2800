package deco.combatevolved.tasks;

import deco.combatevolved.entities.dynamicentities.DynamicEntity;

/**
 * A task that makes an entity do nothing
 */
public class WaitTask extends AbstractTask {
    /**
     * Construct a new wait task for the given entity
     * @param entity the entity this task is for
     */
    public WaitTask(DynamicEntity entity) {
        super(entity);
    }

    /**
     * Returns whether the task is complete
     * @return whether the task is complete
     */
    public boolean isComplete() {
        // the task will never complete on its own
        return false;
    }

    /**
     * Returns whether the task is alive
     * @return whether the task is alive
     */
    public boolean isAlive() {
        // the task is always alive
        return true;
    }

    /**
     * Make the enemy do nothing each tick
     * @param i the current tick
     */
    public void onTick(long i) {
        // do nothing
    }

    /**
     * Resets an old wait task so it can be reused
     * @param entity the new entity the task is for
     */
    public void reset(DynamicEntity entity) {
        this.entity = entity;
    }
}
