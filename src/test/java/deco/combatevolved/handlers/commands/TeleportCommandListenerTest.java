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
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest(GameManager.class)
public class TeleportCommandListenerTest extends BaseGDXTest {
    private TeleportCommandListener listener;
    private PlayerPeon player1, player2;
    private String textureAtlas = "testFrames";

    @Before
    public void setup() {
        listener = new TeleportCommandListener();
        player1 = new PlayerPeon(0, 0, 0.05f, textureAtlas);
        player2 = new PlayerPeon(0, 0, 0.05f, textureAtlas);

        GameManager mockGM = mock(GameManager.class);
        mockStatic(GameManager.class);
        when(GameManager.get()).thenReturn(mockGM);

        AbstractWorld mockWorld = mock(AbstractWorld.class);
        when(mockGM.getWorld()).thenReturn(mockWorld);
        when(mockWorld.getEntityById(1)).thenReturn(player1);
        when(mockWorld.getEntityById(2)).thenReturn(player2);

        NetworkManager mockNM = mock(NetworkManager.class);
        when(mockGM.getManager(NetworkManager.class)).thenReturn(mockNM);
        when(mockNM.getPlayerEntityFromConnection(0)).thenReturn(1);
        when(mockNM.getPlayerEntityFromConnection(1)).thenReturn(2);
    }

    @Test
    public void testCall() {
        List<Object> arguments = new ArrayList<>();
        arguments.add(0);
        arguments.add(1);
        player1.setPosition(2, 2, 2);
        player2.setPosition(8, 8, 8);
        listener.call(arguments, 0);
    }
}
