package deco.combatevolved.renderers;


import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import deco.combatevolved.managers.GameManager;
import deco.combatevolved.observers.CycleObserver;
import deco.combatevolved.managers.TextureManager;

/**
 * Class which renders the day and night of the game, including the icons to show the user
 */
public class DayNightRenderer implements Renderer, CycleObserver {
    private int cycleCode = 1;

    /**
     * Constructor which puts the images on screen and positions them appropriatly using the current sprite batch and
     * camera positioning
     *
     * @param batch
     * @param camera
     */
    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {
        float iconWidth = camera.viewportWidth / 12f;
        float iconHeight = camera.viewportWidth / 12f;
        if (cycleCode == 1) {
            batch.begin();
            batch.draw(GameManager.get().getManager(TextureManager.class).getTexture("dayIcon"),
                    camera.position.x + (camera.viewportWidth / 2f) - iconWidth,
                    camera.position.y + (camera.viewportHeight / 2f) - iconHeight,
                    iconWidth,
                    iconHeight); //, 200, 50);
            batch.end();
        } else if (cycleCode == 2) {
            batch.begin();
            batch.draw(GameManager.get().getManager(TextureManager.class).getTexture("nightBackground"),
                    camera.position.x - Math.round(camera.viewportWidth / 2.0f),
                    camera.position.y - Math.round(camera.viewportHeight / 2.0f),
                    camera.viewportWidth, camera.viewportHeight);
            batch.draw(GameManager.get().getManager(TextureManager.class).getTexture("nightIcon"),
                    camera.position.x + (camera.viewportWidth / 2f) - iconWidth,
                    camera.position.y + (camera.viewportHeight / 2f) - iconHeight,
                    iconWidth,
                    iconHeight); //, 200, 50);
            batch.end();
        } else if (cycleCode == 3) {
            batch.begin();
            batch.draw(GameManager.get().getManager(TextureManager.class).getTexture("sunsetIcon"),
                    camera.position.x + (camera.viewportWidth / 2f) - iconWidth,
                    camera.position.y + (camera.viewportHeight / 2f) - iconHeight,
                    iconWidth,
                    iconHeight); //, 200, 50);
            batch.end();
        }
    }

    @Override
    public void notifyCycleChange(int cycleCode){
        this.cycleCode = cycleCode;
    }
}
