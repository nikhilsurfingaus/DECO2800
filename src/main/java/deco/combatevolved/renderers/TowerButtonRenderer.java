package deco.combatevolved.renderers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import deco.combatevolved.managers.GameManager;
import deco.combatevolved.managers.TextureManager;

/**
 * Class that render the button to open tower inventory
 */
public class TowerButtonRenderer implements Renderer {
    Texture tower = GameManager.get().getManager(TextureManager.class).getTexture("tower_button");

    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera){
        batch.begin();
        batch.draw(tower,camera.position.x + (float) (camera.viewportWidth / 3.2),
                (float) (camera.position.y - camera.viewportHeight / 2.01),
                Gdx.graphics.getWidth()/5, (float) (Gdx.graphics.getHeight()/10.285));
        batch.end();
    }
}