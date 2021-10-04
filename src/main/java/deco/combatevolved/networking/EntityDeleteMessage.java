package deco.combatevolved.networking;

public class EntityDeleteMessage {
    private int entityID;

    /**
     * Gets the entity id to delete.
     * @return entity id to delete.
     */
    public int getEntityID() {
        return entityID;
    }

    /**
     * Sets the entity id to delete in the message.
     * @param entityID entity to delete.
     */
    public void setEntityID(int entityID) {
        this.entityID = entityID;
    }
}
