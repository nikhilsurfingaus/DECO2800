package deco.combatevolved.managers;

import com.esotericsoftware.kryonet.Server;
import deco.combatevolved.handlers.WorldState;
import deco.combatevolved.networking.WorldStateUpdateMessage;
import deco.combatevolved.worlds.AbstractWorld;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest(GameManager.class)
public class NetworkManagerTest {
    private NetworkManager networkManager;
    private Server mockServer;
    private GameManager mockGM;

    @Before
    public void setup() {
        networkManager = new NetworkManager();
        mockGM = mock(GameManager.class);
        mockStatic(GameManager.class);
        when(GameManager.get()).thenReturn(mockGM);
    }

    public void setupServer() {
        mockServer = mock(Server.class);
        networkManager.setServer(mockServer);
    }

    @Test
    public void testSendWorldState() {
        setupServer();

        AbstractWorld mockWorld = mock(AbstractWorld.class);
        WorldState mockWorldState = mock(WorldState.class);
        when(mockGM.getWorld()).thenReturn(mockWorld);
        when(mockWorld.getWorldState()).thenReturn(mockWorldState);
        when(mockWorldState.check()).thenReturn(true);

        networkManager.sendWorldState();

        ArgumentCaptor<WorldStateUpdateMessage> argumentCaptor = ArgumentCaptor.forClass(WorldStateUpdateMessage.class);
        verify(mockServer).sendToAllTCP(argumentCaptor.capture());
        assertEquals(mockWorldState, argumentCaptor.getValue().getWorldState());
    }
}
