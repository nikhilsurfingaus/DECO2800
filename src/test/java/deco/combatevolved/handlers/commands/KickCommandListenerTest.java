package deco.combatevolved.handlers.commands;

import deco.combatevolved.managers.GameManager;
import deco.combatevolved.managers.NetworkManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest(GameManager.class)
public class KickCommandListenerTest {
    private KickCommandListener listener;
    private NetworkManager mockNM;

    @Before
    public void setup() {
        listener = new KickCommandListener();

        GameManager mockGM = mock(GameManager.class);
        mockStatic(GameManager.class);
        when(GameManager.get()).thenReturn(mockGM);

        mockNM = mock(NetworkManager.class);
        when(mockGM.getManager(NetworkManager.class)).thenReturn(mockNM);
        when(mockNM.getClientUsernameFromConnection(0)).thenReturn("username");
    }

    @Test
    public void testCall() {
        List<Object> arguments = new ArrayList<>();
        arguments.add(0);
        listener.call(arguments, 0);

        verify(mockNM).removeClientConnection(0, "username");
    }
}
