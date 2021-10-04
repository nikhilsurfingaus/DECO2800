package deco.combatevolved.renderers.inventoryrenderer;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import deco.combatevolved.entities.dynamicentities.PlayerAttributes;
import deco.combatevolved.entities.items.Inventory;
import deco.combatevolved.managers.*;

public class PlayerBarRenderer extends InventoryPanelRenderer {

    //Boundaries for the player inventory
    private Rectangle playerInvBoundaries;
    //Inventory of player
    private PlayerAttributes player;

    //Number of slots a player has
    private static final int PLAYER_SLOTS = 8;

    public PlayerBarRenderer(Texture invSlot, BitmapFont font, GlyphLayout text, PlayerAttributes player) {
        super(invSlot, font, text, "invOneLine");
        toggleDisplay(); // enable at beginning
        this.player = player;
	}

	@Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {
        batch.draw(panelTexture, camera.position.x - panelTexture.getWidth() / 2,
        camera.position.y - camera.viewportHeight / 2);
    	for (int i = 0; i < PLAYER_SLOTS; i++) {
            renderStack(camera, batch, i, i, i);
        }

        //Calculate inventory boundaries
        playerInvBoundaries = new Rectangle(camera.position.x + (((float)PLAYER_SLOTS / 2) 
        		- PLAYER_SLOTS) * invSlot.getWidth(),
        		camera.position.y - camera.viewportHeight / 2,
        		(float) PLAYER_SLOTS * invSlot.getWidth(),
        		(float) invSlot.getHeight());
        
        //Draw the player's scrap number
        String scrap = "Scrap: " + player.getScrap();
        font.draw(batch, scrap, camera.position.x + Math.round(camera.viewportWidth / 2.3),
                camera.position.y + Math.round(camera.viewportHeight / 2.1));
    }

    private void renderStack(OrthographicCamera camera, SpriteBatch batch, 
            int stackIndex, int i, int j) {
        Inventory.Stack stack = player.getInventory().getStack(stackIndex);
        if (stack != null ) {
            Texture texture = GameManager.get().getManager(TextureManager.class).
            		getTexture(stack.getItem().getTexture());
            String quantity = "x" + stack.getNumItems();
            text.setText(font, quantity);
            renderItem(camera, batch, texture, quantity, i);
        }
    }

    private void renderItem(OrthographicCamera camera, SpriteBatch batch, Texture texture, 
            String quantity, int i) {
        Sprite sprite = new Sprite(texture);
        sprite.setBounds(
            camera.position.x - (((float) PLAYER_SLOTS / 2) - i) *
                    invSlot.getWidth() + (invSlot.getWidth() - ITEM_DIM) / (float) 2,
            camera.position.y - camera.viewportHeight / 2 +
                    (invSlot.getHeight() - ITEM_DIM) / (float) 2,
            ITEM_DIM, ITEM_DIM);
        sprite.draw(batch);
        font.draw(batch, quantity,
            camera.position.x - (((float)PLAYER_SLOTS / 2) - i) * invSlot.getWidth() + 25,
            camera.position.y - camera.viewportHeight / 2 + 20);
    }

    @Override
    public boolean contains(Vector2 position) {
        return display && playerInvBoundaries.contains(position);
    }

    @Override
    public int clickIndex(Vector2 position) {
        float xDist = position.x - playerInvBoundaries.getX();
		float slotWidth = playerInvBoundaries.getWidth() / PLAYER_SLOTS;
		return (int) Math.floor(xDist / slotWidth);	
    }

}