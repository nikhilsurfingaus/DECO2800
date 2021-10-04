package deco.combatevolved.entities;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import deco.combatevolved.BaseGDXTest;
import deco.combatevolved.entities.dynamicentities.PlayerAttributes;
import deco.combatevolved.entities.dynamicentities.PlayerPeon;
import deco.combatevolved.entities.enemyentities.BasicBossEnemy;
import deco.combatevolved.entities.enemyentities.BasicEnemy;
import deco.combatevolved.entities.staticentities.defensivetowers.SimpleTower;
import deco.combatevolved.entities.staticentities.defensivetowers.Tower;
import deco.combatevolved.handlers.KeyboardManager;
import deco.combatevolved.managers.*;
import deco.combatevolved.worlds.CombatEvolvedWorld;
import deco.combatevolved.worlds.Tile;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest(GameManager.class)
public class BossEnemyArtificialIntelligenceTest extends BaseGDXTest {
    private BasicBossEnemy boss;
    private CombatEvolvedWorld world;
    private PlayerPeon player;
    private String textureAtlas = "testFrames";

    @Before
    public void setup() {
        world = new CombatEvolvedWorld();
        for (Tile tile : world.getTileListFromMap()) {
            tile.setObstructed(false);
        }
        mockStatic(GameManager.class);
        GameManager mockGM = mock(GameManager.class);
        NetworkManager mockNM = mock(NetworkManager.class);
        TextureManager mockTM = mock(TextureManager.class);
        when(GameManager.get()).thenReturn(mockGM);
        when(mockGM.getWorld()).thenReturn(world);
        when(mockGM.getManager(NetworkManager.class)).thenReturn(mockNM);
        when(GameManager.getManagerFromInstance(EnemyManager.class)).thenReturn(new EnemyManager());

        when(GameManager.getManagerFromInstance(TextureManager.class)).thenReturn(mockTM);
        TextureAtlas atlas = new TextureAtlas("resources/PlayerCharacter/Soldier/Soldier_Frames.atlas");
        when(mockTM.getAtlas(anyString())).thenReturn(atlas);

        TaskPool tp = new TaskPool();
        when(GameManager.getManagerFromInstance(TaskPool.class)).thenReturn(tp);
        KeyboardManager mockKM = mock(KeyboardManager.class);
        when(GameManager.getManagerFromInstance(KeyboardManager.class)).thenReturn(mockKM);
        InputManager mockIM = mock(InputManager.class);
        when(GameManager.getManagerFromInstance(InputManager.class)).thenReturn(mockIM);
        SoundManager mockSM = mock(SoundManager.class);
        when(GameManager.get().getManager(SoundManager.class)).thenReturn(mockSM);
        PathFindingService pfs = new PathFindingService();
        when(GameManager.get().getManager(PathFindingService.class)).thenReturn(pfs);

        for (BasicEnemy enemy : GameManager.getManagerFromInstance(EnemyManager.class).getEnemyList()) {
            GameManager
                .getManagerFromInstance(EnemyManager.class)
                .removeEnemyFromGame(enemy);
        }
        player = new PlayerAttributes(0, 0, 0, textureAtlas, "Soldier");
        world.addEntity(player);
        boss = new BasicBossEnemy(0, 0);
        GameManager.getManagerFromInstance(EnemyManager.class).addEnemyToGame(boss);
    }

    @After
    public void teardown() {
        boss = null;
        world = null;
        player = null;
        for (BasicEnemy enemy : GameManager.getManagerFromInstance(EnemyManager.class).getEnemyList()) {
            GameManager.getManagerFromInstance(EnemyManager.class).removeEnemyFromGame(enemy);
        }
    }

    // tests that a boss can spawn other enemies
    @Test
    public void bossSpawnTest() {
        boss.onTick(1);
        // the boss should have spawned 3 more enemies, so the total number of
        // enemies should be 4
        assertEquals("The boss did not spawn more enemies",
                4, GameManager.getManagerFromInstance(EnemyManager.class).getEnemyNumber());
    }

    // tests that the boss cannot spawn another set of enemies immediately after
    // spawning some
    @Test
    public void bossSpawnDelayTest() {
        boss.onTick(1);
        // the second time the boss should not spawn more enemies since not
        // enough time has passed since it last spawned enemies
        boss.onTick(1);
        assertEquals("The boss spawned more enemies without waiting",
                4, GameManager.getManagerFromInstance(EnemyManager.class).getEnemyNumber());
    }

    // tests that if the boss waits long enough it can spawn another set of
    // enemies
    @Test
    public void multipleBossSpawnTest() {
        boss.onTick(1);
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            fail();
        }
        // the boss should be able to spawn another set of enemies now
        boss.onTick(1);
        assertEquals("The boss did not spawn another set of enemies",
                7, GameManager.getManagerFromInstance(EnemyManager.class).getEnemyNumber());
    }

    // tests that if at first there are at least ten enemies then the boss
    // does not spawn any more
    @Test
    public void notSpawnTest() {
        for (int i = 0; i < 9; i++) {
            GameManager.getManagerFromInstance(EnemyManager.class).addEnemyToGame(new BasicEnemy(0, 0));
        }
        boss.onTick(1);
        assertEquals("The boss spawned enemies when it should not have",
                10, GameManager.getManagerFromInstance(EnemyManager.class).getEnemyNumber());
    }

    // tests that if there are at least ten enemies after the boss spawns enemies
    // then it does not spawn any more
    @Test
    public void stopSpawningTest() {
        for (int i = 0; i < 6; i++) {
            GameManager.getManagerFromInstance(EnemyManager.class).addEnemyToGame(new BasicEnemy(0, 0));
        }
        boss.onTick(1);
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            fail();
        }
        // the boss should not be able to spawn more enemies since there are
        // already 10 enemies
        boss.onTick(1);
        assertEquals("The boss did not stop spawning enemies",
                10, GameManager.getManagerFromInstance(EnemyManager.class).getEnemyNumber());
    }

    // tests that enemies are spawned within t tiles of the boss
    @Test
    public void spawnDistanceTest() {
        boss.onTick(1);
        for (BasicEnemy enemy : GameManager.getManagerFromInstance(EnemyManager.class).getEnemyList()) {
            if (boss.distance(enemy) > 5) {
                fail("The boss spawned enemies to far away");
            }
        }
    }

    // tests that if enemies are not at the same position as the boss they will
    // still stop the boss from spawning more
    @Test
    public void largerDistanceSpawnTest() {
        for (int i = 0; i < 8; i++) {
            GameManager.getManagerFromInstance(EnemyManager.class).addEnemyToGame(new BasicEnemy(0, 0));
        }
        GameManager.getManagerFromInstance(EnemyManager.class).addEnemyToGame(new BasicEnemy(9, 0));
        boss.onTick(1);
        assertEquals("The boss spawned enemies when it should not have",
                10, GameManager.getManagerFromInstance(EnemyManager.class).getEnemyNumber());
    }

    @Test
    public void outOfRangeSpawnTest() {
        for (int i = 0; i < 8; i++) {
            GameManager.getManagerFromInstance(EnemyManager.class).addEnemyToGame(new BasicEnemy(0, 0));
        }
        GameManager.getManagerFromInstance(EnemyManager.class).addEnemyToGame(new BasicEnemy(11, 0));
        boss.onTick(1);
        assertEquals("The boss did not ignore enemies that were too far away",
                13, GameManager.getManagerFromInstance(EnemyManager.class).getEnemyNumber());
    }

    // tests that boss observers are notified when a boss targets an enemy
    @Test
    public void notifyObserversTest() {
        boss.setPosition(10, 11, 0);
        Tower tower = new SimpleTower(10, 10);
        world.addEntity(tower);
        BossObserver mockedObserver = mock(BossObserver.class);
        GameManager.getManagerFromInstance(EnemyManager.class).addBossObserver(mockedObserver);
        boss.onTick(1);
        boss.onTick(1);
        verify(mockedObserver).bossTargeted(boss, tower);
        world.removeEntity(tower);
    }

    // tests that enemies attack the same targets as bosses
    @Test
    public void bossCommandTest() {
        boss.setPosition(10, 7, 0);
        Tower tower = new SimpleTower(5, 10);
        world.addEntity(tower);
        BasicEnemy enemy = new BasicEnemy(0, 10);
        GameManager.getManagerFromInstance(EnemyManager.class).addEnemyToGame(enemy);
        Tower tower2 = new SimpleTower(-1, 10);
        world.addEntity(tower2);
        boss.onTick(1);
        boss.onTick(1);
        // the enemy should now target tower even though tower2 is closer
        enemy.onTick(1);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            fail();
        }
        for (int i = 0; i < 125; i++) {
            enemy.onTick(1);
        }
        // the enemy should be close to tower
        assertTrue("The enemy did not move to the tower the boss targeted",
                enemy.getPosition().isCloseEnoughToBeTheSame(tower.getPosition(), 1.5f));
        // the enemy should have attacked tower
        assertEquals("The enemy did not attack the tower the boss targeted",
                145, tower.getHealth());
        // the enemy should not have attacked tower 2
        assertEquals("The enemy attacked another tower",
                150, tower2.getHealth());
    }

    // tests that bosses ignore what other bosses are targeting
    @Test
    public void commandOtherBossTest() {
        boss.setPosition(10, 7, 0);
        Tower tower = new SimpleTower(5, 10);
        world.addEntity(tower);
        BasicBossEnemy boss2 = new BasicBossEnemy(0, 10);
        GameManager.getManagerFromInstance(EnemyManager.class).addEnemyToGame(boss2);
        Tower tower2 = new SimpleTower(0, 10);
        world.addEntity(tower2);
        boss.onTick(1);
        boss.onTick(1);
        boss2.onTick(1);
        boss2.onTick(1);
        // boss2 should have attacked the tower instead of targeting the tower
        // that boss was targeting
        assertEquals(120, tower2.getHealth());
    }
}
