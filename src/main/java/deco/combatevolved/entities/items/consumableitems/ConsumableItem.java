package deco.combatevolved.entities.items.consumableitems;

import deco.combatevolved.entities.dynamicentities.PlayerPeon;
import deco.combatevolved.entities.items.Item;
import deco.combatevolved.managers.GameManager;

public abstract class ConsumableItem extends Item {

    private PlayerPeon player = null;

    public ConsumableItem(String name, int rarity, String id, String texture, String description) {
        super(name, rarity, id, texture, description);
    }

    /**
     * Sets the player (mainly for testing purposes)
     * @param player the player consumable works on
     */
    public void setPlayer(PlayerPeon player) {
        this.player = player;
    }

    protected PlayerPeon getPlayer() {
        if (player != null) {
            return player;
        }

        return (PlayerPeon) GameManager.get().getWorld().
                getEntityById(GameManager.get().getPlayerEntityID());
    }

}
