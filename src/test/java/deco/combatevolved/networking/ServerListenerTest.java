package deco.combatevolved.networking;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import deco.combatevolved.commands.Command;
import deco.combatevolved.managers.CommandsManager;
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
@PrepareForTest({GameManager.class})
public class ServerListenerTest {
    private ServerListener serverListener;
    private Connection mockConnection;
    private CommandsManager mockCM;

    @Before
    public void setup() {
        GameManager mockGM = mock(GameManager.class);
        mockStatic(GameManager.class);
        when(GameManager.get()).thenReturn(mockGM);
        mockCM = mock(CommandsManager.class);
        NetworkManager mockNM = mock(NetworkManager.class);
        when(mockGM.getManager(CommandsManager.class)).thenReturn(mockCM);
        when(mockGM.getManager(NetworkManager.class)).thenReturn(mockNM);

        Server mockServer = mock(Server.class);
        serverListener = new ServerListener(mockServer);
        mockConnection = mock(Connection.class);
        when(mockConnection.getID()).thenReturn(2);
    }

    @Test
    public void testChatCommandMessage() {
        ChatCommandMessage message = new ChatCommandMessage();
        message.setCommandName("command");
        List<Object> arguments = new ArrayList<>();
        message.setArguments(arguments);
        serverListener.received(mockConnection, message);

        verify(mockCM).callCommand("command", arguments, 2);
    }
}
