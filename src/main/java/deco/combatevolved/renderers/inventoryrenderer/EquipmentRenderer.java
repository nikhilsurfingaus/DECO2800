package deco.combatevolved.renderers.inventoryrenderer;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import deco.combatevolved.entities.dynamicentities.PlayerAttributes;
import deco.combatevolved.entities.items.EquipmentSlots;
import deco.combatevolved.managers.*;

public class EquipmentRenderer extends InventoryPanelRenderer {

    // Equipments of player
    private EquipmentSlots playerEquipments;
    // Boundaries for the equipment panel
    private Rectangle equipmentBoundaries;
    // Boundaries for the weapon slot
    private Rectangle weaponBoundaries;
    // Boundaries for the helmet slot
    private Rectangle helmetBoundaries;
    // Boundaries for the armour slot
    private Rectangle armourBoundaries;
    // Boundaries for the shoes slot
    private Rectangle shoesBoundaries;

    private PlayerAttributes player;

    public EquipmentRenderer(Texture invSlot, BitmapFont font, GlyphLayout text, PlayerAttributes player) {
        super(invSlot, font, text, "equipmentPanel");
        this.player = player;
        this.playerEquipments = player.getEquipmentSlots();
    }

    @Override
    public boolean contains(Vector2 position) {
        return display && equipmentBoundaries.contains(position);
    }

    @Override
    public int clickIndex(Vector2 cameraPosition) {
        if (weaponBoundaries.contains(cameraPosition)) {
            return 0;
        } else if (helmetBoundaries.contains(cameraPosition)) {
            return 1;
        } else if (armourBoundaries.contains(cameraPosition)) {
            return 2;
        } else if (shoesBoundaries.contains(cameraPosition)) {
            return 3;
        }
        return -1;
    }

    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {
        // Calculate equipment boundaries
        equipmentBoundaries = new Rectangle(
                Math.round(camera.position.x + (((float) ROWS / 2) + 1.4) * invSlot.getWidth()),
                Math.round(camera.position.y - (((float) COLUMNS / 2) - 5) * invSlot.getHeight()),
                panelTexture.getWidth(), panelTexture.getHeight());

        int equipmentSlotWidth = invSlot.getWidth();
        int equipmentSlotHeight = invSlot.getHeight();

        // Calculate weapon boundaries
        weaponBoundaries = new Rectangle(
                Math.round(camera.position.x + (((float) ROWS / 2) + 4.4) * invSlot.getWidth()),
                Math.round(camera.position.y - (((float) COLUMNS / 2) - 7.33) * invSlot.getHeight()),
                equipmentSlotWidth, equipmentSlotHeight);

        // Calculate helmet boundaries
        helmetBoundaries = new Rectangle(
                Math.round(camera.position.x + (((float) ROWS / 2) + 1.7) * invSlot.getWidth()),
                Math.round(camera.position.y - (((float) COLUMNS / 2) - 7.33) * invSlot.getHeight()),
                equipmentSlotWidth, equipmentSlotHeight);

        // Calculate armour boundaries
        armourBoundaries = new Rectangle(
                Math.round(camera.position.x + (((float) ROWS / 2) + 1.7) * invSlot.getWidth()),
                Math.round(camera.position.y - (((float) COLUMNS / 2) - 6.25) * invSlot.getHeight()),
                equipmentSlotWidth, equipmentSlotHeight);

        // Calculate shoes boundaries
        shoesBoundaries = new Rectangle(Math.round(camera.position.x + (((float) ROWS / 2) + 1.7) * invSlot.getWidth()),
                Math.round(camera.position.y - (((float) COLUMNS / 2) - 5.15) * invSlot.getHeight()),
                equipmentSlotWidth, equipmentSlotHeight);

        if (display) {
            // Draw equipment panel
            batch.draw(panelTexture, Math.round(camera.position.x + (((float) ROWS / 2) + 1.4) * invSlot.getWidth()),
                    Math.round(camera.position.y - (((float) COLUMNS / 2) - 5) * invSlot.getHeight()));

            // Draw player
            batch.draw(GameManager.get().getManager(TextureManager.class).getTexture(this.player.getPlayerTexture()),
                    Math.round(camera.position.x + (((float) ROWS / 2) + 2.3) * invSlot.getWidth()),
                    Math.round(camera.position.y - (((float) COLUMNS / 2) - 5.32) * invSlot.getHeight()), 140, 160);

            // Draw player's attributes

            text.setText(font, Integer.toString(player.getLevel()));
            font.draw(batch, text, camera.position.x + Math.round((((float) ROWS / 2) + 5.25) * invSlot.getWidth()),
                    camera.position.y - Math.round((((float) COLUMNS / 2) - 7) * invSlot.getHeight()));

            text.setText(font, Integer.toString(player.getStats("attack")));
            font.draw(batch, text, camera.position.x + Math.round((((float) ROWS / 2) + 5.3) * invSlot.getWidth()),
                    camera.position.y - Math.round((((float) COLUMNS / 2) - 6.51) * invSlot.getHeight()));

            text.setText(font, Integer.toString(player.getStats("defence")));
            font.draw(batch, text, camera.position.x + Math.round((((float) ROWS / 2) + 5.3) * invSlot.getWidth()),
                    camera.position.y - Math.round((((float) COLUMNS / 2) - 5.97) * invSlot.getHeight()));

            text.setText(font, Integer.toString(player.getStats("speed")));
            font.draw(batch, text, camera.position.x + Math.round((((float) ROWS / 2) + 5.3) * invSlot.getWidth()),
                    camera.position.y - Math.round((((float) COLUMNS / 2) - 5.43) * invSlot.getHeight()));

            // Draw equipments on the panel
            if (playerEquipments.getWeapon() != null) {
                Texture weaponTexture = GameManager.get().getManager(TextureManager.class)
                        .getTexture(playerEquipments.getWeapon().getTexture());
                
                batch.draw(weaponTexture,
                        Math.round(camera.position.x + (((float) ROWS / 2) + 4.4) * invSlot.getWidth()),
                        Math.round(camera.position.y - (((float) COLUMNS / 2) - 7.33) * invSlot.getHeight()),
                        equipmentSlotWidth - 5, equipmentSlotHeight - 5);
                
            }

            if (playerEquipments.getHelmet() != null) {
                Texture helmetTexture = GameManager.get().getManager(TextureManager.class)
                        .getTexture(playerEquipments.getHelmet().getTexture());
                batch.draw(helmetTexture,
                        Math.round(camera.position.x + (((float) ROWS / 2) + 1.7) * invSlot.getWidth()),
                        Math.round(camera.position.y - (((float) COLUMNS / 2) - 7.33) * invSlot.getHeight()),
                        equipmentSlotWidth, equipmentSlotHeight);
            }
            if (playerEquipments.getArmour() != null) {
                Texture armourTexture = GameManager.get().getManager(TextureManager.class)
                        .getTexture(playerEquipments.getArmour().getTexture());
                batch.draw(armourTexture,
                        Math.round(camera.position.x + (((float) ROWS / 2) + 1.7) * invSlot.getWidth()),
                        Math.round(camera.position.y - (((float) COLUMNS / 2) - 6.25) * invSlot.getHeight()),
                        equipmentSlotWidth, equipmentSlotHeight);
            }

            if (playerEquipments.getShoes() != null) {
                Texture shoesTexture = GameManager.get().getManager(TextureManager.class)
                        .getTexture(playerEquipments.getShoes().getTexture());
                batch.draw(shoesTexture,
                        Math.round(camera.position.x + (((float) ROWS / 2) + 1.7) * invSlot.getWidth()),
                        Math.round(camera.position.y - (((float) COLUMNS / 2) - 5.15) * invSlot.getHeight()),
                        equipmentSlotWidth, equipmentSlotHeight);
            }
        }
    }

}