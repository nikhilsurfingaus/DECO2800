package deco.combatevolved.managers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Server;

import deco.combatevolved.entities.dynamicentities.Projectile;
import deco.combatevolved.entities.enemyentities.BasicEnemy;
import deco.combatevolved.entities.items.Inventory;
import deco.combatevolved.entities.items.Item;
import deco.combatevolved.entities.staticentities.ItemEntity;
import deco.combatevolved.entities.staticentities.defensivetowers.Tower;
import deco.combatevolved.networking.*;
import deco.combatevolved.CombatEvolvedGame;
import deco.combatevolved.entities.AbstractEntity;
import deco.combatevolved.entities.dynamicentities.PlayerAttributes;
import deco.combatevolved.entities.dynamicentities.PlayerPeon;
import deco.combatevolved.handlers.InputTransmissionManager;
import deco.combatevolved.handlers.KeyboardManager;
import deco.combatevolved.mainmenu.ErrorScreen;
import deco.combatevolved.mainmenu.MainMenuScreen;
import deco.combatevolved.worlds.Tile;

import org.objenesis.strategy.StdInstantiatorStrategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * NetworkManager
 *
 * Handles both server and client side of the networking stack.
 */
public class NetworkManager extends TickableManager  {
    private static final Logger logger = LoggerFactory.getLogger(NetworkManager.class);

    private boolean isConnected = false;
    private boolean isHosting = false;
    private boolean recentDisconnect = false;

    private DisconnectReason disconnectReason = DisconnectReason.GENERIC;

    private int messagesReceived = 0;
    private int messagesSent = 0;

    private static final int BUFFER_SIZE = 1048576;
    private static final int TCP_PORT = 54554;
    private static final int UDP_PORT = 54776;

    private Server server = null;
    private Client client = null;

    private int clientID = 0;

    private HashMap<Integer, String> userConnections = new HashMap<>();

    // Unique identifier for this application. Attempting to connect to a server
    // with the same username as another User on that server is not allowed.
    private String localUsername;
    private String serverPassword;

    // Used to check if the client has finished getting data from the host when it loads
    private boolean clientLoadStatus = true;

    // Mapping of connection to controlled entity
    private HashMap<Integer, Integer> userEntities = new HashMap<>();

    // Mapping of player inventories to controlled entity
    private HashMap<Integer, Inventory> userInventories = new HashMap<>();

    // Flag to ensure that messages received by the client too early are ignored
    private boolean allowMessages = false;

    /**
     * Increments the number of messages received.
     * <p>
     *     Used be the client and server listeners to update the number of
     *     messages received by this particular NetworkManager.
     * </p>
     */
    public void incrementMessagesReceived() {
        messagesReceived++;
    }

    /**
     * Add a user connection to the list of connections
     * @param connectionID - the id of the connected user
     * @param username - the username of the connected user
     */
    public void addClientConnection(Integer connectionID, String username) {
        sendChatMessage("Joined the game.", username);

        if (!userConnections.containsKey(connectionID)) {
            userConnections.put(connectionID, username);
        } else {
            logger.error("A client is attempting to connect on the same ConnectionID as another client!");
        }
    }

    /**
     * Deletes a mapping between the given client and user.
     * <p>
     *     Once a client disconnects from the server (for whatever reason),
     *     if they had a user then it will be deleted. Entity will also be deleted
     * </p>
     * @param connectionID The connectionID of the client to be removed.
     * @param username The username of the client to be removed.
     */
    public void removeClientConnection(Integer connectionID, String username) {
        String message = "Left the game.";
        GameManager.get().getManager(OnScreenMessageManager.class).addMessage("[" + username + "] " + message);
        server.sendToAllTCP(new ChatMessage(username, message, this.getID()));

        userConnections.remove(connectionID, username);
        Integer entityID = userEntities.getOrDefault(connectionID, null);
        userEntities.remove(connectionID);
        if (entityID != null) {
            GameManager.get().getWorld().deleteEntity(entityID);
        }
    }

    /**
     * Retrieves a username mapped to a particular connection ID.
     * @param connectionID The connection ID of the client to retrieve the username of.
     * @return the username mapped to the given connection ID, or null if it doesn't exist.
     */
    public String getClientUsernameFromConnection(int connectionID) {
        return userConnections.getOrDefault(connectionID, null);
    }

    /**
     * Returns the number of messages received
     * @return the number of messages received
     */
    public int getMessagesReceived() {
        return messagesReceived;
    }

    /**
     * Returns the number of messages sent
     * @return the number of messages sent
     */
    public int getMessagesSent() {
        return messagesSent;
    }

    /**
     * Is this user the host?
     * @return true if hosting, false otherwise
     */
    public boolean isHost() {
        return isHosting;
    }

    public void setServerPassword(String serverPassword) {
        this.serverPassword = serverPassword;
    }

    public String getServerPassword() {
        return serverPassword;
    }

    /**
     * Connects to a host at specified ip and port
     * @param ip - the ip of the host
     * @param username - the username to connect with
     * @return boolean - Was the connection successful?
     */
	public boolean connectToHost(String ip, String username, String password) {
        // Set the clientLoadStatus marker to false to tell the GameScreen to not finish initialising till all the
        // data has been sent to the client

        // To quicken dev process empty ip will default to localhost
        if (ip == null || ip.isEmpty()) {
            ip = "localhost";
        }

	    setClientStartLoading();

	    if (username == null || password == null) {
            return false;
        }

        recentDisconnect = false;
        this.localUsername = username;
        this.serverPassword = password;
        logger.info("Connecting with username {}", this.localUsername);
        logger.info("Connecting with password {}", this.serverPassword);
        if (isConnected || isHosting) {
            return false;
        }
        isConnected = true;
        GameManager.get().getManager(OnScreenMessageManager.class).addMessage("Attempting to connect to server...");
        client = new Client(BUFFER_SIZE, BUFFER_SIZE);

        // Register all classes
        Kryo kyro = client.getKryo();
        // KYRO auto registration
        kyro.setRegistrationRequired(false);
        ((Kryo.DefaultInstantiatorStrategy)
                kyro.getInstantiatorStrategy()).setFallbackInstantiatorStrategy(new StdInstantiatorStrategy());

        // Start the client on ports 54555 and 54777
        client.start();
        try {
            client.connect(5000, ip, TCP_PORT, UDP_PORT);
        } catch (IOException e) {
            logger.error("Failed initial connection due to {}", e.toString());
            GameManager.get().getManager(OnScreenMessageManager.class).addMessage("Connection failed.");
            disconnectReason = DisconnectReason.CONNECTIONFAILED;
            isConnected = false;
            recentDisconnect = true;
            return false;
        }

        // Add all listeners for the client, allowing it to receive information from the host.
        client.addListener(new ClientListener(client));

        logger.info("Sending initial connect message to host, requesting initial information to be sent to this client.");
        ConnectMessage request = new ConnectMessage();
        request.setUsername(username);
        request.setPassword(password);

        logger.info("Creating Player Entity");
        // TODO: This was updated from PlayerPeon, needs to actually have a choice in class.
        PlayerAttributes playerAttributes = (PlayerAttributes)
                GameManager.get().getWorld().getEntityById(GameManager.get().getPlayerEntityID());
        playerAttributes.removeControls();
        request.setPlayerEntity(playerAttributes);

        logger.info("Sending client information to host");
        client.sendTCP(request);
        allowMessages = true;
        logger.info("Setup controls");
        clientControlListenerSetup();

        // Broadcast to this client in-game that it has successfully connected to the server.
        GameManager.get().getManager(OnScreenMessageManager.class).addMessage("Connected to server.");
        logger.info("Client was successfully initialised, waiting to receive World data from host");

        return true;
    }

    /**
     * Hosts a game with the specified username on ports (54555, 54777).
     * @param username - the username of the host
     * @return true if
     */
	public boolean startHosting(String username, String password) {
        this.localUsername = username;
        this.serverPassword = password;
        addConnectionPlayerEntity(0, GameManager.get().getPlayerEntityID());
        userConnections.put(0, username);
        logger.info("Hosting with username {}", this.localUsername);
        if (isConnected || isHosting) {
            return false;
        }
        isHosting = true;

        server = new Server(BUFFER_SIZE, BUFFER_SIZE);

        // Register the classes to be serialised with kryo
        Kryo kyro = server.getKryo();
        // KYRO auto registration
        kyro.setRegistrationRequired(false);
        ((Kryo.DefaultInstantiatorStrategy)
                kyro.getInstantiatorStrategy()).setFallbackInstantiatorStrategy(new StdInstantiatorStrategy());

        // Start the server on ports 54555 and 54777
        server.start();
        try {
            server.bind(TCP_PORT, UDP_PORT);
        } catch (IOException e) {
            logger.error("Failed to bind server to ports 54555 and 54777");
            return false;
        }

        // Add all listeners for the host, allowing it to receive information from all its clients.
        logger.info("Attempting to add message listeners to server.");
        server.addListener(new ServerListener(server));

        logger.info("HOST WAS INITIALISED SUCCESSFULLY.");
        return true;
    }

    public void addConnectionPlayerEntity(int connectionID, int playerEntityID) {
	    userEntities.put(connectionID, playerEntityID);
    }

    /**
     * On tick method
     * @param i
     */
    @Override
    public void onTick(long i) {
        if (isConnected) {
            onTickClient();
        } else if (isHosting) {
            onTickHost();
        } else if (recentDisconnect) {
            // If disconnected recently, check if it was a timeout and move the player to the ErrorScreen.

            if (!clientLoadStatus && disconnectReason == DisconnectReason.GENERIC) {
                disconnectReason = DisconnectReason.TIMEOUT;
            }
            GameManager.get().getGame().setScreen(new ErrorScreen(new CombatEvolvedGame(), 
                    "Disconnected from server", disconnectReason.getMessage()));
        }
    }

    /**
     * Runs each tick for hosts.
     */
    private void onTickHost() {
        //Only send messages if there are clients, send only non-static entities
        for (AbstractEntity e : GameManager.get().getWorld().getNonStaticAndItemEntities()) {
            if (e instanceof PlayerPeon || e instanceof Tower || e instanceof BasicEnemy) {
                logger.trace("Sending entity: {}", e);
                SingleEntityUpdateMessage message = new SingleEntityUpdateMessage();
                message.setEntity(e);
                server.sendToAllTCP(message);

                if (e instanceof ItemEntity) {
                    sendInventoryUpdateMessage((ItemEntity) e, InventoryUpdateMessage.UpdateType.I_ADD);
                }
            }
        }

        /* WIP - sending only updated entities
        while(true) {
            AbstractEntity entity = GameManager.get().getManager(UpdateEntityManager.class).getEntityForSending();
            if (entity == null) {
                break;
            }

            if (entity instanceof PlayerPeon) {
                SingleEntityUpdateMessage message = new SingleEntityUpdateMessage();
                message.setEntity(entity);
                server.sendToAllTCP(message);
            }
        }
        */
        sendWorldState();
    }

    /**
     * Runs each tick for clients.
     */
    private void onTickClient(){
        // tick weather on client
        GameManager.get().getWorld().tickWeather();
    }


    /**
     * Sends a chat message to the network clients
     * @param message
     */
    public void sendChatMessage(String message, String username) {
        if (isHosting) {
            logger.info("Sending chat message");
            GameManager.get().getManager(OnScreenMessageManager.class).addMessage("[" + username + "] " + message);
            server.sendToAllTCP(new ChatMessage(username, message, this.getID()));
        } else {
            client.sendTCP(new ChatMessage(username, message, this.getID()));
        }
    }

    /**
     * Get the ID of the client
     * @return int - the client id
     */
    public int getID() {
        if (isHosting) {
            return 0;
        }
        if (isConnected) {
            return client.getID();
        }
        return -1;
    }

    /**
     * Get the username of a host/client
     * @return String - the username of the host/client
     */
    public String getUsername() {
        return localUsername;
    }

    /**
     * Delete the given tile
     * @param t - the tile to delete
     */
    public void deleteTile(Tile t) {
        TileDeleteMessage msg = new TileDeleteMessage();
        msg.setTileID(t.getTileID());
        if (isHosting) {
            logger.info("Sending delete tile message");
            server.sendToAllTCP(msg);
        } else {
            client.sendTCP(msg);
        }
    }



    /**
     * Delete the given entity.
     * <p>
     *     The host will ensure the entity has also been deleted in its own game.
     *     Client deletion of entities is handled in the EntityDeleteMessage class,
     *     which will be sent by the server once it receives the EntityDeleteMessage
     *     from the client.
     * </p>
     * @param e - the entity to delete
     */
    public void deleteEntity(AbstractEntity e) {
        EntityDeleteMessage msg = new EntityDeleteMessage();
        msg.setEntityID(e.getEntityID());
        if (isHosting) {
            logger.info("Sending delete entity message");
            server.sendToAllTCP(msg);
        } else {
            client.sendTCP(msg);
        }
    }

    public void addEntity(AbstractEntity e) {
        if (isHost()) {
            if ((e instanceof ItemEntity || e instanceof BasicEnemy))
                return;
            SingleEntityUpdateMessage message = new SingleEntityUpdateMessage();
            message.setEntity(e);
            server.sendToAllTCP(message);
        }
    }

    public int getPlayerEntityFromConnection(int connectionID) {
        return userEntities.get(connectionID);
    }

    public void sendInput(float[] clickedPosition, int pointer, int button) {
        InputMessage message = new InputMessage();
        message.setEntityID(GameManager.get().getPlayerEntityID());
        message.setClickedPosition(clickedPosition);
        message.setPointer(pointer);
        message.setButton(button);
        client.sendTCP(message);
    }

    public void sendKeyboardInput(int keycode, int inputType) {
        if (!GameManager.getManagerFromInstance(OnScreenMessageManager.class).isTyping()) {
            InputKeyboardMessage message = new InputKeyboardMessage();
            message.setEntityID(GameManager.get().getPlayerEntityID());
            message.setKeycode(keycode);
            message.setInputType(inputType);
            logger.info("Sent key press {} for entity {}", keycode, GameManager.get().getPlayerEntityID());
            client.sendTCP(message);
        }
    }

    public void sendMouseMovement(float[] position) {
        MouseMovementMessage message = new MouseMovementMessage();
        message.setEntityID(GameManager.get().getPlayerEntityID());
        message.setPosition(position);
        client.sendTCP(message);
    }

    public void sendWorldState() {
        if (GameManager.get().getWorld().getWorldState().check()) {
            logger.info("Sending world state");
            WorldStateUpdateMessage message = new WorldStateUpdateMessage(GameManager.get().getWorld().getWorldState());
            server.sendToAllTCP(message);
        }
    }

    // Set up player control transmission
    public void clientControlListenerSetup() {
        InputTransmissionManager inputTransmissionManager = new InputTransmissionManager();
        GameManager.get().addManager(inputTransmissionManager);

        GameManager.getManagerFromInstance(InputManager.class).addTouchDownListener(inputTransmissionManager);
        GameManager.getManagerFromInstance(InputManager.class).addMouseMovedListener(inputTransmissionManager);
        GameManager.getManagerFromInstance(KeyboardManager.class).registerForKeyDown(inputTransmissionManager);
        GameManager.getManagerFromInstance(KeyboardManager.class).registerForKeyUp(inputTransmissionManager);
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public Map<Integer, String> getUserConnections() {
        return userConnections;
    }

    public void setClientFinishLoading() {
        clientLoadStatus = true;
    }

    public void setClientStartLoading() {
        clientLoadStatus = false;
    }

    // Used to check if the client has finished getting data from the host when it loads
    public boolean getClientLoadStatus() {
        return clientLoadStatus;
    }

    /**
     * Instantiates the player inventory on the server side to stop having to
     * send objects on client updates.
     *
     * @param playerEntityId id of the player
     * @param capacity the capacity of the inventory
     */
    public void instantiatePlayerInventory(int playerEntityId, int capacity) {
        int connectionID = userEntities.get(getConnectionFromEntityID(playerEntityId));
        userInventories.put(connectionID, new Inventory(capacity));
    }

    /**
     * Gets the players entity based on the connection id.
     *
     * @param playerEntityId the id of the connected client's inventory.
     * @return the inventory of the player
     */
    public Inventory getPlayerInventory(int playerEntityId) {
        int connectionID = userEntities.get(getConnectionFromEntityID(playerEntityId));
        return userInventories.get(connectionID);
    }

    /**
     * Adds an item to the inventory handled by the server.
     *
     * @param playerEntityId id of the player
     * @param item item to add
     */
    public void addItemPlayerInventory(int playerEntityId, Item item) {
        int connectionID = userEntities.get(getConnectionFromEntityID(playerEntityId));
        Inventory playerInventory = userInventories.get(connectionID);
        playerInventory.addItem(item);
    }

    /**
     * Adds an item to the inventory handled by the server.
     *
     * @param entityId id of the player
     * @param item item to add
     */
    public void addItemToItemEntityInventory(int entityId, Item item, int itemNum) {
        ItemEntity entity = (ItemEntity) GameManager.get().getWorld().getEntityById(entityId);

        if (itemNum <= 0) {
            GameManager.get().getWorld().deleteEntity(entityId);
            return;
        }

        int i = 0;
        while (i > entity.getInventory().stackCount()) {
            if (entity.getInventory().getStack(i).getItem() == item) {
                entity.getInventory().getStack(i).setNumItems(itemNum);
                return;
            }
            i++;
        }
        entity.getInventory().addItem(item, itemNum);
    }

    public void removeItemToItemEntityInventory(int entityId) {
        GameManager.get().getWorld().deleteEntity(entityId);
    }


    public void sendInventoryUpdateMessage(ItemEntity e, InventoryUpdateMessage.UpdateType updateType) {
        for (int i = 0; i < e.getInventory().stackCount(); i++) {
            InventoryUpdateMessage messageInventory = new InventoryUpdateMessage();
            messageInventory.setEntityID(e.getEntityID());
            messageInventory.setItem(e.getInventory().getStack(i).getItem());
            messageInventory.setUpdateType(updateType);
            messageInventory.setItemNum(e.getInventory().getStack(i).getNumItems());

            server.sendToAllTCP(messageInventory);
        }
    }



    /**
     * Removes an item from the inventory handled by the server.
     *
     * @param playerEntityId id of the player
     * @param item item to add
     */
    public void removeItemPlayerInventory(int playerEntityId, Item item) {
        int connectionID = userEntities.get(getConnectionFromEntityID(playerEntityId));
        Inventory playerInventory = userInventories.get(connectionID);
        playerInventory.removeItem(item);
    }

    /**
     * Gets the connection ID of a player from the players entity id.
     *
     * @param playerEntityID player's entity id
     * @return connection id of the player, or -1 if doesn't exist
     */
    public int getConnectionFromEntityID(int playerEntityID) {
        for (Map.Entry<Integer, Integer> entries : userEntities.entrySet()) {
            if (entries.getValue() == playerEntityID) {
                return entries.getKey();
            }
        }
        return -1;
    }

    /**
     * For sending the projectile between host and client.
     * @param playerEntityID
     * @param projectile
     * @param projMsgType
     * @param screenX
     * @param screenY
     */
    public void sendProjectileUpdate(int playerEntityID, Projectile projectile, ProjectileMessage.ProjectileMsgType projMsgType, int screenX, int screenY) {
        ProjectileMessage message = new ProjectileMessage();
        message.setPlayerEntityId(playerEntityID);
        message.setProjectile(projectile);
        message.setScreenX(screenX);
        message.setScreenY(screenY);
        message.setProjectileMsgType(projMsgType);
        server.sendToAllTCP(message);
    }

    public void sendCommand(String name, List<Object> arguments) {
        ChatCommandMessage message = new ChatCommandMessage();
        message.setCommandName(name);
        message.setArguments(arguments);
        client.sendTCP(message);
    }

    public Integer getConnectionIdFromUsername(String username) {
        Integer connectionId = null;
        for (Map.Entry<Integer, String> entry : getUserConnections().entrySet()) {
            if (entry.getValue().equals(username)) {
                connectionId = entry.getKey();
            }
        }
        return connectionId;
    }
    
    /**
     * Gets the current connection status of the client.
     * @return The connection status, true if client is actually host
     */
    public Boolean getConnectionStatus() {
        return isConnected || isHosting;
    }

    /**
     * Sets if the client has recently disconnected or not 
     * @param status If the client has recently disconnected
     * @param reason Reason for disconnecting (if null, assumed it's generic)
     */
    public void setRecentDisconnect(Boolean status, DisconnectReason reason) {
        if (status) {
            isConnected = false;
        }
        recentDisconnect = status;

        if (reason == null) {
            disconnectReason = DisconnectReason.GENERIC;
        } else {
            disconnectReason = reason;
        }
    }

    /**
     * Gets if the client recently disconnected from the server
     * @return The status of recent disconnect
     */
    public Boolean getRecentDisconnect() {
        return recentDisconnect;
    }

    /**
     * Gets the reason for the recent disconnect. 
     * @return DisconnectReason of why the client disconnected.
     */
    public DisconnectReason getDisconnectReason() {
        return disconnectReason;
    }

    public Map<Integer, Integer> getUserEntities() {
        return userEntities;
    }

    public boolean isAllowingMessages() {
        return allowMessages;
    }

    public void setAllowMessages(boolean status) {
        allowMessages = status;
    }

    public void setClientID(int id) {
        clientID = id;
    }

    public int getClientID() {
        return clientID;
    }
}