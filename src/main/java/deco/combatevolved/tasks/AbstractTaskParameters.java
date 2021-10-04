package deco.combatevolved.tasks;

import deco.combatevolved.entities.dynamicentities.DynamicEntity;

/**
 * Contains the parameters to create a task
 */
public abstract class AbstractTaskParameters {
    private DynamicEntity entity;

    /**
     * Stores the entity the task should control
     * @param entity the entity the task should control
     */
    protected AbstractTaskParameters(DynamicEntity entity) {
        this.entity = entity;
    }

    /**
     * Gets the entity that the task should control
     * @return the entity the task should control
     */
    public DynamicEntity getEntity() {
        return entity;
    }
}
