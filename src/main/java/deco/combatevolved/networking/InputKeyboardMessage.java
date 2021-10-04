package deco.combatevolved.networking;

public class InputKeyboardMessage {
    private int entityID;
    private int keycode;
    private int inputType;

    /**
     * Gets the entity id of the player who sent a keyboard input.
     * @return entity id of the player
     */
    public int getEntityID() {
        return entityID;
    }

    /**
     * Sets the entity id of the player keyboard input message.
     * @param entityID entity id of the player
     */
    public void setEntityID(int entityID) {
        this.entityID = entityID;
    }

    /**
     * Gets the keycode of the key that was pressed.
     * @return the keycode of the key that was pressed
     */
    public int getKeycode() {
        return keycode;
    }

    /**
     * Sets the keycode in the message of the key that was pressed.
     * @param keycode the keycode of the key that was pressed.
     */
    public void setKeycode(int keycode) {
        this.keycode = keycode;
    }

    /**
     * Gets the input type.
     * @return the input type
     */
    public int getInputType() {
        return inputType;
    }

    /**
     * Sets the input type of the message.
     * @param inputType the input type
     */
    public void setInputType(int inputType) {
        this.inputType = inputType;
    }
}
