package deco.combatevolved.entities.items;

import org.junit.Test;

import deco.combatevolved.entities.items.consumableitems.ConcreteConsumableItems.Coconut;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class ItemTest {

    Item item = new Item("test", 1);
    Item item2 = new Item("test2", 1, "002", "test2");
    Item item3 = new Item("test3", 1, "003", "test3", "Test description");

    @Test (expected = IllegalArgumentException.class)
    public void testItemZeroRarity() {
        new Item("test", 0);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testItemOverRarity() {
        new Item("test", 6);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testItem2ZeroRarity() {
        new Item("test2", 0, "001", "test2");
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testItem2OverRarity() {
        new Item("test2", 6, "001", "test2");
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testItem3ZeroRarity() {
        new Item("test3", 0, "001", "test3", "Test description");
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testItem3OverRarity() {
        new Item("test3", 6, "001", "test3", "Test description");
    }

    @Test
    public void testItemGetName() {
        assertEquals("test", item.getName());
    }

    @Test
    public void testGetGlowTexture() {
        assertEquals("texture-glow", new Item("test", 1, "id", "texture").getGlowTexture());
    }

    @Test
    public void testItemGetRarity() {
        assertEquals(1, item.getRarity());
    }

    @Test
    public void testHashCode() {
        assertEquals(1 + "test".hashCode() + 1, item.hashCode());
    }

    @Test
    public void testEquals() {
        assertTrue(item.equals(new Item("test", 1)));
        assertEquals(item, new Item("test", 1));
        assertFalse(item.equals(null));
        assertFalse(item.equals(new Object()));
    }
    
    @Test
    public void testNotEqual() {
    	assertFalse(item.equals(null));
    	assertFalse(item.equals(new Object()));
    	assertFalse(item.equals(new Item("test", 2)));
    	assertFalse(item.equals(new Item("not test", 1)));
    }
    
    @Test
    public void testGetId() {
    	assertEquals("002", item2.getId());
    	assertEquals("003", item3.getId());
    }
    
    @Test
    public void testGetTexture() {
    	assertEquals("test2", item2.getTexture());
    	assertEquals("test3", item3.getTexture());
    }
    
    @Test
    public void testGetDescription() {
    	assertEquals("", item.getDescription());
    	assertEquals("", item2.getDescription());
    	assertEquals("Test description", item3.getDescription());
    }
    
    @Test
    public void testSetDescription() {
    	assertEquals("Test description", item3.getDescription());
    	item3.setDescription("New description");
    	assertEquals("New description", item3.getDescription());
    }
    
    @Test
    public void testIsConsumable() {
    	Item coconut = new Coconut("coconut", 1, "coconut", "coconut", "yum"); 
    	assertTrue(coconut.isConsumable());
    }
}