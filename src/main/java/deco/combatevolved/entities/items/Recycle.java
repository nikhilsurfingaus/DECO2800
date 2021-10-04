package deco.combatevolved.entities.items;

import deco.combatevolved.entities.dynamicentities.PlayerPeon;

public class Recycle extends Inventory {

    public Recycle() {
        super(1);
    }

    /**
     * Calculate how much scrap would be generated from the input stack
     * Currently number of items multiplied by rarity
     * 
     * @return integer number of scrap potentially generated from input stack
     */
    public int calculate() {
        if (getStack(0) != null) {
            return getStack(0).getItem().getRarity() * getStack(0).getNumItems();
        }
        return 0;
    }

    /**
     * convert the item in the recycle into the common resources
     *
     * @param player the player convert the item
     * @return true if all items in recycle is recycled successfully, otherwise false
     */
    public boolean convert(PlayerPeon player) {
        int scrap;
        if (getStack(0) == null) {
            return false;
        }
        scrap = calculate();
        player.changeScrap(scrap);
        removeStack(0);
        return true;
    }

}