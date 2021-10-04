package deco.combatevolved.entities.staticentities;

import deco.combatevolved.entities.staticentities.defensivetowers.*;
import deco.combatevolved.worlds.Tile;

import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;

import static org.junit.Assert.*;

public class TowerDefenseManagerTest {
    private TowerDefenseManager towerDefenseManager;
    private SniperTower sniperTower = new SniperTower(new Tile(),true);
    private SplashTower splashTower = new SplashTower(new Tile(),true);
    private SimpleTower simpleTower = new SimpleTower(new Tile(),true);
    private SlowTower slowTower = new SlowTower(new Tile(),true);
    private ZapTower zapTower = new ZapTower(new Tile(),true);
    private MultiTower multiTower = new MultiTower(new Tile(),true);
    private MultiTower floatMulti = new MultiTower((float)1.2,(float)4.6);
    private SlowTower floatSlow = new SlowTower((float)1.2,(float)4.6);
    private SniperTower floatSnip = new SniperTower((float)1.2,(float)4.6);
    private SplashTower floatSplash = new SplashTower((float)1.2,(float)4.6);
    private ZapTower floatZap = new ZapTower((float)1.2,(float)4.6);


    @Before
    public void setUp() {
        towerDefenseManager = new TowerDefenseManager();
    }
    @Test
    public void addTower() {
        assertTrue(this.towerDefenseManager.getTowers().size() == 0);
        this.towerDefenseManager.addTower(null);
        assertTrue(this.towerDefenseManager.getTowers().size() == 0);
        this.towerDefenseManager.addTower(this.sniperTower);
        assertTrue(this.towerDefenseManager.getTowers().size() == 1);
        this.towerDefenseManager.addTower(this.splashTower);
        assertTrue(this.towerDefenseManager.getTowers().size() == 2);
        this.towerDefenseManager.addTower(this.simpleTower);
        assertTrue(this.towerDefenseManager.getTowers().size() == 3);
        this.towerDefenseManager.addTower(this.slowTower);
        assertTrue(this.towerDefenseManager.getTowers().size() == 4);
        this.towerDefenseManager.addTower(this.zapTower);
        assertTrue(this.towerDefenseManager.getTowers().size() == 5);
        this.towerDefenseManager.addTower(this.multiTower);
        assertTrue(this.towerDefenseManager.getTowers().size() == 6);


    }

    @Test
    public void getTower() {
        this.towerDefenseManager.addTower(this.sniperTower);
        this.towerDefenseManager.addTower(this.splashTower);
        this.towerDefenseManager.addTower(this.simpleTower);
        this.towerDefenseManager.addTower(this.slowTower);
        this.towerDefenseManager.addTower(this.zapTower);
        this.towerDefenseManager.addTower(this.multiTower);


        LinkedList<Tower> towers;
        towers = this.towerDefenseManager.getTowers();
        assertEquals("snipertower",towers.get(0).getTowerName());
        assertEquals("splashtower",towers.get(1).getTowerName());
        assertEquals("simpletower",towers.get(2).getTowerName());
        assertEquals("slowtower",towers.get(3).getTowerName());
        assertEquals("zaptower",towers.get(4).getTowerName());
        assertEquals("multitower",towers.get(5).getTowerName());


    }

    @Test
    public void getSpecificTower() {
        this.towerDefenseManager.addTower(this.sniperTower);
        this.towerDefenseManager.addTower(this.splashTower);
        this.towerDefenseManager.addTower(this.simpleTower);
        this.towerDefenseManager.addTower(this.slowTower);
        this.towerDefenseManager.addTower(this.zapTower);
        this.towerDefenseManager.addTower(this.multiTower);


        assertEquals("snipertower",this.towerDefenseManager.getTower(0).getTowerName());
        assertEquals("splashtower",this.towerDefenseManager.getTower(1).getTowerName());
        assertEquals("simpletower",this.towerDefenseManager.getTower(2).getTowerName());
        assertEquals("slowtower",this.towerDefenseManager.getTower(3).getTowerName());
        assertEquals("zaptower",this.towerDefenseManager.getTower(4).getTowerName());
        assertEquals("multitower",this.towerDefenseManager.getTower(5).getTowerName());

        assertEquals("snipertower",this.floatSnip.getTowerName());
        assertEquals("splashtower",this.floatSplash.getTowerName());
        assertEquals("slowtower", this.floatSlow.getTowerName());
        assertEquals("zaptower", this.floatZap.getTowerName());
        assertEquals("multitower", this.floatMulti.getTowerName());
    }

    @Test
    public void removeTower() {
        this.towerDefenseManager.addTower(this.sniperTower);
        this.towerDefenseManager.addTower(this.splashTower);
        this.towerDefenseManager.addTower(this.simpleTower);
        this.towerDefenseManager.addTower(this.slowTower);
        this.towerDefenseManager.addTower(this.zapTower);

        assertEquals(5, this.towerDefenseManager.getTowers().size());

        //this.simpleTower.setHealth(0); NullPointer from this: towerslist is empty when removeTower is called
        this.towerDefenseManager.removeTower(this.simpleTower);

        LinkedList<Tower> towersToRemove = new LinkedList<>();
        towersToRemove = this.towerDefenseManager.removeAllTowers();
        assertEquals(0,towersToRemove.size());

        assertEquals(4, this.towerDefenseManager.getTowers().size());

        /*this.simpleTower.setHealth(0); same null pointer as above
        assertFalse(this.towerDefenseManager.checkTowerAlive(this.simpleTower));*/

    }
}
