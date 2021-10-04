package deco.combatevolved.entities.items;

import deco.combatevolved.BaseGDXTest;
import deco.combatevolved.entities.dynamicentities.PlayerAttributes;
import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class EquipmentSlotsTest extends BaseGDXTest {

    static Equipment weapon1;
    static Equipment weapon2;
    static Equipment shoes1;
    static Equipment armour1;
    static Equipment helmet1;
    PlayerAttributes player;
    String textureAtlas = "testFrames";

    // Replaced some values here because we changed some base attribute stats. Message me (@blee01) if there are any issues

    @Before
    public void setUp() throws Exception {
        player = new PlayerAttributes(1,1,1,textureAtlas,"ENGINEER");
        Map<String,String> attributes1 = new HashMap<String,String>();
        attributes1.put("attack", "10");
        attributes1.put("defence", "-1");
        weapon1 = new Equipment("DiamondSword", 5, "gun", "999", "diamond",
                "description", attributes1);
        Map<String,String> attributes2 = new HashMap<String,String>();
        attributes2.put("speed", "2");
        attributes2.put("defence", "2");
        shoes1 = new Equipment("DiamondShoes", 5, "shoes", "998", "diamond",
                "description",attributes2);
        Map<String,String> attributes3 = new HashMap<String,String>();
        attributes3.put("attack", "1");
        attributes3.put("defence", "-1");
        weapon2 = new Equipment("normalSword", 1, "grenade", "997", "diamond",
                "description", attributes3);
        armour1 = new Equipment("normalArmour", 1, "armour", "996", "diamond",
                "description", attributes2);
        helmet1 = new Equipment("normalHelmet", 1, "helmet", "995", "diamond",
                "description", attributes2);
        player.getInventory().addItem(weapon1,1);
        player.getInventory().addItem(shoes1);
        player.getInventory().addItem(weapon2);
        player.getInventory().addItem(armour1);
        player.getInventory().addItem(helmet1);
    }

    @Test
    public void equip() {
        assertEquals(40,player.getStats("attack"));
        assertEquals(50,player.getStats("defence"));
        assertEquals(true, player.getEquipmentSlots().equip(player.getInventory(),weapon1));
        assertEquals(50,player.getStats("attack"));
        assertEquals(49,player.getStats("defence"));
        assertEquals(true, player.getEquipmentSlots().equip(player.getInventory(),weapon2));
        assertEquals(41,player.getStats("attack"));
        assertEquals(49,player.getStats("defence"));
        assertEquals(20,player.getStats("speed"));
        player.getEquipmentSlots().equip(player.getInventory(),shoes1);
        assertEquals(22,player.getStats("speed"));
        assertEquals(51,player.getStats("defence"));
    }

    @Test
    public void unequip() {
        assertEquals(20,player.getStats("speed"));
        assertEquals(50,player.getStats("defence"));
        player.getEquipmentSlots().equip(player.getInventory(),shoes1);
        assertEquals(22,player.getStats("speed"));
        assertEquals(52,player.getStats("defence"));
        assertEquals(true,player.getEquipmentSlots().unequip(shoes1,player.getInventory()));
        assertEquals(20,player.getStats("speed"));
        assertEquals(50,player.getStats("defence"));

        player.getEquipmentSlots().equip(player.getInventory(),shoes1);
        assertEquals(22,player.getStats("speed"));
        assertEquals(52,player.getStats("defence"));
        assertEquals(true,player.getEquipmentSlots().unequip("shoes",player.getInventory()));
        assertEquals(20,player.getStats("speed"));
        assertEquals(50,player.getStats("defence"));
        
        player.getEquipmentSlots().equip(player.getInventory(),shoes1);
        assertEquals(22,player.getStats("speed"));
        assertEquals(52,player.getStats("defence"));
        player.getEquipmentSlots().unequip(3);
        assertEquals(20,player.getStats("speed"));
        assertEquals(50,player.getStats("defence"));
    }

    @Test
    public void getWeapon() {
        assertEquals(true,player.getEquipmentSlots().equip(player.getInventory(),weapon1));
        assertEquals(weapon1,player.getEquipmentSlots().getWeapon());
    }

    @Test
    public void getShoes() {
        assertEquals(true,player.getEquipmentSlots().equip(player.getInventory(),shoes1));
        assertEquals(shoes1,player.getEquipmentSlots().getShoes());
    }

    @Test
    public void getHelmet() {
        assertEquals(true,player.getEquipmentSlots().equip(player.getInventory(),helmet1));
        assertEquals(helmet1,player.getEquipmentSlots().getHelmet());
    }

    @Test
    public void getArmour() {
        assertEquals(true,player.getEquipmentSlots().equip(player.getInventory(),armour1));
        assertEquals(armour1,player.getEquipmentSlots().getArmour());
    }
    
    @Test 
    public void getEquipment() {
    	assertEquals(true,player.getEquipmentSlots().equip(player.getInventory(), weapon1));
        assertEquals(weapon1, player.getEquipmentSlots().getEquipment(0));
        
        assertEquals(true,player.getEquipmentSlots().equip(player.getInventory(), helmet1));
        assertEquals(helmet1, player.getEquipmentSlots().getEquipment(1));
        
        assertEquals(true,player.getEquipmentSlots().equip(player.getInventory(), armour1));
        assertEquals(armour1, player.getEquipmentSlots().getEquipment(2));
        
        assertEquals(true,player.getEquipmentSlots().equip(player.getInventory(), shoes1));
        assertEquals(shoes1, player.getEquipmentSlots().getEquipment(3));
    }

    @Test
    public void attackPattern() {
        String defaultAttackPattern = player.getDefaultAttackPattern();

        player.getEquipmentSlots().equip(player.getInventory(),weapon2);
        assertEquals("projectile", player.getAttackPattern());
        player.getEquipmentSlots().unequip(0);
        assertEquals(defaultAttackPattern, player.getAttackPattern());

        player.getEquipmentSlots().equip(player.getInventory(),weapon1);
        assertEquals("bullet", player.getAttackPattern());
        player.getEquipmentSlots().unequip(0);
        assertEquals(defaultAttackPattern, player.getAttackPattern());
    }
    
    @Test
    public void getEquipmentNull() {
        player.getEquipmentSlots().unequip(0);
    	assertEquals(null, player.getEquipmentSlots().getEquipment(0));
    }

    @Test
    public void getWeaponNull() {
        player.getEquipmentSlots().unequip(0);
    	assertEquals(null, player.getEquipmentSlots().getWeapon());
    }
    
    @Test
    public void getArmorNull() {
    	assertEquals(null, player.getEquipmentSlots().getArmour());
    }
    
    @Test
    public void getHelmetNull() {
    	assertEquals(null, player.getEquipmentSlots().getHelmet());
    }
    
    @Test
    public void getShoesNull() {
    	assertEquals(null, player.getEquipmentSlots().getShoes());
    }
}