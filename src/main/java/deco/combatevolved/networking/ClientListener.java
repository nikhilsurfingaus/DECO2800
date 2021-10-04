package deco.combatevolved.networking;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import deco.combatevolved.entities.enemyentities.BasicEnemy;
import deco.combatevolved.entities.staticentities.ItemEntity;
import deco.combatevolved.handlers.WorldState;
import deco.combatevolved.managers.DayNightCycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import deco.combatevolved.entities.dynamicentities.PlayerPeon;
import deco.combatevolved.managers.GameManager;
import deco.combatevolved.managers.NetworkManager;
import deco.combatevolved.managers.OnScreenMessageManager;

public class ClientListener extends Listener {
    
    private static final Logger logger = LoggerFactory.getLogger(ClientListener.class);

    private Client client;
    private NetworkManager networkManager = GameManager.get().getManager(NetworkManager.class);

    public ClientListener(Client client) {
        this.client = client;
    }

    public void received(Connection connection, Object object) {
        networkManager.incrementMessagesReceived();

        if (!networkManager.isAllowingMessages()) {
            return;
        }

        if (object instanceof DisconnectMessage) {
            handleDisconnectMessage(object);
        } else if (object instanceof TileUpdateMessage) {
            handleTileUpdateMessage(object);
        } else if (object instanceof ChatMessage) {
            GameManager.get().getManager(OnScreenMessageManager.class).addMessage(object.toString());
        } else if (object instanceof SingleEntityUpdateMessage){
            handleSingleEntityUpdateMessage(object);
        } else if (object instanceof TileDeleteMessage) {
            GameManager.get().getWorld().deleteTile(((TileDeleteMessage) object).getTileID());
        } else if (object instanceof EntityDeleteMessage) {
            GameManager.get().getWorld().deleteEntity(((EntityDeleteMessage) object).getEntityID());
        } else if (object instanceof ConfirmConnectionMessage) {
            handleConfirmConnectionMessage(object);
        } else if (object instanceof WorldStateUpdateMessage) {
            handleWorldStateUpdateMessage((WorldStateUpdateMessage) object);
        } else if (object instanceof InventoryUpdateMessage) {
            handleInventoryUpdate(object);
        } else if (object instanceof ProjectileMessage) {
            handleProjectileMessage(connection, object);
        }
    }

    public void disconnected(Connection connection) {
        logger.info("Disconnected from host");
        GameManager.get().getManager(NetworkManager.class).setRecentDisconnect(true, DisconnectReason.GENERIC);
    }

    private void handleTileUpdateMessage(Object object) {
        if (((TileUpdateMessage) object).getBiomeType() == null) {
            GameManager.get().getWorld().updateTile(((TileUpdateMessage) object).getTile());
        } else {
            GameManager.get().getWorld().updateTile(((TileUpdateMessage) object).getTile(),
                    ((TileUpdateMessage) object).getBiomeType());
        }
        GameManager.get().getWorld().generateNeighbours();
    }

    private void handleSingleEntityUpdateMessage(Object object) {
        if (((SingleEntityUpdateMessage) object).getEntity() instanceof PlayerPeon) {
            ((PlayerPeon) ((SingleEntityUpdateMessage) object).getEntity()).createInventory();
        }
        if (((SingleEntityUpdateMessage) object).getEntity() instanceof ItemEntity) {
            ((ItemEntity) ((SingleEntityUpdateMessage) object).getEntity()).createInventory();
        }
        if (((SingleEntityUpdateMessage) object).getEntity() instanceof BasicEnemy) {
            ((BasicEnemy) ((SingleEntityUpdateMessage) object).getEntity()).getHealthBar().setOwner(
                    (BasicEnemy) ((SingleEntityUpdateMessage) object).getEntity());
        }

        GameManager.get().getWorld().updateEntity(((SingleEntityUpdateMessage) object).getEntity());
    }

    private void handleConfirmConnectionMessage(Object object) {
        logger.info("Initial Data received");
        GameManager.get().setPlayerEntityID(((ConfirmConnectionMessage) object).getPlayerEntityID());

        networkManager.setClientID(((ConfirmConnectionMessage) object).clientID);
        logger.info("Client id set to {}", networkManager.getClientID());
        logger.info("Entity id set to {}", GameManager.get().getPlayerEntityID());

        GameManager.get().getManager(NetworkManager.class).setClientFinishLoading();
    }

    /**
     * Handles when a DisconnectMessage is sent.
     */
    private void handleDisconnectMessage(Object object) {
        DisconnectMessage dMessage = (DisconnectMessage)object;

        // Handle disconnection reason for special cases
        switch (dMessage.getReason()) {
            case INCORRECTPASSWORD:
                logger.info("SERVER PASSWORD DIDN'T MATCH. ABORTED CONNECTION.");
                break;
            default:
            break;
        }

        client.stop();
        GameManager.get().getManager(NetworkManager.class).setRecentDisconnect(true, dMessage.getReason());
    }

    private void handleWorldStateUpdateMessage(WorldStateUpdateMessage message) {
        WorldState worldState = message.getWorldState();
        GameManager.get().getManager(DayNightCycle.class).setCycleCode(worldState.getDayNightCycleCode());
        GameManager.get().getWorld().getWeather().setForecast(worldState.getForecast());
    }

    private void handleInventoryUpdate(Object object) {
        //logger.info("Received inventory update message.");
        InventoryUpdateMessage.UpdateType updateType = ((InventoryUpdateMessage) object).getUpdateType();
        if (updateType == InventoryUpdateMessage.UpdateType.P_ADD) {
            networkManager.addItemPlayerInventory(((InventoryUpdateMessage) object).getEntityID(),
                    ((InventoryUpdateMessage) object).getItem());
        } else if (updateType == InventoryUpdateMessage.UpdateType.P_REMOVE) {
            networkManager.removeItemPlayerInventory(((InventoryUpdateMessage) object).getEntityID(),
                    ((InventoryUpdateMessage) object).getItem());
        } else if (updateType == InventoryUpdateMessage.UpdateType.I_ADD) {
            networkManager.addItemToItemEntityInventory(((InventoryUpdateMessage) object).getEntityID(),
                    ((InventoryUpdateMessage) object).getItem(), ((InventoryUpdateMessage) object).getItemNum());
        } else if (updateType == InventoryUpdateMessage.UpdateType.I_REMOVE) {
            networkManager.removeItemToItemEntityInventory(((InventoryUpdateMessage) object).getEntityID());
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