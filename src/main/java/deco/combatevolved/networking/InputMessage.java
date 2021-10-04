package deco.combatevolved.networking;

public class InputMessage {
    private int entityID;
    private float[] clickedPosition;
    private int pointer;
    private int button;

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
    public float[] getClickedPosition() {
        return clickedPosition;
    }

    /**
     * Sets the posiiton of the click (in terms of x,y) in the message.
     * @param clickedPosition the position of the click
     */
    public void setClickedPosition(float[] clickedPosition) {
        this.clickedPosition = clickedPosition;
    }

    /**
     * Gets the pointer.
     * @return the pointer
     */
    public int getPointer() {
        return pointer;
    }

    /**
     * Sets the pointer in the message.
     * @param pointer the pointer
     */
    public void setPointer(int pointer) {
        this.pointer = pointer;
    }

    /**
     * Gets the button that was pressed.
     * @return the button that was pressed.
     */
    public int getButton() {
        return button;
    }

    /**
     * Sets the button that was pressed in the message.
     * @param button the button that was pressed
     */
    public void setButton(int button) {
        this.button = button;
    }
}
