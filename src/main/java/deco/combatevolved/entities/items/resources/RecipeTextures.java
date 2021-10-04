package deco.combatevolved.entities.items.resources;

import com.badlogic.gdx.graphics.Texture;
import deco.combatevolved.entities.items.Item;
import deco.combatevolved.managers.GameManager;
import deco.combatevolved.managers.TextureManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipeTextures {

    // a list of all items in the game
    private Items items = new Items();

    // map of item crafting configurations, descriptions and images
    private HashMap<Integer, List<Texture>> itemImages = new HashMap<>();

    // map of weapon crafting configurations, descriptions and images
    private HashMap<Integer, List<Texture>> weaponImages = new HashMap<>();

    // map of tower crafting configurations, descriptions and images
    private HashMap<Integer, List<Texture>> towerImages = new HashMap<>();

    // map of textures to their corresponding items
    private HashMap<Texture, Item> craftingItems = new HashMap<>();

    public RecipeTextures() {

        // Textures of all the Crafting Configurations
        Texture gammaStoneCrafting = GameManager.get().getManager(TextureManager.class).getTexture("gammaStoneCrafting");
        Texture steelCrafting = GameManager.get().getManager(TextureManager.class).getTexture("steelCrafting");
        Texture midGrenadeCrafting = GameManager.get().getManager(TextureManager.class).getTexture("grenadeCrafting");
        Texture highGrenadeCrafting = GameManager.get().getManager(TextureManager.class).getTexture("grenadeCrafting");
        Texture pendingCrafting = GameManager.get().getManager(TextureManager.class).getTexture("pendingCrafting");
        Texture plasmaCrafting = GameManager.get().getManager(TextureManager.class).getTexture("plasmaCrafting");
        Texture reinforcedConcreteCrafting = GameManager.get().getManager(TextureManager.class).getTexture("reinforcedConcreteCrafting");
        Texture spikedMaceCrafting = GameManager.get().getManager(TextureManager.class).getTexture("spikedMaceCrafting");
        Texture spudGunCrafting = GameManager.get().getManager(TextureManager.class).getTexture("spudGunCrafting");
        Texture simpleTowerCrafting = GameManager.get().getManager(TextureManager.class).getTexture("simpleTowerCrafting");

        // Textures for all descriptions
        Texture gammaStoneDesc = GameManager.get().getManager(TextureManager.class).getTexture("gammaStoneDesc");
        Texture steelDesc = GameManager.get().getManager(TextureManager.class).getTexture("steelDesc");
        Texture grenadeDesc = GameManager.get().getManager(TextureManager.class).getTexture("grenadeDesc");
        Texture pendingDesc = GameManager.get().getManager(TextureManager.class).getTexture("pendingDesc");
        Texture plasmaDesc = GameManager.get().getManager(TextureManager.class).getTexture("plasmaDesc");
        Texture reinforcedConcreteDesc = GameManager.get().getManager(TextureManager.class).getTexture("reinforcedConcreteDesc");
        Texture spikedMaceDesc = GameManager.get().getManager(TextureManager.class).getTexture("spikedMaceDesc");
        Texture spudGunDesc = GameManager.get().getManager(TextureManager.class).getTexture("spudGunDesc");
        Texture simpleTowerDesc = GameManager.get().getManager(TextureManager.class).getTexture("basicTowerDesc");
        Texture multiTowerDesc = GameManager.get().getManager(TextureManager.class).getTexture("multiTowerDesc");
        Texture sniperTowerDesc = GameManager.get().getManager(TextureManager.class).getTexture("sniperTowerDesc");
        Texture slowTowerDesc = GameManager.get().getManager(TextureManager.class).getTexture("slowTowerDesc");
        Texture splashTowerDesc = GameManager.get().getManager(TextureManager.class).getTexture("splashTowerDesc");
        Texture zapTowerDesc = GameManager.get().getManager(TextureManager.class).getTexture("zapTowerDesc");

        // Textures for all images of crafted items
        Texture gammaStone = GameManager.get().getManager(TextureManager.class).getTexture("gammastone");
        Texture steel = GameManager.get().getManager(TextureManager.class).getTexture("steel");
        Texture grenade = GameManager.get().getManager(TextureManager.class).getTexture("grenade");
        Texture pending = GameManager.get().getManager(TextureManager.class).getTexture("stone");
        Texture plasma = GameManager.get().getManager(TextureManager.class).getTexture("plasma");
        Texture reinforcedConcrete = GameManager.get().getManager(TextureManager.class).getTexture("reinforcedconcrete");
        Texture spikedMace = GameManager.get().getManager(TextureManager.class).getTexture("spikedmace");
        Texture spudGun = GameManager.get().getManager(TextureManager.class).getTexture("spudgun");
        Texture simpleTower = GameManager.get().getManager(TextureManager.class).getTexture("simpletower_D");
        Texture sniperTower = GameManager.get().getManager(TextureManager.class).getTexture("snipertower_D");
        Texture slowTower = GameManager.get().getManager(TextureManager.class).getTexture("slowtower_D");
        Texture splashTower = GameManager.get().getManager(TextureManager.class).getTexture("splashtower_D");
        Texture multiTower = GameManager.get().getManager(TextureManager.class).getTexture("multitower");
        Texture zapTower = GameManager.get().getManager(TextureManager.class).getTexture("zaptower_D");

        //weapon
        Texture midGun = GameManager.get().getManager(TextureManager.class).getTexture("gun_mid");
        Texture highGun = GameManager.get().getManager(TextureManager.class).getTexture("gun_high");
        Texture midGrenade = GameManager.get().getManager(TextureManager.class).getTexture("grenade_mid");
        Texture highGrenade = GameManager.get().getManager(TextureManager.class).getTexture("grenade_high");
        Texture lowHelmet = GameManager.get().getManager(TextureManager.class).getTexture("helmet_low");
        Texture midHelmet = GameManager.get().getManager(TextureManager.class).getTexture("helmet_mid");
        Texture highHelmet = GameManager.get().getManager(TextureManager.class).getTexture("helmet_high");
        Texture lowArmour = GameManager.get().getManager(TextureManager.class).getTexture("armour_low");
        Texture midArmour = GameManager.get().getManager(TextureManager.class).getTexture("armour_mid");
        Texture highArmour = GameManager.get().getManager(TextureManager.class).getTexture("armour_high");
        Texture lowShoes = GameManager.get().getManager(TextureManager.class).getTexture("shoes_low");
        Texture midShoes = GameManager.get().getManager(TextureManager.class).getTexture("shoes_mid");
        Texture highShoes = GameManager.get().getManager(TextureManager.class).getTexture("shoes_high");


        List<Texture> simpleTowerTextures = new ArrayList<>();
        simpleTowerTextures.add(simpleTowerCrafting);
        simpleTowerTextures.add(simpleTowerDesc);
        simpleTowerTextures.add(simpleTower);

        List<Texture> multiTowerTextures = new ArrayList<>();
        multiTowerTextures.add(pendingCrafting);
        multiTowerTextures.add(multiTowerDesc);
        multiTowerTextures.add(multiTower);

        List<Texture> sniperTowerTextures = new ArrayList<>();
        sniperTowerTextures.add(pendingCrafting);
        sniperTowerTextures.add(sniperTowerDesc);
        sniperTowerTextures.add(sniperTower);

        List<Texture> slowTowerTextures = new ArrayList<>();
        slowTowerTextures.add(pendingCrafting);
        slowTowerTextures.add(slowTowerDesc);
        slowTowerTextures.add(slowTower);

        List<Texture> splashTowerTextures = new ArrayList<>();
        splashTowerTextures.add(pendingCrafting);
        splashTowerTextures.add(splashTowerDesc);
        splashTowerTextures.add(splashTower);

        List<Texture> zapTowerTextures = new ArrayList<>();
        zapTowerTextures.add(pendingCrafting);
        zapTowerTextures.add(zapTowerDesc);
        zapTowerTextures.add(zapTower);

        List<Texture> spudGunTextures = new ArrayList<>();
        spudGunTextures.add(spudGunCrafting);
        spudGunTextures.add(spudGunDesc);
        spudGunTextures.add(spudGun);

        List<Texture> steelTextures = new ArrayList<>();
        steelTextures.add(steelCrafting);
        steelTextures.add(steelDesc);
        steelTextures.add(steel);

        List<Texture> reinforcedConcreteTextures = new ArrayList<>();
        reinforcedConcreteTextures.add(reinforcedConcreteCrafting);
        reinforcedConcreteTextures.add(reinforcedConcreteDesc);
        reinforcedConcreteTextures.add(reinforcedConcrete);

        List<Texture> spikedMaceTextures = new ArrayList<>();
        spikedMaceTextures.add(spikedMaceCrafting);
        spikedMaceTextures.add(spikedMaceDesc);
        spikedMaceTextures.add(spikedMace);

        List<Texture> plasmaTextures = new ArrayList<>();
        plasmaTextures.add(plasmaCrafting);
        plasmaTextures.add(plasmaDesc);
        plasmaTextures.add(plasma);

        List<Texture> midGunTextures = new ArrayList<>();
        midGunTextures.add(spudGunCrafting);
        midGunTextures.add(spudGunDesc);
        midGunTextures.add(midGun);

        List<Texture> highGunTextures = new ArrayList<>();
        midGunTextures.add(spudGunCrafting);
        midGunTextures.add(spudGunDesc);
        midGunTextures.add(highGun);

        List<Texture> lowHelmetTextures = new ArrayList<>();
        midGunTextures.add(spudGunCrafting);
        midGunTextures.add(spudGunDesc);
        midGunTextures.add(lowHelmet);

        List<Texture> midHelmetTextures = new ArrayList<>();
        midGunTextures.add(spudGunCrafting);
        midGunTextures.add(spudGunDesc);
        midGunTextures.add(midHelmet);

        List<Texture> highHelmetTextures = new ArrayList<>();
        midGunTextures.add(spudGunCrafting);
        midGunTextures.add(spudGunDesc);
        midGunTextures.add(highHelmet);

        List<Texture> lowArmourTextures = new ArrayList<>();
        midGunTextures.add(spudGunCrafting);
        midGunTextures.add(spudGunDesc);
        midGunTextures.add(lowArmour);

        List<Texture> midArmourTextures = new ArrayList<>();
        midGunTextures.add(spudGunCrafting);
        midGunTextures.add(spudGunDesc);
        midGunTextures.add(midArmour);

        List<Texture> highArmourTextures = new ArrayList<>();
        midGunTextures.add(spudGunCrafting);
        midGunTextures.add(spudGunDesc);
        midGunTextures.add(highArmour);

        List<Texture> lowShoesTextures = new ArrayList<>();
        midGunTextures.add(spudGunCrafting);
        midGunTextures.add(spudGunDesc);
        midGunTextures.add(lowShoes);

        List<Texture> midShoesTextures = new ArrayList<>();
        midGunTextures.add(spudGunCrafting);
        midGunTextures.add(spudGunDesc);
        midGunTextures.add(midShoes);

        List<Texture> highShoesTextures = new ArrayList<>();
        midGunTextures.add(spudGunCrafting);
        midGunTextures.add(spudGunDesc);
        midGunTextures.add(highShoes);

        List<Texture> midGrenadeTextures = new ArrayList<>();
        midGrenadeTextures.add(midGrenadeCrafting);
        midGrenadeTextures.add(grenadeDesc);
        midGrenadeTextures.add(midGrenade);

        List<Texture> highGrenadeTextures = new ArrayList<>();
        highGrenadeTextures.add(highGrenadeCrafting);
        highGrenadeTextures.add(grenadeDesc);
        highGrenadeTextures.add(highGrenade);

        List<Texture> gammaStoneTextures = new ArrayList<>();
        gammaStoneTextures.add(gammaStoneCrafting);
        gammaStoneTextures.add(gammaStoneDesc);
        gammaStoneTextures.add(gammaStone);

        List<Texture> pendingTextures = new ArrayList<>();
        pendingTextures.add(pendingCrafting);
        pendingTextures.add(pendingDesc);
        pendingTextures.add(pending);

        itemImages.put(0, steelTextures);
        itemImages.put(1, reinforcedConcreteTextures);
        itemImages.put(2, plasmaTextures);
        itemImages.put(3, gammaStoneTextures);


        for (int i = itemImages.size(); i < 14; i++) {
            itemImages.put(i, pendingTextures);
        }

        weaponImages.put(0, spikedMaceTextures);
        weaponImages.put(1, midGrenadeTextures);
//        weaponImages.put(2, highGrenadeTextures);
        //weaponImages.put(3, spudGunTextures);
        weaponImages.put(2, midGunTextures);
//        weaponImages.put(2, highGunTextures);
//        weaponImages.put(5, lowHelmetTextures);
//        weaponImages.put(6, midHelmetTextures);
//        weaponImages.put(7, highHelmetTextures);
//        weaponImages.put(8, lowArmourTextures);
//        weaponImages.put(9, midArmourTextures);
//        weaponImages.put(10, highArmourTextures);
//        weaponImages.put(11, lowShoesTextures);
//        weaponImages.put(12, midShoesTextures);

        for (int i = weaponImages.size(); i < 14; i++) {
            weaponImages.put(i, pendingTextures);
        }

        towerImages.put(0, simpleTowerTextures);
        towerImages.put(1, splashTowerTextures);
        towerImages.put(2, multiTowerTextures);
        towerImages.put(3, slowTowerTextures);
        towerImages.put(4, sniperTowerTextures);
        towerImages.put(5, zapTowerTextures);

        for (int i = towerImages.size(); i < 14; i++) {
            towerImages.put(i, pendingTextures);
        }

        craftingItems.put(gammaStoneCrafting, items.getItems().get("gammastone"));
        craftingItems.put(steelCrafting, items.getItems().get("steel"));
        craftingItems.put(midGrenadeCrafting, items.getItems().get("midGrenade"));
        //craftingItems.put(highGrenadeCrafting, items.getItems().get("highGrenade"));
        craftingItems.put(spikedMaceCrafting, items.getItems().get("spikedmace"));
        //craftingItems.put(spudGunCrafting, items.getItems().get("spudgun"));
        craftingItems.put(reinforcedConcreteCrafting, items.getItems().get("reinforcedconcrete"));
        craftingItems.put(plasmaCrafting, items.getItems().get("plasma"));
        craftingItems.put(spudGunCrafting, items.getItems().get("midGun"));
        //craftingItems.put(spudGunCrafting, items.getItems().get("highGun"));

    }

    public Map<Integer, List<Texture>> getItemImages() {
        return this.itemImages;
    }

    public Map<Integer, List<Texture>> getWeaponImages() {
        return this.weaponImages;
    }

    public Map<Integer, List<Texture>> getTowerImages() {
        return this.towerImages;
    }

    public Map<Texture, Item> getCraftingItems() {
        return this.craftingItems;
    }
}
