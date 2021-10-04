package deco.combatevolved.worlds;

import deco.combatevolved.handlers.WorldState;
import deco.combatevolved.managers.GameManager;
import deco.combatevolved.worlds.weather.WeatherModel;
import deco.combatevolved.worlds.weather.WeatherState;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(GameManager.class)
public class WorldStateTest {
    private WorldState worldState;

    @Before
    public void setup() {
        worldState = new WorldState();
    }

    @Test
    public void testUpdateCycle() {
        worldState.notifyCycleChange(1);
        assertEquals(1, worldState.getDayNightCycleCode());
    }

    @Test
    public void testAlert() {
        mockStatic(GameManager.class);
        GameManager mockGM = mock(GameManager.class);
        AbstractWorld mockWorld = mock(AbstractWorld.class);
        WeatherModel mockWeather = mock(WeatherModel.class);
        WeatherState[] mockForecast = {mock(WeatherState.class), mock(WeatherState.class), mock(WeatherState.class)};

        when(GameManager.get()).thenReturn(mockGM);
        when(mockGM.getWorld()).thenReturn(mockWorld);
        when(mockWorld.getWeather()).thenReturn(mockWeather);
        when(mockWeather.getForecast()).thenReturn(mockForecast);

        worldState.Alert();
        assertArrayEquals(mockForecast, worldState.getForecast());
        assertTrue(worldState.check());
    }

    @Test
    public void testCheckChange() {
        worldState.notifyCycleChange(1);
        assertTrue(worldState.check());
    }

    @Test
    public void testCheckNoChange() {
        worldState.check();
        assertFalse("World state changed is not reset on check", worldState.check());
    }

    @Test
    public void testCheckNoChangeOnCheck() {
        worldState.check();
        assertFalse("World state changed is not reset on check", worldState.check());
        assertFalse("World state changed is altered by check if false", worldState.check());
    }
}
