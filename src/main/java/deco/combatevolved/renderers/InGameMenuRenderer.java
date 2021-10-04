package deco.combatevolved.renderers;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import deco.combatevolved.managers.GameManager;
import deco.combatevolved.managers.TextureManager;
import deco.combatevolved.renderers.inventoryrenderer.InventoryRenderer;

public class InGameMenuRenderer extends InventoryRenderer implements Renderer {

    // Display the MenuBackground
    private boolean displayMenu = false;

    private Texture overlayBackground = GameManager.get().getManager(TextureManager.class).getTexture("overlayBackground");
    private Texture menuModal = GameManager.get().getManager(TextureManager.class).getTexture("modalBackground");


    // the camera
    private OrthographicCamera camera;


    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {
        setCamera(camera);
        batch.begin();
        renderInGameMenu(batch);
        batch.end();
    }

    /**
     * Renders the in-game menu background
     * @param batch The batch to render onto
     */
    private void renderInGameMenu(SpriteBatch batch) {
        if (displayMenu) {
            batch.draw(menuModal, this.camera.position.x - 302.5f,
                    this.camera.position.y - 230f);
            batch.draw(overlayBackground, this.camera.position.x - 2000f,
                    this.camera.position.y - 1200f / 2);


        }


    }

    /**
     * Sets the camera
     * @param camera The camera
     */
    private void setCamera(OrthographicCamera camera) {
        this.camera = (PotateCamera) camera;
    }


    /**
     * Toggles whether the in-game menu background is being displayed
     */
    public void toggleMenuBackground() {
        displayMenu = !displayMenu;
    }



}
