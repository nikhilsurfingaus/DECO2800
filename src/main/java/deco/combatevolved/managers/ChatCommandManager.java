package deco.combatevolved.managers;

import deco.combatevolved.entities.dynamicentities.AgentEntity;
import deco.combatevolved.entities.dynamicentities.PlayerPeon;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Old deprecated manager for chat commands
 */
public class ChatCommandManager  extends AbstractManager {
    private static final Logger logger = LoggerFactory.getLogger(NetworkManager.class);

    public ChatCommandManager() {

    }

    /**
     * Spawns 1000 agents on the map.
     * @param unsentMessage the message sent.
     * @param senderID the entityID of the sender.
     */
    public static void commandDuck(String unsentMessage, int senderID) {
        logger.info("Spawning 1000 agents :)");
        for (int i = 0; i < 1000; ++i) {
            GameManager.get().getWorld().addEntity(new AgentEntity(0f, 0f, 0.05f));
        }
    }

    /**
     * Handles showing the resources for the client.
     * @param unsentMessage the message sent.
     * @param senderID the entityID of the sender.
     */
    public static void commandResources(String unsentMessage, int senderID) {
        logger.info("Yet to be implemented...");
        //System.out.println(GameManager.get().getManager(NetworkManager.class).getPlayerEntityFromConnection(GameManager.get().getManager(NetworkManager.class).getID()));
        //GameManager.get().getPlayerEntityID();
    }

    /**
     * Handles teleporting the player back to spawn (defined as 0, 0).
     * @param unsentMessage the message sent.
     * @param senderID the entityID of the sender.
     */
    public static void commandSpawn(String unsentMessage, int senderID) {
        logger.info(senderID + ": Teleporting to spawn.");
        String[] msg = unsentMessage.trim().split("\\s+");
        if (msg.length != 1 && msg[0] != "/spawn") {
            logger.info("ERROR: Teleporting to spawn for " + senderID);
        }
        PlayerPeon peon = (PlayerPeon) GameManager.get().getWorld()
                .getEntityById(senderID);
        peon.resetMovementTask();
        peon.setPosition(0, 0, 0);
    }

    /**
     * Handles teleporting the specified player to another players location.
     * @param unsentMessage the message sent.
     */
    public static void commandTeleport(String unsentMessage) {
        PlayerPeon fromPlayer = null;
        PlayerPeon toPlayer = null;

        String[] msg = unsentMessage.trim().split("\\s+");

        if (msg.length != 3) {
            logger.info("ERROR: Teleport arguments were wrong.");
        } else if (msg[1].equals(msg[2])) {
            logger.info("ERROR: Teleport players were the same.");
        }

        Map<Integer, String> userConnections = GameManager.get().getManager(NetworkManager.class).getUserConnections();
        for (Map.Entry<Integer, String> entry : userConnections.entrySet()) {
            if (entry.getValue().equals(msg[1])) {
                fromPlayer = (PlayerPeon) GameManager.get().getWorld()
                        .getEntityById(GameManager.get().getManager(NetworkManager.class).getPlayerEntityFromConnection(entry.getKey()));
            } else if (entry.getValue().equals(msg[2])) {
                toPlayer = (PlayerPeon) GameManager.get().getWorld()
                        .getEntityById(GameManager.get().getManager(NetworkManager.class).getPlayerEntityFromConnection(entry.getKey()));
            }
        }
        if (fromPlayer == null || toPlayer == null) {
            logger.info("ERROR: Specified players not found.");
        } else {
            fromPlayer.resetMovementTask();
            float col = toPlayer.getCol();
            float row = toPlayer.getRow();
            int height = toPlayer.getZ();
            fromPlayer.setPosition(col, row, height);
        }
    }

    /**
     * Handles kicking a player from the server.
     * @param unsentMessage the message sent.
     * @param senderID the entityID of the sender.
     */
    public static void commandKick(String unsentMessage, int senderID) {
        String[] msg = unsentMessage.trim().split("\\s+");
        if (msg.length != 2) {
            logger.info("ERROR: Kick arguments wrong.");
        }
        Map<Integer, String> userConnections = GameManager.get().getManager(NetworkManager.class).getUserConnections();
        for (Map.Entry<Integer, String> entry : userConnections.entrySet()) {
            if (msg[1].equals(entry.getValue())) {
                GameManager.get().getManager(NetworkManager.class).removeClientConnection(entry.getKey(), entry.getValue());
                break;
            }
        }
    }
}
