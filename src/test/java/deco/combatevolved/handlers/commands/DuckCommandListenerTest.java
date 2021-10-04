package deco.combatevolved.handlers.commands;

import deco.combatevolved.managers.GameManager;
import deco.combatevolved.worlds.AbstractWorld;
import deco.combatevolved.worlds.ServerWorld;
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

public class DuckCommandListenerTest {
    private DuckCommandListener listener;
    private AbstractWorld world;

    @Before
    public void setup() {
        listener = new DuckCommandListener();
        world = new ServerWorld();

        GameManager gameManager = GameManager.get();
        gameManager.setWorld(world);
    }

    @Test
    public void testCall() {
        listener.call(new ArrayList<>(), 0);
        assertEquals(1000, world.getEntities().size());
    }
}
