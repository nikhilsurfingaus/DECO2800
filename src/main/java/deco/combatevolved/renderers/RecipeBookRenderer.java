package deco.combatevolved.renderers;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import deco.combatevolved.entities.dynamicentities.PlayerPeon;
import deco.combatevolved.entities.items.Inventory;
import deco.combatevolved.entities.items.Item;
import deco.combatevolved.entities.items.RecipeBook;
import deco.combatevolved.entities.items.resources.Items;
import deco.combatevolved.entities.items.resources.RecipeTextures;
import deco.combatevolved.managers.GameManager;
import deco.combatevolved.managers.SoundManager;
import deco.combatevolved.managers.TextureManager;
import deco.combatevolved.observers.KeyDownObserver;
import deco.combatevolved.observers.KeyUpObserver;
import deco.combatevolved.observers.TouchDownObserver;
import com.badlogic.gdx.math.Rectangle;
import deco.combatevolved.util.WorldUtil;
import deco.combatevolved.renderers.inventoryrenderer.InventoryRenderer;

import java.util.*;


/**
 * A class that handles the rendering of a Recipe Book
 */
public class RecipeBookRenderer extends InventoryRenderer implements Renderer, TouchDownObserver, KeyDownObserver, KeyUpObserver {

    // Display the recipe book.
    private boolean displayBook = false;

    // Determines which buttons were pressed
    public int backButtonPressed = 0;
    public int topButtonPressed = 1;
    public int bottomButtonPressed = 0;

    //Timer for buttons to change back.
    private  long buttonTime = 0;
    private long startingTime = 0;
    private long currentTime = 0;


    // Items that have already been crafted
    private List<String> craftedItems = new ArrayList<>();

    // Class that handles textures to display
    private RecipeTextures recipeTextures = new RecipeTextures();

    // Texture of all the buttons used in recipe book.
    private Texture recipeBackNormal = GameManager.get().getManager(TextureManager.class).getTexture("recipeBackButtonNormal");
    private Texture recipeBackClicked = GameManager.get().getManager(TextureManager.class).getTexture("recipeBackButtonClicked");
    private Texture equipmentsBtnNormal = GameManager.get().getManager(TextureManager.class).getTexture("equipmentsButtonNormal");
    private Texture equipmentsBtnClicked = GameManager.get().getManager(TextureManager.class).getTexture("equipmentsButtonClicked");
    private Texture itemsBtnNormal = GameManager.get().getManager(TextureManager.class).getTexture("itemsButtonNormal");
    private Texture itemsBtnClicked = GameManager.get().getManager(TextureManager.class).getTexture("itemsButtonClicked");
    private Texture towersBtnNormal = GameManager.get().getManager(TextureManager.class).getTexture("towersButtonNormal");
    private Texture towersBtnClicked = GameManager.get().getManager(TextureManager.class).getTexture("towersButtonClicked");
    private Texture homePageNormal = GameManager.get().getManager(TextureManager.class).getTexture("homePageButtonNormal");
    private Texture homePageClicked = GameManager.get().getManager(TextureManager.class).getTexture("homePageButtonClicked");
    private Texture leftPageNormal = GameManager.get().getManager(TextureManager.class).getTexture("leftPageButtonNormal");
    private Texture leftPageClicked = GameManager.get().getManager(TextureManager.class).getTexture("leftPageButtonClicked");
    private Texture rightPageNormal = GameManager.get().getManager(TextureManager.class).getTexture("rightPageButtonNormal");
    private Texture rightPageClicked = GameManager.get().getManager(TextureManager.class).getTexture("rightPageButtonClicked");
    private Texture craftingImage = GameManager.get().getManager(TextureManager.class).getTexture("blankImages");
    private Texture descImage = craftingImage;
    private Texture itemImage = craftingImage;
    private Texture craftingBtn= GameManager.get().getManager(TextureManager.class).getTexture("craftingDisabled");
    private Texture currentItem = null;

    // Temporary background of recipe book.
    private Texture recipeBookSample = GameManager.get().getManager(TextureManager.class).getTexture("recipeBookSample");

    // Current list viewed in recipe book.
    private Texture currentPageDisplay = GameManager.get().getManager(TextureManager.class).getTexture("equipmentsPageOne");

    // Hashtable for all item pages.
    private Map<Integer, String> allItemPages = new HashMap<>();
    // Hashtable for all weapon pages.
    private Map<Integer, String> allEquipmentsPages = new HashMap<>();
    // Hashtable for all tower pages.
    private Map<Integer, String> allTowerPages = new HashMap<>();

    // map of item crafting configurations, descriptions and images
    private Map<Integer, List<Texture>> itemImages = recipeTextures.getItemImages();

    // map of weapon crafting configurations, descriptions and images
    private Map<Integer, List<Texture>> weaponImages = recipeTextures.getWeaponImages();

    // map of tower crafting configurations, descriptions and images
    private Map<Integer, List<Texture>> towerImages = recipeTextures.getTowerImages();

    // map of textures to their corresponding item
    private Map<Texture, Item> craftableItems = recipeTextures.getCraftingItems();

    // Variable to identify current tab.
    private String currentTab = "Equipments";

    // Variables to identify individual tabs.
    private String itemsTab = "Item";
    private String equipmentsTab = "Equipments";
    private String towersTab = "Towers";

    // boundaries for buttons
    private Rectangle backBtnBounds;
    private Rectangle itemsBtnBounds;
    private Rectangle towersBtnBounds;
    private Rectangle equipmentsBtnBounds;
    private Rectangle homeBtnBounds;
    private Rectangle leftBtnBounds;
    private Rectangle rightBtnBounds;
    private Rectangle objectBtnBounds;
    private Rectangle craftingBtnBounds;

    // boundaries of the recipe book
    private Rectangle recipeBounds;

    // the camera
    private OrthographicCamera camera;
    // the current page of the recipe book
    private int currentPage = 0;

    @Override
    /**
     * Handle rendering of Recipe Book
     *
     * @param batch Batch to render onto
     * @param camera The camera
     */
    public void render(SpriteBatch batch, OrthographicCamera camera) {
        setCamera(camera);
        batch.begin();
        renderRecipe(batch);
        batch.end();

    }

    /**
     * Renders the Recipe Book
     * @param batch The batch to render onto
     */
    private void renderRecipe(SpriteBatch batch) {

        if (displayBook) {

            currentTime = System.currentTimeMillis();
            buttonTime = currentTime - startingTime;

            batch.draw(recipeBookSample, this.camera.position.x - 922.5f/2,
                    this.camera.position.y - 562.5f/2, 922.5f, 562.5f);

            batch.draw(recipeBackNormal,this.camera.position.x - 838.35f/2 ,
                    this.camera.position.y + 127.125f/2, 135f,175.5f);


            batch.draw(craftingBtn, this.camera.position.x + 184.5f,
                    this.camera.position.y - 28.8f, 81f, 36f);

            batch.draw(currentPageDisplay,this.camera.position.x - 838.35f/2 ,
                    this.camera.position.y - 406.125f/2, 405f,252f);

            batch.draw(leftPageNormal,this.camera.position.x - 838.35f/2 ,
                    this.camera.position.y - 478.125f/2, 135f,36f);

            batch.draw(homePageNormal,this.camera.position.x - 568.125f/2 ,
                    this.camera.position.y - 478.125f/2, 135f,36f);

            batch.draw(rightPageNormal,this.camera.position.x - 298.125f/2 ,
                    this.camera.position.y - 478.125f/2, 135f,36f);

            batch.draw(craftingImage, this.camera.position.x + 135f,
                    this.camera.position.y + 18f, 180, 225);

            batch.draw(descImage, this.camera.position.x + 52.2f,
                    this.camera.position.y - 225f, 162, 162);

            batch.draw(itemImage, this.camera.position.x + 258f,
                    this.camera.position.y - 225f, 162, 180);

            if (backButtonPressed == 1 && buttonTime < 200) {
                batch.draw(recipeBackClicked,this.camera.position.x - 838.35f/2 ,
                        this.camera.position.y + 127.125f/2, 135f,175.5f);
            }


            if (topButtonPressed == 1) {
                batch.draw(equipmentsBtnClicked,this.camera.position.x - 568.125f/2 ,
                        this.camera.position.y + 361.125f/2, 270f,58.5f);
                batch.draw(itemsBtnNormal,this.camera.position.x - 568.125f/2 ,
                        this.camera.position.y + 244.125f/2, 270f,58.5f);
                batch.draw(towersBtnNormal,this.camera.position.x - 568.125f/2 ,
                        this.camera.position.y + 127.125f/2, 270f,58.5f);
            }
            if (topButtonPressed == 2) {
                batch.draw(equipmentsBtnNormal,this.camera.position.x - 568.125f/2 ,
                        this.camera.position.y + 361.125f/2, 270f,58.5f);
                batch.draw(itemsBtnClicked,this.camera.position.x - 568.125f/2 ,
                        this.camera.position.y + 244.125f/2, 270f,58.5f);
                batch.draw(towersBtnNormal,this.camera.position.x - 568.125f/2 ,
                        this.camera.position.y + 127.125f/2, 270f,58.5f);
            }
            if (topButtonPressed == 3) {
                batch.draw(equipmentsBtnNormal,this.camera.position.x - 568.125f/2 ,
                        this.camera.position.y + 361.125f/2, 270f,58.5f);
                batch.draw(itemsBtnNormal,this.camera.position.x - 568.125f/2 ,
                        this.camera.position.y + 244.125f/2, 270f,58.5f);
                batch.draw(towersBtnClicked,this.camera.position.x - 568.125f/2 ,
                        this.camera.position.y + 127.125f/2, 270f,58.5f);
            }

            if (bottomButtonPressed == 1 && buttonTime < 500) {
                batch.draw(leftPageClicked,this.camera.position.x - 838.35f/2 ,
                        this.camera.position.y - 478.125f/2, 135f,36f);

            }
            if (bottomButtonPressed == 2 && buttonTime < 500) {
                batch.draw(homePageClicked,this.camera.position.x - 568.125f/2 ,
                        this.camera.position.y - 478.125f/2, 135f,36f);

            }
            if (bottomButtonPressed == 3 && buttonTime < 500) {
                batch.draw(rightPageClicked,this.camera.position.x - 298.125f/2 ,
                        this.camera.position.y - 478.125f/2, 135f,36f);

            }
        }



        backBtnBounds = new Rectangle(this.camera.position.x - 838.35f/2 ,
                this.camera.position.y + 127.125f/2, 135f,175.5f);

        equipmentsBtnBounds = new Rectangle(this.camera.position.x - 568.125f/2 ,
                this.camera.position.y + 361.125f/2, 270f,58.5f);

        itemsBtnBounds = new Rectangle(this.camera.position.x - 568.125f/2 ,
                this.camera.position.y + 244.125f/2, 270f,58.5f);

        towersBtnBounds = new Rectangle(this.camera.position.x - 568.125f/2 ,
                this.camera.position.y + 127.125f/2, 270f,58.5f);

        homeBtnBounds = new Rectangle(this.camera.position.x - 568.125f/2 ,
                this.camera.position.y - 478.125f/2, 135f,36f);

        leftBtnBounds = new Rectangle(this.camera.position.x - 838.35f/2 ,
                this.camera.position.y - 478.125f/2, 135f,36f);

        rightBtnBounds = new Rectangle(this.camera.position.x - 298.125f/2 ,
                this.camera.position.y - 478.125f/2, 135f,36f);

        objectBtnBounds = new Rectangle(this.camera.position.x - 838.35f/2,
                this.camera.position.y - 202.5f,405f, 252f);

        craftingBtnBounds = new Rectangle(this.camera.position.x + 184.5f,
                this.camera.position.y -28.8f, 81f, 36f);

        if (isCraftable()) {
            craftingBtn = GameManager.get().getManager(TextureManager.class).getTexture("craftingEnabled");
        } else {
            craftingBtn = GameManager.get().getManager(TextureManager.class).getTexture("craftingDisabled");
        }


    }

    @Override
    public void notifyKeyUp(int keycode) {
        // TODO Auto-generated method stub

    }

    @Override
    public void notifyKeyDown(int keycode) {

        // TODO Auto-generated method stub

    }

    @Override
    public void notifyTouchDown(int screenX, int screenY, int pointer, int button) {

        if (displayBook) {
            float[] mouse = WorldUtil.screenToWorldCoordinates(Gdx.input.getX(), Gdx.input.getY());
            Vector2 cameraPos = new Vector2(mouse[0], mouse[1]);


            allEquipmentsPages.put(0, "equipmentsPageOne");
            allEquipmentsPages.put(1, "equipmentsPageTwo");
            allItemPages.put(0, "itemsPageOne");
            allItemPages.put(1, "itemsPageTwo");
            allTowerPages.put(0, "towersPageOne");
            allTowerPages.put(1, "towersPageTwo");

            if (button == 0) {


                if (backBtnBounds.contains(cameraPos)) {
                    backButtonPressed = 1;
                    toggleRecipe();
                }

                if (craftingBtnBounds.contains(cameraPos)) {
                    if (isCraftable()) {
                        craftNewItem();
                    }
                }

                if (objectBtnBounds.contains(cameraPos)) {
                    setCamera(this.camera);
                    int imageIndex = getObjectIndex(cameraPos);
                    displayCraftingImage(imageIndex, currentPage);
                }

                getCurrentTab(cameraPos);

                int newPageIndex = -1;
                if (leftBtnBounds.contains(cameraPos)) {
                    bottomButtonPressed = 1;
                    startingTime = System.currentTimeMillis();
                    newPageIndex = currentPage - 1;
                } else if (homeBtnBounds.contains(cameraPos)) {
                    bottomButtonPressed = 2;
                    startingTime = System.currentTimeMillis();
                    newPageIndex = 0;
                } else if (rightBtnBounds.contains(cameraPos)) {
                    bottomButtonPressed = 3;
                    startingTime = System.currentTimeMillis();
                    newPageIndex = currentPage + 1;
                }

                if (newPageIndex > -1) {
                    if (currentTab.equals(itemsTab) && allItemPages.containsKey(newPageIndex)) {
                        System.out.println(topButtonPressed);
                        setCurrentPageDisplay(allItemPages, newPageIndex);
                    } else if (currentTab.equals(equipmentsTab) && allEquipmentsPages.containsKey(newPageIndex)) {
                        System.out.println(topButtonPressed);
                        setCurrentPageDisplay(allEquipmentsPages, newPageIndex);
                    } else if (currentTab.equals(towersTab) && allTowerPages.containsKey(newPageIndex)) {
                        System.out.println(topButtonPressed);
                        setCurrentPageDisplay(allTowerPages, newPageIndex);
                    }
                }

            }
        }
    }

    /**
     * CurrentPageDisplay the currentPageDisplay to set.
     */
    public void setCurrentPageDisplay(Map<Integer, String> pages, int pageIndex) {
        currentPageDisplay = GameManager.get().getManager(TextureManager.class).getTexture(pages.get(pageIndex));
        currentPage = pageIndex;
    }

    private void getCurrentTab(Vector2 cameraPos) {
        if (itemsBtnBounds.contains(cameraPos)) {
            currentPageDisplay = GameManager.get().getManager(TextureManager.class).getTexture(allItemPages.get(0));
            topButtonPressed = 2;
            currentPage = 0;
            currentTab = itemsTab;
        } else if (towersBtnBounds.contains(cameraPos)) {
            currentPageDisplay = GameManager.get().getManager(TextureManager.class).getTexture(allTowerPages.get(0));
            topButtonPressed = 3;
            currentPage = 0;
            currentTab = towersTab;
        } else if (equipmentsBtnBounds.contains(cameraPos)) {
            currentPageDisplay = GameManager.get().getManager(TextureManager.class).getTexture(allEquipmentsPages.get(0));
            topButtonPressed = 1;
            currentPage = 0;
            currentTab = equipmentsTab;
        }
    }

    /**
     * Displayes the image related to the current page and index
     * @param imageIndex - the index number (1-7)
     * @param currentPage - the current page number (0, 1)
     */
    private void displayCraftingImage(int imageIndex, int currentPage) {
        if (currentPage == 1) {
            imageIndex += 7;
        }

        if (currentTab.equals(itemsTab)) {
            craftingImage = itemImages.get(imageIndex).get(0);
            currentItem = craftingImage;
            descImage = itemImages.get(imageIndex).get(1);
            itemImage = itemImages.get(imageIndex).get(2);
        } else if (currentTab.equals(equipmentsTab)) {
            craftingImage = weaponImages.get(imageIndex).get(0);
            currentItem = craftingImage;
            descImage = weaponImages.get(imageIndex).get(1);
            itemImage = weaponImages.get(imageIndex).get(2);
        } else if (currentTab.equals(towersTab)) {
            craftingImage = towerImages.get(imageIndex).get(0);
            currentItem = craftingImage;
            descImage = towerImages.get(imageIndex).get(1);
            itemImage = towerImages.get(imageIndex).get(2);
        }
    }

    /**
     * Crafts a new item and transfers it to the player's inventory
     * if they have the correct quantity of items. Also removes the resources
     * used from the player's inventory.
     */
    private void craftNewItem() {
        Items allItems = new Items();
        PlayerPeon player = (PlayerPeon)GameManager.get().getWorld().getEntityById(GameManager.get().getPlayerEntityID());
        Inventory playerInventory = player.getInventory();
        RecipeBook recipeBook = new RecipeBook(allItems);
        List<Item> items = recipeBook.getCraftingRecipes().get(craftableItems.get(currentItem));

        for (Item item : items) {
            if (item != allItems.getItems().get("empty")) {
                playerInventory.removeItem(item);
            }
        }

        playerInventory.addItem(craftableItems.get(currentItem));

    }

    /**
     * Sets the camera
     * @param camera The camera
     */
    private void setCamera(OrthographicCamera camera) {
        this.camera = (PotateCamera) camera;
    }


    /**
     * Creates a rectangular boundary for the recipe book
     * @param x The left position of rectangle
     * @param y The bottom position of rectangle
     * @param width The width of rectangle
     * @param height The height of rectangle
     */
    private void setRecipeBounds(float x, float y, float width, float height) {
        this.recipeBounds = new Rectangle(x, y, width, height);
    }

    private int getObjectIndex(Vector2 cameraPosition) {
        int button = 0;
        float yIndex = cameraPosition.y;

        while (yIndex > objectBtnBounds.getY()) {
            yIndex -= 36;
            button++;
        }
        return 7 - button;
    }

    /**
     * Gets the page of the recipe book the player has clicked
     * @param cameraPosition The position of the camera
     * @return 2 if next page, 1 if previous page, 0 if neither
     */
    private int getPageIndex(Vector2 cameraPosition) {
        float xPos = cameraPosition.x - recipeBounds.getX();
        float yPos = cameraPosition.y - recipeBounds.getY();

        if (xPos > 340f) {
            return 2;
        } else if (xPos < 330f) {
            return 1;
        }

        return 0;
    }


    /**
     * Determines whether the player has the correct quantity of items making up the current item
     * displayed in the recipe book.
     * @return true if craftable, false otherwise
     */
    private boolean isCraftable() {
        if (currentItem == null || currentItem == GameManager.get().getManager(TextureManager.class).getTexture("pendingCrafting")) {
            return false;
        }

        Items items = new Items();
        PlayerPeon player = (PlayerPeon)GameManager.get().getWorld().getEntityById(GameManager.get().getPlayerEntityID());
        Inventory playerInventory = player.getInventory();
        RecipeBook recipeBook = new RecipeBook(items);
        List<Item> itemsToCheck = recipeBook.getCraftingRecipes().get(craftableItems.get(currentItem));
        HashMap<Item, Integer> requiredQuantities = new HashMap<>();

        for (Item item : itemsToCheck) {
            if (requiredQuantities.containsKey(item) && item != items.getItems().get("empty")) {
                Integer quantity = requiredQuantities.get(item);
                quantity++;
                requiredQuantities.replace(item, quantity);
            } else if (item != items.getItems().get("empty")) {
                requiredQuantities.put(item, 1);
            }

            if (item != items.getItems().get("empty") &&
                    playerInventory.getItemsNum(item) < requiredQuantities.get(item)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Toggles whether the Recipe Book is being displayed
     */
    public void toggleRecipe() {
        GameManager.get().getManager(SoundManager.class).playSound("Sound Effect/EFFECT_equipItem_01_Inactive.mp3");
        displayBook = !displayBook;
    }

    /**
     * Indicates if the book is currently open
     * @return whether book is currently open
     */
    public boolean isBookOpen() {
        return displayBook;
    }
}

