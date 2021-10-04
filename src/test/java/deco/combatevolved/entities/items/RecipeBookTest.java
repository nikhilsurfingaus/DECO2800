package deco.combatevolved.entities.items;

import deco.combatevolved.entities.items.resources.Items;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

public class RecipeBookTest {

    private Items items = new Items();
    private RecipeBook recipes = new RecipeBook(items);

    /**
     * Tests if recipes are created correctly
     */
    @Test
    public void createRecipesTest() {

        Items items = new Items();
        RecipeBook recipeBook = new RecipeBook(items);

        List<String> grenadeRecipie = new ArrayList<>();

        grenadeRecipie.add("001");
        grenadeRecipie.add("001");
        grenadeRecipie.add("005");

        grenadeRecipie.add("014");
        grenadeRecipie.add("013");
        grenadeRecipie.add("014");

        grenadeRecipie.add("001");
        grenadeRecipie.add("001");
        grenadeRecipie.add("005");

        Assert.assertEquals("Grenade", recipeBook.getCraftedItem(grenadeRecipie).getName());
    }

    @Test
    public void testGetCraftingRecipes() {
        Item emptyItem = items.getItems().get("empty");
        Item mentosItem = items.getItems().get("mentos");
        Item bronzeItem = items.getItems().get("bronze");
        Item colaItem = items.getItems().get("cola");

        List<Item> grenadeItems = new ArrayList<>();

        grenadeItems.add(emptyItem);
        grenadeItems.add(mentosItem);
        grenadeItems.add(emptyItem);
        grenadeItems.add(emptyItem);
        grenadeItems.add(mentosItem);
        grenadeItems.add(emptyItem);
        grenadeItems.add(bronzeItem);
        grenadeItems.add(colaItem);
        grenadeItems.add(bronzeItem);

        assertEquals(recipes.getCraftingRecipes().get(items.getItems().get("grenade")), grenadeItems);
    }
}
