package deco.combatevolved.entities.items.consumableitems;

import deco.combatevolved.entities.dynamicentities.PlayerPeon;
import deco.combatevolved.entities.items.Item;
import deco.combatevolved.managers.GameManager;
import deco.combatevolved.renderers.ConsumableRenderer;

public abstract class ActiveItem extends ConsumableItem {

    private long timeUsed = System.currentTimeMillis();

    public ActiveItem(String name, int rarity, String id, String texture, String description) {
        super(name, rarity, id, texture, description);
    }

    public long getTimeUsed() {
        return timeUsed;
    }

    public void setTimeUsed(long timeUsed) {
        this.timeUsed = timeUsed;
    }

    /**
     * To be overwritten by sub classes
     */
    public void revertNormal() {}
}
