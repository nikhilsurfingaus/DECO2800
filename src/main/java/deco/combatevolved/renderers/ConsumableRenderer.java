package deco.combatevolved.renderers;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import deco.combatevolved.entities.dynamicentities.PlayerPeon;
import deco.combatevolved.entities.items.consumableitems.ActiveItem;
import deco.combatevolved.managers.GameManager;
import deco.combatevolved.managers.TextureManager;

import java.util.ArrayList;
import java.util.List;

public class ConsumableRenderer implements Renderer {

    private PlayerPeon player;
    private BitmapFont font;

    public void create() {
        player = (PlayerPeon) GameManager.get().getWorld().
                getEntityById(GameManager.get().getPlayerEntityID());
        font = new BitmapFont();
    }

    public void render(SpriteBatch batch, OrthographicCamera camera) {

        batch.begin();
        renderText(batch, camera);
        renderConsumables(batch, camera);
        batch.end();
    }

    private void renderConsumables(SpriteBatch batch, OrthographicCamera camera) {

        List<ActiveItem> consumableItems = player.getActiveConsumables();
        List<ActiveItem> drawnItems = new ArrayList<>();

        if (consumableItems == null) {
            return;
        }

        for (int i = 0; i < consumableItems.size(); i++) {

            // so we don't get duplicates. Consuming the same item replaces the timer.
            if (drawnItems.contains(consumableItems.get(i))) {
                return;
            }

            drawnItems.add(consumableItems.get(i));

            font.draw(batch, this.formatTime(consumableItems.get(i).getTimeUsed()),1205,
                    460 - (100 * i));

            // rendering consumable images
            Sprite sprite = new Sprite(GameManager.get().getManager(TextureManager.class)
                    .getTexture(consumableItems.get(i).getTexture()));
            sprite.setBounds(1130,
                   420 - (100 * i),
                   sprite.getWidth() / 3, sprite.getHeight() / 3);

            sprite.draw(batch);
        }
    }

    private void renderText(SpriteBatch batch, OrthographicCamera camera) {

        if (player.getActiveConsumables().size() == 0) {
            return;
        }

        font.draw(batch, "Consumables", 1130,
                520);
    }

    /**
     * Returns a string containing the time remaining on the consumable, e.g. 3:45 (3 minutes and 45 seconds
     * @return
     */
    private String formatTime(long timeUsed) {

        String time;

        int seconds = 60 - (int) (System.currentTimeMillis() - timeUsed) / 1000 % 60;
        int minutes = 4 - (int) (System.currentTimeMillis() - timeUsed) / 1000 / 60;

        if (seconds == 60) {
            time = String.format("%d:%s", minutes + 1, "00");
        } else if (seconds < 10) {
            time = String.format("%d:0%s", minutes, seconds);
        } else {
            time =  String.format("%d:%d", minutes, seconds);
        }
        return time;
    }
}
