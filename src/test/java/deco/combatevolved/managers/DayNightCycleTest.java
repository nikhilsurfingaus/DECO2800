package deco.combatevolved.managers;

import org.junit.*;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest(GameManager.class)
public class DayNightCycleTest {
    EnemyManager enemyManager;
    static DayNightCycle dayNightCycle;

    @Before
    public void setup() {
        mockStatic(GameManager.class);
        when(GameManager.get()).thenReturn(mock(GameManager.class));
        when(GameManager.get().getManager(NetworkManager.class)).thenReturn(mock(NetworkManager.class));
        when(GameManager.get().getManager(NetworkManager.class).isHost()).thenReturn(true);
        dayNightCycle = new DayNightCycle(System.currentTimeMillis(), 0,0,0);
        enemyManager = new EnemyManager(5,5);
        dayNightCycle.addObserver(enemyManager);
        when(GameManager.get().getManager(DayNightCycle.class)).thenReturn(dayNightCycle);
    }

    @After
    public void tearDown() {
        dayNightCycle.removeObserver(enemyManager);
        enemyManager = null;
        dayNightCycle = null;
    }

    @Test
    public void noCycleChange() {
        assertEquals("initial cycle is incorrect",1, enemyManager.getCurrentCycle());
    }

    @Test
    public void singleCycleChange() {
        dayNightCycle.onTick(1);
        assertEquals("single cycle change did not work", 3, enemyManager.getCurrentCycle());
    }

    @Test
    public void multipleCycleChange() {
        dayNightCycle.onTick(1);
        dayNightCycle.onTick(1);
        assertEquals("multiple cycle change was unsuccessful", 2, enemyManager.getCurrentCycle());
    }

    @Test
    public void removeObserver() {
        dayNightCycle.removeObserver(enemyManager);
        dayNightCycle.onTick(1);
        assertEquals("observer was not removed", 1, enemyManager.getCurrentCycle());
    }
}
