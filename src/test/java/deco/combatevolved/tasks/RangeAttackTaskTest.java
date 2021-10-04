package deco.combatevolved.tasks;

import deco.combatevolved.BaseGDXTest;
import deco.combatevolved.entities.AbstractEntity;
import deco.combatevolved.entities.dynamicentities.Bullet;
import deco.combatevolved.entities.dynamicentities.PlayerPeon;
import deco.combatevolved.entities.enemyentities.RangeEnemy;
import deco.combatevolved.managers.EnemyManager;
import deco.combatevolved.managers.GameManager;
import deco.combatevolved.worlds.CombatEvolvedWorld;
import deco.combatevolved.worlds.Tile;
import deco.combatevolved.entities.staticentities.defensivetowers.SimpleTower;
import deco.combatevolved.entities.staticentities.defensivetowers.Tower;

import deco.combatevolved.worlds.worldgen.WorldGenParamBag;
import org.junit.Before;
import org.junit.Test;

import deco.combatevolved.entities.ArtificialIntelligence;

import static org.junit.Assert.*;

public class RangeAttackTaskTest extends BaseGDXTest {

    private RangeAttackTask task;
    private Tower target;
    private PlayerPeon player;
    private RangeEnemy entity;
    private ArtificialIntelligence ai;

    @Before
    public void setUp() {
        GameManager.get().setWorld(new CombatEvolvedWorld());
        GameManager.get().addManager(new EnemyManager(1,1));
        target = new SimpleTower(new Tile(), true);

        // PlayerPeon has been added as the Range enemy requires to have an enemy
//        player = new PlayerPeon();
//        GameManager.get().getWorld().addEntity(player);
        
        entity = new RangeEnemy(0,0);
        GameManager.get().getWorld().addEntity(entity);
        GameManager.get().getWorld().addEntity(target);
        entity = new RangeEnemy(0,0);
        GameManager.get().getManager(EnemyManager.class).addEnemyToGame(entity);
        task = new RangeAttackTask(entity, target, target.getPosition());
    }

    @Test
    public void attackTest() {
        float initialHealth = target.getHealth();
        task.onTick(0);
        Bullet bullet = null;
        for (AbstractEntity entity : GameManager.get().getWorld().getEntities()) {
            if (entity instanceof Bullet) {
                bullet = (Bullet) entity;
            }
        }
        bullet.onTick(1);
        assertEquals(target.getHealth(), initialHealth - entity.getDamage(), 0.01);
        assertTrue(task.isComplete());
    }
}