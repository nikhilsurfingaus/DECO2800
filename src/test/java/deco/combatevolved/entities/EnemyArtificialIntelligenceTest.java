package deco.combatevolved.entities;

import deco.combatevolved.BaseGDXTest;
import deco.combatevolved.entities.dynamicentities.PlayerAttributes;
import deco.combatevolved.entities.enemyentities.BasicEnemy;
import deco.combatevolved.entities.dynamicentities.PlayerPeon;
import deco.combatevolved.entities.staticentities.defensivetowers.SimpleTower;
import deco.combatevolved.exceptions.EnemyValueException;
import deco.combatevolved.managers.CollisionManager;
import deco.combatevolved.managers.GameManager;
import deco.combatevolved.worlds.CombatEvolvedWorld;
import deco.combatevolved.util.HexVector;
import deco.combatevolved.worlds.Tile;
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

public class EnemyArtificialIntelligenceTest extends BaseGDXTest {
    private CombatEvolvedWorld world;
    private PlayerPeon player;
    private BasicEnemy enemy;
    private float row, col;
    private SimpleTower tower;
    private String textureAtlas = "testFrames";

    @Before
    public void setup() {
        world = new CombatEvolvedWorld();
        player = new PlayerAttributes(0, 0, 0.05f, textureAtlas, "Soldier");
        world.addEntity(player);
        for (Tile tile : world.getTileListFromMap()) {
            tile.setObstructed(false);
        }
        GameManager.get().setWorld(world);
        GameManager.get().setPlayerEntityID(player.getEntityID());
        row = player.getRow();
        col = player.getCol();
        tower = new SimpleTower(0, -5);
        tower.setHealth(100);
        world.addEntity(tower);

        CollisionManager mockedCM = mock(CollisionManager.class);
        when(mockedCM.willCollide(any(AbstractEntity.class), any(HexVector.class))).thenReturn(false);
        mockStatic(GameManager.class, CALLS_REAL_METHODS);
//        when(GameManager.getManagerFromInstance(CollisionManager.class)).thenReturn(mockedCM);
    }

    @After
    public void teardown() {
        world = null;
        enemy = null;
    }

    // test that the enemy can move to a player in an adjacent column
    @Test
    public void adjacentColumnTest() throws InterruptedException {
        enemy = spy(new BasicEnemy(row, col + 1));
        when(enemy.getRange()).thenReturn(0f);

        world.addEntity(enemy);
        // on first tick the enemy should request a movement task
        enemy.onTick(1);
        // need to wait for pathfinding to complete

        Thread.sleep(100);

        for (int i = 0; i < 50; i++) {
            enemy.onTick(1);
        }

        assertTrue("the enemy did not move to the adjacent player",
                player.getPosition().isCloseEnoughToBeTheSame(enemy.getPosition()));
    }

    // test that the enemy can move to a player in an adjacent row
    @Test
    public void adjacentRowTest() throws InterruptedException {
        enemy = spy(new BasicEnemy(row + 1, col + 0.5f));
        when(enemy.getRange()).thenReturn(0f);

        world.addEntity(enemy);
        enemy.onTick(1);

        Thread.sleep(100);

        for (int i = 0; i < 50; i++) {
            enemy.onTick(1);
        }

        assertTrue("the enemy did not move to the adjacent player",
                player.getPosition().isCloseEnoughToBeTheSame(enemy.getPosition()));
    }

    // test that the enemy can move to a player from a longer distance
    @Test
    public void moveTest() throws InterruptedException {
        enemy = spy(new BasicEnemy(row, col + 2));
        when(enemy.getRange()).thenReturn(0f);

        world.addEntity(enemy);
        enemy.onTick(1);

        Thread.sleep(100);

        for (int i = 0; i < 50; i++) {
            enemy.onTick(1);
        }

        assertTrue("the enemy did not move to the player",
                player.getPosition().isCloseEnoughToBeTheSame(enemy.getPosition()));
    }

    // test that the enemy can target the player when they change position
    @Test
    public void changeDestinationTest() throws InterruptedException {
        enemy = spy(new BasicEnemy(row + 1, col + 0.5f));
        when(enemy.getRange()).thenReturn(0f);

        world.addEntity(enemy);
        enemy.onTick(1);

        Thread.sleep(100);

        for (int i = 0; i < 50; i++) {
            enemy.onTick(1);
        }

        player.setRow(3);
        enemy.onTick(100);

        Thread.sleep(100);

        for (int i = 0; i < 75; i++) {
            enemy.onTick(1);
        }

        assertTrue("the enemy did not move to the player after it changed position",
                player.getPosition().isCloseEnoughToBeTheSame(enemy.getPosition()));
    }

    // test that the enemy can avoid obstacles to get to the player
    @Test
    public void obstructionTest() throws InterruptedException {
        enemy = spy(new BasicEnemy(row, col - 2));
        when(enemy.getRange()).thenReturn(0f);

        world.addEntity(enemy);
        world.getTile(row, col-1).setObstructed(true);
        enemy.onTick(100);

        Thread.sleep(100);

        for (int i = 0; i < 61; i++) {
            enemy.onTick(1);
        }

        // the enemy should not be at the player yet because they have to go
        // around the obstacle
        assertFalse("the enemy did not avoid the obstacle",
                player.getPosition().isCloseEnoughToBeTheSame(enemy.getPosition()));

       // enemy.onTick(100);
        for (int i = 0; i < 50; i++) {
            enemy.onTick(1);
        }

        assertTrue("the enemy did not reach the player after avoiding the obstacle",
                player.getPosition().isCloseEnoughToBeTheSame(enemy.getPosition()));
    }

    // test that the enemy can attack a tower
    @Test
    public void attackTest() {

        enemy = new BasicEnemy(0, -5);

        world.addEntity(enemy);

        enemy.onTick(1);
        assertEquals("the enemy did not attack the tower",
                95, tower.getHealth());
    }

    // test that the enemy can attack multiple times
    @Test
    public void attackMultipleTimesTest() {

        enemy = new BasicEnemy(0, -5.5f);

        world.addEntity(enemy);

        enemy.onTick(1);
        enemy.onTick(1);
        assertEquals(95, tower.getHealth());
    }

    // test that the enemy cannot attack a player that is out of range
    @Test
    public void outOfRangeTest() {
        enemy = new BasicEnemy(0, -7);

        world.addEntity(enemy);

        // enemy is not in range of tower, so it should not attack
        enemy.onTick(1);

        assertEquals(100, tower.getHealth());
    }

    // test that the enemy can move to a tower and then attack it
    @Test
    public void moveThenAttackTest() throws InterruptedException {

        enemy = new BasicEnemy(0, -7);

        world.addEntity(enemy);

        enemy.onTick(100);

        Thread.sleep(100);

        for (int i = 0; i < 11; i++) {
            enemy.onTick(1);
        }
        // the enemy should not have previously been in range of the tower
        // so it should not have attacked the tower yet
        assertEquals("The enemy attacked before it was in range",
                100, tower.getHealth());
        enemy.onTick(1);
        assertEquals("The enemy did not attack the tower",
                95, tower.getHealth());
    }

    // test that the enemy waits between attacks
    @Test
    public void attackDelayTest() throws InterruptedException {

        enemy = new BasicEnemy(0, -5.5f);

        world.addEntity(enemy);

        enemy.onTick(100);
        // the enemy should not attack a second time, since not enough time has passed
        enemy.onTick(100);
        assertEquals("The enemy attack without waiting",
                95, tower.getHealth());

        Thread.sleep(3500);

        // now enough time should have passed for the enemy to attack
        enemy.onTick(100);
        assertEquals("The enemy did not attack a second time",
                90, tower.getHealth());
    }
}
