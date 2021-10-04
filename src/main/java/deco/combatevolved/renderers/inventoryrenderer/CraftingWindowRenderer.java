package deco.combatevolved.renderers.inventoryrenderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import deco.combatevolved.entities.dynamicentities.PlayerAttributes;
import deco.combatevolved.entities.items.Inventory;
import deco.combatevolved.entities.items.Item;
import deco.combatevolved.managers.GameManager;
import deco.combatevolved.managers.OnScreenMessageManager;
import deco.combatevolved.managers.TextureManager;
import deco.combatevolved.entities.staticentities.CraftingTable;
import deco.combatevolved.util.HexVector;
import deco.combatevolved.util.WorldUtil;
import deco.combatevolved.renderers.inventoryrenderer.InventoryRenderer;

import java.util.ArrayList;
import java.util.List;

/**
 * Class which renders the crafting window for the game
 */
public class CraftingWindowRenderer extends InventoryRenderer {

    private OrthographicCamera camera;
    boolean isCrafting = false;
    private static int craftRows = 3;
    private static int craftColumns = 3;
    private Texture craftingGrid = GameManager.get().getManager(TextureManager.class).getTexture("craftingGrid");
    private List<Rectangle> craftingBoxes = new ArrayList<>();
    private Rectangle craftedItemBox;
    private CraftingTable craftingTable;
    private Item followingItem = null;
    private static final int ITEM_DIM = 50;
    private PlayerAttributes player;
    private Inventory playerInventory;
    private Rectangle playerInvBoundaries;
    private static final int PLAYER_SLOTS = 8;

    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {

        this.camera = camera;

        if (font == null) {
            font = new BitmapFont();
            font.getData().setScale(1f);
            font.setColor(0, 0, 0, 1);
        }

        batch.begin();
        renderStashCrafting(batch);
        renderSelectedItem(batch); // most likely, this is what is causing the double click issue. Fix this method
        batch.end();
    }

    /**
     * Renders the frame of the crafting window
     */
    private void renderStashCrafting(SpriteBatch batch) {

        Vector3 coords = this.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
        float[] mouse = new float[] { coords.x, coords.y };
        Vector2 cameraPos = new Vector2(mouse[0], mouse[1]);

        this.setCraftingBoxes(camera); // updates boxes

        player = (PlayerAttributes) GameManager.get().getWorld().getEntityById(GameManager.get().getPlayerEntityID());
        playerInventory = player.getInventory();
        playerInvBoundaries = new Rectangle(camera.position.x + (((float) PLAYER_SLOTS / 2)
                - PLAYER_SLOTS) * invSlot.getWidth(),
                camera.position.y - camera.viewportHeight / 2,
                (float) PLAYER_SLOTS * invSlot.getWidth(),
                (float) invSlot.getHeight());


        //Render vehicle inventory stash
        if (isCrafting) {

            Sprite craftingScreen = new Sprite(craftingGrid);
            craftingScreen.setBounds(camera.position.x - 2 * craftingGrid.getWidth(), camera.position.y - craftingGrid.getHeight() / 3,
                    craftingGrid.getWidth(), craftingGrid.getHeight());
            craftingScreen.draw(batch);

        }
    }

    /**
     * Displays all the items inside the crafting table inside their respective slots
     *
     * @param craftingTable
     * @param batch
     * @param camera
     */
    public void displayItems(CraftingTable craftingTable, SpriteBatch batch, OrthographicCamera camera) {

        this.craftingTable = craftingTable;

        // checking if an item can be crafted
        craftingTable.setCraftedItem(craftingTable.craftItem());

        if (isCrafting) {

            int count = 0; // needed as we are converting from 2d array to 1d list.
            for (int i = 0; i < craftRows; i++) {
                for (int j = 0; j < craftColumns; j++) {

                    Item item;

                    try {
                        item = craftingTable.getItems().get(count);
                    } catch (NullPointerException e) {
                        item = null;
                    }

                    // Item is not empty
                    if (item != null) {
                        Texture itemTexture = GameManager.get().getManager(TextureManager.class).getTexture(item.getTexture());
                        displayItem(batch, camera, itemTexture, i, j);
                    }

                    count++;
                }
            }
        }
    }

    /**
     * Displays an item texture to the player's crafting window.
     *
     * @param batch   the SpriteBatch in GameScreen
     * @param camera  Debug camera in GameScreen
     * @param texture the texture of the item
     * @param column  the column in the crafting window (starts at 0)
     * @param row     the row in the crafting window (starts at 0)
     */
    public void displayItem(SpriteBatch batch, OrthographicCamera camera, Texture texture, int column, int row) {

        batch.begin();
        batch.draw(texture, camera.position.x - 2 * craftingGrid.getWidth() + ((float) column * 7/5 * ITEM_DIM) + ((float) ITEM_DIM / 2),
                camera.position.y - (row * 75) + 160,
                ITEM_DIM,
                ITEM_DIM);
        batch.end();
    }

    public void displayCraftedItem(Item craftedItem, SpriteBatch batch, OrthographicCamera camera) {

        if (craftedItem == null || !isCrafting) {
            return;
        }

        Texture texture = GameManager.get().getManager(TextureManager.class).getTexture(craftedItem.getTexture());

        batch.begin();

        batch.draw(texture, camera.position.x - 2 * craftingGrid.getWidth() + 95,
                camera.position.y - 120, ITEM_DIM, ITEM_DIM);

        batch.end();
    }

    public void toggleCraft() {
        isCrafting = !isCrafting;
    }

    public boolean isCraftingOn() {
        return isCrafting;
    }

    /**
     * Notifies the observers of the mouse button being pushed down
     *
     * @param screenX the x position the mouse was pressed at
     * @param screenY the y position the mouse was pressed at
     * @param pointer
     * @param button  the button which was pressed
     */
    @Override
    public void notifyTouchDown(int screenX, int screenY, int pointer, int button) {

        Vector3 coords = this.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
        float[] mouse = new float[] { coords.x, coords.y };
        Vector2 cameraPos = new Vector2(mouse[0], mouse[1]);

        if (button == 1) {

            HexVector craftingTablePosition = craftingTable.getPosition();
            float[] clickedCoords = WorldUtil.screenToWorldCoordinates(screenX, screenY);
            clickedCoords = WorldUtil.worldCoordinatesToColRow(clickedCoords[0], clickedCoords[1]);

            if (GameManager.get().getManager(OnScreenMessageManager.class).isTyping()) {
                return;
            }

            if (craftingTablePosition.getCol() == clickedCoords[0] && craftingTablePosition.getRow() == clickedCoords[1]) {
                this.toggleCraft();
            }
            return;
        }

        if (followingItem != null) {

            if (button == 0) {
                if (playerInvBoundaries.contains(cameraPos)) {
                    int index = this.getPlayerInvClickIndex(cameraPos);
                    playerInventory.addItemAtIndex(followingItem, index);
                    followingItem = null;
                }
            }

            if (button == 0 && isCrafting) {

                for (int i = 0; i < 9; i++) {

                    if (craftingBoxes.get(i).contains(cameraPos) && craftingTable.isFreeSlot(i)) {
                        craftingTable.addItem(followingItem, i);
                        followingItem = null;
                        break; // no point going through rest of loop
                    } else if (craftingBoxes.get(i).contains(cameraPos) && !craftingTable.isFreeSlot(i)) {
                        Item temp = followingItem;
                        followingItem = craftingTable.getItem(i);
                        craftingTable.removeItem(i);
                        craftingTable.addItem(temp, i);
                    }
                }
            }

        } else {

            if (button == 0 && isCrafting) {

                for (int i = 0; i < 9; i++) {
                    if (craftingBoxes.get(i).contains(cameraPos)) {

                        followingItem = craftingTable.getItem(i);
                        craftingTable.removeItem(i);
                        break; // no point going through rest of loop
                    }
                }

                if (craftedItemBox.contains(cameraPos)) {
                    craftingTable.clear();
                    followingItem = craftingTable.getCraftedItem();
                }
            }
        }
    }

    /**
     * Renders any selected item onto mouse position
     *
     * @param batch The sprite batch
     */
    private void renderSelectedItem(SpriteBatch batch) {

        if (followingItem == null) {
            return;
        }

        // update camera to new method here as well

        Texture item = GameManager.get().getManager(TextureManager.class)
                .getTexture(followingItem.getTexture());
        float[] mouse = WorldUtil.screenToWorldCoordinates(Gdx.input.getX(),
                Gdx.input.getY());
        batch.draw(item, mouse[0], mouse[1], ITEM_DIM, ITEM_DIM);
    }

    /**
     * Calculate the player inventory index that was clicked
     *
     * @param cameraPosition position in camera frame
     * @return index of player inventory
     */
    int getPlayerInvClickIndex(Vector2 cameraPosition) {
        float xDist = cameraPosition.x - playerInvBoundaries.getX();
        float slotWidth = playerInvBoundaries.getWidth() / PLAYER_SLOTS;
        return (int) Math.floor(xDist / slotWidth);
    }

    /**
     * Allows items to be added to crafting table from the inventory renderer
     *
     * @param item  the item to be added to the crafting table
     * @param index where in the crafting table (0-8) to add the item.
     */
    void addToCraftingTable(Item item, int index) {

        craftingTable.addItem(item, index);

    }

    /**
     * Gets the rectangles for the crafting boxes of the crafting table
     *
     * @return
     */
    List<Rectangle> getCraftingBoxes() {
        return this.craftingBoxes;
    }

    private void setCraftingBoxes(OrthographicCamera camera) {

        // these positions are still a bit funky, this comment is a reminder for myself to fix them

        craftedItemBox = new Rectangle(camera.position.x - 2 * craftingGrid.getWidth() + 95,
                camera.position.y - 120, ITEM_DIM + 5, ITEM_DIM + 5);

        craftingBoxes.clear();

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Rectangle box = new Rectangle(camera.position.x - 2 * craftingGrid.getWidth() + ((float) i * 7/5 * ITEM_DIM) + ((float) ITEM_DIM / 2),
                        camera.position.y - (j * 75) + 160, ITEM_DIM + 5, ITEM_DIM + 5);
                this.craftingBoxes.add(box);
            }
        }
    }
}
