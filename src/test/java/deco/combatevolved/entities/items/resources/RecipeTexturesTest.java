package deco.combatevolved.entities.items.resources;

import com.badlogic.gdx.graphics.Texture;
import deco.combatevolved.entities.items.Item;
import deco.combatevolved.managers.GameManager;
import deco.combatevolved.managers.TextureManager;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class RecipeTexturesTest {

    private RecipeTextures recipeTextures = new RecipeTextures();
    private Map<Texture, Item> itemImages = new HashMap<>();
    private List<Texture> itemTextures = new ArrayList<>();
    private List<Texture> weaponTextures = new ArrayList<>();
    private List<Texture> towerTextures = new ArrayList<>();
    private Items items = new Items();


    @Before
    public void setup() {

        Texture gammaStoneCrafting = GameManager.get().getManager(TextureManager.class).getTexture("gammaStoneCrafting");
        Texture gammaStoneDesc = GameManager.get().getManager(TextureManager.class).getTexture("gammaStoneDesc");
        Texture gammaStone = GameManager.get().getManager(TextureManager.class).getTexture("gammastone");

        Texture grenadeCrafting = GameManager.get().getManager(TextureManager.class).getTexture("grenadeCrafting");
        Texture grenadeDesc = GameManager.get().getManager(TextureManager.class).getTexture("grenadeDesc");
        Texture midGrenade = GameManager.get().getManager(TextureManager.class).getTexture("grenade_mid");

        itemTextures.add(gammaStoneCrafting);
        itemTextures.add(gammaStoneDesc);
        itemTextures.add(gammaStone);

        weaponTextures.add(grenadeCrafting);
        weaponTextures.add(grenadeDesc);
        weaponTextures.add(midGrenade);

        itemImages.put(gammaStoneCrafting, items.getItems().get("gammastone"));
    }

    @Test
    public void testGetItemImagesValid() {
        assertEquals(itemTextures, recipeTextures.getItemImages().get(3));
    }


    @Test
    public void testGetWeaponItemImagesValid() {
        assertEquals(weaponTextures, recipeTextures.getWeaponImages().get(1));
    }

    @Test
    public void testGetTowerImagesValid() {

    }

    @Test
    public void testGetCraftingItem() {
        assertTrue(recipeTextures.getCraftingItems().containsKey(itemTextures.get(0)));
    }
}
