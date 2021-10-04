package deco.combatevolved.tasks;

import deco.combatevolved.entities.dynamicentities.DynamicEntity;
import deco.combatevolved.util.HexVector;

/**
 * A task that makes an entity move in a straight line towards a destination
 * ignoring obstructions
 */
public class FlyingMovementTask extends AbstractTask {
    private boolean complete;
    private boolean alive;

    private HexVector destination;

    /**
     * Construct a flying movement task with the given parameters
     * @param entity the entity this task is for
     * @param destination the destination the entity should move to
     */
    public FlyingMovementTask(DynamicEntity entity, HexVector destination) {
        super(entity);
        this.destination = destination;
        complete = false;
        alive = true;
    }

    /**
     * Moves the entity in the direction of the destination
     * @param i the current tick
     */
    public void onTick(long i) {
        entity.moveTowards(destination);
        if (entity.getPosition().isCloseEnoughToBeTheSame(destination)) {
            complete = true;
        }
    }

    /**
     * Gets whether the task is complete
     * @return whether the task is complete
     */
    public boolean isComplete() {
        return complete;
    }

    /**
     * Gets whether the task is alive
     * @return whether the task is alive
     */
    public boolean isAlive() {
        return alive;
    }

    /**
     * Resets an old flying movement task so that it can be reused
     * @param entity the new entity for this flying movement task to control
     * @param destination the new destination the entity should move to
     */
    public void reset(DynamicEntity entity, HexVector destination) {
        this.entity = entity;
        this.destination = destination;
        complete = false;
        alive = true;
    }

    public HexVector getDestination() {
        return destination;
    }
}
