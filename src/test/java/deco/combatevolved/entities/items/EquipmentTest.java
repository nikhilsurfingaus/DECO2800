package deco.combatevolved.entities.items;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import java.util.*;

public class EquipmentTest {

    Equipment equipment1;
    Equipment equipment2;

    @Before
    public void setUp() throws Exception {
        Map <String,String> attributes = new HashMap<String,String>();
        attributes.put("attack", "10");
        attributes.put("defence", "-1");
        equipment1 = new Equipment("DiamondSword", 5, "gun", "999", "diamond",
                "description",attributes);
    }

    @Test
    public void constructUseDefaultSetting() {
        try {
            equipment2 = new Equipment("testDefaultSetting", 2, "gun", "998", "diamond");
            fail("Exception expected.");
        } catch (Exception expected) {
            assertEquals("Didn't find the default setting of the equipment", expected.getMessage());
        }
        try {
            equipment2 = new Equipment("testDefaultSetting", 5, "gun", "998", "diamond");
        } catch (Exception expected) {
            fail("Exception not expected.");
        }
        assertEquals("gun", equipment2.getType());
        assertEquals(1, equipment2.getSpeed());
        assertEquals(10, equipment2.getHealth());
        assertEquals("description test", equipment2.getDescription());
    }

    @Test
    public void getType() {
        assertEquals("gun", equipment1.getType());
    }

    @Test
    public void getAttack() {
        assertEquals(10, equipment1.getAttack());
    }

    @Test
    public void getDefence() {
        assertEquals(-1, equipment1.getDefence());
    }

    @Test
    public void getSpeed() {
        assertEquals(0, equipment1.getSpeed());
    }

    @Test
    public void getHealth() {
        assertEquals(0, equipment1.getHealth());
    }

    @Test
    public void testDefaultAttributeShoeLow() {
        try {
            equipment2 = new Equipment("shoes_low", 1, "shoe", "999", "shoe");
        } catch (Exception expected) {
            fail("Exception not expected.");
        }

        assertEquals("shoe", equipment2.getType());
        assertEquals(1, equipment2.getSpeed());
        assertEquals(5, equipment2.getDefence());
        assertEquals("Speed+1, Defence+5", equipment2.getDescription());
    }

    @Test
    public void testDefaultAttributeShoeMid() {
        try {
            equipment2 = new Equipment("shoes_mid", 2, "shoe", "999", "shoe");
        } catch (Exception expected) {
            fail("Exception not expected.");
        }

        assertEquals("shoe", equipment2.getType());
        assertEquals(3, equipment2.getSpeed());
        assertEquals(10, equipment2.getDefence());
        assertEquals("Speed+3, Defence+10", equipment2.getDescription());
    }

    @Test
    public void testDefaultAttributeShoeHigh() {
        try {
            equipment2 = new Equipment("shoes_high", 3, "shoe", "999", "shoe");
        } catch (Exception expected) {
            fail("Exception not expected.");
        }

        assertEquals("shoe", equipment2.getType());
        assertEquals(5, equipment2.getSpeed());
        assertEquals(30, equipment2.getDefence());
        assertEquals("Speed+5, Defence+30", equipment2.getDescription());
    }

    @Test
    public void testDefaultAttributeHelmetLow() {
        try {
            equipment2 = new Equipment("helmet_low", 1, "helmet", "999", "helmet");
        } catch (Exception expected) {
            fail("Exception not expected.");
        }

        assertEquals("helmet", equipment2.getType());
        assertEquals(5, equipment2.getDefence());
        assertEquals("Defence+5", equipment2.getDescription());
    }

    @Test
    public void testDefaultAttributeHelmetMid() {
        try {
            equipment2 = new Equipment("helmet_mid", 2, "helmet", "999", "helmet");
        } catch (Exception expected) {
            fail("Exception not expected.");
        }

        assertEquals("helmet", equipment2.getType());
        assertEquals(20, equipment2.getDefence());
        assertEquals("Defence+20", equipment2.getDescription());
    }

    @Test
    public void testDefaultAttributeHelmetHigh() {
        try {
            equipment2 = new Equipment("helmet_high", 3, "helmet", "999", "helmet");
        } catch (Exception expected) {
            fail("Exception not expected.");
        }

        assertEquals("helmet", equipment2.getType());
        assertEquals(40, equipment2.getDefence());
        assertEquals("Defence+40", equipment2.getDescription());
    }

    @Test
    public void testDefaultAttributeArmourLow() {
        try {
            equipment2 = new Equipment("armour_low", 1, "armour", "999", "armour");
        } catch (Exception expected) {
            fail("Exception not expected.");
        }

        assertEquals("armour", equipment2.getType());
        assertEquals(0, equipment2.getSpeed());
        assertEquals(10, equipment2.getDefence());
        assertEquals("Defence+10", equipment2.getDescription());
    }

    @Test
    public void testDefaultAttributeArmourMid() {
        try {
            equipment2 = new Equipment("armour_mid", 2, "armour", "999", "armour");
        } catch (Exception expected) {
            fail("Exception not expected.");
        }

        assertEquals("armour", equipment2.getType());
        assertEquals(-1, equipment2.getSpeed());
        assertEquals(30, equipment2.getDefence());
        assertEquals("Defence+30,Speed-1", equipment2.getDescription());
    }

    @Test
    public void testDefaultAttributeArmourHigh() {
        try {
            equipment2 = new Equipment("armour_high", 3, "armour", "999", "armour");
        } catch (Exception expected) {
            fail("Exception not expected.");
        }

        assertEquals("armour", equipment2.getType());
        assertEquals(-2, equipment2.getSpeed());
        assertEquals(50, equipment2.getDefence());
        assertEquals("Defence+50,Speed-2", equipment2.getDescription());
    }

    @Test
    public void testDefaultAttributeGunLow() {
        try {
            equipment2 = new Equipment("gun_low", 1, "gun", "999", "gun");
        } catch (Exception expected) {
            fail("Exception not expected.");
        }

        assertEquals("gun", equipment2.getType());
        assertEquals("attack pattern: bullet", equipment2.getDescription());
    }

    @Test
    public void testDefaultAttributeGunMid() {
        try {
            equipment2 = new Equipment("gun_mid", 2, "gun", "999", "gun");
        } catch (Exception expected) {
            fail("Exception not expected.");
        }

        assertEquals("gun", equipment2.getType());
        assertEquals(30, equipment2.getAttack());
        assertEquals("attack pattern: bullet, attack+30", equipment2.getDescription());
    }

    @Test
    public void testDefaultAttributeGunHigh() {
        try {
            equipment2 = new Equipment("gun_high", 3, "gun", "999", "gun");
        } catch (Exception expected) {
            fail("Exception not expected.");
        }

        assertEquals("gun", equipment2.getType());
        assertEquals(50, equipment2.getAttack());
        assertEquals("attack pattern: bullet, attack+50", equipment2.getDescription());
    }

    @Test
    public void testDefaultAttributeGrenadeLow() {
        try {
            equipment2 = new Equipment("grenade_low", 1, "grenade", "999", "grenade");
        } catch (Exception expected) {
            fail("Exception not expected.");
        }

        assertEquals("grenade", equipment2.getType());
        assertEquals("attack pattern: projectile", equipment2.getDescription());
    }

    @Test
    public void testDefaultAttributeGrenadeMid() {
        try {
            equipment2 = new Equipment("grenade_mid", 2, "grenade", "999", "grenade");
        } catch (Exception expected) {
            fail("Exception not expected.");
        }

        assertEquals("grenade", equipment2.getType());
        assertEquals(30, equipment2.getAttack());
        assertEquals("attack pattern: projectile, attack+30", equipment2.getDescription());
    }

    @Test
    public void testDefaultAttributeGrenadeHigh() {
        try {
            equipment2 = new Equipment("grenade_high", 3, "grenade", "999", "grenade");
        } catch (Exception expected) {
            fail("Exception not expected.");
        }

        assertEquals("grenade", equipment2.getType());
        assertEquals(50, equipment2.getAttack());
        assertEquals("attack pattern: projectile, attack+50", equipment2.getDescription());
    }
}