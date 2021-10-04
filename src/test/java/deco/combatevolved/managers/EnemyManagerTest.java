package deco.combatevolved.managers;

import deco.combatevolved.BaseGDXTest;
import deco.combatevolved.entities.AbstractEntity;
import deco.combatevolved.entities.dynamicentities.PlayerAttributes;
import deco.combatevolved.entities.dynamicentities.PlayerPeon;
import deco.combatevolved.entities.enemyentities.BasicEnemy;
import deco.combatevolved.entities.enemyentities.HealerEnemy;
import deco.combatevolved.exceptions.EnemyValueException;
import deco.combatevolved.worlds.CombatEvolvedWorld;
import deco.combatevolved.entities.ArtificialIntelligence;
import deco.combatevolved.factories.EnemyEntityFactory;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;

import org.junit.Test;

public class EnemyManagerTest extends BaseGDXTest {
    private CombatEvolvedWorld spawnCombatEvolvedWorld;
    private ArtificialIntelligence ai;
    private EnemyManager enemyManager;
    private String textureAtlas = "testFrames";

    @Before
    public void setup() {
        spawnCombatEvolvedWorld = new CombatEvolvedWorld();
        GameManager.get().setWorld(spawnCombatEvolvedWorld);
        enemyManager = new EnemyManager(2,5);
        GameManager.get().addManager(enemyManager);
        GameManager.getManagerFromInstance(EnemyManager.class).resetExp();
        // instance of player for enemyExpTest() -> (BasicEnemy) loseHealth
        PlayerPeon player = new PlayerAttributes(0, 1, 0.05f, textureAtlas, "Soldier");
        spawnCombatEvolvedWorld.addEntity(player);
    }

    @After
    public void teardown() {
        ai = null;
        enemyManager = null;
        spawnCombatEvolvedWorld = null;
    }

    @Test
    public void initialSpawnTest() {
        enemyManager.spawn();
        int count = 0;
        int health = 10;
        for (AbstractEntity entity : GameManager.get().getWorld().getEntities()) {
            if (entity instanceof BasicEnemy) {
                health = ((BasicEnemy) entity).getHealth();
                count++;
            }
        }
        //assertEquals(1,health);
        assertEquals("The entity list does not contain the correct number of enemies",
                2, count);
        count = 0;
        for (BasicEnemy enemyEntity : enemyManager.getEnemyList()) {
            count++;
        }
        assertEquals("The enemy list does not contain the correct number of enemies",
                2, enemyManager.getEnemyList().size());
    }

    @Test
    public void maxSpawnTest() {
            enemyManager.spawn();
            enemyManager.spawn();
            enemyManager.spawn();
            assertEquals("The enemy list does not contain the correct number of enemies",
                    5, enemyManager.getEnemyList().size());
    }
    @Test
    public void enemyManagerParameterTest() {
        int spawnRate = enemyManager.getenemySpawnNumber();
        int totalEnemies = enemyManager.getEnemySpawnTotal();
        assertEquals(2, spawnRate);
        assertEquals(5, totalEnemies);
        enemyManager.spawn();
        int currentEnemies = enemyManager.getEnemyNumber();
        assertEquals(2,currentEnemies);
        enemyManager.spawn();
        currentEnemies = enemyManager.getEnemyNumber();
        assertEquals(4,currentEnemies);
        enemyManager.spawn();
        currentEnemies = enemyManager.getEnemyNumber();
        assertEquals(5,currentEnemies);
    }

//    @Test
//    public void damageEnemyTest() throws EnemyValueException {
//        BasicEnemy enemy = EnemyEntityFactory.createEnemy("basic", 0, 0);
//        enemyManager.damageEnemy(enemy,5);
//        assertEquals("did not do the correct damage",66, enemy.getHealth());
//        enemyManager.damageEnemy(enemy,1000);
//        assertEquals("enemy did not die",0, enemy.getHealth());
//    }

    @Test
    public void slowEnemyTest() throws  EnemyValueException {
        BasicEnemy enemy = EnemyEntityFactory.createEnemy("basic", 0, 0);
        enemyManager.slowEnemy(enemy);
        assertEquals("enemy not slowed", 0.025f,enemy.getSpeed(),0.00001);
    }


    @Test
    public void enemyHealTest() throws EnemyValueException {
        BasicEnemy enemy1 = EnemyEntityFactory.createEnemy("basic", 1, 1);
        BasicEnemy enemy2 = EnemyEntityFactory.createEnemy("basic", 0, 10);
        BasicEnemy healer = EnemyEntityFactory.createEnemy("healer", 0, 0);
        enemy1.loseHealth(5);
        enemy2.loseHealth(5);
        healer.loseHealth(5);
        ((HealerEnemy)healer).setLastHeal(System.currentTimeMillis() -4001);
        enemyManager.addEnemyToGame(enemy1);
        enemyManager.addEnemyToGame(enemy2);
        enemyManager.addEnemyToGame(healer);
        enemyManager.enemyHeal();
        assertEquals("enemy should of been healed", 70, enemy1.getHealth());
        assertEquals("enemy should not of been healed",65,enemy2.getHealth());
        assertEquals("healer should not of been healed",70,healer.getHealth());
    }


/*
    @Test
    public void enemyResetExpTest() throws EnemyValueException {
        GameManager.get().getManager(EnemyManager.class).resetExp();
        assertEquals("exp was not 0",0, GameManager.get().getManager(EnemyManager.class).getExp());
        BasicEnemy enemy1 = EnemyEntityFactory.createEnemy("basic", 1, 1);
        enemy1.loseHealth(1000);
        GameManager.get().getManager(EnemyManager.class).resetExp();
        assertEquals("exp was not reset back to 0",
                0, GameManager.get().getManager(EnemyManager.class).getExp());
    }
*/
    /*
    @Test
    public void enemyExpTest() throws EnemyValueException {
        GameManager.get().getManager(EnemyManager.class).resetExp();
        BasicEnemy enemy1 = EnemyEntityFactory.createEnemy("basic", 1, 1);
        BasicEnemy enemy2 = EnemyEntityFactory.createEnemy("fast", 1, 1);
        BasicEnemy enemy3 = EnemyEntityFactory.createEnemy("heavy", 1, 1);
        BasicEnemy enemy4 = EnemyEntityFactory.createEnemy("small", 1, 1);
        BasicEnemy enemy5 = EnemyEntityFactory.createEnemy("flying", 1, 1);
        GameManager.get().getManager(EnemyManager.class).addEnemyToGame(enemy1);
        GameManager.get().getManager(EnemyManager.class).addEnemyToGame(enemy2);
        GameManager.get().getManager(EnemyManager.class).addEnemyToGame(enemy3);
        GameManager.get().getManager(EnemyManager.class).addEnemyToGame(enemy4);
        GameManager.get().getManager(EnemyManager.class).addEnemyToGame(enemy5);
        assertEquals("",0,GameManager.get().getManager(EnemyManager.class).getExp());
        enemy1.loseHealth(1000);
        assertEquals("",5,GameManager.get().getManager(EnemyManager.class).getExp());
        enemy2.loseHealth(1000);
        assertEquals("",10,GameManager.get().getManager(EnemyManager.class).getExp());
        enemy3.loseHealth(1000);
        assertEquals("",30,GameManager.get().getManager(EnemyManager.class).getExp());
        enemy4.loseHealth(1000);
        assertEquals("",33,GameManager.get().getManager(EnemyManager.class).getExp());
        enemy5.loseHealth(1000);
        assertEquals("",43,GameManager.get().getManager(EnemyManager.class).getExp());
    }
*/
}
