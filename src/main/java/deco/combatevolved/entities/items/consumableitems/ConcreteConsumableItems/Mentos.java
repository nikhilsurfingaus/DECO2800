package deco.combatevolved.entities.items.consumableitems.ConcreteConsumableItems;

import deco.combatevolved.entities.items.consumableitems.Active;
import deco.combatevolved.entities.items.consumableitems.ActiveItem;
import deco.combatevolved.entities.items.consumableitems.Consumable;

public class Mentos extends ActiveItem implements Consumable, Active {

    private int maxOffence;

    public Mentos(String name, int rarity, String id, String texture, String description) {
        super(name, rarity, id, texture, description);
    }

    /**
     * Doubles player's offence
     */
    public void consume() {
        this.updateStats();
        this.setTimeUsed(System.currentTimeMillis());
    }

    private void updateStats() {
        maxOffence = this.getPlayer().getOffenceMax();
        this.getPlayer().setOffenceMax(maxOffence * 2);
    }

    /**
     * Returns player's offence to normal
     */
    @Override
    public void revertNormal() {
        this.getPlayer().setOffenceMax(maxOffence);
    }
}
