package deco.combatevolved.entities.items.resources;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import deco.combatevolved.entities.items.Item;

public class ItemsTest {

    Items items;

    @Before
    public void before() {
        items = new Items();
    }

    @Test
    public void testGetItems() {
        Map<String, Item> itemMap = items.getItems();
        assertNotNull(itemMap);
        assertTrue(itemMap.size() > 0);
        assertNotNull(itemMap.get("empty"));
    }

    @Test
    public void testGetSpawnableItems() {
        List<Item> itemList;
        for (int rarity = 1; rarity < 6; rarity++) {
            itemList = items.getSpawnableItems(rarity);
            assertNotNull(itemList);
            assertFalse(itemList.isEmpty());
            for (Item item : itemList) {
                assertEquals(rarity, item.getRarity());
            }
        }
    }

    @Test
    public void testGetRandomSpawnableItem() {
        Map<String, Item> itemList = items.getItems();
        Item item;
        for (int rarity = 1; rarity < 6; rarity++) {
            item = items.getRandomSpawnableItem(rarity);
            assertNotNull(item);
            assertEquals(rarity, item.getRarity());
            assertTrue(itemList.containsValue(item)); 
        }
    }

}