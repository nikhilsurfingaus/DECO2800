package deco.combatevolved.commands;

import deco.combatevolved.managers.CommandsManager;
import deco.combatevolved.managers.GameManager;
import deco.combatevolved.managers.NetworkManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({GameManager.class})
public class CommandsManagerTest {
    private CommandsManager manager;
    private Command mockCommand;
    private List<Object> args = new LinkedList<>();

    @Before
    public void setup() {
        GameManager mockGM = mock(GameManager.class);
        mockStatic(GameManager.class);
        when(GameManager.get()).thenReturn(mockGM);
        NetworkManager mockNM = mock(NetworkManager.class);
        when(mockNM.getID()).thenReturn(1);
        when(mockGM.getManager(NetworkManager.class)).thenReturn(mockNM);

        manager = new CommandsManager();
        mockCommand = mock(Command.class);
        when(mockCommand.getName()).thenReturn("command");
        when(mockCommand.checkPermission(Command.Permission.USER)).thenReturn(true);
        when(mockCommand.cleanArguments(new ArrayList<>())).thenReturn(args);
        manager.addCommand(mockCommand);
    }

    @Test
    public void testGetCommands() {
        assertEquals(mockCommand, manager.getCommands().get(0));
    }

    @Test
    public void testCallCommand() {
        manager.callCommand("command");
        verify(mockCommand).call(args, 1);
    }
}
