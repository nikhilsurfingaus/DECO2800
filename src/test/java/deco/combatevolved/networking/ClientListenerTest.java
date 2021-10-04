package deco.combatevolved.networking;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import deco.combatevolved.handlers.WorldState;
import deco.combatevolved.managers.DayNightCycle;
import deco.combatevolved.managers.GameManager;
import deco.combatevolved.managers.NetworkManager;
import deco.combatevolved.worlds.AbstractWorld;
import deco.combatevolved.worlds.Tile;
import deco.combatevolved.worlds.biomes.BiomeType;
import deco.combatevolved.worlds.weather.WeatherModel;
import deco.combatevolved.worlds.weather.WeatherState;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest(GameManager.class)
public class ClientListenerTest {
    private ClientListener clientListener;
    private Client mockClient;
    private GameManager mockGM;
    private Connection mockConnection;

    @Before
    public void setup() {
        mockClient = mock(Client.class);
        mockConnection = mock(Connection.class);
        clientListener = new ClientListener(mockClient);

        GameManager.get().getManager(NetworkManager.class).setAllowMessages(true);
        mockGM = mock(GameManager.class);
        mockStatic(GameManager.class);
        when(GameManager.get()).thenReturn(mockGM);
        AbstractWorld mockWorld = mock(AbstractWorld.class);
        when(mockGM.getWorld()).thenReturn(mockWorld);
    }

    @Test
    public void testWorldStateUpdateMessage() {
        DayNightCycle mockDNC = mock(DayNightCycle.class);
        when(mockGM.getManager(DayNightCycle.class)).thenReturn(mockDNC);

        WeatherModel mockWeather = mock(WeatherModel.class);
        WeatherState[] forecast = {mock(WeatherState.class), mock(WeatherState.class), mock(WeatherState.class)};
        when(mockGM.getWorld().getWeather()).thenReturn(mockWeather);
        when(mockWeather.getForecast()).thenReturn(forecast);

        WorldState worldState = new WorldState();
        worldState.notifyCycleChange(1);
        worldState.Alert();
        WorldStateUpdateMessage message = new WorldStateUpdateMessage(worldState);
        clientListener.received(mockConnection, message);

        verify(mockDNC).setCycleCode(1);
        verify(mockWeather).setForecast(forecast);
    }


    @Test
    public void testTileUpdateMessageBiome() {
        Tile tile = mock(Tile.class);
        BiomeType biome = mock(BiomeType.class);

        TileUpdateMessage message = new TileUpdateMessage();
        message.setTile(tile);
        message.setBiomeType(biome);
        clientListener.received(mockConnection, message);

        verify(mockGM.getWorld()).updateTile(tile, biome);
        verify(mockGM.getWorld()).generateNeighbours();
    }

    @Test
    public void testTileUpdateMessageNoBiome() {
        Tile tile = mock(Tile.class);

        TileUpdateMessage message = new TileUpdateMessage();
        message.setTile(tile);
        clientListener.received(mockConnection, message);

        verify(mockGM.getWorld()).updateTile(tile);
        verify(mockGM.getWorld()).generateNeighbours();
    }

    @Test
    public void testConfirmConnectMessage() {
        NetworkManager mockNM = mock(NetworkManager.class);
        when(mockGM.getManager(NetworkManager.class)).thenReturn(mockNM);

        ConfirmConnectionMessage message = new ConfirmConnectionMessage();
        message.setPlayerEntityID(9);

        clientListener.received(mockConnection, message);

        verify(mockGM).setPlayerEntityID(9);
        verify(mockNM).setClientFinishLoading();
    }
}
