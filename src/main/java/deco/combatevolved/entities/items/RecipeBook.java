package deco.combatevolved.entities.items;

import deco.combatevolved.entities.items.resources.Items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages the recipes in the game
 */
public class RecipeBook {

    protected HashMap<List<String>, Item> map = new HashMap<>();
    protected HashMap<Item, List<Item>> craftingRecipes = new HashMap<>();
    private Items items;

    /**
     * Creates recipe book
     */
    public RecipeBook(Items items) {

        this.items = items;
        createRecipes();
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
     * @return - true if the combination exists, false if otherwise.
     */
    public Item getCraftedItem(List<String> entry) {

        Item newItem;
        newItem = this.map.get(entry);

        return newItem;
    }

    public Map<Item, List<Item>> getCraftingRecipes() {
        return craftingRecipes;
    }

    /**
     * Returns tha hashmap containing all possible combinations of resources.
     * @return - tha hashmap containing all possible combinations of resources.
     */
    public Map<List<String>, Item> recipeMap() {
        return this.map;
    }

    /**
     * Manually creates all the recipes in the game.
     */
    private void createRecipes() {

        List<String> grenadeIngredients = new ArrayList<>();
        List<String> midGrenadeIngredients = new ArrayList<>();
        List<String> spudGunIngredients = new ArrayList<>();
        List<String> gammaStoneIngredients = new ArrayList<>();
        List<String> reinforcedConcreteIngredients = new ArrayList<>();
        List<String> steelIngredients = new ArrayList<>();
        List<String> plasmaIngredients = new ArrayList<>();
        List<String> spikedMaceIngredients = new ArrayList<>();
        List<String> simpleTowerIngredients = new ArrayList<>();

        List<Item> grenadeItems = new ArrayList<>();
        List<Item> midGrenadeItems = new ArrayList<>();
        List<Item> spudGunItems = new ArrayList<>();
        List<Item> gammaStoneItems = new ArrayList<>();
        List<Item> reinforcedConcreteItems = new ArrayList<>();
        List<Item> steelItems = new ArrayList<>();
        List<Item> plasmaItems = new ArrayList<>();
        List<Item> spikedMaceItems = new ArrayList<>();
        List<Item> simpleTowerItems = new ArrayList<>();

        String empty = items.getItems().get("empty").getId();
        String mentos = items.getItems().get("mentos").getId();
        String bronze = items.getItems().get("bronze").getId();
        String cola = items.getItems().get("cola").getId();
        String roots = items.getItems().get("roots").getId();
        String wood = items.getItems().get("wood").getId();
        String potato = items.getItems().get("potato").getId();
        String moonrocks = items.getItems().get("moonrocks").getId();
        String plutonium = items.getItems().get("plutonium").getId();
        String unobtanium = items.getItems().get("unobtanium").getId();
        String concrete = items.getItems().get("concrete").getId();
        String silver = items.getItems().get("silver").getId();
        String copper = items.getItems().get("copper").getId();
        String nickel = items.getItems().get("nickel").getId();
        String iron = items.getItems().get("iron").getId();
        String redstone = items.getItems().get("redstone").getId();
        String coconut = items.getItems().get("coconut").getId();
        String rope = items.getItems().get("rope").getId();

        Item emptyItem = items.getItems().get("empty");
        Item mentosItem = items.getItems().get("mentos");
        Item bronzeItem = items.getItems().get("bronze");
        Item colaItem = items.getItems().get("cola");
        Item rootsItem = items.getItems().get("roots");
        Item woodItem = items.getItems().get("wood");
        Item potatoItem = items.getItems().get("potato");
        Item moonrocksItem = items.getItems().get("moonrocks");
        Item plutoniumItem = items.getItems().get("plutonium");
        Item unobtaniumItem = items.getItems().get("unobtanium");
        Item concreteItem = items.getItems().get("concrete");
        Item silverItem = items.getItems().get("silver");
        Item copperItem = items.getItems().get("copper");
        Item nickelItem = items.getItems().get("nickel");
        Item ironItem = items.getItems().get("iron");
        Item redstoneItem = items.getItems().get("redstone");
        Item coconutItem = items.getItems().get("coconut");
        Item ropeItem = items.getItems().get("rope");

        // creating mid-grenade recipe
        midGrenadeIngredients.add(wood);
        midGrenadeIngredients.add(empty);
        midGrenadeIngredients.add(empty);
        midGrenadeIngredients.add(empty);
        midGrenadeIngredients.add(empty);
        midGrenadeIngredients.add(empty);
        midGrenadeIngredients.add(empty);
        midGrenadeIngredients.add(empty);
        midGrenadeIngredients.add(empty);

        midGrenadeItems.add(woodItem);
        midGrenadeItems.add(emptyItem);
        midGrenadeItems.add(emptyItem);
        midGrenadeItems.add(emptyItem);
        midGrenadeItems.add(emptyItem);
        midGrenadeItems.add(emptyItem);
        midGrenadeItems.add(emptyItem);
        midGrenadeItems.add(emptyItem);
        midGrenadeItems.add(emptyItem);

        map.put(midGrenadeIngredients, items.getItems().get("midGrenade"));
        craftingRecipes.put(items.getItems().get("midGrenade"), midGrenadeItems);
        map.put(midGrenadeIngredients, items.getItems().get("midGun"));
        craftingRecipes.put(items.getItems().get("midGun"), midGrenadeItems);
        map.put(midGrenadeIngredients, items.getItems().get("highGun"));
        craftingRecipes.put(items.getItems().get("highGun"), midGrenadeItems);
        map.put(midGrenadeIngredients, items.getItems().get("highGrenade"));
        craftingRecipes.put(items.getItems().get("highGrenade"), midGrenadeItems);
        //helmet
        map.put(midGrenadeIngredients, items.getItems().get("lowHelmet"));
        craftingRecipes.put(items.getItems().get("lowHelmet"), midGrenadeItems);
        map.put(midGrenadeIngredients, items.getItems().get("midHelmet"));
        craftingRecipes.put(items.getItems().get("midHelmet"), midGrenadeItems);
        map.put(midGrenadeIngredients, items.getItems().get("highHelmet"));
        craftingRecipes.put(items.getItems().get("highHelmet"), midGrenadeItems);
        //armour
        map.put(midGrenadeIngredients, items.getItems().get("lowArmour"));
        craftingRecipes.put(items.getItems().get("lowArmour"), midGrenadeItems);
        map.put(midGrenadeIngredients, items.getItems().get("midArmour"));
        craftingRecipes.put(items.getItems().get("midArmour"), midGrenadeItems);
        map.put(midGrenadeIngredients, items.getItems().get("highArmour"));
        craftingRecipes.put(items.getItems().get("highArmour"), midGrenadeItems);
        //shoes
        map.put(midGrenadeIngredients, items.getItems().get("lowShoes"));
        craftingRecipes.put(items.getItems().get("lowShoes"), midGrenadeItems);
        map.put(midGrenadeIngredients, items.getItems().get("midShoes"));
        craftingRecipes.put(items.getItems().get("midShoes"), midGrenadeItems);
        map.put(midGrenadeIngredients, items.getItems().get("highShoes"));
        craftingRecipes.put(items.getItems().get("highShoes"), midGrenadeItems);

        // creating grenade recipe
        grenadeIngredients.add(empty);
        grenadeIngredients.add(empty);
        grenadeIngredients.add(bronze);
        grenadeIngredients.add(mentos);
        grenadeIngredients.add(cola);
        grenadeIngredients.add(mentos);
        grenadeIngredients.add(empty);
        grenadeIngredients.add(empty);
        grenadeIngredients.add(bronze);

        grenadeItems.add(emptyItem);
        grenadeItems.add(mentosItem);
        grenadeItems.add(emptyItem);
        grenadeItems.add(emptyItem);
        grenadeItems.add(mentosItem);
        grenadeItems.add(emptyItem);
        grenadeItems.add(bronzeItem);
        grenadeItems.add(colaItem);
        grenadeItems.add(bronzeItem);

        map.put(grenadeIngredients, items.getItems().get("grenade"));
        craftingRecipes.put(items.getItems().get("grenade"), grenadeItems);

        // creating spud gun recipe
        spudGunIngredients.add(empty);
        spudGunIngredients.add(wood);
        spudGunIngredients.add(wood);
        spudGunIngredients.add(roots);
        spudGunIngredients.add(wood);
        spudGunIngredients.add(empty);
        spudGunIngredients.add(empty);
        spudGunIngredients.add(potato);
        spudGunIngredients.add(empty);

        spudGunItems.add(emptyItem);
        spudGunItems.add(rootsItem);
        spudGunItems.add(emptyItem);
        spudGunItems.add(woodItem);
        spudGunItems.add(woodItem);
        spudGunItems.add(potatoItem);
        spudGunItems.add(woodItem);
        spudGunItems.add(emptyItem);
        spudGunItems.add(emptyItem);

        map.put(spudGunIngredients, items.getItems().get("spudgun"));
        craftingRecipes.put(items.getItems().get("spudgun"), spudGunItems);

        // creating gamma stone recipe
        gammaStoneIngredients.add(empty);
        gammaStoneIngredients.add(moonrocks);
        gammaStoneIngredients.add(plutonium);
        gammaStoneIngredients.add(empty);
        gammaStoneIngredients.add(moonrocks);
        gammaStoneIngredients.add(unobtanium);
        gammaStoneIngredients.add(empty);
        gammaStoneIngredients.add(moonrocks);
        gammaStoneIngredients.add(plutonium);

        gammaStoneItems.add(emptyItem);
        gammaStoneItems.add(emptyItem);
        gammaStoneItems.add(emptyItem);
        gammaStoneItems.add(moonrocksItem);
        gammaStoneItems.add(moonrocksItem);
        gammaStoneItems.add(moonrocksItem);
        gammaStoneItems.add(plutoniumItem);
        gammaStoneItems.add(unobtaniumItem);
        gammaStoneItems.add(plutoniumItem);

        map.put(gammaStoneIngredients, items.getItems().get("gammastone"));
        craftingRecipes.put(items.getItems().get("gammastone"), gammaStoneItems);

        // creating reinforced concrete recipe
        reinforcedConcreteIngredients.add(empty);
        reinforcedConcreteIngredients.add(silver);
        reinforcedConcreteIngredients.add(empty);
        reinforcedConcreteIngredients.add(concrete);
        reinforcedConcreteIngredients.add(concrete);
        reinforcedConcreteIngredients.add(concrete);
        reinforcedConcreteIngredients.add(empty);
        reinforcedConcreteIngredients.add(silver);
        reinforcedConcreteIngredients.add(empty);

        reinforcedConcreteItems.add(emptyItem);
        reinforcedConcreteItems.add(concreteItem);
        reinforcedConcreteItems.add(emptyItem);
        reinforcedConcreteItems.add(silverItem);
        reinforcedConcreteItems.add(concreteItem);
        reinforcedConcreteItems.add(silverItem);
        reinforcedConcreteItems.add(emptyItem);
        reinforcedConcreteItems.add(concreteItem);
        reinforcedConcreteItems.add(emptyItem);

        map.put(reinforcedConcreteIngredients, items.getItems().get("reinforcedconcrete"));
        craftingRecipes.put(items.getItems().get("reinforcedconcrete"), reinforcedConcreteItems);

        // creating steel recipe
        steelIngredients.add(copper);
        steelIngredients.add(nickel);
        steelIngredients.add(copper);
        steelIngredients.add(nickel);
        steelIngredients.add(iron);
        steelIngredients.add(nickel);
        steelIngredients.add(copper);
        steelIngredients.add(nickel);
        steelIngredients.add(copper);

        steelItems.add(copperItem);
        steelItems.add(nickelItem);
        steelItems.add(copperItem);
        steelItems.add(nickelItem);
        steelItems.add(ironItem);
        steelItems.add(nickelItem);
        steelItems.add(copperItem);
        steelItems.add(nickelItem);
        steelItems.add(copperItem);


        map.put(steelIngredients, items.getItems().get("steel"));
        craftingRecipes.put(items.getItems().get("steel"), steelItems);

        // creating plasma recipe
        plasmaIngredients.add(redstone);
        plasmaIngredients.add(empty);
        plasmaIngredients.add(silver);
        plasmaIngredients.add(empty);
        plasmaIngredients.add(silver);
        plasmaIngredients.add(redstone);
        plasmaIngredients.add(redstone);
        plasmaIngredients.add(empty);
        plasmaIngredients.add(silver);

        plasmaItems.add(redstoneItem);
        plasmaItems.add(emptyItem);
        plasmaItems.add(redstoneItem);
        plasmaItems.add(emptyItem);
        plasmaItems.add(silverItem);
        plasmaItems.add(emptyItem);
        plasmaItems.add(silverItem);
        plasmaItems.add(redstoneItem);
        plasmaItems.add(silverItem);

        map.put(plasmaIngredients, items.getItems().get("plasma"));
        craftingRecipes.put(items.getItems().get("plasma"), plasmaItems);

        // creating spiked mace recipe
        spikedMaceIngredients.add(iron);
        spikedMaceIngredients.add(empty);
        spikedMaceIngredients.add(empty);
        spikedMaceIngredients.add(coconut);
        spikedMaceIngredients.add(rope);
        spikedMaceIngredients.add(rope);
        spikedMaceIngredients.add(iron);
        spikedMaceIngredients.add(empty);
        spikedMaceIngredients.add(empty);

        spikedMaceItems.add(ironItem);
        spikedMaceItems.add(coconutItem);
        spikedMaceItems.add(ironItem);
        spikedMaceItems.add(emptyItem);
        spikedMaceItems.add(ropeItem);
        spikedMaceItems.add(emptyItem);
        spikedMaceItems.add(emptyItem);
        spikedMaceItems.add(ropeItem);
        spikedMaceItems.add(emptyItem);

        map.put(spikedMaceIngredients, items.getItems().get("spikedmace"));
        craftingRecipes.put(items.getItems().get("spikedmace"), spikedMaceItems);

        // creating spiked mace recipe
        simpleTowerIngredients.add(iron);
        simpleTowerIngredients.add(empty);
        simpleTowerIngredients.add(empty);
        simpleTowerIngredients.add(empty);
        simpleTowerIngredients.add(empty);
        simpleTowerIngredients.add(empty);
        simpleTowerIngredients.add(empty);
        simpleTowerIngredients.add(empty);
        simpleTowerIngredients.add(empty);

        simpleTowerItems.add(ironItem);
        simpleTowerItems.add(emptyItem);
        simpleTowerItems.add(emptyItem);
        simpleTowerItems.add(emptyItem);
        simpleTowerItems.add(emptyItem);
        simpleTowerItems.add(emptyItem);
        simpleTowerItems.add(emptyItem);
        simpleTowerItems.add(emptyItem);
        simpleTowerItems.add(emptyItem);

        map.put(simpleTowerIngredients, items.getItems().get("simpleTower"));
        craftingRecipes.put(items.getItems().get("simpleTower"), simpleTowerItems);
    }
}
