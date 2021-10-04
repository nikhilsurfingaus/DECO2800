package deco.combatevolved.renderers.inventoryrenderer;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import deco.combatevolved.managers.GameManager;
import deco.combatevolved.managers.TextureManager;
import com.badlogic.gdx.graphics.OrthographicCamera;

public abstract class InventoryPanelRenderer {

    Texture invSlot;
    Texture panelTexture;
    BitmapFont font;
    GlyphLayout text;

    //Number of rows in vehicle inventory
    static final int ROWS = 8;
    //Number of columns in vehicle inventory
    static final int COLUMNS = 8;
    //Dimensions of item
    static final int ITEM_DIM = 40;

    //Whether or not to display
    boolean display = false;

    public InventoryPanelRenderer(Texture invSlot, BitmapFont font, GlyphLayout text, String texture) {
        this.invSlot = invSlot;
        this.font = font;
        this.text = text;
        this.panelTexture = GameManager.get().getManager(TextureManager.class).getTexture(texture);
    }

    /**
     * Toggles the visibility of the inventory panel
     */
    public void toggleDisplay() {
        display = !display;
    }

    /**
     * Renders the inventory panel
     * @param batch
     * @param camera
     */
    public abstract void render(SpriteBatch batch, OrthographicCamera camera);

    /**
     * Check if the selected position is within a rendered inventory panel
     * @param position
     * @return return true if inside a visible inventory panel
     */
    public abstract boolean contains(Vector2 position);

    /**
	 * Calculate inventory stack that was clicked
	 * 
	 * @param cameraPosition
	 * @return index of the inventory stack index
	 */
    public abstract int clickIndex(Vector2 position);

    public void dispose() {
        panelTexture.dispose();
    }

}
