package deco.combatevolved.renderers;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import deco.combatevolved.entities.AbstractEntity;
import deco.combatevolved.entities.dynamicentities.PlayerPeon;
import deco.combatevolved.entities.enemyentities.BasicEnemy;
import deco.combatevolved.entities.enemyentities.HealerEnemy;
import deco.combatevolved.entities.staticentities.defensivetowers.Tower;
import deco.combatevolved.managers.NetworkManager;
import deco.combatevolved.tasks.MeleeAttackTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import deco.combatevolved.entities.dynamicentities.AgentEntity;
import deco.combatevolved.entities.items.Inventory;
import deco.combatevolved.entities.staticentities.ControlTowers;
import deco.combatevolved.entities.staticentities.ItemEntity;
import deco.combatevolved.entities.staticentities.StaticEntity;
import deco.combatevolved.managers.GameManager;
import deco.combatevolved.managers.InputManager;
import deco.combatevolved.managers.TextureManager;
import deco.combatevolved.tasks.AbstractTask;
import deco.combatevolved.tasks.MovementTask;
import deco.combatevolved.util.HexVector;
import deco.combatevolved.util.Vector2;
import deco.combatevolved.util.WorldUtil;
import deco.combatevolved.worlds.Tile;

/**
 * A ~simple~ complex hex renderer for DECO2800 games
 * 
 */
public class Renderer3D implements Renderer {

	@SuppressWarnings("unused")
	private final Logger logger = LoggerFactory.getLogger(Renderer3D.class);

	private BitmapFont font;
	
	//mouse cursor
	private static final String TEXTURE_SELECTION = "selection";
	private static final String TEXTURE_DESTINATION = "selection";
	private static final String TEXTURE_PATH = "path";

	private int tilesSkipped = 0;

	private TextureManager textureManager = GameManager.getManagerFromInstance(TextureManager.class);

	/**
	 * Renders onto a batch, given a renderables with entities It is expected
	 * that AbstractWorld contains some entities and a Map to read tiles from
	 * 
	 * @param batch
	 *            Batch to render onto
	 */
	@Override
	public void render(SpriteBatch batch, OrthographicCamera camera) {
		if (font == null) {
			font = new BitmapFont();
			font.getData().setScale(1f);
		}

		// Adds a delay to the client rendering if all data hasn't been received
		if (!GameManager.get().getManager(NetworkManager.class).getClientLoadStatus()) {
			logger.info("Render3D is waiting for tile data");
			try	{
				Thread.sleep(1000);
			} catch(InterruptedException e) {
				logger.error("Error sleeping thread", e);
				Thread.currentThread().interrupt();
			}  	
		}

		// Render tiles onto the map
		List<Tile> tileMap = GameManager.get().getWorld().getTileListFromMap();
		List<Tile> tilesToBeSkipped = new ArrayList<>();
				
		batch.begin();
		// Render elements section by section
		//	tiles will render the static entity attaced to each tile after the tile is rendered

		tilesSkipped = 0;
		for (Tile t: tileMap) {
			// Render each tile
			renderTile(batch, camera, tileMap, tilesToBeSkipped, t);

			// Render each undiscovered area
		}

		renderAbstractEntities(batch, camera);

		renderMouse(batch);

		debugRender(batch, camera);

		batch.end();
	}
	
	
	/**
	 * Render a single tile.
	 * @param batch the sprite batch.
	 * @param camera the camera.
	 * @param tileMap the tile map.
	 * @param tilesToBeSkipped a list of tiles to skip.
	 * @param tile the tile to render.
	 */
	private void renderTile(SpriteBatch batch, OrthographicCamera camera, List<Tile> tileMap, List<Tile> tilesToBeSkipped, Tile tile) {

        if (tilesToBeSkipped.contains(tile)) {
            return;
        }
        float[] tileWorldCord = WorldUtil.colRowToWorldCords(tile.getCol(), tile.getRow());

        if (WorldUtil.areCoordinatesOffScreen(tileWorldCord[0], tileWorldCord[1], camera)) {
            tilesSkipped++;
            GameManager.get().setTilesRendered(tileMap.size() - tilesSkipped);
            GameManager.get().setTilesCount(tileMap.size());
            return;
        }

        Texture tex = tile.getTexture();
			batch.draw(tex, tileWorldCord[0], tileWorldCord[1], tex.getWidth() * WorldUtil.SCALE_X,
					tex.getHeight() * WorldUtil.SCALE_Y);
		GameManager.get().setTilesRendered(tileMap.size() - tilesSkipped);
		GameManager.get().setTilesCount(tileMap.size());
		

	}


	

	/**
	 * Render the tile under the mouse.
	 * @param batch the sprite batch.
	 */
    private void renderMouse(SpriteBatch batch) {
        Vector2 mousePosition = GameManager.getManagerFromInstance(InputManager.class).getMousePosition();

        Texture tex = textureManager.getTexture(TEXTURE_SELECTION);

        // get mouse position
        float[] worldCoord = WorldUtil.screenToWorldCoordinates(mousePosition.getX(), mousePosition.getY());

        // snap to the tile under the mouse by converting mouse position to colRow then back to mouse coordinates
        float[] colRow = WorldUtil.worldCoordinatesToColRow(worldCoord[0], worldCoord[1]);

        float[] snapWorldCoord = WorldUtil.colRowToWorldCords(colRow[0], colRow[1] + 1);

        //Needs to getTile with a HexVector for networking to work atm
        Tile tile = GameManager.get().getWorld().getTile(new HexVector(colRow[0], colRow[1]));

        if (tile != null) {
            batch.draw(tex, (int) snapWorldCoord[0], (int) snapWorldCoord[1]  - (tex.getHeight() * WorldUtil.SCALE_Y), 
                    tex.getWidth() * WorldUtil.SCALE_X,
                    tex.getHeight() * WorldUtil.SCALE_Y);
        }

	}	
	
	
	
    /**
	 * Render all the entities on in view, including movement tiles, and excluding undiscovered area.
	 * @param batch the sprite batch.
	 * @param camera the camera.
	 */
	private void renderAbstractEntities(SpriteBatch batch, OrthographicCamera camera) {
		List<AbstractEntity> entities = GameManager.get().getWorld().getSortedEntities();
		int entitiesSkipped = 0;
		logger.debug("NUMBER OF ENTITIES IN ENTITY RENDER LIST: {}", entities.size());
		for (AbstractEntity entity : entities) {
			Texture tex = textureManager.getTexture(entity.getTextureString());
			float[] entityWorldCoord = WorldUtil.colRowToWorldCords(entity.getCol(), entity.getRow());

			// If it's offscreen
			if (WorldUtil.areCoordinatesOffScreen(entityWorldCoord[0], entityWorldCoord[1], camera)) {
				entitiesSkipped++;
				continue;
			}
			
			/* Draw Peon */
			// Place movement tiles
			if (entity instanceof AgentEntity) {
				if (GameManager.get().getShowPath()) {
					renderPeonMovementTiles(batch, camera, entity, entityWorldCoord);
				}
				renderAbstractEntity(batch, entity, entityWorldCoord, tex, entity.getRotation());
			} else if (entity instanceof StaticEntity) {
				StaticEntity staticEntity = (StaticEntity) entity;
				Set<HexVector> childrenPositions = staticEntity.getChildrenPositions();
				for(HexVector childPos: childrenPositions) {
					Texture childTex = staticEntity.getTexture(childPos);
					
					float[] childWorldCoord = WorldUtil.colRowToWorldCords(childPos.getCol(), childPos.getRow());

					if (entity instanceof ItemEntity) {
						renderItemEntity(batch, (ItemEntity) entity, childWorldCoord);
					} else if(entity instanceof ControlTowers) {
		                float w = childTex.getWidth();
		                float h = childTex.getHeight();
		                float drawX = childWorldCoord[0] + ((TextureManager.TILE_WIDTH - w) * WorldUtil.SCALE_X / 2);
		                float drawY = childWorldCoord[1];
		                batch.draw(childTex, drawX, drawY, w * WorldUtil.SCALE_X, h * WorldUtil.SCALE_Y);
					} else {
						batch.draw(
							childTex, 
							childWorldCoord[0], 
							childWorldCoord[1], 
							childTex.getWidth() * WorldUtil.SCALE_X, 
							childTex.getHeight() * WorldUtil.SCALE_Y);
					}
				}
			} else {
				//Allows towers to be rendered with rotation
				if (entity instanceof Tower) {
					renderAbstractEntity(batch, entity, entityWorldCoord, tex, entity.getRotation());
					if (((Tower) entity).isFrozen()) {
						float width = tex.getWidth() * entity.getColRenderLength() * WorldUtil.SCALE_X * 1f;
						float height = tex.getHeight() * entity.getRowRenderLength() * WorldUtil.SCALE_Y * 1f;

						float x = entityWorldCoord[0];
						float y = entityWorldCoord[1];
						batch.draw(GameManager.get().getManager(TextureManager.class).getTexture("frozen"),
								x, y, width, height);
					}
                } else if (entity instanceof BasicEnemy){
                	float scale = 1f;
					if (entity instanceof HealerEnemy) {
						if (((HealerEnemy) entity).healDisplay()){
							float width = ((HealerEnemy) entity).getHealRange() *
									tex.getWidth() * entity.getColRenderLength() * WorldUtil.SCALE_X;
							float height = ((HealerEnemy) entity).getHealRange() *
									tex.getHeight() * entity.getRowRenderLength() * WorldUtil.SCALE_Y * 0.9f;

							logger.trace("Heal Ring width {} height {}", width, height);
							float x = entityWorldCoord[0] - width / 2;
							float y = entityWorldCoord[1] - height / 2;
							batch.draw(GameManager.get().getManager(TextureManager.class).getTexture("healRing"),
									x, y, width, height);
							entityWorldCoord = WorldUtil.colRowToWorldCords(
									entity.getCol(),entity.getRow() + 0.1f);
						}
					} else if (((BasicEnemy) entity).hasAttacked()) {
						double direction = ((BasicEnemy) entity).getAngle();
						entityWorldCoord = WorldUtil.colRowToWorldCords(
								entity.getCol() - 0.1f * (float) Math.cos(Math.toRadians(direction)),
								entity.getRow() - 0.1f * (float) Math.sin(Math.toRadians(direction)));
					}
					renderEnemyEntity(batch, entity, entityWorldCoord, tex, scale);

					if (((BasicEnemy) entity).isDamaged()) {
						float width = tex.getWidth() * entity.getColRenderLength() * WorldUtil.SCALE_X * 0.5f;
						float height = tex.getHeight() * entity.getRowRenderLength() * WorldUtil.SCALE_Y * 0.5f;

						float x = entityWorldCoord[0] + width / 2;
						float y = entityWorldCoord[1] + height / 2;
						batch.draw(GameManager.get().getManager(TextureManager.class).getTexture("enemyHit"),
								x, y, width, height);
					}
				} else {
					renderAbstractEntity(batch, entity, entityWorldCoord, tex);
				}
			}
		}

		GameManager.get().setEntitiesRendered(entities.size() - entitiesSkipped);
		GameManager.get().setEntitiesCount(entities.size());
	}
	
	private void renderItemEntity(SpriteBatch batch, ItemEntity entity, float[] childWorldCoord) {
		float w = TextureManager.TILE_WIDTH * ItemEntity.TEXTURE_SCALE_FACTOR * WorldUtil.SCALE_X;
		float h = TextureManager.TILE_HEIGHT * ItemEntity.TEXTURE_SCALE_FACTOR * WorldUtil.SCALE_Y;
		float x = childWorldCoord[0] + (TextureManager.TILE_WIDTH - w) * WorldUtil.SCALE_X / 2;
		float y = childWorldCoord[1] + (TextureManager.TILE_HEIGHT - h) * WorldUtil.SCALE_Y / 2;

		Inventory inventory = entity.getInventory();
		for (int i = 0; i < inventory.stackCount(); i++) {
			Inventory.Stack stack = inventory.getStack(i);
			if (stack != null) {
				// TODO once all textures are implemented get rid of the conditional
				Texture texture;
				if (GameManager.get().getManager(TextureManager.class).hasTexture(stack.getItem().getGlowTexture())) {
					texture = GameManager.get().getManager(TextureManager.class).getTexture(stack.getItem().getGlowTexture());
				} else {
					texture = GameManager.get().getManager(TextureManager.class).getTexture(stack.getItem().getTexture());
				}
				
				batch.draw(texture,
					x + entity.getOffsetX(i) - ItemEntity.TEXTURE_OFFSET, 
					y + entity.getOffsetY(i) - ItemEntity.TEXTURE_OFFSET, 
					w, h);
				font.draw(batch, (inventory.getStack(i).getNumItems() > 1) ? "x" + inventory.getStack(i).getNumItems() : "", 
					x + entity.getOffsetX(i) - ItemEntity.TEXTURE_OFFSET + (w * 0.5f),
					y + entity.getOffsetY(i) - ItemEntity.TEXTURE_OFFSET + (h * 0.5f));
			}
		}
	}

	private void renderEnemyEntity(SpriteBatch batch, AbstractEntity entity, float[] entityWorldCord, Texture tex,
                                   float scale){
        float x = entityWorldCord[0];
        float y = entityWorldCord[1];

        float width = tex.getWidth() * entity.getColRenderLength() * WorldUtil.SCALE_X * scale;
        float height = tex.getHeight() * entity.getRowRenderLength() * WorldUtil.SCALE_Y * scale;
        batch.draw(tex, x, y, width, height);
    }

	private void renderAbstractEntity(SpriteBatch batch, AbstractEntity entity, float[] entityWorldCord, Texture tex) {
        float x = entityWorldCord[0];
		float y = entityWorldCord[1];

        float width = tex.getWidth() * entity.getColRenderLength() * WorldUtil.SCALE_X;
        float height = tex.getHeight() * entity.getRowRenderLength() * WorldUtil.SCALE_Y;
        batch.draw(tex, x, y, width, height);
    }

	// Added the rotation variable and logic so that an entity can be rendered at any rotation
	private void renderAbstractEntity(SpriteBatch batch, AbstractEntity entity, float[] entityWorldCord, Texture tex,
									  float rotation) {
		if (entity instanceof PlayerPeon) {
			if (((PlayerPeon) entity).isDamaged() && ((PlayerPeon) entity).isAlive()) {
				entity.setColor(1, 0, 0, 1);
			} else {
				entity.setColor(1, 1, 1, 1);
			}
			entity.draw(batch);
			return;
		}

		float x = entityWorldCord[0];
		float y = entityWorldCord[1];

		float width = tex.getWidth() * entity.getColRenderLength() * WorldUtil.SCALE_X;
		float height = tex.getHeight() * entity.getRowRenderLength() * WorldUtil.SCALE_Y;

		TextureRegion texReg = new TextureRegion(tex);

		batch.draw(texReg, x, y, width / 2, height / 2, width, height, 1, 1, rotation);
	}

	private void renderPeonMovementTiles(SpriteBatch batch, OrthographicCamera camera, AbstractEntity entity, float[] entityWorldCord) {
		AgentEntity actor = (AgentEntity) entity;
		AbstractTask task = actor.getTask();
		if (task instanceof MovementTask) {
			if (((MovementTask)task).getPath() == null) { //related to issue #8
				return;
			}
			List<Tile> path = ((MovementTask)task).getPath();
			for (Tile tile : path) {
				// Place transparent tiles for the path, but place a non-transparent tile for the destination
				Texture tex = path.get(path.size() - 1) == tile ?
						textureManager.getTexture(TEXTURE_DESTINATION) : textureManager.getTexture(TEXTURE_PATH);
				float[] tileWorldCord = WorldUtil.colRowToWorldCords(tile.getCol(), tile.getRow());
				if (WorldUtil.areCoordinatesOffScreen(tileWorldCord[0], tileWorldCord[1], camera)) {
					tilesSkipped++;
					continue;
				}
				batch.draw(tex, tileWorldCord[0],
						tileWorldCord[1], 
						tex.getWidth() * WorldUtil.SCALE_X,
						tex.getHeight() * WorldUtil.SCALE_Y);

			}
		}
	}
	
	private void debugRender(SpriteBatch batch, OrthographicCamera camera) {

		if (GameManager.get().getShowCoords()) {
			List<Tile> tileMap = GameManager.get().getWorld().getTileListFromMap();
			for (Tile tile : tileMap) {
				float[] tileWorldCord = WorldUtil.colRowToWorldCords(tile.getCol(), tile.getRow());

				if (!WorldUtil.areCoordinatesOffScreen(tileWorldCord[0], tileWorldCord[1], camera)) {
					font.draw(batch, 
							tile.toString(),
							tileWorldCord[0] + WorldUtil.TILE_WIDTH / 4.5f,
							tileWorldCord[1]);
				}

			}
		}

		if (GameManager.get().getShowCoordsEntity()) {
			List<AbstractEntity> entities = GameManager.get().getWorld().getEntities();
			for (AbstractEntity entity : entities) {
				float[] tileWorldCord = WorldUtil.colRowToWorldCords(entity.getCol(), entity.getRow());

				if (!WorldUtil.areCoordinatesOffScreen(tileWorldCord[0], tileWorldCord[1], camera)) {
					font.draw(batch, String.format("%.0f, %.0f", entity.getCol(), entity.getRow()),
							tileWorldCord[0], tileWorldCord[1]);
				}
			}
		}
	}
}
