package deco.combatevolved.entities.items.resources;

import deco.combatevolved.entities.items.Item;

/**
 * Represents an in-game resource that can be collected by the player and used
 * for in-game functionality such as crafting.
 */
public class Resource extends Item {
    public Resource(String name, int rarity, String id, String texture) {
        super(name, rarity, id, texture);
        }
    public Resource(String name, int rarity) {
        super(name, rarity);
    }
}


