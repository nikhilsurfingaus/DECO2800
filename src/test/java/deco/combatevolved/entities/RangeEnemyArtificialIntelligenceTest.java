package deco.combatevolved.entities;

import deco.combatevolved.BaseGDXTest;
import deco.combatevolved.entities.dynamicentities.PlayerAttributes;
import deco.combatevolved.entities.enemyentities.RangeEnemy;
import deco.combatevolved.entities.dynamicentities.PlayerPeon;
import deco.combatevolved.entities.dynamicentities.Bullet;
import deco.combatevolved.entities.staticentities.defensivetowers.SimpleTower;
import deco.combatevolved.managers.GameManager;
import deco.combatevolved.worlds.CombatEvolvedWorld;
import deco.combatevolved.worlds.Tile;
import deco.combatevolved.worlds.worldgen.WorldGenParamBag;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Test;

public class RangeEnemyArtificialIntelligenceTest extends BaseGDXTest {
    private CombatEvolvedWorld world;
    private PlayerPeon player;
    private RangeEnemy enemy;
    private float row, col;
    private SimpleTower tower;
    private String textureAtlas = "testFrames";

    @Before
    public void setup() {
        world = new CombatEvolvedWorld();
        player = new PlayerAttributes(0, 1, 0.05f, textureAtlas, "Soldier");
        world.addEntity(player);
        for (Tile tile : world.getTileListFromMap()) {
            tile.setObstructed(false);
        }
        GameManager.get().setWorld(world);
        row = player.getRow();
        col = player.getCol();
        tower = new SimpleTower(0, -5);
        tower.setHealth(100);
        world.addEntity(tower);
    }

    @After
    public void teardown() {
        world = null;
        enemy = null;
    }

    // tests that the ranged enemy fires bullets
    @Test
    public void bulletTest() {
        enemy = new RangeEnemy(0, -6);
        enemy.onTick(1);
        Bullet bullet = null;
        for (AbstractEntity entity : world.getEntities()) {
            if (entity instanceof Bullet) {
                bullet = (Bullet) entity;
                break;
            }
        }
        assertNotNull("The ranged enemy did not fire a bullet", bullet);
    }

    // tests that there is a delay between firing bullets
    @Test
    public void delayTest() {
        enemy = new RangeEnemy(0, -6);
        enemy.onTick(1);
        enemy.onTick(2);
        int count = 0;
        for (AbstractEntity entity : world.getEntities()) {
            if (entity instanceof Bullet) {
                count++;
            }
        }
        assertEquals("The ranged enemy did not wait after firing a bullet",
                1, count);
    }

    // tests that the bullet is remove after hitting a tower
    @Test
    public void removeTest() {
        enemy = new RangeEnemy(0, -6);
        enemy.onTick(1);
        Bullet bullet = null;
        for (AbstractEntity entity : world.getEntities()) {
            if (entity instanceof Bullet) {
                bullet = (Bullet) entity;
                break;
            }
        }
        for (int i = 0; i < 20; i++) {
            bullet.onTick(1);
        }
        assertFalse("The bullet was not removed after travelling a long enough distance",
                world.getEntities().contains(bullet));
    }

    // tests that the bullet is removed after travelling far enough
    @Test
    public void removeDistanceTest() {
        enemy = new RangeEnemy(0, -4);
        enemy.setRange(3);
        enemy.onTick(1);
        world.removeEntity(tower);
        Bullet bullet = null;
        for (AbstractEntity entity : world.getEntities()) {
            if (entity instanceof Bullet) {
                bullet = (Bullet) entity;
            }
        }

        for (int i = 0; i < 35; i++) {
            bullet.onTick(1);
        }

        assertFalse(world.getEntities().contains(bullet));
    }

    // tests that the bullet damages the tower
    @Test
    public void damageTest() {
        enemy = new RangeEnemy(0, -6);
        enemy.onTick(1);
        Bullet bullet = null;
        for (AbstractEntity entity : world.getEntities()) {
            if (entity instanceof Bullet) {
                bullet = (Bullet) entity;
                break;
            }
        }
        while (world.getEntities().contains(bullet)) {
            bullet.onTick(1);
        }
        assertEquals("The bullet did not damage the tower",
                98, tower.getHealth());
    }

    // tests that the enemy can shoot from a longer range
    @Test
    public void longerRangeTest() {
        enemy = new RangeEnemy(0, -14);
        enemy.onTick(1);
        Bullet bullet = null;
        for (AbstractEntity entity : world.getEntities()) {
            if (entity instanceof Bullet) {
                bullet = (Bullet) entity;
                break;
            }
        }
        while (world.getEntities().contains(bullet)) {
            bullet.onTick(1);
        }
        assertEquals("The ranged enemy did not attack the tower",
                98, tower.getHealth());
    }

    // tests that a ranged enemy does not fire a bullet if they are out of range
    @Test
    public void outOfRangeTest() {
        enemy = new RangeEnemy(0, -16);
        enemy.onTick(1);
        for (AbstractEntity entity : world.getEntities()) {
            if (entity instanceof Bullet) {
                fail("The ranged enemy fired a bullet while out of range");
            }
        }
    }
}


