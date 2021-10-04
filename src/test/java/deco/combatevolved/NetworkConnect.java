package deco.combatevolved;

import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;

import com.esotericsoftware.kryonet.Server;
import deco.combatevolved.entities.dynamicentities.PlayerAttributes;
import deco.combatevolved.entities.dynamicentities.PlayerPeon;
import deco.combatevolved.entities.enemyentities.BasicEnemy;
import deco.combatevolved.mainmenu.ErrorScreen;
import deco.combatevolved.managers.EnemyManager;
import deco.combatevolved.managers.GameManager;
import deco.combatevolved.managers.NetworkManager;
import deco.combatevolved.worlds.AbstractWorld;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import deco.combatevolved.worlds.CombatEvolvedWorld;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Gdx.class, NetworkManager.class})
public class NetworkConnect extends BaseGDXTest {

    private NetworkManager serverManager;
    private NetworkManager clientManager;
    private String textureAtlas = "testFrames";

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockStatic(Gdx.class);
        Sound mockSound = mock(Sound.class);
        Audio mockAudioSystem = mock(Audio.class);
        CombatEvolvedGame mockGame = mock(CombatEvolvedGame.class);
        whenNew(ErrorScreen.class).withAnyArguments().thenReturn(mock(ErrorScreen.class));
        Gdx.audio = mockAudioSystem;
        doReturn(mockSound).when(mockAudioSystem).newSound(any(FileHandle.class));

        // Sets up a server and client
        serverManager = new NetworkManager();
        clientManager = new NetworkManager();

        // Creates a new SeedWorld for the host to use
        AbstractWorld world = new CombatEvolvedWorld();
        GameManager.get().setWorld(world);

        // FIXME Do we need all these references?
        PlayerPeon playerPeon = new PlayerAttributes(0f, 0f, 0.05f, textureAtlas, "SOLDIER");
        EnemyManager enemyManager = new EnemyManager(3,10);
        GameManager.get().addManager(enemyManager);
        GameManager.get().getWorld().addEntity(playerPeon);
        GameManager.get().setPlayerEntityID(playerPeon.getEntityID());

        GameManager.get().setGame(mockGame);
    }

    /**
     * Checks if the client and the host can connect.
     * If an exception is thrown, assert false with stacktrace.
     */
    @Test
    public void testClientHostConnect() {
        serverManager.startHosting("Host", "");
        clientManager.connectToHost("localhost", "Client", "");
    }

    /**
     * Checks if the client and host can maintain a connection for 30 ticks.
     * This test is designed to make sure that the networking still works.
     * If you comment this test out, I will find you.
     */
    @Test
    public void testClientHostTick() {
        GameManager.get().getManager(NetworkManager.class).startHosting("Host", "");
        clientManager.connectToHost("localhost", "Client", "");

        // Runs 30 ticks of the game. 
        // If an exception is thrown, the test fails
        for (int i = 0; i < 30; i++) {
            GameManager.get().onTick(i);
            clientManager.onTick(i);
        }
    }
}