package deco.combatevolved.renderers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar.ProgressBarStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

import deco.combatevolved.entities.dynamicentities.PlayerAttributes;
import deco.combatevolved.entities.dynamicentities.PlayerPeon;
import deco.combatevolved.managers.EnemyManager;
import deco.combatevolved.managers.GameManager;
import deco.combatevolved.managers.NetworkManager;
import deco.combatevolved.managers.TextureManager;
import deco.combatevolved.util.WorldUtil;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class GameplayInterfaceRenderer implements Renderer {
    //Font
    BitmapFont font;
    // Status Bars
    // Health Bar Container Texture
    Texture healthBarContainerTexture =
            GameManager.get().getManager(TextureManager.class).getTexture(
                    "healthBarContainer");
    // Health Bar Container Texture Region
    TextureRegion healthBarContainer = new TextureRegion(healthBarContainerTexture);
    // Health Bar Texture
    Texture healthBarTexture =
            GameManager.get().getManager(TextureManager.class).getTexture(
                    "healthBar");
    // Health Bar Texture Region
    TextureRegion healthBar = new TextureRegion(healthBarTexture);

    // Energy Bar Container Texture
    Texture energyBarContainerTexture = GameManager.get().getManager(TextureManager.class).getTexture(
            "energyBarContainer");
    // Energy Bar Container Texture Region
    TextureRegion energyBarContainer = new TextureRegion(energyBarContainerTexture);
    // Energy Bar Texture
    Texture energyBarTexture =
            GameManager.get().getManager(TextureManager.class).getTexture(
                    "energyBar");
    // Energy Bar Texture Region
    TextureRegion energyBar = new TextureRegion(energyBarTexture);

    // Raid Bar Container Texture
    Texture raidBarContainerTexture =
            GameManager.get().getManager(TextureManager.class).getTexture(
            "raidBarContainer");
    // Raid Bar Container Texture Region
    TextureRegion raidBarContainer =
            new TextureRegion(raidBarContainerTexture);
    // Raid Bar Texture
    Texture raidBarTexture =
            GameManager.get().getManager(TextureManager.class).getTexture(
                    "raidBar");
    // Raid Bar Texture Region
    TextureRegion raidBar = new TextureRegion(raidBarTexture);
    // Raid Bar enemies remaining text font
    BitmapFont enemiesRemainingFont = new BitmapFont(Gdx.files.internal(
            "resources/fonts/Space Frigate/spacefri.fnt"));

    // Exp Bar Container Texture
    Texture expBarContainerTexture =
            GameManager.get().getManager(TextureManager.class).getTexture(
                    "expBarContainer");
    // Exp Bar Container Texture Region
    TextureRegion expBarContainer = new TextureRegion(expBarContainerTexture);
    // Exp Bar Texture
    Texture expBarTexture =
            GameManager.get().getManager(TextureManager.class).getTexture(
                    "expBar");
    Texture levelTexture = GameManager.get().getManager(TextureManager.class).getTexture("levelTexture");
    // Exp Bar Texture Region
    TextureRegion expBar = new TextureRegion(expBarTexture);

    private BitmapFont reviveFont = new BitmapFont(Gdx.files.internal(
            "resources/fonts/Space Frigate/spacefri.fnt"));
    private BitmapFont arrowFont = new BitmapFont(Gdx.files.internal("resources/fonts/arrow.fnt"));
    private Map<Integer, ProgressBar> reviveMap = new HashMap<>();

    public void render(SpriteBatch batch, OrthographicCamera camera) {
        if (font == null) {
            font = new BitmapFont();
            font.getData().setScale(1f);
        }

        batch.begin();
        // render interface
        renderGamePlayInterface(batch, camera);
        batch.end();
    }

    /**
     * Renders the GamePlay interfaces such as status bars
     * @param batch - render interfaces onto
     * @param camera - camera
     */
    public void renderGamePlayInterface(SpriteBatch batch,
                                        OrthographicCamera camera) {
        // Render health bar
        renderHealthBar(batch, camera);
        // Render energy bar
        renderEnergyBar(batch, camera);
        // Render raid bar
        renderRaidBar(batch, camera);

        renderExpBar(batch, camera);

        renderReviveText(batch);
    }

    /**
     * Renders the health bar
     * @param batch - render health bar onto
     * @param camera - camera
     */
    private void renderHealthBar(SpriteBatch batch, OrthographicCamera camera) {
        PlayerPeon player = (PlayerPeon) GameManager.get().getWorld().getEntityById(GameManager.get().getPlayerEntityID());

        // Set health bar dimension based on player health
        int health = player.getHealth();
        int maxHealth = player.getMaxPlayerHealth();

        // Set texture region width
        int width = (int) ((healthBarTexture.getWidth()) * (health/(float) maxHealth));
        healthBar.setRegionWidth(width);

        // Draw health bar
        batch.draw(healthBar,
                camera.position.x - Math.round(camera.viewportWidth / 2.05),
                camera.position.y + Math.round(camera.viewportHeight / 2.5),
                (float) (Gdx.graphics.getWidth()/(5.87/(health/(float) maxHealth))),
                (float) (Gdx.graphics.getHeight()/12.95));

        // Set texture region for health bar container
        int HPwidth = (healthBarContainerTexture.getWidth());
        healthBarContainer.setRegionWidth(HPwidth);

        // Draw health bar container
        batch.draw(healthBarContainer,
                camera.position.x - Math.round(camera.viewportWidth / 2.05),
                camera.position.y + Math.round(camera.viewportHeight / 2.5),
                (float) (Gdx.graphics.getWidth()/4.98), Gdx.graphics.getHeight()/13f);
    }

    /**
     * Renders the energy bar
     * @param batch - render energy bar onto
     * @param camera - camera
     */
    private void renderEnergyBar(SpriteBatch batch, OrthographicCamera camera) {
        // Set energy bar dimension based on player energy
        int energy = ((PlayerPeon) GameManager.get().getWorld()
                .getEntityById(GameManager.get().getPlayerEntityID())).getPlayerEnergy();
        int maxEnergy = ((PlayerPeon) GameManager.get().getWorld()
                .getEntityById(GameManager.get().getPlayerEntityID())).getMaxPlayerEnergy();

        // Set texture region width
        int width = (int) ((energyBarTexture.getWidth()) * (energy/(float) maxEnergy));
        energyBar.setRegionWidth(width);

        // Draw energy bar
        batch.draw(energyBar, camera.position.x - Math.round(camera.viewportWidth / 2.05),
                camera.position.y + Math.round(camera.viewportHeight / 3.25),
                Gdx.graphics.getWidth()/(6/(energy/(float) maxEnergy)),
                Gdx.graphics.getHeight()/13);

        // Set texture region for energy bar container
        int energyBarWidth = (energyBarContainerTexture.getWidth());
        energyBarContainer.setRegionWidth(energyBarWidth);
        // Draw energy bar container
        batch.draw(energyBarContainer,
                camera.position.x - Math.round(camera.viewportWidth / 2.05),
                camera.position.y + Math.round(camera.viewportHeight / 3.25),
                (float) (Gdx.graphics.getWidth()/4.98),
                Gdx.graphics.getHeight()/13);
    }

    /**
     * Renders the raid bar
     * @param batch - render raid bar onto
     * @param camera - camera
     */
    private void renderRaidBar(SpriteBatch batch, OrthographicCamera camera) {
        // Set texture region for raid bar container
        int raidBarWidth = (raidBarContainerTexture.getWidth());
        raidBarContainer.setRegionWidth(raidBarWidth);
        // Draw raid bar container
        // DEFAULT x - 8.5, y - 2.355, width - 4.98, height - 13
        // decrease value in y --> shifts up, increase --> shifts down
        // increase height --> shorter, decrease --> taller
        // decrease width --> shorter, increase --> longer
        batch.draw(raidBarContainer,
                (float) (camera.position.x - (Gdx.graphics.getWidth() * 0.001) - Math.round(camera.viewportWidth / 8.5)),
                (float) (camera.position.y - (Gdx.graphics.getHeight() * 0.005) + Math.round(camera.viewportHeight / 2.45)),
                (float) (Gdx.graphics.getWidth() * 0.25),
                (float) (Gdx.graphics.getHeight() * 0.1));


        // Set raid bar dimension based on enemies remaining
        int enemiesLeft =
                GameManager.getManagerFromInstance(EnemyManager.class).getEnemyNumber();
        int spawnTotal =
                GameManager.getManagerFromInstance(EnemyManager.class).getEnemySpawnTotal();

        // Set texture region width
        int width =
                (int) ((raidBarTexture.getWidth()) * (enemiesLeft/(float) spawnTotal));
        raidBar.setRegionWidth(width);
        //raidBar.setRegionHeight(raidBarTexture.getHeight());

        // Draw raid bar
        // GOOD VALUES --> x - 9, y - 2.45, width - 5.35, height - 12
        batch.draw(raidBar,
                (float) (camera.position.x - (Gdx.graphics.getWidth() * 0.001) - Math.round(camera.viewportWidth / 8.5)),
                (float) (camera.position.y - (Gdx.graphics.getHeight() * 0.005) + Math.round(camera.viewportHeight / 2.2)),
                (float) (Gdx.graphics.getWidth() * 0.25 * (enemiesLeft/(float) spawnTotal)),
                (float) (Gdx.graphics.getHeight() * 0.1)/2);

        // Render enemies remaining text
        String text = Integer.toString(enemiesLeft);
        enemiesRemainingFont.getData().setScale(0.5f);
        // Draw text in middle of raid bar
        enemiesRemainingFont.draw(batch, text,
                (float) (camera.position.x + (Gdx.graphics.getWidth() * 0.19) -
                        Math.round(camera.viewportWidth / 8.5)),
                (float) (camera.position.y + (Gdx.graphics.getHeight() * 0.0258) +
                        Math.round(camera.viewportHeight / 2.45)));

        /*
        // Draw placeholder raid bar
        Texture placeholderRaidBar = GameManager.get().getManager(TextureManager.class).getTexture(
                "placeholderRaidBar");
        batch.draw(placeholderRaidBar,
                camera.position.x - Math.round(camera.viewportWidth / 8.5),
                camera.position.y + Math.round(camera.viewportHeight / 2.355),
                (float) (Gdx.graphics.getWidth()/4.98),
                Gdx.graphics.getHeight()/13);
         */

    }

    /**
     * Renders the experience bar. Bar gets bigger the more exp the player gains.
     * Experience bar resets after a level up.
     *
     * @param batch - render exp bar onto
     * @param camera - camera
     */
    private void renderExpBar(SpriteBatch batch, OrthographicCamera camera) {
        int exp = ((PlayerAttributes) GameManager.get().getWorld()
                .getEntityById(GameManager.get().getPlayerEntityID())).getExp();
        int level = ((PlayerAttributes) GameManager.get().getWorld()
                .getEntityById(GameManager.get().getPlayerEntityID())).getLevel();
        int maxExp = 80 + (20 * level);

        if(level > 10){
            levelTexture = GameManager.get().getManager(TextureManager.class).getTexture("levelTexture2");
            //set different level texture upon level above 10
        }

        // Set texture region width
        int width = (int) ((expBarTexture.getWidth()) * (exp /(float)maxExp));
        expBar.setRegionWidth(width);


        // Draw exp bar
        batch.draw(expBar,
                camera.position.x - Math.round(camera.viewportWidth / 2.05),
                camera.position.y + Math.round(camera.viewportHeight / 4.65),
                (float) (Gdx.graphics.getWidth()/(5.87/(exp/(float)maxExp))),
                (float) (Gdx.graphics.getHeight()/12.95));

        // Set texture region for exp bar container
        int expWidth = (expBarContainerTexture.getWidth());
        expBarContainer.setRegionWidth(expWidth);

        // Draw exp bar container
        batch.draw(expBarContainer,
                camera.position.x - Math.round(camera.viewportWidth / 2.05),
                camera.position.y + Math.round(camera.viewportHeight / 4.65),
                (float) (Gdx.graphics.getWidth()/4.98), Gdx.graphics.getHeight()/13);

        /*float iconWidth = camera.viewportWidth / 24f;
        float iconHeight = camera.viewportWidth / 24f;

        batch.draw(levelTexture,camera.position.x - Math.round(camera.viewportWidth / 2.05),
                camera.position.y + Math.round(camera.viewportHeight / 4.65),iconWidth
                ,iconHeight );*/

        font.getData().setScale(1.5f);
        font.setColor(1, 1, 1, 1);
        String levelStr = Integer.toString(level);
        font.draw(batch, levelStr,camera.position.x - Math.round(camera.viewportWidth / 2.05) + 126,
                camera.position.y + Math.round(camera.viewportHeight / 4.65) + 37,220, Align.center,true);

    }

    /**
     * Renders the revive text and revive progress bar above dead players.
     *
     * @param batch batch to draw on
     */
    private void renderReviveText(SpriteBatch batch) {
        PlayerPeon entity;
        Collection<Integer> players = GameManager.get().getManager(NetworkManager.class).getUserEntities().values();
        float[] entityWorldCoord;
        String reviveText = "REVIVE";

        for (int player : players) {
            entity = (PlayerPeon) GameManager.get().getWorld().getEntityById(player);

            if (!reviveMap.containsKey(player)) {
                reviveMap.put(player, constructReviveBar());
            }

            ProgressBar reviveBar = reviveMap.get(player);

            if (!entity.isAlive()) {
                entityWorldCoord = WorldUtil.colRowToWorldCords(entity.getCol(), entity.getRow());
                entityWorldCoord[0] -= 20;
                entityWorldCoord[1] += 95;

                if (entity.getReviveCounter() != 0) {
                    reviveBar.setValue(entity.getReviveCounter());
                    reviveBar.setPosition(entityWorldCoord[0] - 6, entityWorldCoord[1] + 7);
                    reviveBar.draw(batch, 1);
                }

                reviveFont.draw(batch, reviveText, entityWorldCoord[0], entityWorldCoord[1]);
                arrowFont.draw(batch, Character.toString((char) 9660), entityWorldCoord[0] + 55, entityWorldCoord[1] - 25);
                reviveMap.replace(player, reviveBar);
            }
        }
    }

    /**
     * Constructs a revive bar.
     *
     * @return A revive bar
     */
    public ProgressBar constructReviveBar() {
        Pixmap pixmap =  new Pixmap(100, 20, Format.RGBA8888);
        pixmap.setColor(Color.RED);
        pixmap.fill();
        TextureRegionDrawable drawable = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
        pixmap.dispose();
        ProgressBarStyle progressBarStyle = new ProgressBarStyle();
        progressBarStyle.background = drawable;

        pixmap = new Pixmap(0, 20, Format.RGBA8888);
        pixmap.setColor(Color.GREEN);
        pixmap.fill();
        drawable = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
        pixmap.dispose();

        progressBarStyle.knob = drawable;

        pixmap = new Pixmap(100, 20, Format.RGBA8888);
        pixmap.setColor(Color.GREEN);
        pixmap.fill();
        drawable = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
        pixmap.dispose();

        progressBarStyle.knobBefore = drawable;

        return new ProgressBar(0, 5, 0.1f, false, progressBarStyle);
    }
}
