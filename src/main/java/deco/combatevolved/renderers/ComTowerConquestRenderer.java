package deco.combatevolved.renderers;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import deco.combatevolved.managers.GameManager;
import deco.combatevolved.managers.TextureManager;
import deco.combatevolved.observers.ComTowerObserver;

public class ComTowerConquestRenderer implements Renderer, ComTowerObserver {
    Texture conquest = null;
    boolean active = false;

    @Override
    /**
     * renders the conquest display
     */
    public void render(SpriteBatch batch, OrthographicCamera camera) {
        if (active) {
            batch.begin();
            batch.draw(this.conquest, camera.position.x - (camera.viewportWidth / 2f),
                    camera.position.y - (camera.viewportHeight / 2f),
                    camera.viewportWidth/4f, camera.viewportWidth/32f); //400*50 proportion
            batch.end();
        }
    }

    @Override
    /**
     * method for being notified of current conquest status.
     */
    public void notifyComConquest(int conquestCompletion) {
        if (conquestCompletion <= 0){
            this.active = false;
        } else if (conquestCompletion < 12){
            this.active = true;
            this.conquest = GameManager.get().getManager(TextureManager.class).getTexture("conquest0");
        } else if (conquestCompletion < 25){
            this.active = true;
            this.conquest = GameManager.get().getManager(TextureManager.class).getTexture("conquest12");
        } else if (conquestCompletion < 37){
            this.active = true;
            this.conquest = GameManager.get().getManager(TextureManager.class).getTexture("conquest25");
        } else if (conquestCompletion < 50){
            this.active = true;
            this.conquest = GameManager.get().getManager(TextureManager.class).getTexture("conquest37");
        } else if (conquestCompletion < 62){
            this.active = true;
            this.conquest = GameManager.get().getManager(TextureManager.class).getTexture("conquest50");
        } else if (conquestCompletion < 75){
            this.active = true;
            this.conquest = GameManager.get().getManager(TextureManager.class).getTexture("conquest62");
        } else if (conquestCompletion < 87){
            this.active = true;
            this.conquest = GameManager.get().getManager(TextureManager.class).getTexture("conquest75");
        } else if (conquestCompletion <= 100){
            this.active = true;
            this.conquest = GameManager.get().getManager(TextureManager.class).getTexture("conquest87");
        } else {
            this.active = true;
            this.conquest = GameManager.get().getManager(TextureManager.class).getTexture("conquest100");
        }
    }
}
