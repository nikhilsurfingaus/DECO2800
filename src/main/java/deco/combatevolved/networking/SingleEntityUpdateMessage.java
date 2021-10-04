package deco.combatevolved.networking;

import deco.combatevolved.entities.AbstractEntity;

/**
 * Updates (or creates) a single entity in the game.
 */
public class SingleEntityUpdateMessage {
    private AbstractEntity entity;

    /**
     * Gets the entity object to update in the game.
     * @return entity object
     */
    public AbstractEntity getEntity() {
        return entity;
    }

    /**
     * Sets the entity object to delete in the message.
     * @param entity entity object
     */
    public void setEntity(AbstractEntity entity) {
        this.entity = entity;
    }
}