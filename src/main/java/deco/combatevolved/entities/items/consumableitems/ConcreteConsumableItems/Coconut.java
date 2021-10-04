package deco.combatevolved.entities.items.consumableitems.ConcreteConsumableItems;

import deco.combatevolved.entities.items.consumableitems.Active;
import deco.combatevolved.entities.items.consumableitems.ActiveItem;
import deco.combatevolved.entities.items.consumableitems.Consumable;

public class Coconut extends ActiveItem implements Consumable, Active {

    private int maxDefence;

    public Coconut(String name, int rarity, String id, String texture, String description) {
        super(name, rarity, id, texture, description);
    }

    /**
     * Doubles player's defence
     */
    public void consume() {
        this.updateStats();
        this.setTimeUsed(System.currentTimeMillis());
    }

    private void updateStats() {
        maxDefence = this.getPlayer().getDefenceMax();
        this.getPlayer().setDefenceMax(maxDefence * 2);
    }

    /**
     * Returns player's defence to normal
     */
    @Override
    public void revertNormal() {
        this.getPlayer().setDefenceMax(maxDefence);
    }
}
