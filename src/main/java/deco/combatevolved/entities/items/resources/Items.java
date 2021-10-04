package deco.combatevolved.entities.items.resources;

import deco.combatevolved.entities.items.Equipment;
import deco.combatevolved.entities.items.Item;
import deco.combatevolved.entities.items.consumableitems.ConcreteConsumableItems.Coconut;
import deco.combatevolved.entities.items.consumableitems.ConcreteConsumableItems.Cola;
import deco.combatevolved.entities.items.consumableitems.ConcreteConsumableItems.Medkit;
import deco.combatevolved.entities.items.consumableitems.ConcreteConsumableItems.Mentos;

import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Creates and holds all possible items in the game
 */
public class Items {
	//Map of all items in the game
    private static Map<String, Item> itemMap = new HashMap<>();

    public Items() {
        createItems();
    }

    /**
     * Returns a map of all items in the game
     * 
     * @return a map of all items in the game
     */
    public Map<String, Item> getItems() {
        return itemMap;
    }

    /**
     * Creates all the items in the game
     */
    private void createItems() {

        String placeholderTexture = "stone";

        // empty represents an empty slot
        Item empty = new Item("Empty", 1, "001", "empty");
        Item wood = new Item("Wood", 1, "002", "wood","This item comes from trees and roots. It's a useful and basic craftable item.");
        Item stone = new Item("Stone", 1, "003", placeholderTexture,"A common thing you can find on the ground.");
        Item copper = new Item("Copper", 1, "004", "copper","This is a medal. It's very useful, don't throw it!");
        Item bronze = new Item("Bronze", 1, "005", "bronze","This is a medal. It's probably one of the compositions of a powerful weapon.");
        Item roots = new Item("Roots", 1, "006", "roots","You find the underground part of trees!");
        Item nickel = new Item("Nickel", 1, "007", "nickel","This is a medal, something for making a rare but useful item");
        Item potato = new Item("Potato", 1, "008", "potato", "It tastes great, and it can help to craft great things");
        Item coconut = new Coconut("Coconut", 1, "009", placeholderTexture,"Coconut milk probably tastes good, but coconut will also be powerful weapon if you throw it to others.");
        Item oxygen = new Item("Oxygen", 1, "010", placeholderTexture,"You can't live without oxygen.");
        Item iron = new Item("Iron", 2, "011", "iron","This is a medal but it's more rare. It's useful in crafting items.");
        Item concrete = new Item("Concrete", 2, "012", "concrete","It's a artificial stone mixed with dry cement, water and some other materials with specific amount of proportion.");
        Item cola = new Cola("Cola", 2, "013", "cola","Cola is a popular soft drink but if you shake the bottle, it will probably explode.");
        Item mentos = new Mentos("Mentos", 2, "014", "mentos", "Mentos is mint sugar. Remember not to put them into cola.");
        Item spudgun = new Item("Spud Gun", 2, "015", "spudgun", "We know it's a toy gun and we made it from potato, but it's still a powerful weapon.");
        Item rope = new Item("Rope", 2, "016", "rope", "Rope is a powerful tool, you can use it to drag, lift and tie up items.");
        Item steel = new Item("Steel", 3, "017", "steel", "Steel is a useful item which is a major component used in buildings, tools,  machines and weapons.");
        Item silver = new Item("Silver", 3, "018", "silver", "This is a shiny and expensive medal. Maybe you will successfully build an upgraded steel by using silver.");
        Item moonrocks = new Item("Moon Rocks", 3, "019", "moonrocks", "This is a rock from the moon which is obviously pretty hard to find one.");
        Item redstone = new Item("Red Stone", 3, "020", "redstone", "This is a stone but with special color. You may use redstone to craft some amazing items.");
        Item spikedmace = new Item("Spiked Mace", 3, "021", "spikedmace", "A powerful weapon. Be careful not to hurt yourself when using it.");
        Item gold = new Item("Gold", 4, "022", "gold", "Gold is a precious metal that is widely used. It is also eaten by rich people... strange");
        Item titanium = new Item("Titanium", 4, "023", "titanium", "A medal with a silver color, low density, and high strength.");
        Item unobtanium = new Item("Unobtanium", 4, "024", "unobtanium", "A fictional, extremely rare, costly, and impossible material but it's also useful and fascinating.");
        Item reinforcedconcrete = new Item("Reinforced Concrete", 4, "025", "reinforcedconcrete", "A composite material formed by adding steel mesh, steel plate or fiber to concrete.");
        Item grenade = new Item("Grenade", 4, "026", "grenade", "Grenade is a powerful explosive dangerous weapon. It has high damage and cause powerful explosion.");
        Item plasma = new Item("Plasma", 4, "027", "plasma", "A mixture of gas ions and atoms. It is the most abundant form of ordinary matter in the universe");
        Item plutonium = new Item("Plutonium", 5, "028", "plutonium", "A actinide metal of silvery-gray appearance. Be careful it's radioactive.");
        Item uranium = new Item("Uranium", 5, "029", "uranium", "A silvery white, weakly radioactive metal. ");
        Item diamond = new Item("Diamond", 5, "030", "diamond", "A precious gem. It shines and glisten under the light.");
        Item alientech = new Item("Alien Tech", 5, "031", "alientech", "Advanced technology used by the inhabitatnts of the land");
        Item gammastone = new Item("Gamma Stone", 5, "032", "gammastone", "The rarest and strongest material present in the world.");

        Equipment lowGun = new Equipment("gun_low", 1, "gun", "401", "gun_low");
        Equipment midGun = new Equipment("gun_mid", 2, "gun", "402", "gun_mid");
        Equipment highGun = new Equipment("gun_high", 3, "gun", "403", "gun_low");
        Equipment lowGrenade = new Equipment("grenade_low", 1, "grenade", "404", "grenade_low");
        Equipment midGrenade = new Equipment("grenade_mid", 2, "grenade", "405", "grenade_mid");
        Equipment highGrenade = new Equipment("grenade_high", 3, "grenade", "406", "grenade_low");
        Item medkit = new Medkit("Medkit", 2, "033", "medkit" , "Cures any injury, ailment or mortal wound. We're not sure what it's made of, but it's probably illegal.");

        Item simpleTower = new Item("Simple Tower", 1, "034", "simpletower_D" , "A turret used to fight enemies.");

        // adding newly created items to the hash map.
        itemMap.put("empty", empty);
        itemMap.put("simpleTower", simpleTower);
        itemMap.put("wood", wood);
        itemMap.put(placeholderTexture, stone);
        itemMap.put("copper", copper);
        itemMap.put("bronze", bronze);
        itemMap.put("roots", roots);
        itemMap.put("nickel", nickel);
        itemMap.put("potato", potato);
        itemMap.put("coconut", coconut);
        itemMap.put("oxygen", oxygen);
        itemMap.put("iron", iron);
        itemMap.put("concrete", concrete);
        itemMap.put("cola", cola);
        itemMap.put("mentos", mentos);
        itemMap.put("spudgun", spudgun);
        itemMap.put("rope", rope);
        itemMap.put("steel", steel);
        itemMap.put("silver", silver);
        itemMap.put("moonrocks", moonrocks);
        itemMap.put("redstone", redstone);
        itemMap.put("spikedmace", spikedmace);
        itemMap.put("gold", gold);
        itemMap.put("titanium", titanium);
        itemMap.put("unobtanium", unobtanium);
        itemMap.put("reinforcedconcrete", reinforcedconcrete);
        itemMap.put("grenade", grenade);
        itemMap.put("plasma", plasma);
        itemMap.put("plutonium", plutonium);
        itemMap.put("uranium", uranium);
        itemMap.put("diamond", diamond);
        itemMap.put("alientech", alientech);
        itemMap.put("gammastone", gammastone);
        itemMap.put("medkit", medkit);
        itemMap.put("lowGun", lowGun);
        itemMap.put("midGun", midGun);
        itemMap.put("highGun", highGun);
        itemMap.put("lowGrenade", lowGrenade);
        itemMap.put("midGrenade", midGrenade);
        itemMap.put("highGrenade", highGrenade);
    }

    /**
     * Returns a list of items that can spawn on the map
     * 
     * @param rarity level of item rarity
     * @return a list of spawnable items of given rarity
     */
    public List<Item> getSpawnableItems(int rarity) {
        // TODO differentiate items by their biome and eligibility to spawn
        return itemMap.values()
            .stream()
            .filter(item -> item.getRarity() == rarity)
            .collect(Collectors.toList());
    }

    public Item getRandomSpawnableItem(int rarity) {
        List<Item> eligibleItems = getSpawnableItems(rarity);
        return eligibleItems.get(new Random().nextInt(eligibleItems.size()));
    }

}
