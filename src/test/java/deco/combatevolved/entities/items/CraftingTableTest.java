package deco.combatevolved.entities.items;

import deco.combatevolved.entities.items.resources.Items;
import deco.combatevolved.entities.items.resources.Resource;
import deco.combatevolved.entities.staticentities.CraftingTable;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CraftingTableTest {
    private CraftingTable craftingTable;
    private RecipeBook recipes;
    private Inventory inventory;
    private Items items;

    /**
     * Create a CraftingTable and Recipes that can be used for testing.
     */
    @Before
    public void setup() {
        items = new Items();
        recipes = new RecipeBook(items);
        craftingTable = new CraftingTable();
        inventory = new Inventory(10);

    }

    /**
     * Assert the table can correctly identify that a combination
     * is valid.
     */
    @Test
    public void testValidAdd() {
        craftingTable.addItem(items.getItems().get("wood"), 1);
        craftingTable.addItem(items.getItems().get("wood"), 2);
        craftingTable.addItem(items.getItems().get("wood"), 4);
        craftingTable.addItem(items.getItems().get("roots"), 3);
        craftingTable.addItem(items.getItems().get("potato"), 7);

        assertTrue(recipes.isEntry(craftingTable.getCraftingId()));
    }

    /**
     * Assert the table can correctly identify that a combination
     * is invalid.
     */
    @Test
    public void testInvalidAdd() {
        Item roots = new Resource("roots", 1);
        Item wood = new Resource("wood", 1);

        craftingTable.addItem(wood, 3);
        craftingTable.addItem(wood, 4);
        craftingTable.addItem(wood, 6);
        craftingTable.addItem(roots, 1);

        assertFalse(recipes.isEntry(craftingTable.getCraftingId()));
    }

    /**
     * Assert that the correct type of Item is returned from the
     * crafting table
     */
    @Test
    public void testNewItem() {

        craftingTable.addItem(items.getItems().get("moonrocks"), 1);
        craftingTable.addItem(items.getItems().get("moonrocks"), 4);
        craftingTable.addItem(items.getItems().get("moonrocks"), 7);
        craftingTable.addItem(items.getItems().get("plutonium"), 2);
        craftingTable.addItem(items.getItems().get("unobtanium"), 5);
        craftingTable.addItem(items.getItems().get("plutonium"), 8);

        Item gammaStone = new Item("Gamma Stone", 5);

        assertEquals(gammaStone.getName(),
                recipes.getCraftedItem(craftingTable.getCraftingId()).getName());
    }

    /**
     * Assert that the Crafting Table is cleared correctly
     */
    @Test
    public void testClear() {
        craftingTable.addItem(items.getItems().get("moonrocks"), 3);
        craftingTable.addItem(items.getItems().get("moonrocks"), 4);
        craftingTable.addItem(items.getItems().get("moonrocks"), 5);
        craftingTable.addItem(items.getItems().get("plutonium"), 6);
        craftingTable.addItem(items.getItems().get("moonrocks"), 7);
        craftingTable.addItem(items.getItems().get("moonrocks"), 8);

        craftingTable.clear();

        for (int i = 0; i < 9; i++) {
            assertEquals(null, craftingTable.getItem(i));
        }
    }

    /**
     * Assert that an item added to the table is correctly removed and
     * transferred back to an inventory slot
     */
    @Test
    public void testReturntoInventory() {
        Item moonRock = new Item("moon-rock", 3);
        craftingTable.addItem(moonRock, 3);

        assertTrue(craftingTable.returnToInventory(inventory, 3));
        assertEquals(1, inventory.getItemsNum(moonRock));
        assertNull(craftingTable.getItem(3));
    }


    /**
     * Assert that a newly created item is transferred to the inventory upon
     * successful creation
     */
    @Test
    public void testNewItemTransfer() {
        craftingTable.addItem(items.getItems().get("moonrocks"), 1);
        craftingTable.addItem(items.getItems().get("moonrocks"), 4);
        craftingTable.addItem(items.getItems().get("moonrocks"), 7);
        craftingTable.addItem(items.getItems().get("plutonium"), 2);
        craftingTable.addItem(items.getItems().get("unobtanium"), 5);
        craftingTable.addItem(items.getItems().get("plutonium"), 8);

        Item gammaStone = new Item("Gamma Stone", 5);

        craftingTable.transferToInventory(inventory, craftingTable.getCraftingId());

        assertEquals(1, inventory.getItemsNum(gammaStone));
    }
}
