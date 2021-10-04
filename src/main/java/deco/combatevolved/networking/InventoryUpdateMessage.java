package deco.combatevolved.networking;

import deco.combatevolved.entities.items.Item;

public class InventoryUpdateMessage {

    /**
     * Nicer way of storing the update type.
     */
    public enum UpdateType {
        P_ADD,
        P_REMOVE,
        P_TRANSFER,
        I_ADD,
        I_REMOVE
    }

    private UpdateType updateType;
    private Item item;
    private int entityID;
    private int itemNum;

    /**
     * The type of update of this message to handle.
     * @return the type of update
     */
    public UpdateType getUpdateType() {
        return updateType;
    }

    /**
     * Sets the type of update of this message to be handled.
     * @param updateType the type of update to be handled
     */
    public void setUpdateType(UpdateType updateType) {
        this.updateType = updateType;
    }

    /**
     * Gets the item to add/remove.
     * @return item to add/remove
     */
    public Item getItem() {
        return item;
    }

    /**
     * Sets the item to add/remove.
     * @param item item to add/remove
     */
    public void setItem(Item item) {
        this.item = item;
    }

    /**
     * Gets the item number to add/remove.
     * @return number of items to add/remove
     */
    public int getItemNum() {
        return itemNum;
    }

    /**
     * Sets the number of items to add/remove.
     * @param itemNum number of the items to add/remove
     */
    public void setItemNum(int itemNum) {
        this.itemNum = itemNum;
    }

    /**
     * Gets the player entity id associated to this message.
     * @return player entity id
     */
    public int getEntityID() {
        return entityID;
    }

    /**
     * Sets the player entity id associated to this message.
     * @param entityID player entity id
     */
    public void setEntityID(int entityID) {
        this.entityID = entityID;
    }
}
