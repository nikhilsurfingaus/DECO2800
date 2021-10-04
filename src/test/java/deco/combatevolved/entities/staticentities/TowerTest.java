package deco.combatevolved.entities.staticentities;

import deco.combatevolved.entities.staticentities.defensivetowers.*;
import deco.combatevolved.managers.GameManager;
import deco.combatevolved.worlds.CombatEvolvedWorld;
import deco.combatevolved.worlds.Tile;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(GameManager.class)
public class TowerTest {
    private Tower sniper, simple, splash, slow, zap, multi;

    @Before
    public void setup(){
        simple = new SimpleTower(new Tile(), true);
        sniper = new SniperTower(new Tile(), true);
        splash = new SplashTower(new Tile(), true);
        slow = new SlowTower(new Tile(), true);
        zap = new ZapTower(new Tile(), true);
        multi = new MultiTower(new Tile(), true);
        //negativeDmg = new SimpleTower(;
    }

    /**Tests getting the towers names
     *
     */
    @Test
    public void getTowerName() {
        assertEquals("simpletower", this.simple.getTowerName());
        assertEquals("snipertower", this.sniper.getTowerName());
        assertEquals("splashtower", this.splash.getTowerName());
        assertEquals("slowtower", this.slow.getTowerName());
        assertEquals("zaptower", this.zap.getTowerName());
        assertEquals("multitower", this.multi.getTowerName());
    }

    @Test
    public void getTowerTexture() {
        assertEquals("simpletower_D", this.simple.getTextureString());
        assertEquals("snipertower_D", this.sniper.getTextureString());
        assertEquals("slowtower_D", this.slow.getTextureString());
        assertEquals("splashtower_D", this.splash.getTextureString());
        assertEquals("zaptower_D", this.zap.getTextureString());
        assertEquals("multitower", this.multi.getTextureString());
    }

    /**Tests getting the towers health values
     *
     */
    @Test
    public void getHealth() {
        assertEquals(TowerConstants.SIMPLE_TOWER_MAX_HEALTH, this.simple.getHealth());
        assertEquals(TowerConstants.SNIPER_TOWER_MAX_HEALTH, this.sniper.getHealth());
        assertEquals(TowerConstants.SPLASH_TOWER_MAX_HEALTH, this.splash.getHealth());
        assertEquals(TowerConstants.SLOW_TOWER_MAX_HEALTH, this.slow.getHealth());
        assertEquals(TowerConstants.ZAP_TOWER_MAX_HEALTH, this.zap.getHealth());
        assertEquals(TowerConstants.MULTI_TOWER_MAX_HEALTH, this.multi.getHealth());

        this.simple.setHealth(1);
        this.simple.gainFullHealth();
        assertEquals(TowerConstants.SIMPLE_TOWER_MAX_HEALTH ,
        this.simple.getHealth());
    }

    /**Tests getting the towers damage values
     *
     */
    @Test
    public void getDamage() {
        assertEquals(TowerConstants.SIMPLE_TOWER_DAMAGE, this.simple.getDamage());
        assertEquals(TowerConstants.SNIPER_TOWER_DAMAGE, this.sniper.getDamage());
        assertEquals(TowerConstants.SPLASH_TOWER_DAMAGE, this.splash.getDamage());
        assertEquals(TowerConstants.SLOW_TOWER_DAMAGE, this.slow.getDamage());
        assertEquals(TowerConstants.ZAP_TOWER_DAMAGE, this.zap.getDamage());
        assertEquals(TowerConstants.MULTI_TOWER_DAMAGE, this.multi.getDamage());

        //this.simple.setFreezeTime();
        //assertTrue(this.simple.isFrozen());
    }

    /**Tests getting the towers range values
     *
     */
    @Test
    public void getRange() {
        assertEquals(TowerConstants.SIMPLE_TOWER_RANGE, this.simple.getRange());
        assertEquals(TowerConstants.SNIPER_TOWER_RANGE, this.sniper.getRange());
        assertEquals(TowerConstants.SPLASH_TOWER_RANGE, this.splash.getRange());
        assertEquals(TowerConstants.SLOW_TOWER_RANGE, this.slow.getRange());
        assertEquals(TowerConstants.ZAP_TOWER_RANGE, this.zap.getRange());
        assertEquals(TowerConstants.MULTI_TOWER_RANGE, this.multi.getRange());

    }

    /**Tests getting the towers attack speed values
     *
     */
    @Test
    public void getAttackSpeed() {

        assertEquals(TowerConstants.SIMPLE_TOWER_ATTACK_SPEED,
         this.simple.getAttackSpeed(), 1.2);

        boolean val = false;
        if(this.simple.getAttackSpeed() == TowerConstants.SIMPLE_TOWER_ATTACK_SPEED){
            val = true;
        }
        assertTrue(val);

        boolean val2 = false;
        if(this.sniper.getAttackSpeed() == TowerConstants.SNIPER_TOWER_ATTACK_SPEED){
            val2 = true;
        }
        assertTrue(val2);

        boolean val3 = false;
        if(this.splash.getAttackSpeed() == TowerConstants.SPLASH_TOWER_ATTACK_SPEED){
            val3 = true;
        }
        assertTrue(val3);

        boolean val4 = false;
        if(this.slow.getAttackSpeed() == TowerConstants.SLOW_TOWER_ATTACK_SPEED){
            val4 = true;
        }
        assertTrue(val4);
        boolean val5 = false;
        if(this.zap.getAttackSpeed() == TowerConstants.ZAP_TOWER_ATTACK_SPEED){
            val5 = true;
        }
        assertTrue(val5);

        boolean val6 = false;
        if(this.multi.getAttackSpeed() == TowerConstants.MULTI_TOWER_ATTACK_SPEED){
            val6 = true;
        }
        assertTrue(val6);
    }

    /**Tests towers taking damage
     *
     */
    @Test
    public void takeDamage() {
        assertEquals(TowerConstants.SIMPLE_TOWER_MAX_HEALTH, this.simple.getHealth());
        this.simple.takeDamage(20);
        assertEquals(TowerConstants.SIMPLE_TOWER_MAX_HEALTH - 20, this.simple.getHealth());
        this.slow.takeDamage(10);
        assertEquals(TowerConstants.SLOW_TOWER_MAX_HEALTH - 10,this.slow.getHealth());
        this.splash.takeDamage(220);
        assertEquals(TowerConstants.SPLASH_TOWER_MAX_HEALTH - 220,this.splash.getHealth());
        // assertEquals("splashtower_damage1",splash.getTextureString()); <= not yet available yet apparently
        this.zap.takeDamage(30);
        assertEquals(TowerConstants.ZAP_TOWER_MAX_HEALTH - 30,this.zap.getHealth());
        this.multi.takeDamage(30);
        assertEquals(TowerConstants.MULTI_TOWER_MAX_HEALTH - 30,
        this.multi.getHealth());

    }

    /**Tests setting towers health values
     *
     */
    @Test
    public void setHealth(){
        this.simple.setHealth(50);
        assertEquals(50, this.simple.getHealth());

        this.sniper.setHealth(30);
        assertEquals(30, this.sniper.getHealth());

        this.splash.setHealth(40);
        assertEquals(40, this.splash.getHealth());

        this.zap.setHealth(70);
        assertEquals(70, this.zap.getHealth());

        this.multi.setHealth(70);
        assertEquals(70, this.multi.getHealth());

        this.slow.setHealth(60);
        assertEquals(60, this.slow.getHealth());
    }

    /**Tests setting towers damage values
     *
     */
    @Test
    public void setDamage(){
        this.simple.setDamage(70);
        assertEquals(70,this.simple.getDamage());

        this.sniper.setDamage(20);
        assertEquals(20,this.sniper.getDamage());

        this.splash.setDamage(15);
        assertEquals(15,this.splash.getDamage());

        this.zap.setDamage(50);
        assertEquals(50,this.zap.getDamage());

        this.multi.setDamage(50);
        assertEquals(50,this.multi.getDamage());

        this.slow.setDamage(30);
        assertEquals(30,this.slow.getDamage());
    }

    /**Tests setting towers range values
     *
     */
    @Test
    public void setRange(){
        this.simple.setRange(5);
        assertEquals(5,this.simple.getRange());

        this.sniper.setRange(5);
        assertEquals(5,this.sniper.getRange());

        this.splash.setRange(5);
        assertEquals(5,this.splash.getRange());

        this.zap.setRange(3);
        assertEquals(3,this.zap.getRange());

        this.multi.setRange(3);
        assertEquals(3,this.multi.getRange());

        this.slow.setRange(4);
        assertEquals(4,this.slow.getRange());
    }

    /**Tests setting towers attackspeed values
     *
     */
    @Test
    public void setAttackSpeed(){
        this.simple.setAttackSpeed(3.87f);
        assertEquals(3.87f, this.simple.getAttackSpeed(), 0);

        boolean val = false;
        if(this.simple.getAttackSpeed() == 3.87f){
            val = true;
        }
        assertTrue(val);

        this.sniper.setAttackSpeed(3.69f);

        boolean va2 = false;
        if(this.sniper.getAttackSpeed() == 3.69f){
            va2 = true;
        }
        assertTrue(va2);

        this.splash.setAttackSpeed(3.99f);

        boolean va3 = false;
        if(this.splash.getAttackSpeed() == 3.99f){
            va3 = true;
        }
        assertTrue(va3);

        this.zap.setAttackSpeed(2.1f);
        boolean va4 = false;
        if(this.zap.getAttackSpeed() == 2.1f){
            va4 = true;
        }
        assertTrue(va4);

        this.multi.setAttackSpeed(2.13f);
        boolean va5 = false;
        if(this.multi.getAttackSpeed() == 2.13f){
            va5 = true;
        }
        assertTrue(va5);

        this.slow.setAttackSpeed(2.13f);
        boolean va6 = false;
        if(this.slow.getAttackSpeed() == 2.13f){
            va6 = true;
        }
        assertTrue(va6);
    }

    /**Tests that invalid values are fixed
     *
     */
    @Test
    public void checkNegatives() {

        //Sets the stats to invalid values
        simple.setAttackSpeed(-1);
        simple.setDamage(-100);
        simple.setRange(-10);

        //Makes sure all the values were correctly changed
        assertEquals(0, simple.getDamage());
        assertEquals(0.1, simple.getAttackSpeed(), 0.01);
        assertEquals(1, simple.getRange());

    }

    /**Tests that if target was found
     *
     */
    @Test
    public void targetFound() {
        assertFalse(this.simple.wasTargetFound());
        assertFalse(this.multi.wasTargetFound());
        assertFalse(this.sniper.wasTargetFound());
        assertFalse(this.zap.wasTargetFound());
        assertFalse(this.slow.wasTargetFound());
        assertFalse(this.splash.wasTargetFound());

        this.splash.setFreezeTime();
        assertEquals(this.multi.getFreezeTime(), 0);

    }

    /**Makes sure tower is removed from game when it dies
     *
     */
    @Test
    public void death() {

        GameManager gm = GameManager.get();

        //Creates a world with just the simple tower
        CombatEvolvedWorld world = new CombatEvolvedWorld();
        world.addEntity(this.simple);

//      Creates a tower manager and adds the simple tower to it
        TowerDefenseManager towerDefenseManager = new TowerDefenseManager();

//        //Uses power mockito to return the tower defense manager from the static method
//        mockStatic(GameManager.class, CALLS_REAL_METHODS);
//        when(GameManager.getManagerFromInstance(TowerDefenseManager.class)).thenReturn(towerDefenseManager);

        //Sets the world and manager
        gm.setWorld(world);
        gm.addManager(towerDefenseManager);

        //Adds in the simple tower
        towerDefenseManager.addTower(this.simple);

        //Checks to see if simple tower is alive
        assertTrue(towerDefenseManager.checkTowerAlive(this.simple));

        //Should kill simpleTower
        this.simple.setHealth(0);

        //Makes sure tower is now dead
        assertFalse(towerDefenseManager.checkTowerAlive(this.simple));

        this.multi.gainHealth(5);
        assertEquals(205, this.multi.getHealth());

        assertFalse(this.multi.towerDestroyed());
        this.multi.loseHealth(500);
        assertTrue(this.multi.towerDestroyed());

    }

    @Test
    public void isRotatable() {
        assertTrue(TowerConstants.SIMPLE_TOWER_ROTATE);
        assertTrue(TowerConstants.SPLASH_TOWER_ROTATE);
        assertTrue(TowerConstants.SNIPER_TOWER_ROTATE);
        assertTrue(TowerConstants.ZAP_TOWER_ROTATE);
        assertFalse(TowerConstants.MULTI_TOWER_ROTATE);
        assertTrue(TowerConstants.SLOW_TOWER_ROTATE);
    }

    @Test
    public void getSuffix() {
        assertEquals("_D", simple.getSuffix());
        assertEquals("_D", zap.getSuffix());
        assertEquals("_D", sniper.getSuffix());
        assertEquals("_D", splash.getSuffix());
        assertEquals("_D", multi.getSuffix());
        assertEquals("_D", slow.getSuffix());
    }

    @Test
    public void setSuffix() {
        simple.setSuffix("_DR");
        assertEquals("_DR", simple.getSuffix());
        splash.setSuffix("_DR");
        assertEquals("_DR", splash.getSuffix());
        zap.setSuffix("_DR");
        assertEquals("_DR", zap.getSuffix());
        slow.setSuffix("_DR");
        assertEquals("_DR", slow.getSuffix());
    }

    @Test
    public void loseHealth() {
        simple.setHealth(100);
        simple.takeDamage(20);
        assertEquals(80, simple.getHealth());
        splash.setHealth(100);
        splash.takeDamage(90);
        assertEquals(10, splash.getHealth());
        sniper.setHealth(100);
        sniper.takeDamage(50);
        assertEquals(50, sniper.getHealth());
        zap.setHealth(100);
        zap.takeDamage(10);
        assertEquals(90, zap.getHealth());
        multi.setHealth(100);
        multi.takeDamage(25);
        assertEquals(75, multi.getHealth());
        slow.setHealth(100);
        slow.takeDamage(30);
        assertEquals(70, slow.getHealth());
    }

    @Test
    public void gainHealth() {
        simple.setHealth(100);
        simple.gainHealth(10);
        assertEquals( 110, simple.getHealth());
        splash.setHealth(100);
        splash.gainHealth(30);
        assertEquals( 130, splash.getHealth());
        sniper.setHealth(100);
        sniper.gainHealth(100);
        assertEquals( 200, sniper.getHealth());
        zap.setHealth(100);
        zap.gainHealth(50);
        assertEquals( 150, zap.getHealth());
        multi.setHealth(100);
        multi.gainHealth(10);
        assertEquals( 110, multi.getHealth());
        slow.setHealth(100);
        slow.gainHealth(34);
        assertEquals( 134, slow.getHealth());
    }

    @Test
    public void gainFullHealth() {
        simple.setHealth(TowerConstants.SIMPLE_TOWER_MAX_HEALTH);
        assertEquals(TowerConstants.SIMPLE_TOWER_MAX_HEALTH, simple.getHealth());
        splash.setHealth(TowerConstants.SPLASH_TOWER_MAX_HEALTH);
        assertEquals(TowerConstants.SPLASH_TOWER_MAX_HEALTH, splash.getHealth());
        sniper.setHealth(TowerConstants.SNIPER_TOWER_MAX_HEALTH);
        assertEquals(TowerConstants.SNIPER_TOWER_MAX_HEALTH, sniper.getHealth());
        zap.setHealth(TowerConstants.ZAP_TOWER_MAX_HEALTH);
        assertEquals(TowerConstants.ZAP_TOWER_MAX_HEALTH, zap.getHealth());
        multi.setHealth(TowerConstants.MULTI_TOWER_MAX_HEALTH);
        assertEquals(TowerConstants.MULTI_TOWER_MAX_HEALTH, multi.getHealth());
        slow.setHealth(TowerConstants.SLOW_TOWER_MAX_HEALTH);
        assertEquals(TowerConstants.SLOW_TOWER_MAX_HEALTH, slow.getHealth());
    }

}