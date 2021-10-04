package deco.combatevolved.entities.staticentities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;

import deco.combatevolved.worlds.Tile;
import org.junit.Test;

import deco.combatevolved.entities.items.Item;

public class ItemEntityTest {

    ItemEntity itemEntity;
    Item[] itemArray = new Item[] { new Item("1", 1), new Item("2", 2), new Item("3", 3) };

    @Before
    public void before() {
        itemEntity = new ItemEntity(new Tile(null, 1f, 1f), new Item("test", 1));
    }

    @Test
    public void testConstructorOverload() {
        itemEntity = new ItemEntity(new Tile(null, 1f, 1f), itemArray);
        assertEquals(3, itemEntity.getInventory().stackCount());
        assertEquals(new Item("1", 1), itemEntity.getInventory().getStack(0).getItem());
        assertEquals(new Item("2", 2), itemEntity.getInventory().getStack(1).getItem());
        assertEquals(new Item("3", 3), itemEntity.getInventory().getStack(2).getItem());
    }

    @Test
    public void testAnotherConstructorOverload() {
        itemEntity = new ItemEntity(new Tile(null, 1f, 1f), new Item("1", 1), 3);
    }

    @Test
    public void testGetItem() {
        assertNotNull(itemEntity.getInventory());
        assertTrue(itemEntity.getInventory().stackCount() > 0);
        assertNotNull(itemEntity.getInventory().getStack(0));
        assertNotNull(itemEntity.getInventory().getStack(0).getItem());
        assertEquals(new Item("test", 1), itemEntity.getInventory().getStack(0).getItem());
    }

    @Test
    public void testAddItemArray() {
        itemEntity.addItemArray(itemArray);
        assertEquals(4, itemEntity.getInventory().stackCount());
        assertEquals(new Item("test", 1), itemEntity.getInventory().getStack(0).getItem());
        assertEquals(new Item("1", 1), itemEntity.getInventory().getStack(1).getItem());
        assertEquals(new Item("2", 2), itemEntity.getInventory().getStack(2).getItem());
        assertEquals(new Item("3", 3), itemEntity.getInventory().getStack(3).getItem());
    }

    @Test
    public void testGetOffsetX() {
        assertEquals(0, Float.compare(-15f, itemEntity.getOffsetX(5)));
        assertEquals(0, Float.compare(-15f, itemEntity.getOffsetX(3)));
        assertEquals(0, Float.compare(15f, itemEntity.getOffsetX(6)));
        assertEquals(0, Float.compare(15f, itemEntity.getOffsetX(4)));
        assertEquals(0, Float.compare(25f, itemEntity.getOffsetX(2)));
        assertEquals(0, Float.compare(-25f, itemEntity.getOffsetX(1)));
        assertEquals(0, Float.compare(0f, itemEntity.getOffsetX(0)));
    }

    @Test
    public void testGetOffsetY() {
        assertEquals(0, Float.compare(-25f, itemEntity.getOffsetY(6)));
        assertEquals(0, Float.compare(-25f, itemEntity.getOffsetY(5)));
        assertEquals(0, Float.compare(25f, itemEntity.getOffsetY(4)));
        assertEquals(0, Float.compare(25f, itemEntity.getOffsetY(3)));
        assertEquals(0, Float.compare(0f, itemEntity.getOffsetY(2)));
        assertEquals(0, Float.compare(0f, itemEntity.getOffsetY(1)));
        assertEquals(0, Float.compare(0f, itemEntity.getOffsetY(0)));
    }

}