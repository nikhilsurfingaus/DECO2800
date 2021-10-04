package deco.combatevolved.entities.staticentities;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import deco.combatevolved.Tickable;
import deco.combatevolved.entities.RenderConstants;
import deco.combatevolved.entities.items.Inventory;
import deco.combatevolved.entities.items.Item;
import deco.combatevolved.entities.items.RecipeBook;
import deco.combatevolved.entities.items.resources.Items;
import deco.combatevolved.managers.GameManager;
import deco.combatevolved.managers.SoundManager;
import deco.combatevolved.worlds.Tile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The CraftingTable will be used for players to combine resources into
 * specific combinations in order to build new, more improved items.
 * If the combination of resources is a valid combination, then the CraftingTable
 * will return the new resource object to the player and store it in their inventory.
 * The CraftingTable will be stored on the map as an entity and accessed by the player
 * when within it's vicinity.
 */
public class CraftingTable extends StaticEntity implements Tickable {

    private final Logger logger = LoggerFactory.getLogger(CraftingTable.class);

    // items currently in the crafting table.
    private List<Item> items;

    // all possible recipes for the player to craft from
    private RecipeBook craftingRecipes;

    // all items in the game
    private Items allItems;

    // the item that has been crafted.
    private Item craftedItem;

    /**
     * A CraftingTable object that checks combinations of resources and determines whether
     * an item can be created from them. The CraftingTable will be stored on the map as an
     * entity and accessed by the player when within it's vicinity.
     *
     * @param col the col position on the world
     * @param row the row position on the world
     * @param renderOrder the height position on the world
     * @param parts the parts of the CraftingTable
     */
    public CraftingTable(float col, float row, int renderOrder, List<Part> parts) {
        super(col, row, renderOrder, parts);
        logger.info("Making a crafting table at {}, {}", col, row);
        this.setTexture("craftingtable");
        items = new ArrayList<>(9);
        allItems = new Items();
        this.craftingRecipes = new RecipeBook(allItems);

        for (int i = 0; i < 9; i++) {
            items.add(i, null);
        }

        this.craftedItem = null;

    }

    /**
     * A CraftingTable object that checks combinations of resources and determines whether
     * an item can be created from them. The CraftingTable will be stored on the map as an
     * entity and accessed by the player when within it's vicinity.
     *
     * @param t the tile on the world
     * @param obstructed true if occupied, false otherwise.
     */
    public CraftingTable(Tile t, boolean obstructed) {
        super(t, RenderConstants.BUILDING_RENDER, "craftingtable", obstructed);
        items = new ArrayList<>(9);
        allItems = new Items();
        this.craftingRecipes = new RecipeBook(allItems);

        for (int i = 0; i < 9; i++) {
            items.add(i, null);
        }


        // test items to check functionality of crafting.
        //this.addItem(allItems.getItems().get("medkit"),0);

        this.addItem(allItems.getItems().get("wood"), 1);
        this.addItem(allItems.getItems().get("wood"), 2);
        this.addItem(allItems.getItems().get("wood"), 4);
        this.addItem(allItems.getItems().get("roots"), 1);
        this.addItem(allItems.getItems().get("potato"), 7);
        //this.addItem(allItems.getItems().get("silver"),5);
        //this.addItem(allItems.getItems().get("copper"),6);
        //this.addItem(allItems.getItems().get("copper"),7);
        //this.addItem(allItems.getItems().get("iron"),8);

    }


    /**
     * A new CraftingTable object.
     */
    public CraftingTable() {
        items = new ArrayList<>(9);
        allItems = new Items();
        this.craftingRecipes = new RecipeBook(allItems);

        for (int i = 0; i < 9; i++) {
            items.add(i, null);
        }
    }

    /**
     * Returns the item in the table at given index
     * @param index The index of the item
     * @return The name of an item in the table
     * @throws IllegalArgumentException if index is invalid
     */
    public Item getItem(int index) {
        if (index < 0 || index > 9) {
            throw new IllegalArgumentException("invalid grid index");
        }
        return items.get(index);
    }

    /**
     * Returns the name of an item in the table
     * @param index The index of the item
     * @return The name of an item in the table
     * @throws IllegalArgumentException if index is invalid
     */
    public String getItemName(int index) {
        if (index < 0 || index > 9) {
            throw new IllegalArgumentException("invalid grid index");
        }
        return items.get(index).getName();
    }


    /**
     * Returns the items currently in the crafting table.
     *
     * @return the items in the crafting table
     */
    public List<Item> getItems() {
        return items;
    }

    /**
     * Adds an item to the crafting table for crafting.
     *
     * @param item The item to add
     * @param index The indexk represented by a square on the table
     * @throws IllegalArgumentException if index not between 0 & 9 (inclusive)
     */
    public void addItem(Item item, int index) {
        if (index < 0 || index > 9) {
            throw new IllegalArgumentException("invalid crafting index");
        }
        if (this.isFreeSlot(index)) {
            items.set(index, item);
        }
    }

    /**
     * Returns whether there is already an item at given index in table
     * @param index index to check
     * @return true if empty
     */
    public boolean isFreeSlot(int index) {
        return items.get(index) == null;
    }

    /**
     * Removes a non-null item from the grid
     * @param index index to remove from
     */
    public void removeItem(int index) {
        if (items.get(index) != null) {
            items.set(index, null);
        }
    }

    /**
     * Clear the craftingTable of all items
     */
    public void clear() {
        for (int i = 0; i < 9; i++) {
            items.set(i, null);
        }
    }

    /**
     * Puts an item that is being stored in the crafting table back into an
     * inventory slot.
     * @param inventory The player's inventory
     * @param index The index of the item
     * @return true if successfully added
     */
    public boolean returnToInventory(Inventory inventory, int index) {
        if (items.get(index) != null && inventory.addItem(items.get(index)) == 0) {
            this.removeItem(index);
            return true;
        }
        return false;
    }

    /**
     * Transfers a newly crafted item to the player's inventory
     * @param inventory The player's inventory
     * @param entry The String ID of the crafting table
     */
    public void transferToInventory(Inventory inventory, List<String> entry) {
        if (craftingRecipes.isEntry(entry)) {
            Item newItem = craftingRecipes.getCraftedItem(entry);
            //GameManager.get().getManager(SoundManager.class).playSound("Sound Effect/EFFECT_click_01_Inactive.mp3");
            inventory.addItem(newItem);
        }
    }

    /**
     * Generates the identifier for all the items placed in the crafting tables
     * to be put into the hashmap to check for valid item combination.
     *
     * @return Crafting ID
     */
    public List<String> getCraftingId() {

        ArrayList<String> craftingId = new ArrayList<>();

        for (Item item : items) {

            if (item == null) {
                craftingId.add("001");
            } else {
                craftingId.add(item.getId());
            }
        }
        return craftingId;
    }

    /**
     * Attempts to craft an item
     * @return the item that is crafted. Otherwise, returns null
     */
    public Item craftItem() {

        if (craftingRecipes.isEntry(this.getCraftingId())) {
            List<String> temp = new ArrayList<>(this.getCraftingId()); // deep copy
            return craftingRecipes.getCraftedItem(temp);
        } else {
            return null;
        }
    }

    /**
     * Sets the crafted item to the specified item
     * @param item the item you want the player to have crafted
     */
    public void setCraftedItem(Item item) {

        this.craftedItem = item;
    }

    /**
     * Gets the item the player has crafted.
     * This will just be null if they haven't crafted anything.
     * @return the crafted item the player has made
     */
    public Item getCraftedItem() {

        return this.craftedItem;
    }

    /**
     * Sets crafted item to null. (Use once player collects item from slot)
     */
    public void resetCraftedItem() {

        this.craftedItem = null;

    }

    /**
     * Returns a rectangle that wraps around the crafting table on the map. Used
     * to detect if the crafting table has been clicked
     * @return
     */
    public Rectangle getRectangleBounds() {

        return new Rectangle(0, 0, 1000,1000);
    }
}