package deco.combatevolved.tasks;

import deco.combatevolved.entities.staticentities.defensivetowers.SimpleTower;
import deco.combatevolved.entities.staticentities.defensivetowers.Tower;
import deco.combatevolved.worlds.Tile;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TowerTargetTaskTest {
    private Tower t1;
    private TowerTargetTask ab;
    @Before
    public void setup(){
        t1 = new SimpleTower(new Tile(), true);
        ab = new TowerTargetTask(t1);
    }
    @Test
    public void isComplete() {
        assertTrue(!ab.isComplete());
    }

    @Test
    public void isAlive() {
        assertTrue(ab.isAlive());
        ab.getTarget();
        assertTrue(!ab.isAlive());
    }

    @Test
    public void getTarget() {
        assertEquals(null, ab.getTarget());
    }
}