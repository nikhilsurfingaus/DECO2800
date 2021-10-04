package deco.combatevolved.networking;

import com.esotericsoftware.kryonet.Server;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import deco.combatevolved.entities.enemyentities.BasicEnemy;
import deco.combatevolved.entities.staticentities.defensivetowers.Tower;
import deco.combatevolved.util.WorldUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import deco.combatevolved.entities.AbstractEntity;
import deco.combatevolved.entities.dynamicentities.PlayerAttributes;
import deco.combatevolved.entities.dynamicentities.PlayerPeon;
import deco.combatevolved.entities.staticentities.ItemEntity;
import deco.combatevolved.managers.CommandsManager;
import deco.combatevolved.managers.GameManager;
import deco.combatevolved.managers.NetworkManager;
import deco.combatevolved.managers.OnScreenMessageManager;
import deco.combatevolved.worlds.Tile;
import deco.combatevolved.worlds.biomes.BiomeType;

public class ServerListener extends Listener {

    private static final Logger logger = LoggerFactory.getLogger(ServerListener.class);

    private Server server;
    private NetworkManager networkManager = GameManager.get().getManager(NetworkManager.class);

    public ServerListener(Server server) {
        this.server = server;
    }

    @Override
    public void received (Connection connection, Object object) {

        networkManager.incrementMessagesReceived();

        if (object instanceof SingleEntityUpdateMessage) {
            handleSingleEntityUpdateMessage(object);
        } else if (object instanceof ChatMessage) {
            handleChatMessage(object);
        } else if (object instanceof ConnectMessage) {
            handleConnectMessage(connection, object);
        } else if (object instanceof ChatCommandMessage) {
            handleChatCommandMessage(connection, object);
        } else if (object instanceof TileDeleteMessage) {
            GameManager.get().getWorld().deleteTile(((TileDeleteMessage) object).getTileID());
        } else if (object instanceof InputMessage) {
            handleInputMessage(object);
        } else if (object instanceof InputKeyboardMessage) {
            handleInputKeyboardMessage(object);
        } else if (object instanceof MouseMovementMessage) {
            handleMouseMovementMessage(object);
        } else if (object instanceof InventoryUpdateMessage) {
            handleInventoryUpdate(connection, object);
        } else if (object instanceof ProjectileMessage) {
            handleProjectileMessage(connection, object);
        }
    }

    public void disconnected(Connection connection) {
        logger.info("Detected client disconnect, cleaning up");
        networkManager.removeClientConnection(connection.getID(), networkManager.getUserConnections().get(connection.getID()));
    }

    private void handleSingleEntityUpdateMessage(Object object) {
        SingleEntityUpdateMessage message = (SingleEntityUpdateMessage) object;

        logger.info("Host received entity: ", message.toString());

        GameManager.get().getWorld().updateEntity(((SingleEntityUpdateMessage) object).getEntity());
    }

    private void handleChatMessage(Object object) {
        // Handles the ChatMessage on server side for the clients, but not the hosts.

        // Forward the chat message to all clients
        server.sendToAllTCP(object);
        GameManager.get().getManager(OnScreenMessageManager.class).addMessage(object.toString());
    }

    private void handleChatCommandMessage(Connection connection, Object object) {
        GameManager.get().getManager(CommandsManager.class).callCommand(
                ((ChatCommandMessage) object).getCommandName(),
                ((ChatCommandMessage) object).getArguments(),
                connection.getID()
        );
    }

    private void handleConnectMessage(Connection connection, Object object) {
        // Check to see if the client password matches the server password.
        if (!((ConnectMessage) object).getPassword().equals(GameManager.get().getManager(NetworkManager.class).getServerPassword())) {
            logger.info("{} tried to connect with incorrect password", connection.getID());
            DisconnectMessage msg = new DisconnectMessage(DisconnectReason.INCORRECTPASSWORD);
            server.sendToTCP(connection.getID(), msg);
        } else {
            logger.info("{} connected", connection.getID());
            networkManager.sendChatMessage(connection.getID() + " connected.", "Server");
            networkManager.setClientID(connection.getID());

            // Reply by sending the biome map
            logger.info("Sending tiles");
            TileUpdateMessage message = new TileUpdateMessage();
            ConcurrentHashMap<BiomeType, CopyOnWriteArrayList<Tile>> biomeMap =
                    GameManager.get().getWorld().getBiomeMap();
            
            for (Map.Entry<BiomeType, CopyOnWriteArrayList<Tile>> entry :
                    biomeMap.entrySet()) {
                CopyOnWriteArrayList<Tile> tiles = entry.getValue();
                for (Tile t : tiles) {
                    message.setTile(t);
                    message.setBiomeType(entry.getKey());
                    server.sendToTCP(connection.getID(), message);
                }
            }
            logger.info("Finished sending tiles.");

            // Create a new playerpeon entity for the new connection
            ((ConnectMessage) object).getPlayerEntity().setEntityID();
            ((PlayerAttributes)((ConnectMessage) object).getPlayerEntity()).createInventory();
            GameManager.get().getWorld().addEntity(((ConnectMessage) object).getPlayerEntity());
            networkManager.addConnectionPlayerEntity(connection.getID(), ((ConnectMessage) object).
                    getPlayerEntity().getEntityID());

            // Send every entity one by one to the client
            logger.info("Sending entities");
            SingleEntityUpdateMessage messageEntity = new SingleEntityUpdateMessage();
            for (AbstractEntity e : GameManager.get().getWorld().getEntities()) {
                // TODO: Reimplement Items as currently they crash stackoverflow the server
                // TODO: Fix the enemy class so that they can be sent too
                if (e instanceof ItemEntity || e instanceof Tower) {
                    continue;
                }
                messageEntity.setEntity(e);
                server.sendToAllTCP(messageEntity);
            }
            logger.info("Finished sending entities");

            // Tell client initial data has been sent
            ConfirmConnectionMessage cMessage = new ConfirmConnectionMessage();
            cMessage.setPlayerEntityID(((ConnectMessage) object).getPlayerEntity().getEntityID());
            cMessage.clientID = connection.getID();
            server.sendToTCP(connection.getID(), cMessage);
            String clientUsername = ((ConnectMessage) object).getUsername();
            networkManager.addClientConnection(connection.getID(), clientUsername);

            logger.info("Connection " + connection.getID() + ", initial data sent.");
        }
    }

    private void handleInputMessage(Object object) {
        PlayerPeon entity = (PlayerPeon) GameManager.get().getWorld()
                .getEntityById(((InputMessage) object).getEntityID());
        logger.info("Received remote input at {} for entity {}", ((InputMessage) object).getClickedPosition(),
                ((InputMessage) object).getEntityID());
        entity.notifyTouchDown(((InputMessage) object).getClickedPosition(), ((InputMessage) object).getPointer(),
                ((InputMessage) object).getButton());
    }

    private void handleInputKeyboardMessage(Object object) {
        PlayerPeon entity = (PlayerPeon) GameManager.get().getWorld()
                .getEntityById(((InputKeyboardMessage) object).getEntityID());
        logger.info("Received remote key press {} for entity {}", ((InputKeyboardMessage) object).getKeycode(),
                ((InputKeyboardMessage) object).getEntityID());
        if (((InputKeyboardMessage) object).getInputType() == 0) {
            entity.notifyKeyDown(((InputKeyboardMessage) object).getKeycode());
        } else if (((InputKeyboardMessage) object).getInputType() == 1) {
            entity.notifyKeyUp(((InputKeyboardMessage) object).getKeycode());
        }
    }

    private void handleMouseMovementMessage(Object object) {
        PlayerPeon entity = (PlayerPeon) GameManager.get().getWorld()
                .getEntityById(((MouseMovementMessage) object).getEntityID());
        logger.trace("Received remote input at {} for entity {}", ((MouseMovementMessage) object).getPosition(),
                ((MouseMovementMessage) object).getEntityID());
        float[] position = ((MouseMovementMessage) object).getPosition();
        entity.setPlayerRotation(position[0], position[1]);
    }

    private void handleInventoryUpdate(Connection connection, Object object) {
        logger.info("Received update message for connection {}.", connection.getID());
        InventoryUpdateMessage.UpdateType updateType = ((InventoryUpdateMessage) object).getUpdateType();
        if (updateType == InventoryUpdateMessage.UpdateType.P_ADD) {
            networkManager.addItemPlayerInventory(connection.getID(), ((InventoryUpdateMessage) object).getItem());
        } else if (updateType == InventoryUpdateMessage.UpdateType.P_REMOVE) {
            networkManager.removeItemPlayerInventory(connection.getID(), ((InventoryUpdateMessage) object).getItem());
        }
    }

    private void handleProjectileMessage(Connection connection, Object object) {
        logger.info("Receieved projectile message for connection {}.", connection.getID());
        ProjectileMessage projMsg = ((ProjectileMessage) object);
        if (projMsg.getProjectileMsgType() == ProjectileMessage.ProjectileMsgType.ADD) {
            ((PlayerPeon) GameManager.get().getWorld().getEntityById(projMsg.getPlayerEntityId())).addProjectile();
        } else if (projMsg.getProjectileMsgType() == ProjectileMessage.ProjectileMsgType.SHOOT) {
            ((PlayerPeon) GameManager.get().getWorld().getEntityById(projMsg.getPlayerEntityId())).shootProjectile(projMsg.getScreenX(), projMsg.getScreenY());
        }
    }
}