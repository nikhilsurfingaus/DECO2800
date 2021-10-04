package deco.combatevolved.handlers.commands;

import deco.combatevolved.commands.Command;
import deco.combatevolved.managers.CommandsManager;
import deco.combatevolved.managers.GameManager;
import deco.combatevolved.managers.OnScreenMessageManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest(GameManager.class)
public class HelpCommandListenerTest {
    private OnScreenMessageManager mockOSSM;
    private CommandsManager mockCM;
    private HelpCommandListener listener;

    @Before
    public void setup() {
        listener = new HelpCommandListener();

        GameManager mockGM = mock(GameManager.class);
        mockStatic(GameManager.class);
        when(GameManager.get()).thenReturn(mockGM);

        mockOSSM = mock(OnScreenMessageManager.class);
        when(mockGM.getManager(OnScreenMessageManager.class)).thenReturn(mockOSSM);

        mockCM = mock(CommandsManager.class);
        when(mockGM.getManager(CommandsManager.class)).thenReturn(mockCM);
    }

    @Test
    public void testCall() {
        List<Command> commands = new LinkedList<>();
        Command c1 = mock(Command.class);
        when(c1.toString()).thenReturn("c1");
        commands.add(c1);
        Command c2 = mock(Command.class);
        when(c2.toString()).thenReturn("c2");
        commands.add(c2);
        Command c3 = mock(Command.class);
        when(c3.toString()).thenReturn("c3");
        commands.add(c3);
        when(mockCM.getCommands()).thenReturn(commands);

        listener.call(new ArrayList<>(), 0);
        verify(mockOSSM).addMessage("c1");
        verify(mockOSSM).addMessage("c2");
        verify(mockOSSM).addMessage("c3");
    }
}
