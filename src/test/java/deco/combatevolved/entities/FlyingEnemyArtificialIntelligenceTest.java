package deco.combatevolved.entities;

import deco.combatevolved.BaseGDXTest;
import deco.combatevolved.entities.dynamicentities.PlayerAttributes;
import deco.combatevolved.entities.enemyentities.BasicEnemy;
import deco.combatevolved.entities.dynamicentities.PlayerPeon;
import deco.combatevolved.entities.enemyentities.FlyingEnemy;
import deco.combatevolved.entities.staticentities.defensivetowers.SimpleTower;
import deco.combatevolved.managers.GameManager;
import deco.combatevolved.util.HexVector;
import deco.combatevolved.worlds.CombatEvolvedWorld;
import deco.combatevolved.worlds.Tile;
import deco.combatevolved.managers.CollisionManager;
import deco.combatevolved.worlds.worldgen.WorldGenParamBag;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import static org.mockito.Mockito.*;
import org.powermock.modules.junit4.PowerMockRunner;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({GameManager.class})

public class FlyingEnemyArtificialIntelligenceTest extends BaseGDXTest {
    private BasicEnemy enemy;
    private PlayerPeon player;
    private SimpleTower tower;
    private FlyingEnemyArtificialIntelligence ai;
    private CombatEvolvedWorld world;
    private String textureAtlas = "testFrames";

    @Before
    public void setup() {
        world = new CombatEvolvedWorld();
        for (Tile tile : world.getTileListFromMap()) {
            tile.setObstructed(false);
        }
        GameManager.get().setWorld(world);
        player = new PlayerAttributes(0, 1, 0.05f, textureAtlas, "Soldier");
        world.addEntity(player);
        GameManager.get().setPlayerEntityID(player.getEntityID());
        tower = new SimpleTower(0, -5);
        tower.setHealth(100);
        world.addEntity(tower);
        ai = new FlyingEnemyArtificialIntelligence(player);

        CollisionManager mockedCM = mock(CollisionManager.class);
        when(mockedCM.willCollide(any(AbstractEntity.class), any(HexVector.class))).thenReturn(false);
        mockStatic(GameManager.class, CALLS_REAL_METHODS);
        when(GameManager.getManagerFromInstance(CollisionManager.class)).thenReturn(mockedCM);
    }

    @After
    public void teardown() {
        world = null;
        ai = null;
        enemy = null;
        tower = null;
    }

    // tests that a flying enemy moves in a straight line to the player
    @Test
    public void moveTest() {
        enemy = spy(new FlyingEnemy(0, 2));
        when(enemy.getRange()).thenReturn(0f);

        for (int i = 0; i < 20; i++) {
            enemy.onTick(1);
        }
        assertTrue(enemy.getPosition().isCloseEnoughToBeTheSame(new HexVector(0, 1)));
        for (int i = 0; i < 10; i++) {
            enemy.onTick(1);
        }
        assertTrue("The flying enemy did not move to the player", enemy.getPosition().isCloseEnoughToBeTheSame(player.getPosition()));
    }

    // tests that a flying enemy ignores obstructions
    @Test
    public void obstructionTest() {
        enemy = spy(new FlyingEnemy(0, 2));
        when(enemy.getRange()).thenReturn(0f);

        world.getTile(0, 1).setObstructed(true);
        for (int i = 0; i < 20; i++) {
            enemy.onTick(1);
        }
        assertTrue("The flying enemy did not ignore the obstacle",
                enemy.getPosition().isCloseEnoughToBeTheSame(new HexVector(0, 1)));
        for (int i = 0; i < 10; i++) {
            enemy.onTick(1);
        }
        assertTrue("The flying enemy did not ignore te obstacle",
                enemy.getPosition().isCloseEnoughToBeTheSame(player.getPosition()));
    }

    // tests that a flying enemy can target the player when they change position
    @Test
    public void changeDestinationTest() {
        enemy = spy(new FlyingEnemy(0, 2));
        when(enemy.getRange()).thenReturn(0f);

        for (int i = 0; i < 20; i++) {
            enemy.onTick(1);
        }
        player.setRow(1);
        for (int i = 0; i < 10; i++) {
            enemy.onTick(1);
        }
        assertTrue("The flying enemy did not move to the player after it changed position",
                enemy.getPosition().isCloseEnoughToBeTheSame(player.getPosition()));
    }
}