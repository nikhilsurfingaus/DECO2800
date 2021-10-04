package deco.combatevolved.entities.items.consumableitems.ConcreteConsumableItems;

import deco.combatevolved.entities.items.consumableitems.Active;
import deco.combatevolved.entities.items.consumableitems.ActiveItem;
import deco.combatevolved.entities.items.consumableitems.Consumable;

public class Cola extends ActiveItem implements Consumable, Active {

    private float maxSpeed;

    public Cola(String name, int rarity, String id, String texture, String description) {
        super(name, rarity, id, texture, description);
    }

    /**
     * Doubles the player's speed
     */
    public void consume() {
        this.updateStats();
        this.setTimeUsed(System.currentTimeMillis());
    }

    private void updateStats() {
        maxSpeed = this.getPlayer().getSpeed();
        this.getPlayer().setCurrentSpeed(maxSpeed * 2);
    }

    /**
     * Returns player's speed to normal
     */
    @Override
    public void revertNormal() {
        this.getPlayer().setSpeed(maxSpeed);
    }
}
