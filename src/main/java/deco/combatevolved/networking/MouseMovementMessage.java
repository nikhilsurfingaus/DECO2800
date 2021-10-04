package deco.combatevolved.networking;

public class MouseMovementMessage {
    private int entityID;
    private float[] clickedPosition;

    /**
     * Gets the entity id of the player that gave input.
     * @return the entity id of the player
     */
    public int getEntityID() {
        return entityID;
    }

    /**
     * Sets the entity id of the player that gave input in the message.
     * @param entityID the entity id of the player
     */
    public void setEntityID(int entityID) {
        this.entityID = entityID;
    }

    /**
     * Gets the position of the click (in terms of x, y)
     * @return the position of the click
     */
    public float[] getPosition() {
        return clickedPosition;
    }

    /**
     * Sets the posiiton of the click (in terms of x,y) in the message.
     * @param position the position of the click
     */
    public void setPosition(float[] position) {
        this.clickedPosition = position;
    }
}
