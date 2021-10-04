package deco.combatevolved.renderers.inventoryrenderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import deco.combatevolved.entities.items.Inventory;
import deco.combatevolved.managers.*;

public class StashRenderer extends InventoryPanelRenderer {

    //Inventory of vehicle
    private Inventory stash;

    //Font for disable text
    private BitmapFont disableFont = new BitmapFont(Gdx.files.internal("resources/fonts/Arial.fnt"));
    //Disable text
    private static GlyphLayout disableText = new GlyphLayout();

    //Boundaries for the player inventory
    private Rectangle stashInvBoundaries;
    // Check to see if player is close enough to enable stash
    private boolean withinStashDist = false;

    private static final int PLAYER_SLOTS = 8;

    public StashRenderer(Texture invSlot, BitmapFont font, GlyphLayout text, Inventory stash) {
        super(invSlot, font, text, "invGrid");
        this.stash = stash;
        disableFont.getData().setScale(1f);
        disableFont.setColor(0, 0, 0, 1);
        disableText.setText(disableFont, "You must be near the vehicle");
    }

	@Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {
        //Calculate stash boundaries
        stashInvBoundaries = new Rectangle(camera.position.x + (((float)PLAYER_SLOTS / 2) - PLAYER_SLOTS) * invSlot.getWidth(),
        		camera.position.y - ((float)COLUMNS / 2 - 1) * invSlot.getHeight(),
        		(float) PLAYER_SLOTS * invSlot.getWidth(),
        		(float) PLAYER_SLOTS * invSlot.getHeight());
        
        if (!display) {
            return;
        }

        Sprite sprite = new Sprite(panelTexture);
        sprite.setBounds(camera.position.x - panelTexture.getWidth() / 2, 
        		camera.position.y - panelTexture.getHeight() / 2 + 85, 
        		panelTexture.getWidth(), panelTexture.getHeight());
        if (!withinStashDist && display) {
            disableFont.draw(batch, disableText, camera.position.x - disableText.width / 2, 
                    camera.position.y + 65);
        	sprite.setAlpha(0.2f);
        }
        sprite.draw(batch);

    	for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {          	
                renderStack(camera, batch, (ROWS * j) + i, i, j);
            }
    	}
    }

    /**
	 * Renders all inventory stacks for stash
	 * 
	 * @param camera the camera
	 * @param batch the sprite batch
     * @param stackIndex index of stack
	 * @param i the column to render
     * @param j the row to render
	 */
    private void renderStack(OrthographicCamera camera, SpriteBatch batch, 
            int stackIndex, int i, int j) {
        Inventory.Stack stack = stash.getStack(stackIndex);
        if (stack != null ) {
            Texture texture = GameManager.get().getManager(TextureManager.class).
            		getTexture(stack.getItem().getTexture());
            String quantity = "x" + stack.getNumItems();
            text.setText(font, quantity);
            renderItem(camera, batch, texture, quantity, i, j);
        }
    }

    private void renderItem(OrthographicCamera camera, SpriteBatch batch, Texture texture, 
            String quantity, int i, int j) {
        Sprite sprite = new Sprite(texture);
        sprite.setBounds(
            camera.position.x - (((float) ROWS / 2) - i) *
                    invSlot.getWidth() + (invSlot.getWidth() - ITEM_DIM) / (float) 2, 
            camera.position.y + (((float) COLUMNS / 2) - j) *
                    invSlot.getHeight() + (invSlot.getHeight() - ITEM_DIM) / (float) 2, 
            ITEM_DIM, ITEM_DIM);

        if (!withinStashDist) {
        	sprite.setAlpha(0.2f);
        }

        sprite.draw(batch);

        font.draw(batch, quantity, 
                camera.position.x - (((float) ROWS / 2) - i - 1) *
                        invSlot.getWidth() - text.width - 5, 
                camera.position.y + (((float) COLUMNS / 2) - j) *
                        invSlot.getHeight() + 20);
    }

    public void stashDist(boolean state) {
        withinStashDist = state;
    }

	@Override
    public boolean contains(Vector2 position) {
        return display && stashInvBoundaries.contains(position);
    }

	@Override
    public int clickIndex(Vector2 position) {
        float slotWidth = stashInvBoundaries.getWidth() / PLAYER_SLOTS;
		float slotHeight = stashInvBoundaries.getHeight() / PLAYER_SLOTS;
		float xDist = position.x - stashInvBoundaries.getX();
		float yDist = position.y - stashInvBoundaries.getY() + slotHeight;
		return (int) ((PLAYER_SLOTS * (PLAYER_SLOTS - Math.floor(yDist / slotHeight))) +
				Math.floor(xDist / slotWidth)); 
    }

}