package deco.combatevolved.renderers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.utils.Align;
import deco.combatevolved.entities.dynamicentities.*;
import deco.combatevolved.managers.CameraManager;
import deco.combatevolved.managers.GameManager;
import deco.combatevolved.managers.InputManager;
import deco.combatevolved.managers.TextureManager;
import deco.combatevolved.observers.TouchDownObserver;
import deco.combatevolved.util.WorldUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Class that renders the skill tree on an appropriate action by the
 * user
 */
public class SkillTreeRenderer implements Renderer, TouchDownObserver {

    private boolean displaySkillTree = false;
    private PlayerAttributes player;
    private SkillTreev2 playerSkills;
    private Sprite background = new Sprite(GameManager.get().getManager(TextureManager.class).getTexture("skillsBackground"));

    private Sprite attackDamage1 = new Sprite(GameManager.get().getManager(TextureManager.class).getTexture("attackDamage1"));
    private Sprite attackDamage2 = new Sprite(GameManager.get().getManager(TextureManager.class).getTexture("attackDamage2"));
    private Sprite attackDamage3 = new Sprite(GameManager.get().getManager(TextureManager.class).getTexture("attackDamage3"));
    private Sprite attackDamage4 = new Sprite(GameManager.get().getManager(TextureManager.class).getTexture("attackDamage4"));
    private Sprite attackMortalReminder = new Sprite(GameManager.get().getManager(TextureManager.class).getTexture("attackMortalReminder"));
    private Sprite attackDoubleEdged = new Sprite(GameManager.get().getManager(TextureManager.class).getTexture("attackDoubleEdged"));
    private Sprite attackExplosion = new Sprite(GameManager.get().getManager(TextureManager.class).getTexture("attackExplosion"));
    private Sprite attackFireTouch = new Sprite(GameManager.get().getManager(TextureManager.class).getTexture("attackFireTouch"));
    private Sprite attackFlameTrail = new Sprite(GameManager.get().getManager(TextureManager.class).getTexture("attackFlameTrail"));
    private Sprite attackMoreExplosion = new Sprite(GameManager.get().getManager(TextureManager.class).getTexture("attackMoreExplosion"));
    private Sprite defenceHealth1 = new Sprite(GameManager.get().getManager(TextureManager.class).getTexture("defenceHealth1"));
    private Sprite defenceHealth2 = new Sprite(GameManager.get().getManager(TextureManager.class).getTexture("defenceHealth2"));
    private Sprite defenceHealth3 = new Sprite(GameManager.get().getManager(TextureManager.class).getTexture("defenceHealth3"));
    private Sprite defenceHealth4 = new Sprite(GameManager.get().getManager(TextureManager.class).getTexture("defenceHealth4"));
    private Sprite defenceAdvancedHealing = new Sprite(GameManager.get().getManager(TextureManager.class).getTexture("defenceAdvancedHealing"));
    private Sprite defenceApothecary = new Sprite(GameManager.get().getManager(TextureManager.class).getTexture("defenceApothecary"));
    private Sprite defenceUnkillable = new Sprite(GameManager.get().getManager(TextureManager.class).getTexture("defenceUnkillable"));
    private Sprite defenceThickArmour = new Sprite(GameManager.get().getManager(TextureManager.class).getTexture("defenceThickArmour"));
    private Sprite defenceThornArmour = new Sprite(GameManager.get().getManager(TextureManager.class).getTexture("defenceThornArmour"));
    private Sprite defenceGuardian = new Sprite(GameManager.get().getManager(TextureManager.class).getTexture("defenceGuardian"));
    private Sprite utilityTowerUpgrade1 = new Sprite(GameManager.get().getManager(TextureManager.class).getTexture("utilityTowerUpgrade1"));
    private Sprite utilityTowerUpgrade2 = new Sprite(GameManager.get().getManager(TextureManager.class).getTexture("utilityTowerUpgrade2"));
    private Sprite utilityTowerUpgrade3 = new Sprite(GameManager.get().getManager(TextureManager.class).getTexture("utilityTowerUpgrade3"));
    private Sprite utilityTowerUpgrade4 = new Sprite(GameManager.get().getManager(TextureManager.class).getTexture("utilityTowerUpgrade4"));
    private Sprite utilityEfficiency = new Sprite(GameManager.get().getManager(TextureManager.class).getTexture("utilityEfficiency"));
    private Sprite utilityEndlessItems = new Sprite(GameManager.get().getManager(TextureManager.class).getTexture("utilityEndlessItems"));
    private Sprite utilityFortune = new Sprite(GameManager.get().getManager(TextureManager.class).getTexture("utilityFortune"));
    private Sprite utilityShootFaster = new Sprite(GameManager.get().getManager(TextureManager.class).getTexture("utilityShootFaster"));
    private Sprite utilityLaserBullets = new Sprite(GameManager.get().getManager(TextureManager.class).getTexture("utilityLaserBullets"));
    private Sprite utilityStickyBullets = new Sprite(GameManager.get().getManager(TextureManager.class).getTexture("utilityStickyBullets"));

    private Sprite lockedSkill = new Sprite(GameManager.get().getManager(TextureManager.class).getTexture("lockedSkill"));

    private BitmapFont titleFont = new BitmapFont(Gdx.files.internal("resources/fonts/Space Frigate/spacefri.fnt"));
    private BitmapFont font = new BitmapFont(Gdx.files.internal("resources/fonts/Red Hat Display/RedHatDisplay-Regular.fnt"));
    private Texture invDesBackground = GameManager.get().getManager(TextureManager.class).getTexture("invDesBackground");

    private HashMap<String, Rectangle> skillSpriteMap;

    private Dialog dialog = new Dialog("Warning", GameManager.get().getSkin());
    private Dialog dialog2 = new Dialog("Warning", GameManager.get().getSkin());
    private Dialog dialog3 = new Dialog("Warning", GameManager.get().getSkin());
    private Stage stage;

    public void create() {
        player = (PlayerAttributes) GameManager.get().getWorld().getEntityById(GameManager.get().getPlayerEntityID());
        playerSkills = player.getPlayerSkills();

        GameManager.getManagerFromInstance(InputManager.class).addTouchDownListener(this);

        skillSpriteMap = new HashMap<>();
        // Sets the warning pop ups
        font.getData().setScale(0.5f);
        font.setColor(1, 1, 1, 1);
        dialog.text("You don't enough skill points!");
        dialog.button("OK");
        dialog2.text("You have already learnt this skill!");
        dialog2.button("OK");
        dialog3.text("You don't have sufficient level to learn this skill");
        dialog3.button("OK");
        // Sets the opacity of the locked skill icon as it will be rendered on top of the skills
        lockedSkill.setColor(lockedSkill.getColor().r, lockedSkill.getColor().g, lockedSkill.getColor().b, 0.65f);

        this.stage = GameManager.get().getStage();
    }


    /**
     * Renders the skill tree
     * @param batch - batch
     * @param camera - camera
     */
    public void render(SpriteBatch batch, OrthographicCamera camera) {
        if (displaySkillTree) { // Checks if the toggle is on
            float iconWidth = camera.viewportWidth / 24f;
            float iconHeight = camera.viewportWidth / 24f;
            // Draws the skill tree background sprite
            batch.begin();
            background.setBounds(camera.position.x - camera.viewportWidth / 4,
                    camera.position.y - (camera.viewportHeight / 2 - iconHeight * 0.5f),
                    camera.viewportWidth / 2, camera.viewportHeight - iconHeight * 1f);
            background.draw(batch);
            batch.end();

            // For drawing lines between each skill icon
            ShapeRenderer sr = new ShapeRenderer();
            sr.setProjectionMatrix(camera.combined);
            sr.begin(ShapeRenderer.ShapeType.Line);
            Gdx.gl.glLineWidth(5);
            sr.setColor(Color.BLACK);
            // Lines for attack skill tree
            sr.line((camera.position.x), (camera.position.y - (camera.viewportHeight / 2f - iconHeight)),
                    (camera.position.x), (camera.position.y - (camera.viewportHeight / 2f - iconHeight * 5.5f)));
                // Draws lines for the left side
            sr.line(camera.position.x, camera.position.y - (camera.viewportHeight / 2f - iconHeight * 1.5f),
                    camera.position.x - iconWidth * 2.25f, camera.position.y - (camera.viewportHeight / 2f - iconHeight * 2.75f));
            sr.line(camera.position.x - iconWidth * 1.9f, camera.position.y - (camera.viewportHeight / 2f - iconHeight * 2.75f),
                    camera.position.x - iconWidth * 2f, camera.position.y - (camera.viewportHeight / 2f - iconHeight * 4f));
            sr.line(camera.position.x - iconWidth * 2f, camera.position.y - (camera.viewportHeight / 2f - iconHeight * 4.5f),
                    camera.position.x, camera.position.y - (camera.viewportHeight / 2f - iconHeight * 6f));
                // Draws lines for the right side
            sr.line(camera.position.x, camera.position.y - (camera.viewportHeight / 2f - iconHeight * 1.5f),
                    camera.position.x + iconWidth * 1.5f, camera.position.y - (camera.viewportHeight / 2f - iconHeight * 2.25f));
            sr.line(camera.position.x + iconWidth * 1.5f, camera.position.y - (camera.viewportHeight / 2f - iconHeight * 2.25f),
                    camera.position.x + iconWidth * 2.75f, camera.position.y - (camera.viewportHeight / 2f - iconHeight * 3.75f));
            sr.line(camera.position.x + iconWidth * 1.5f, camera.position.y - (camera.viewportHeight / 2f - iconHeight * 2.25f),
                    camera.position.x + iconWidth * 1.5f, camera.position.y - (camera.viewportHeight / 2f - iconHeight * 3.5f));
            sr.line(camera.position.x + iconWidth * 1.25f, camera.position.y - (camera.viewportHeight / 2f - iconHeight * 3.75f),
                    camera.position.x + iconWidth * 2f, camera.position.y - (camera.viewportHeight / 2f - iconHeight * 5.25f));
            sr.line(camera.position.x + iconWidth * 3f, camera.position.y - (camera.viewportHeight / 2f - iconHeight * 4f),
                    camera.position.x + iconWidth * 2f, camera.position.y - (camera.viewportHeight / 2f - iconHeight * 5.25f));
            sr.line(camera.position.x + iconWidth * 2f, camera.position.y - (camera.viewportHeight / 2f - iconHeight * 5.5f),
                    camera.position.x, camera.position.y - (camera.viewportHeight / 2f - iconHeight * 6f));

            // Lines for defence skill tree
            sr.line(camera.position.x - iconWidth * 4.75f, camera.position.y - (camera.viewportHeight / 2f - iconHeight * 10.5f),
                    camera.position.x - iconWidth * 1f, camera.position.y - (camera.viewportHeight / 2f - iconHeight * 7.5f));
                // Right side
            sr.line(camera.position.x - iconWidth * 4.75f, camera.position.y - (camera.viewportHeight / 2f - iconHeight * 10.5f),
                    camera.position.x - iconWidth * 4.75f, camera.position.y - (camera.viewportHeight / 2f - iconHeight * 8.75f));
            sr.line(camera.position.x - iconWidth * 4.75f, camera.position.y - (camera.viewportHeight / 2f - iconHeight * 8.75f),
                    camera.position.x - iconWidth * 4f, camera.position.y - (camera.viewportHeight / 2f - iconHeight * 7f));
            sr.line(camera.position.x - iconWidth * 4f, camera.position.y - (camera.viewportHeight / 2f - iconHeight * 7.15f),
                    camera.position.x - iconWidth * 2.5f, camera.position.y - (camera.viewportHeight / 2f - iconHeight * 6.5f));
            sr.line(camera.position.x - iconWidth * 2.5f, camera.position.y - (camera.viewportHeight / 2f - iconHeight * 6.5f),
                    camera.position.x - iconWidth * 1f, camera.position.y - (camera.viewportHeight / 2f - iconHeight * 7.5f));
                // Left side
            sr.line(camera.position.x - iconWidth * 4.75f, camera.position.y - (camera.viewportHeight / 2f - iconHeight * 10.5f),
                    camera.position.x - iconWidth * 3f, camera.position.y - (camera.viewportHeight / 2f - iconHeight * 10.75f));
            sr.line(camera.position.x - iconWidth * 3f, camera.position.y - (camera.viewportHeight / 2f - iconHeight * 10.75f),
                    camera.position.x - iconWidth * 1.25f, camera.position.y - (camera.viewportHeight / 2f - iconHeight * 10.5f));
            sr.line(camera.position.x - iconWidth * 1.25f, camera.position.y - (camera.viewportHeight / 2f - iconHeight * 10.5f),
                    camera.position.x - iconWidth * 0.5f, camera.position.y - (camera.viewportHeight / 2f - iconHeight * 9f));
            sr.line(camera.position.x - iconWidth * 0.5f, camera.position.y - (camera.viewportHeight / 2f - iconHeight * 9f),
                    camera.position.x - iconWidth * 1f, camera.position.y - (camera.viewportHeight / 2f - iconHeight * 7.5f));

            // Lines for utility skill tree
            sr.line(camera.position.x + iconWidth * 4.75f, camera.position.y - (camera.viewportHeight / 2f - iconHeight * 10.5f),
                    camera.position.x + iconWidth * 1f, camera.position.y - (camera.viewportHeight / 2f - iconHeight * 7.5f));

            sr.line(camera.position.x + iconWidth * 4.75f, camera.position.y - (camera.viewportHeight / 2f - iconHeight * 10.5f),
                    camera.position.x + iconWidth * 3.25f, camera.position.y - (camera.viewportHeight / 2f - iconHeight * 11f));
            sr.line(camera.position.x + iconWidth * 3.25f, camera.position.y - (camera.viewportHeight / 2f - iconHeight * 11f),
                    camera.position.x + iconWidth * 1f, camera.position.y - (camera.viewportHeight / 2f - iconHeight * 10.75f));
            sr.line(camera.position.x + iconWidth * 3.25f, camera.position.y - (camera.viewportHeight / 2f - iconHeight * 11f),
                    camera.position.x + iconWidth * 1.75f, camera.position.y - (camera.viewportHeight / 2f - iconHeight * 9.75f));
            sr.line(camera.position.x + iconWidth * 1.75f, camera.position.y - (camera.viewportHeight / 2f - iconHeight * 9.75f),
                    camera.position.x + iconWidth * 1f, camera.position.y - (camera.viewportHeight / 2f - iconHeight * 7.5f));
            sr.line(camera.position.x + iconWidth * 1f, camera.position.y - (camera.viewportHeight / 2f - iconHeight * 10.75f),
                    camera.position.x + iconWidth * 1f, camera.position.y - (camera.viewportHeight / 2f - iconHeight * 7.5f));

            sr.line(camera.position.x + iconWidth * 4.75f, camera.position.y - (camera.viewportHeight / 2f - iconHeight * 10.5f),
                    camera.position.x + iconWidth * 5f, camera.position.y - (camera.viewportHeight / 2f - iconHeight * 8.75f));
            sr.line(camera.position.x + iconWidth * 5f, camera.position.y - (camera.viewportHeight / 2f - iconHeight * 8.75f),
                    camera.position.x + iconWidth * 4.25f, camera.position.y - (camera.viewportHeight / 2f - iconHeight * 7.25f));
            sr.line(camera.position.x + iconWidth * 4.25f, camera.position.y - (camera.viewportHeight / 2f - iconHeight * 7.25f),
                    camera.position.x + iconWidth * 2.75f, camera.position.y - (camera.viewportHeight / 2f - iconHeight * 7f));
            sr.line(camera.position.x + iconWidth * 2.75f, camera.position.y - (camera.viewportHeight / 2f - iconHeight * 7f),
                    camera.position.x + iconWidth * 1f, camera.position.y - (camera.viewportHeight / 2f - iconHeight * 7.5f));

            sr.end();

            batch.begin();
            // Draws each skill icon
            attackDamage1.setBounds(camera.position.x - iconWidth / 2,
                    camera.position.y - (camera.viewportHeight / 2f - iconHeight * 1f),
                    iconWidth, iconHeight);
            attackDamage1.draw(batch);
            attackDamage2.setBounds(camera.position.x - iconWidth / 2,
                    camera.position.y - (camera.viewportHeight / 2f - iconHeight * 2.5f),
                    iconWidth, iconHeight);
            attackDamage2.draw(batch);
            attackDamage3.setBounds(camera.position.x - iconWidth / 2,
                    camera.position.y - (camera.viewportHeight / 2f - iconHeight * 4f),
                    iconWidth, iconHeight);
            attackDamage3.draw(batch);
            attackDamage4.setBounds(camera.position.x - iconWidth / 2,
                    camera.position.y - (camera.viewportHeight / 2f - iconHeight * 5.5f),
                    iconWidth, iconHeight);
            attackDamage4.draw(batch);
            attackMortalReminder.setBounds(camera.position.x - iconWidth * 2.25f,
                    camera.position.y - (camera.viewportHeight / 2f - iconHeight * 2.25f),
                    iconWidth, iconHeight);
            attackMortalReminder.draw(batch);
            attackDoubleEdged.setBounds(camera.position.x - iconWidth * 2.5f,
                    camera.position.y - (camera.viewportHeight / 2f - iconHeight * 4f),
                    iconWidth, iconHeight);
            attackDoubleEdged.draw(batch);
            attackExplosion.setBounds(camera.position.x + iconWidth * 1f,
                    camera.position.y - (camera.viewportHeight / 2f - iconHeight * 2f),
                    iconWidth, iconHeight);
            attackExplosion.draw(batch);
            attackFireTouch.setBounds(camera.position.x + iconWidth * 1f,
                    camera.position.y - (camera.viewportHeight / 2f - iconHeight * 3.5f),
                    iconWidth, iconHeight);
            attackFireTouch.draw(batch);
            attackFlameTrail.setBounds(camera.position.x + iconWidth * 2.5f,
                    camera.position.y - (camera.viewportHeight / 2f - iconHeight * 3.5f),
                    iconWidth, iconHeight);
            attackFlameTrail.draw(batch);
            attackMoreExplosion.setBounds(camera.position.x + iconWidth * 1.5f,
                    camera.position.y - (camera.viewportHeight / 2f - iconHeight * 5f),
                    iconWidth, iconHeight);
            attackMoreExplosion.draw(batch);
            defenceHealth1.setBounds(camera.position.x - iconWidth * 5.25f,
                    camera.position.y - (camera.viewportHeight / 2f - iconHeight * 10f),
                    iconWidth, iconHeight);
            defenceHealth1.draw(batch);
            defenceHealth2.setBounds(camera.position.x - iconWidth * 4f,
                    camera.position.y - (camera.viewportHeight / 2f - iconHeight * 9f),
                    iconWidth, iconHeight);
            defenceHealth2.draw(batch);
            defenceHealth3.setBounds(camera.position.x - iconWidth * 2.75f,
                    camera.position.y - (camera.viewportHeight / 2f - iconHeight * 8f),
                    iconWidth, iconHeight);
            defenceHealth3.draw(batch);
            defenceHealth4.setBounds(camera.position.x - iconWidth * 1.5f,
                    camera.position.y - (camera.viewportHeight / 2f - iconHeight * 7f),
                    iconWidth, iconHeight);
            defenceHealth4.draw(batch);
            defenceAdvancedHealing.setBounds(camera.position.x - iconWidth * 5.25f,
                    camera.position.y - (camera.viewportHeight / 2f - iconHeight * 8.25f),
                    iconWidth, iconHeight);
            defenceAdvancedHealing.draw(batch);
            defenceApothecary.setBounds(camera.position.x - iconWidth * 4.5f,
                    camera.position.y - (camera.viewportHeight / 2f - iconHeight * 6.75f),
                    iconWidth, iconHeight);
            defenceApothecary.draw(batch);
            defenceUnkillable.setBounds(camera.position.x - iconWidth * 3f,
                    camera.position.y - (camera.viewportHeight / 2f - iconHeight * 6f),
                    iconWidth, iconHeight);
            defenceUnkillable.draw(batch);
            defenceThickArmour.setBounds(camera.position.x - iconWidth * 3.5f,
                    camera.position.y - (camera.viewportHeight / 2f - iconHeight * 10.25f),
                    iconWidth, iconHeight);
            defenceThickArmour.draw(batch);
            defenceThornArmour.setBounds(camera.position.x - iconWidth * 1.75f,
                    camera.position.y - (camera.viewportHeight / 2f - iconHeight * 10f),
                    iconWidth, iconHeight);
            defenceThornArmour.draw(batch);

            defenceGuardian.setBounds(camera.position.x - iconWidth * 1f,
                    camera.position.y - (camera.viewportHeight / 2f - iconHeight * 8.5f),
                    iconWidth, iconHeight);
            defenceGuardian.draw(batch);
            utilityTowerUpgrade1.setBounds(camera.position.x + iconWidth * 4.25f,
                    camera.position.y - (camera.viewportHeight / 2f - iconHeight * 10f),
                    iconWidth, iconHeight);
            utilityTowerUpgrade1.draw(batch);
            utilityTowerUpgrade2.setBounds(camera.position.x + iconWidth * 3f,
                    camera.position.y - (camera.viewportHeight / 2f - iconHeight * 9f),
                    iconWidth, iconHeight);
            utilityTowerUpgrade2.draw(batch);
            utilityTowerUpgrade3.setBounds(camera.position.x + iconWidth * 1.75f,
                    camera.position.y - (camera.viewportHeight / 2f - iconHeight * 8f),
                    iconWidth, iconHeight);
            utilityTowerUpgrade3.draw(batch);
            utilityTowerUpgrade4.setBounds(camera.position.x + iconWidth / 2,
                    camera.position.y - (camera.viewportHeight / 2f - iconHeight * 7f),
                    iconWidth, iconHeight);
            utilityTowerUpgrade4.draw(batch);
            utilityEfficiency.setBounds(camera.position.x + iconWidth * 2.75f,
                    camera.position.y - (camera.viewportHeight / 2f - iconHeight * 10.5f),
                    iconWidth, iconHeight);
            utilityEfficiency.draw(batch);
            utilityFortune.setBounds(camera.position.x + iconWidth * 0.5f,
                    camera.position.y - (camera.viewportHeight / 2f - iconHeight * 10.25f),
                    iconWidth, iconHeight);
            utilityFortune.draw(batch);
            utilityEndlessItems.setBounds(camera.position.x + iconWidth * 1.25f,
                    camera.position.y - (camera.viewportHeight / 2f - iconHeight * 9.25f),
                    iconWidth, iconHeight);
            utilityEndlessItems.draw(batch);
            utilityShootFaster.setBounds(camera.position.x + iconWidth * 4.5f,
                    camera.position.y - (camera.viewportHeight / 2f - iconHeight * 8.25f),
                    iconWidth, iconHeight);
            utilityShootFaster.draw(batch);
            utilityLaserBullets.setBounds(camera.position.x + iconWidth * 3.75f,
                    camera.position.y - (camera.viewportHeight / 2f - iconHeight * 6.75f),
                    iconWidth, iconHeight);
            utilityLaserBullets.draw(batch);
            utilityStickyBullets.setBounds(camera.position.x + iconWidth * 2.25f,
                    camera.position.y - (camera.viewportHeight / 2f - iconHeight * 6.5f),
                    iconWidth, iconHeight);
            utilityStickyBullets.draw(batch);

            // Puts all skill sprites' positions into the hash map
            skillSpriteMap.put("attackDamage1", attackDamage1.getBoundingRectangle());
            skillSpriteMap.put("attackDamage2", attackDamage2.getBoundingRectangle());
            skillSpriteMap.put("attackDamage3", attackDamage3.getBoundingRectangle());
            skillSpriteMap.put("attackDamage4", attackDamage4.getBoundingRectangle());
            skillSpriteMap.put("attackExplosion", attackExplosion.getBoundingRectangle());
            skillSpriteMap.put("attackMoreExplosion", attackMoreExplosion.getBoundingRectangle());
            skillSpriteMap.put("attackFlameTrail", attackFlameTrail.getBoundingRectangle());
            skillSpriteMap.put("attackMortalReminder", attackMortalReminder.getBoundingRectangle());
            skillSpriteMap.put("attackDoubleEdged", attackDoubleEdged.getBoundingRectangle());
            skillSpriteMap.put("attackFireTouch", attackFireTouch.getBoundingRectangle());
            skillSpriteMap.put("defenceHealth1", defenceHealth1.getBoundingRectangle());
            skillSpriteMap.put("defenceHealth2", defenceHealth2.getBoundingRectangle());
            skillSpriteMap.put("defenceHealth3", defenceHealth3.getBoundingRectangle());
            skillSpriteMap.put("defenceHealth4", defenceHealth4.getBoundingRectangle());
            skillSpriteMap.put("defenceThickArmour", defenceThickArmour.getBoundingRectangle());
            skillSpriteMap.put("defenceThornArmour", defenceThornArmour.getBoundingRectangle());
            skillSpriteMap.put("defenceGuardian", defenceGuardian.getBoundingRectangle());
            skillSpriteMap.put("defenceAdvancedHealing", defenceAdvancedHealing.getBoundingRectangle());
            skillSpriteMap.put("defenceApothecary", defenceApothecary.getBoundingRectangle());
            skillSpriteMap.put("defenceUnkillable", defenceUnkillable.getBoundingRectangle());
            skillSpriteMap.put("utilityTowerUpgrade1", utilityTowerUpgrade1.getBoundingRectangle());
            skillSpriteMap.put("utilityTowerUpgrade2", utilityTowerUpgrade2.getBoundingRectangle());
            skillSpriteMap.put("utilityTowerUpgrade3", utilityTowerUpgrade3.getBoundingRectangle());
            skillSpriteMap.put("utilityTowerUpgrade4", utilityTowerUpgrade4.getBoundingRectangle());
            skillSpriteMap.put("utilityShootFaster", utilityShootFaster.getBoundingRectangle());
            skillSpriteMap.put("utilityLaserBullets", utilityLaserBullets.getBoundingRectangle());
            skillSpriteMap.put("utilityStickyBullets", utilityStickyBullets.getBoundingRectangle());
            skillSpriteMap.put("utilityEfficiency", utilityEfficiency.getBoundingRectangle());
            skillSpriteMap.put("utilityEndlessItems", utilityEndlessItems.getBoundingRectangle());
            skillSpriteMap.put("utilityFortune", utilityFortune.getBoundingRectangle());

            // Draws the "Skills" page title
            titleFont.getData().setScale(1f);
            titleFont.setColor(72,72,72,1);
            titleFont.draw(batch, "Skills", camera.position.x - iconWidth,
                    camera.position.y - (camera.viewportHeight / 2 - iconHeight * 12.6f),
                    200,  Align.left, true);
            // Draws the skill point availability
            font.getData().setScale(0.6f);
            font.setColor(72,72,72,1);
            font.draw(batch, "Available Skill Points  " + player.getSkillPoint(), camera.position.x - iconWidth * 1.35f,
                    camera.position.y - (camera.viewportHeight / 2 - iconHeight * 12),
                    200,  Align.left, true);

            // Draws locks on locked skills
            for (Map.Entry<String, Rectangle> entry : skillSpriteMap.entrySet()) {
                if (!playerSkills.hasLearnt(playerSkills.getSkill(entry.getKey()))) {
                    lockedSkill.setBounds(entry.getValue().getX(), entry.getValue().getY(), iconWidth, iconHeight);
                    lockedSkill.draw(batch);
                }
            }

            // Displays the skill's description when hovered
            for (Map.Entry<String, Rectangle> entry : skillSpriteMap.entrySet()) {

                float[] mouse = WorldUtil.screenToWorldCoordinates(Gdx.input.getX(), Gdx.input.getY());
                // Checks if mouse is hovered over a skill icon
                if (entry.getValue().contains(mouse[0], mouse[1])) {

                    Sprite sprite = new Sprite(invDesBackground);
                    float descPositionY = entry.getValue().getY() + iconHeight;
                    int descHeight = 100;
                    // If skill description is too long, increase height of background sprite
                    if (playerSkills.getSkill(entry.getKey()).getDescription().length() > 40) {
                        descPositionY = descPositionY - 70;
                        descHeight = 170;
                    }
                    sprite.setBounds(entry.getValue().getX() + iconWidth, descPositionY, 250, descHeight);
                    float positionX = entry.getValue().getX() + iconWidth;
                    float positionY = entry.getValue().getY() + iconHeight;
                    sprite.draw(batch);
                    renderSkillDescription(entry.getKey(), batch, positionX, positionY);
                }
            }
            batch.end();
        }
    }

    /**
     * Toggles the skill tree display
     */
    public void toggleSkillTree() {
        displaySkillTree = !displaySkillTree;

        player = (PlayerAttributes) GameManager.get().getWorld().getEntityById(GameManager.get().getPlayerEntityID());

        if(displaySkillTree) {
            GameManager.get().getManager(CameraManager.class).setMouseCameraMove(false);
        } else {
            GameManager.get().getManager(CameraManager.class).setMouseCameraMove(true);
        }
    }

    /**
     * Renders a skill's name and description next to the relative sprite
     * @param skillName - Name of skill
     * @param batch - sprite batch
     * @param positionX - Position X of skill sprite
     * @param positionY - Position Y of skill sprite
     */
    private void renderSkillDescription(String skillName, SpriteBatch batch, float positionX, float positionY) {
        SkillNode skill = playerSkills.getSkill(skillName);
        // Sets the font
        font.getData().setScale(0.8f);
        font.setColor(1, 1, 1, 1);

        // Draws the skill name
        font.draw(batch, skill.getName(), positionX + 15, positionY + 85, 220, Align.left, true);

        // Draws the skill description
        font.getData().setScale(0.6f);
        font.draw(batch, skill.getDescription(), positionX + 15, positionY + 50, 220, Align.left, true);

    }

    @Override
    public void notifyTouchDown(int screenX, int screenY, int pointer, int button) {
        float[] mouse = WorldUtil.screenToWorldCoordinates(Gdx.input.getX(), Gdx.input.getY());

        if (displaySkillTree) {
            for (Map.Entry<String, Rectangle> entry : skillSpriteMap.entrySet()) {
                int levelRequirement = playerSkills.getSkill(entry.getKey()).getLevelRequirement();
                if (entry.getValue().contains(mouse[0], mouse[1]) && player.getLevel() < levelRequirement) {
                    dialog3.show(stage);
                } else {
                    if (entry.getValue().contains(mouse[0], mouse[1]) && player.getSkillPoint() > 0) {
                        SkillNode skillToLearn = playerSkills.getSkill(entry.getKey());
                        if (!playerSkills.hasLearnt(skillToLearn)) {
                            playerSkills.learnSkill(skillToLearn);
                        } else {
                            dialog2.show(stage);
                        }
                    } else if (entry.getValue().contains(mouse[0], mouse[1]) && player.getSkillPoint() == 0) {
                        dialog.show(stage);
                    }
                }
            }
        }
    }
}
