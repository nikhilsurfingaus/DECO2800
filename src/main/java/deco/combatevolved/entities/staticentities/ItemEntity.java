package deco.combatevolved.entities.staticentities;

import deco.combatevolved.entities.RenderConstants;
import deco.combatevolved.worlds.Tile;
import deco.combatevolved.entities.items.Item;
import deco.combatevolved.entities.items.Inventory;

public class ItemEntity extends StaticEntity {

    // Maximum number of different items to store on a tile
    public static final int MAX_ITEM_STACKS = 7;
    // Factor to reduce item size by
    public static final float TEXTURE_SCALE_FACTOR = 0.25f;
    // Item texture offset
    public static final float TEXTURE_OFFSET = 8f;
    // Item storage
    private transient Inventory inventory = new Inventory(MAX_ITEM_STACKS);

    /**
     * Instantiates a new ItemEntity instance
     * Essentially a StaticEntity with an item
     * 
     * @param tile tile to save as child
     * @param item item to store
     */
    public ItemEntity(Tile tile, Item item) {
        this(tile, item, 1);
    }

    /**
     * Instantiates this entity with the given items stored
     *
     * @param tile tile to save as child
     * @param items array of items to store
     */
    public ItemEntity(Tile tile, Item[] items) {
        super(tile, RenderConstants.ITEM_RENDER, null, false);
        collidable = false;
        for (Item item : items) {
            inventory.addItem(item);
        }
    }

    /**
     * Instansiates this entity with multiple instances of a single item type
     * 
     * @param tile tile to save as child
     * @param item item to store
     * @param numItems number of given item to store
     */
    public ItemEntity(Tile tile, Item item, int numItems) {
        super(tile, RenderConstants.ITEM_RENDER, null, false);
        collidable = false;
        inventory.addItem(item, numItems);
    }

    /**
     * Retrieves the inventory stored in this entity
     * 
     * @return inventory object this entity stores
     */
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Adds items from an array into the inventory
     * Assumes only each element in the array is an individual item (i.e. does not handle stacking)
     * 
     * @param items item array to add
     */
    public void addItemArray(Item[] items) {
        for (Item item : items) {
            inventory.addItem(item);
        }
    }

    public void createInventory() {
        inventory = new Inventory(MAX_ITEM_STACKS);
    }

    /**
     * The following two functions will position items in the tile in the following order
     *   _____
     *  / 3 4 \
     * \ 1 0 2 \
     *  \ 5 6 /
     *   -----
     */

    /**
     * Returns the horizontal offset to position the item at i
     * 
     * @param i position to offset item to
     * @return offset
     */
	public float getOffsetX(int i) {
		switch (i) {
            case 5: // same col as 3
            case 3: return -15f;
            case 6: // same col as 4
            case 4: return 15f;
            case 2: return 25f;
            case 1: return -25f;
            default: return 0f;
        }
	}

    /**
     * Returns the vertical offset to position the item at i
     * 
     * @param i position to offset item to
     * @return offset
     */
	public float getOffsetY(int i) {
		switch (i) {
            case 6: // same row as 5
            case 5: return -25f;
            case 4: // same row as 3
            case 3: return 25f;
            default: return 0f; // 0, 1, 2 will be same row
        }
	}
}