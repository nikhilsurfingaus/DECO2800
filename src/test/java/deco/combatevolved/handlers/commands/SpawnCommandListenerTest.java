package deco.combatevolved.handlers.commands;

import deco.combatevolved.BaseGDXTest;
import deco.combatevolved.entities.dynamicentities.PlayerPeon;
import deco.combatevolved.managers.GameManager;
import deco.combatevolved.managers.NetworkManager;
import deco.combatevolved.worlds.AbstractWorld;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest(GameManager.class)
public class SpawnCommandListenerTest extends BaseGDXTest {
    private SpawnCommandListener listener;
    private PlayerPeon playerPeon;
    private String textureAtlas = "testFrames";

    @Before
    public void setup() {
        listener = new SpawnCommandListener();
        playerPeon = new PlayerPeon(0, 0, 0.05f, textureAtlas);

        GameManager mockGM = mock(GameManager.class);
        mockStatic(GameManager.class);
        when(GameManager.get()).thenReturn(mockGM);

        AbstractWorld mockWorld = mock(AbstractWorld.class);
        when(mockGM.getWorld()).thenReturn(mockWorld);
        when(mockWorld.getEntityById(1)).thenReturn(playerPeon);

        NetworkManager mockNM = mock(NetworkManager.class);
        when(mockGM.getManager(NetworkManager.class)).thenReturn(mockNM);
        when(mockNM.getPlayerEntityFromConnection(0)).thenReturn(1);
    }

    @Test
    public void testCall() {
        playerPeon.setPosition(5, 5, 5);
        listener.call(new ArrayList<>(), 0);

        assertEquals(0, playerPeon.getCol(), 0);
        assertEquals(0, playerPeon.getRow(), 0);
        assertEquals(0, playerPeon.getZ(), 0);
    }
}
