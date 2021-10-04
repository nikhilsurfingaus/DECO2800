package deco.combatevolved.entities.staticentities;

import deco.combatevolved.entities.staticentities.defensivetowers.TowerConstants;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TowerConstantsTest {
    private TowerConstants tc;

    @Test
    public void test() {
        assertEquals(tc.SIMPLE_TOWER_DAMAGE,20);
        assertEquals(tc.SIMPLE_TOWER_RANGE ,4);
        assertEquals(tc.SIMPLE_TOWER_MAX_HEALTH,150);
        assertEquals(tc.SIMPLE_TOWER_ATTACK_SPEED, 0.5f, 0);
        assertTrue(tc.SIMPLE_TOWER_ROTATE);

        assertEquals(tc.SPLASH_TOWER_DAMAGE,15);
        assertEquals(tc.SPLASH_TOWER_RANGE ,4);
        assertEquals(tc.SPLASH_TOWER_MAX_HEALTH,250);
        assertEquals(tc.SPLASH_TOWER_ATTACK_SPEED, 2f, 0);
        assertTrue(tc.SPLASH_TOWER_ROTATE);

        assertEquals(tc.SNIPER_TOWER_DAMAGE,160);
        assertEquals(tc.SNIPER_TOWER_RANGE ,8);
        assertEquals(tc.SNIPER_TOWER_MAX_HEALTH,100);
        assertEquals(tc.SNIPER_TOWER_ATTACK_SPEED, 2f, 0);
        assertTrue(tc.SNIPER_TOWER_ROTATE);

        assertEquals(tc.SLOW_TOWER_DAMAGE,10);
        assertEquals(tc.SLOW_TOWER_RANGE ,6);
        assertEquals(tc.SLOW_TOWER_MAX_HEALTH,150);
        assertEquals(tc.SLOW_TOWER_ATTACK_SPEED, 0.75f, 0);
        assertTrue(tc.SLOW_TOWER_ROTATE);

        assertEquals(tc.ZAP_TOWER_DAMAGE,5);
        assertEquals(tc.ZAP_TOWER_RANGE ,5);
        assertEquals(tc.ZAP_TOWER_MAX_HEALTH,125);
        assertEquals(tc.ZAP_TOWER_ATTACK_SPEED, 0.1f, 0);
        assertTrue(tc.ZAP_TOWER_ROTATE);

        assertEquals(tc.MULTI_TOWER_DAMAGE,20);
        assertEquals(tc.MULTI_TOWER_RANGE ,4);
        assertEquals(tc.MULTI_TOWER_MAX_HEALTH,200);
        assertEquals(tc.MULTI_TOWER_ATTACK_SPEED, 0.5f, 0);
        assertFalse(tc.MULTI_TOWER_ROTATE);

        assertEquals(174.5, this.tc.simpleTotal(), 0 );
        assertEquals(271.0, this.tc.splashTotal(), 0 );
        assertEquals(270.0, this.tc.sniperTotal(), 0 );
        assertEquals(135.1, this.tc.zapTotal(), 0.5 );
        assertEquals(224.5, this.tc.multiTotal(), 0 );
        assertEquals(166.75, this.tc.slowTotal(), 0 );
    }

}