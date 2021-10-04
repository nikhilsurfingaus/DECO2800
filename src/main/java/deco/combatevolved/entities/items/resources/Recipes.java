package deco.combatevolved.entities.items.resources;

import deco.combatevolved.entities.items.Equipment;
import deco.combatevolved.entities.items.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Recipes will be used to determine whether or not the player has set a
 * valid combination of materials when crafting. If it is a correct combination,
 * the resulting resource object will be returned and placed in the player's
 * inventory.
 */
public class Recipes {
    protected HashMap<List<String>, Item> map = new HashMap<>();

    /**
     * A recipe object that stores the various craftable items.
     */
    public Recipes() {
        String moonRock = "moon-rock";
        String zero = "0";
        String mentos = "mentos";
        String wood = "wood";
        String concrete = "concrete";
        String copper = "copper";
        String nickel = "nickel";
        String iron = "iron";
        String silver = "siver";
        String plutonium = "plutonium";
        String roots = "roots";
        String unobtanium = "unobtanium";
        String potato = "potato";
        String bronze = "bronze";
        String cola = "cola";
        String redStone = "red-stone";
        String coconut = "coconut";
        String rope = "rope";

        List<String>  grenadeRecipe = new ArrayList<>();
        grenadeRecipe.add(zero);
        grenadeRecipe.add(mentos);
        grenadeRecipe.add(zero);
        grenadeRecipe.add(zero);
        grenadeRecipe.add(mentos);
        grenadeRecipe.add(zero);
        grenadeRecipe.add(bronze);
        grenadeRecipe.add(cola);
        grenadeRecipe.add(bronze);

        List<String> spudGunRecipe = new ArrayList<>();

        spudGunRecipe.add(zero);
        spudGunRecipe.add(wood);
        spudGunRecipe.add(wood);
        spudGunRecipe.add(roots);
        spudGunRecipe.add(wood);
        spudGunRecipe.add(zero);
        spudGunRecipe.add(zero);
        spudGunRecipe.add(potato);
        spudGunRecipe.add(zero);

        List<String> gammaStoneRecipe = new ArrayList<>();
        gammaStoneRecipe.add(zero);
        gammaStoneRecipe.add(zero);
        gammaStoneRecipe.add(zero);
        gammaStoneRecipe.add(moonRock);
        gammaStoneRecipe.add(moonRock);
        gammaStoneRecipe.add(moonRock);
        gammaStoneRecipe.add(plutonium);
        gammaStoneRecipe.add(unobtanium);
        gammaStoneRecipe.add(plutonium);

        List<String> reinforcedConcreteRecipe = new ArrayList<>();
        reinforcedConcreteRecipe.add(zero);
        reinforcedConcreteRecipe.add(concrete);
        reinforcedConcreteRecipe.add(zero);
        reinforcedConcreteRecipe.add(silver);
        reinforcedConcreteRecipe.add(concrete);
        reinforcedConcreteRecipe.add(silver);
        reinforcedConcreteRecipe.add(zero);
        reinforcedConcreteRecipe.add(concrete);
        reinforcedConcreteRecipe.add(zero);

        List<String> steelRecipe = new ArrayList<>();
        steelRecipe.add(concrete);
        steelRecipe.add(nickel);
        steelRecipe.add(copper);
        steelRecipe.add(nickel);
        steelRecipe.add(iron);
        steelRecipe.add(nickel);
        steelRecipe.add(copper);
        steelRecipe.add(nickel);
        steelRecipe.add(copper);

        List<String> plasmaRecipe = new ArrayList<>();
        plasmaRecipe.add(redStone);
        plasmaRecipe.add(zero);
        plasmaRecipe.add(redStone);
        plasmaRecipe.add(zero);
        plasmaRecipe.add(silver);
        plasmaRecipe.add(zero);
        plasmaRecipe.add(silver);
        plasmaRecipe.add(redStone);
        plasmaRecipe.add(silver);

        List<String> spikedMaceRecipe = new ArrayList<>();
        spikedMaceRecipe.add(iron);
        spikedMaceRecipe.add(coconut);
        spikedMaceRecipe.add(iron);
        spikedMaceRecipe.add(zero);
        spikedMaceRecipe.add(rope);
        spikedMaceRecipe.add(zero);
        spikedMaceRecipe.add(zero);
        spikedMaceRecipe.add(rope);
        spikedMaceRecipe.add(zero);

        List<String> simpleTowerRecipe = new ArrayList<>();
        spikedMaceRecipe.add(zero);
        spikedMaceRecipe.add(iron);
        spikedMaceRecipe.add(zero);
        spikedMaceRecipe.add(zero);
        spikedMaceRecipe.add(iron);
        spikedMaceRecipe.add(zero);
        spikedMaceRecipe.add(concrete);
        spikedMaceRecipe.add(concrete);
        spikedMaceRecipe.add(concrete);

        Item grenade = new Item("Grenade", 4);
        Item gammaStone = new Item("Gamma Stone", 5);
        Item reinforcedConcrete = new Item ("Reinforced Concrete", 4);
        Item steel = new Item("Steel", 3);
        Item plasma = new Item("Plasma", 4);
        Item spikedMace = new Item("Spiked Mace", 3);
        Item simpleTower = new Item("Simple Tower", 1);
        Equipment spudGun = new Equipment("gun_low", 1, "gun","401" , "gun_low");


        this.map.put(grenadeRecipe, grenade);
        this.map.put(spudGunRecipe, spudGun);
        this.map.put(gammaStoneRecipe, gammaStone);
        this.map.put(reinforcedConcreteRecipe, reinforcedConcrete);
        this.map.put(steelRecipe, steel);
        this.map.put(plasmaRecipe, plasma);
        this.map.put(spikedMaceRecipe, spikedMace);
        this.map.put(simpleTowerRecipe, simpleTower);

    }

    /**
     * Checks whether current combination is a valid material.
     * @param entry - The current combination of resources.
     * @return - true if the combination exists, false if otherwise.
     */
    public boolean isEntry(List<String> entry) {
        return this.map.containsKey(entry);
    }

    /**
     * Returns the item matching the crafted combination if it is valid.
     * @param entry - The current combination of resources.
     * @return - the crafted item if it exists, NULL otherwise
     */
    public Item getCraftedItem(List<String> entry) {
        Item newItem;
        newItem = this.map.get(entry);

        return newItem;

    }

    /**
     * Returns tha hashmap containing all possible combinations of resources.
     * @return - tha hashmap containing all possible combinations of resources.
     */
    public Map<List<String>, Item> recipeMap() {
        return this.map;
    }
}