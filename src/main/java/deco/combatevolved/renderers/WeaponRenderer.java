package deco.combatevolved.renderers;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import deco.combatevolved.managers.GameManager;
import deco.combatevolved.managers.TextureManager;

public class WeaponRenderer implements Renderer {

    private SpriteBatch batch;

    private Texture projectile;

    private TextureRegion projectileRegion;

    private TextureRegionDrawable projectileDrawable;

    private ImageButton button;
    /**
     * Constructor to initialise the weapon projectile
     */
    public WeaponRenderer() {
        projectile = GameManager.get().getManager(TextureManager.class).getTexture("projectile");
        projectileRegion = new TextureRegion(projectile);
        projectileDrawable = new TextureRegionDrawable(projectileRegion);
        button = new ImageButton(projectileDrawable);
    }

    /**
     * Renders the projectile onto the screen, as well as appropritaly accomodating the camera positon
     *
     * @param camera
     * @param batch
     */
    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        batch.end();
    }
}
