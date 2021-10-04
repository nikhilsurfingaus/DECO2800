package deco.combatevolved.renderers.inventoryrenderer;

import java.util.*;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import deco.combatevolved.entities.items.Recycle;
import deco.combatevolved.managers.*;

public class RecycleRenderer extends InventoryPanelRenderer {

    //Recycle of player
    private Recycle playerRecycle;
    //Boundaries for the recycle panel
    private Rectangle recycleBoundaries;
    //Boundaries for the recycle convert button
    private Rectangle convertBoundaries;
    //Boundaries for the recycle input slot
    private Rectangle inputBoundaries;

    public RecycleRenderer(Texture invSlot, BitmapFont font, GlyphLayout text, Recycle playerRecycle) {
        super(invSlot, font, text, "recycle");
        this.playerRecycle = playerRecycle;
    }

	@Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {
        text.setText(font, "Recycle");
        
        //Calculate recycle boundaries
        recycleBoundaries = new Rectangle(camera.position.x + (((float)ROWS / 2) + 1) * invSlot.getWidth(),
                camera.position.y - ((float)COLUMNS / 2) * invSlot.getHeight(),
        		panelTexture.getWidth(),
        		panelTexture.getHeight());
        
        //Calculate convert boundaries
        convertBoundaries = new Rectangle(camera.position.x + (((float)ROWS / 2) + 1) * invSlot.getWidth() + 69,
                camera.position.y - ((float)COLUMNS / 2) * invSlot.getHeight() + 21,
        		145, 40);
        
        //Calculate input boundaries
        inputBoundaries = new Rectangle(Math.round(camera.position.x +
                ((((float)ROWS / 2) + 1.5) + 0.1) * invSlot.getWidth()),
                Math.round(camera.position.y -
                        ((((float)COLUMNS / 2) - 2.8) + 0.9) * invSlot.getHeight()),
                ITEM_DIM, ITEM_DIM);
        
        if (display) {
            batch.draw(panelTexture,
					camera.position.x + (((float)ROWS / 2) + 1) * invSlot.getWidth(),
                    camera.position.y - ((float)COLUMNS / 2) * invSlot.getHeight());

            font.draw(batch, text,
					camera.position.x + Math.round((((float) ROWS / 2) + 3.1) * invSlot.getWidth()),
                    camera.position.y - Math.round((((float)COLUMNS / 2) - 3.2) * invSlot.getHeight()));


            //Render recycle slots
            batch.draw(invSlot,
					Math.round(camera.position.x + (((float)ROWS / 2) + 1.5) * invSlot.getWidth()),
                    Math.round(camera.position.y - (((float) COLUMNS / 2) - 1.8) * invSlot.getHeight()));
            batch.draw(invSlot,
					Math.round(camera.position.x + (((float)ROWS / 2) + 4.6) * invSlot.getWidth()),
                    Math.round(camera.position.y - (((float)COLUMNS / 2) - 1.8) * invSlot.getHeight()));

            if (playerRecycle.calculate() != 0) {
                Texture inputItem = GameManager.get().getManager(TextureManager.class)
                        .getTexture(playerRecycle.getStack(0).getItem().getTexture());
                //Render items into input slots
                batch.draw(inputItem,
						Math.round(camera.position.x +
								((((float)ROWS / 2) + 1.5) + 0.1) * invSlot.getWidth()),
                        Math.round(camera.position.y -
								((((float)COLUMNS / 2) - 2.8) + 0.9) * invSlot.getHeight()),
                        ITEM_DIM, ITEM_DIM);
                String inputQuantity = "x" + playerRecycle.getStack(0).getNumItems();
                font.draw(batch, inputQuantity,
						Math.round(camera.position.x +
								((((float)ROWS / 2) + 1.5) + 0.4) * invSlot.getWidth()),
                        Math.round(camera.position.y -
								((((float)COLUMNS / 2) - 2.8) + 0.6) * invSlot.getHeight()));

                Texture outputItem = GameManager.get().getManager(TextureManager.class)
                		.getTexture("scrap");
                //Render items into output slots
                batch.draw(outputItem,
                        Math.round(camera.position.x +
								((((float) ROWS / 2) + 1.5) + 3.2) * invSlot.getWidth()),
                        Math.round(camera.position.y -
								((((float) COLUMNS / 2) - 2.8) + 0.9) * invSlot.getHeight()),
                        invSlot.getWidth() - 10, invSlot.getHeight() - 10);

                //Render quantity to output slot
                String outputQuantity = "x" + Integer.toString(playerRecycle.calculate());
                font.draw(batch, outputQuantity,
                        Math.round(camera.position.x +
								((((float) ROWS / 2) + 1.5) + 3.6) * invSlot.getWidth()),
                        Math.round(camera.position.y -
								((((float) COLUMNS / 2) - 2.8) + 0.6) * invSlot.getHeight()));
            }
        }

    }

	@Override
    public boolean contains(Vector2 position) {
        return display && recycleBoundaries.contains(position);
    }

    public boolean convert(Vector2 position) {
        return display && convertBoundaries.contains(position);
    }

	@Override
    public int clickIndex(Vector2 cameraPosition) {
        if (inputBoundaries.contains(cameraPosition)) {
            return 0;
        }
        return -1;
    }

}