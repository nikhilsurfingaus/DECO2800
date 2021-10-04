package deco.combatevolved.commands;

import deco.combatevolved.handlers.commands.AbstractCommandListener;
import deco.combatevolved.managers.CommandsManager;
import deco.combatevolved.managers.GameManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({GameManager.class})
public class CommandFactoryTest {
    private CommandsManager mockCM;
    private GameManager mockGM;
    private AbstractCommandListener mockCommandListener;

    private void baseTests(Command command) {
        assertEquals("command", command.getName());
        assertEquals(command.getListener(), mockCommandListener);
        assertEquals(Command.Permission.USER, command.getPermission());
    }

    private void argTests(Command command, HashMap<String, ArgumentType> arguments) {
        for (Map.Entry<String, ArgumentType> entry: arguments.entrySet()) {
            assertTrue(command.getArguments().containsKey(entry.getKey()));
            assertEquals(entry.getValue(), command.getArguments().get(entry.getKey()));
        }
    }

    @Before
    public void setup() {
        mockGM = mock(GameManager.class);
        mockStatic(GameManager.class);
        when(GameManager.get()).thenReturn(mockGM);

        mockCM = mock(CommandsManager.class);
        when(mockGM.getManager(CommandsManager.class)).thenReturn(mockCM);

        mockCommandListener = mock(AbstractCommandListener.class);
    }

    @Test
    public void testBaseCommand() {
        Command command = CommandFactory.getCommand("command", mockCommandListener,
                Command.Permission.USER);
        baseTests(command);
    }

    @Test
    public void testArgsCommand() {
        HashMap<String, ArgumentType> arguments = new HashMap<>();
        arguments.put("arg1", ArgumentType.TEXT);
        arguments.put("arg2", ArgumentType.NUMBER);
        Command command = CommandFactory.getCommand("command", mockCommandListener,
                Command.Permission.USER, arguments);
        baseTests(command);
        argTests(command, arguments);
    }

    @Test
    public void testNoArgsCommand() {
        HashMap<String, ArgumentType> arguments = new HashMap<>();
        Command command = CommandFactory.getCommand("command", mockCommandListener,
                Command.Permission.USER, arguments);
        baseTests(command);
        argTests(command, arguments);
    }

    @Test
    public void testDescriptionCommand() {
        Command command = CommandFactory.getCommand("command", mockCommandListener,
                Command.Permission.USER, "description");
        baseTests(command);
        assertEquals("description", command.getDescription());
    }

    @Test
    public void testDuplicateCommand() {
        Command mockCommand = mock(Command.class);
        when(mockCommand.toString()).thenReturn("command");
        List<Command> mockCommandsList = new ArrayList<>();
        mockCommandsList.add(mockCommand);
        when(mockCM.getCommands()).thenReturn(mockCommandsList);
        CommandFactory.getCommand("command", mockCommandListener, Command.Permission.USER);
        verify(mockCM, never()).addCommand(any(Command.class));
    }

    @Test
    public void testCommandAdded() {
        Command mockCommand = mock(Command.class);
        when(mockCommand.toString()).thenReturn("notCommand");
        List<Command> mockCommandsList = new ArrayList<>();
        mockCommandsList.add(mockCommand);
        when(mockCM.getCommands()).thenReturn(mockCommandsList);
        CommandFactory.getCommand("command", mockCommandListener, Command.Permission.USER);
        verify(mockCM).addCommand(any(Command.class));
    }
}
