package deco.combatevolved.commands;

import deco.combatevolved.handlers.commands.AbstractCommandListener;
import deco.combatevolved.managers.GameManager;
import deco.combatevolved.managers.NetworkManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.support.membermodification.MemberModifier.replace;

@RunWith(PowerMockRunner.class)
@PrepareForTest({GameManager.class})
public class CommandTest {
    private Command command;
    private AbstractCommandListener mockCommandListener;
    private NetworkManager mockNM;

    @Before
    public void setup() {
        mockCommandListener = mock(AbstractCommandListener.class);
        command = new Command();
        GameManager mockGM = mock(GameManager.class);
        mockNM = mock(NetworkManager.class);
        mockStatic(GameManager.class);
        when(GameManager.get()).thenReturn(mockGM);
        when(mockGM.getManager(NetworkManager.class)).thenReturn(mockNM);
    }

    @Test
    public void testName() {
        command.setName("command");
        assertEquals("command", command.getName());
    }

    @Test
    public void testListener() {
        command.setListener(mockCommandListener);
        assertEquals(mockCommandListener, command.getListener());
    }

    @Test
    public void testDescription() {
        command.setDescription("Description");
        assertEquals("Description", command.getDescription());
    }

    @Test
    public void testAddTextArgument() {
        command.addArgument("arg1", ArgumentType.TEXT);
        assertTrue(command.getArguments().containsKey("arg1"));
        assertEquals(ArgumentType.TEXT, command.getArguments().get("arg1"));
    }

    @Test
    public void testAddNumberArgument() {
        command.addArgument("arg1", ArgumentType.NUMBER);
        assertTrue(command.getArguments().containsKey("arg1"));
        assertEquals(ArgumentType.NUMBER, command.getArguments().get("arg1"));
    }

    @Test
    public void testAddUserArgument() {
        command.addArgument("arg1", ArgumentType.USER);
        assertTrue(command.getArguments().containsKey("arg1"));
        assertEquals(ArgumentType.USER, command.getArguments().get("arg1"));
    }

    @Test
    public void testPermission() {
        command.setPermission(Command.Permission.USER);
        assertEquals(Command.Permission.USER, command.getPermission());
    }

    @Test
    public void testCheckPermissionClient() {
        command.setPermission(Command.Permission.CLIENT);
        assertTrue(command.checkPermission(Command.Permission.USER));
        assertTrue(command.checkPermission(Command.Permission.ADMIN));
        assertTrue(command.checkPermission(Command.Permission.OWNER));
    }

    @Test
    public void testCheckPermissionUser() {
        command.setPermission(Command.Permission.USER);
        assertTrue(command.checkPermission(Command.Permission.USER));
        assertTrue(command.checkPermission(Command.Permission.ADMIN));
        assertTrue(command.checkPermission(Command.Permission.OWNER));
    }

    @Test
    public void testCheckPermissionAdmin() {
        command.setPermission(Command.Permission.ADMIN);
        assertFalse(command.checkPermission(Command.Permission.USER));
        assertTrue(command.checkPermission(Command.Permission.ADMIN));
        assertTrue(command.checkPermission(Command.Permission.OWNER));
    }

    @Test
    public void testCheckPermissionOwner() {
        command.setPermission(Command.Permission.OWNER);
        assertFalse(command.checkPermission(Command.Permission.USER));
        assertFalse(command.checkPermission(Command.Permission.ADMIN));
        assertTrue(command.checkPermission(Command.Permission.OWNER));
    }

    private void setupMultipleArguments() {
        command.addArgument("arg1", ArgumentType.TEXT);
        command.addArgument("arg2", ArgumentType.NUMBER);
    }

    @Test
    public void testCleanText() {
        command.addArgument("arg1", ArgumentType.TEXT);
        List<String> rawArguments = new LinkedList<>();
        rawArguments.add("a");
        List<Object> cleanArguments = command.cleanArguments(rawArguments);
        assertTrue(cleanArguments.get(0) instanceof String);
        assertEquals("a", cleanArguments.get(0));
    }

    @Test
    public void testCleanNumberValid() {
        command.addArgument("arg1", ArgumentType.NUMBER);
        List<String> rawArguments = new LinkedList<>();
        rawArguments.add("1");
        List<Object> cleanArguments = command.cleanArguments(rawArguments);
        assertTrue(cleanArguments.get(0) instanceof Integer);
        assertEquals(1, cleanArguments.get(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCleanNumberNonInteger() {
        command.addArgument("arg1", ArgumentType.NUMBER);
        List<String> rawArguments = new LinkedList<>();
        rawArguments.add("one");
        command.cleanArguments(rawArguments);
    }

    @Test
    public void testCleanUserValid() {
        when(mockNM.getConnectionIdFromUsername("username")).thenReturn(1);
        command.addArgument("arg1", ArgumentType.USER);
        List<String> rawArguments = new LinkedList<>();
        rawArguments.add("username");
        List<Object> cleanArguments = command.cleanArguments(rawArguments);
        assertTrue(cleanArguments.get(0) instanceof Integer);
        assertEquals(1, cleanArguments.get(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCleanUserInvalidUsername() {
        when(mockNM.getConnectionIdFromUsername("username")).thenReturn(null);
        command.addArgument("arg1", ArgumentType.USER);
        List<String> rawArguments = new LinkedList<>();
        rawArguments.add("username");
        command.cleanArguments(rawArguments);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCleanTooManyArguments() {
        setupMultipleArguments();
        List<String> rawArguments = new LinkedList<>();
        rawArguments.add("a");
        rawArguments.add("1");
        rawArguments.add("22");
        command.cleanArguments(rawArguments);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCleanTooFewArguments() {
        setupMultipleArguments();
        List<String> rawArguments = new LinkedList<>();
        rawArguments.add("a");
        command.cleanArguments(rawArguments);
    }

    @Test
    public void testToString() {
        command.setName("command");
        setupMultipleArguments();

        assertEquals("command <arg1:text> <arg2:number>", command.toString());
    }

    @Test
    public void testCallHost() {
        when(mockNM.isHost()).thenReturn(true);
        setupMultipleArguments();
        command.setListener(mockCommandListener);
        List<String> rawArguments = new LinkedList<>();
        rawArguments.add("a");
        rawArguments.add("1");
        List<Object> cleanArguments = command.cleanArguments(rawArguments);
        command.call(cleanArguments, 0);
        verify(mockCommandListener).call(cleanArguments, 0);
    }

    @Test
    public void testCallClient() {
        when(mockNM.isHost()).thenReturn(false);
        setupMultipleArguments();
        command.setPermission(Command.Permission.CLIENT);
        command.setListener(mockCommandListener);
        List<String> rawArguments = new LinkedList<>();
        rawArguments.add("a");
        rawArguments.add("1");
        List<Object> cleanArguments = command.cleanArguments(rawArguments);
        command.call(cleanArguments, 0);
        verify(mockCommandListener).call(cleanArguments, 0);
    }

    @Test
    public void testCallNetwork() {
        when(mockNM.isHost()).thenReturn(false);
        setupMultipleArguments();
        command.setPermission(Command.Permission.USER);
        command.setListener(mockCommandListener);
        List<String> rawArguments = new LinkedList<>();
        rawArguments.add("a");
        rawArguments.add("1");
        List<Object> cleanArguments = command.cleanArguments(rawArguments);
        command.call(cleanArguments, 0);
        verify(mockNM).sendCommand(command.getName(), cleanArguments);
    }
}
