package deco.combatevolved.networking;

public class ConfirmConnectionMessage {
    private int playerEntityID;
    public int clientID;

    /**
     * Gets the player entity ID.
     * @return player entity id
     */
    public int getPlayerEntityID() {
        return playerEntityID;
    }

    /**
     * Sets the player entity ID for the message.
     * @param playerEntityID player entity id
     */
    public void setPlayerEntityID(int playerEntityID) {
        this.playerEntityID = playerEntityID;
    }
}
