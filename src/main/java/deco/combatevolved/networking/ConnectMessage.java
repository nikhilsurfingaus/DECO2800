package deco.combatevolved.networking;

import deco.combatevolved.entities.AbstractEntity;
import deco.combatevolved.entities.dynamicentities.PlayerPeon;

public class ConnectMessage {
    private String username;
    private PlayerPeon playerEntity;
    private String password;

    /**
     * Gets the player's username.
     * @return player username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the player's username for the message.
     * @param username player's username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the player's entity id.
     * @return player's entity id
     */
    public AbstractEntity getPlayerEntity() {
        return playerEntity;
    }

    /**
     * Sets the player's entity id for the message.
     * @param playerEntity player's entity id
     */
    public void setPlayerEntity(PlayerPeon playerEntity) {
        this.playerEntity = playerEntity;
    }

    /**
     * Gets the player's password used to connect to the server.
     * @return player's password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the player's password for the message.
     * @param password password for the server
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
