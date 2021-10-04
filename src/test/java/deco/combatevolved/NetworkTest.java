package deco.combatevolved;


import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Server;

import deco.combatevolved.entities.dynamicentities.PlayerAttributes;
import deco.combatevolved.entities.items.Item;
import deco.combatevolved.handlers.KeyboardManager;
import deco.combatevolved.managers.*;

import deco.combatevolved.worlds.AbstractWorld;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;


@RunWith(PowerMockRunner.class)
@PrepareForTest({GameManager.class, NetworkManager.class})
public class NetworkTest extends BaseGDXTest {


//    @Mock
//    private GameManager mockGameManager;
//
//    @Mock
//    private ClientListener mockListenerClient;

    private GameManager mockGM;

    private Server mockedServer;

    private Client mockedClient;

    private TextureManager mockTM;

    private TextureAtlas atlas = new TextureAtlas("resources/PlayerCharacter/Soldier/Soldier_Frames.atlas");

    @InjectMocks
    private NetworkManager serverManager;
    @InjectMocks
    private NetworkManager clientManager;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        // Mock the GameManager
        mockGM = mock(GameManager.class);
        mockStatic(GameManager.class);
        when(GameManager.get()).thenReturn(mockGM);

        // Mock the TextureManager
        mockTM = mock(TextureManager.class);
        when(GameManager.getManagerFromInstance(TextureManager.class)).thenReturn(mockTM);
        doReturn(atlas).when(mockTM).getAtlas(anyString());

        // Mock the OnScreenMessageManager
        OnScreenMessageManager mockOSMM = mock(OnScreenMessageManager.class);
        doReturn(mockOSMM).when(mockGM).getManager(OnScreenMessageManager.class);

        // Mock the InputManager
        InputManager mockIM = mock(InputManager.class);
        when(GameManager.getManagerFromInstance(InputManager.class)).thenReturn(mockIM);

        // Mock the KeyboardManager
        KeyboardManager mockKBM = mock(KeyboardManager.class);
        when(GameManager.getManagerFromInstance(KeyboardManager.class)).thenReturn(mockKBM);

        //Mocking the client and Server class
        mockedClient = PowerMockito.mock(Client.class);
        mockedServer = PowerMockito.mock(Server.class);

        //NetworkManagers for a client and a host
        clientManager = new NetworkManager();
        serverManager = new NetworkManager();

        // Set network manager to the server
        doReturn(serverManager).when(mockGM).getManager(NetworkManager.class);
    }

    @After
    public void cleanup() {
        mockedClient.stop();
        mockedClient.close();
        mockedServer.stop();
        mockedServer.close();
    }

    //TODO fix this test
    /*
    @Test (expected = IllegalStateException.class)
    public void clientCannotDeleteUser() {
        clientManager.serverDeleteUser("host");
    }*/


    @Test
    public void testIncrementMessagesReceived() {
        assertEquals(0, serverManager.getMessagesReceived());
        serverManager.incrementMessagesReceived();
        assertEquals(1, serverManager.getMessagesReceived());
    }



 
    @Test
    public void testGetClientUsernameNonExistingKey() {
        serverManager.startHosting("host", "");
        HashMap<Integer, String> expectedResult = new HashMap<>();

        serverManager.addClientConnection(0, "Client1");
        serverManager.addClientConnection(1, "Client2");

        expectedResult.put(0, "Client1");
        expectedResult.put(1, "Client2");
        assertNull(serverManager.getClientUsernameFromConnection(3));
    }

    @Test
    public void testGetClientUsernameExistingKey() {
        serverManager.startHosting("host", "");
        HashMap<Integer, String> expectedResult = new HashMap<>();

        serverManager.addClientConnection(0, "Client1");
        serverManager.addClientConnection(1, "Client2");

        expectedResult.put(0, "Client1");
        expectedResult.put(1, "Client2");
        assertEquals("Client2", serverManager.getClientUsernameFromConnection(1));
    }

    //////////////////////////////////////////
    //Tests involving client and server here//
    //////////////////////////////////////////
    @Test
    public void testGetUsernameAsHost() {
        serverManager.startHosting("Host", "");
        assertEquals("Host", serverManager.getUsername());
    }

    @Test
    public void testGetUsernameAsClient() {
        when(GameManager.get().getPlayerEntityID()).thenReturn(1);
        AbstractWorld mockWorld = mock(AbstractWorld.class);
        when(GameManager.get().getWorld()).thenReturn(mockWorld);
        PlayerAttributes player = new PlayerAttributes(0, 0, 0, "", "Soldier");
        when(mockWorld.getEntityById(1)).thenReturn(player);
        clientManager.connectToHost("", "Client", "");
        assertEquals("Client", clientManager.getUsername());
    }

    @Test
    public void testGetEntityAsHost() {
        // Check if player is created correctly on server setup
        serverManager.startHosting("host", "");
        assertEquals(0, serverManager.getPlayerEntityFromConnection(0));
    }

    @Test
    public void testInventoryManager() {
        serverManager.startHosting("host", "");

        serverManager.addClientConnection(0, "Client1");
        serverManager.addClientConnection(1, "Client2");

        serverManager.addConnectionPlayerEntity(0, 50);
        serverManager.addConnectionPlayerEntity(1, 69);

        serverManager.instantiatePlayerInventory(50, 8);
        serverManager.instantiatePlayerInventory(69, 2);

        Item bigItem = new Item("Big Item", 4);
        Item smallItem = new Item("Small Item", 2);

        serverManager.addItemPlayerInventory(69, bigItem);
        serverManager.addItemPlayerInventory(50, smallItem);

        assertEquals(bigItem, serverManager.getPlayerInventory(69).getStack(0).getItem());
        assertEquals(smallItem, serverManager.getPlayerInventory(50).getStack(0).getItem());

        serverManager.removeItemPlayerInventory(50, smallItem);
        assertEquals(0, serverManager.getPlayerInventory(50).stackCount());
    }
}
