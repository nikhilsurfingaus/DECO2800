package deco.combatevolved.entities;

import deco.combatevolved.entities.dynamicentities.DynamicEntity;
import deco.combatevolved.tasks.AbstractTask;

/**
 * An ArtificialIntelligence controls a Dynamic by giving it an instance
 * of AbstractTask when requested
 */
public interface ArtificialIntelligence<T extends DynamicEntity> {
    /**
     * Gets a task for the specified entity
     * Returns null if there is no new task for the entity
     * @param entity The entity to perform the task
     * @return The task for the entity, or null if there is no new task
     */
    AbstractTask getTask(T entity);
}
