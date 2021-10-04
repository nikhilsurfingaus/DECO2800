package deco.combatevolved.entities.staticentities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import deco.combatevolved.managers.GameManager;
import deco.combatevolved.managers.TextureManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

import com.badlogic.gdx.graphics.Texture;
import com.google.gson.annotations.Expose;

import deco.combatevolved.entities.AbstractEntity;
import deco.combatevolved.util.HexVector;
import deco.combatevolved.util.WorldUtil;
import deco.combatevolved.worlds.Tile;


/**
 * A StaticEntity is an entity that does not move in the world. For example a
 * rock, or a house.
 */
public class StaticEntity extends AbstractEntity {
	transient private final Logger log = LoggerFactory.getLogger(StaticEntity.class);

	//private transient HashMap<String, Object> dataToSave = new HashMap<>();


	//pos, texture
	@Expose
	public Map<HexVector, String> children;

	public StaticEntity() {
		super();
	}

	public StaticEntity(Tile tile, int renderOrder , String texture , boolean obstructed) {
		super(tile.getCol(), tile.getRow(), renderOrder);

		children = new HashMap<>();
		children.put(tile.getCoordinates(), texture);
		if(!WorldUtil.validColRow(tile.getCoordinates())) {
			 log.debug(tile.getCoordinates() + " Is Invalid:");
			 return;
		}
		tile.setParent(this);
		tile.setObstructed(obstructed);	
	}

	public StaticEntity(float col, float row, int renderOrder, List<Part> entityParts) {
		super(col, row, renderOrder);

		Tile center = GameManager.get().getWorld().getTile(this.getPosition());

		if (center == null) {
			log.debug("Center is null"); 
			return;
		}
		
		if(!WorldUtil.validColRow(center.getCoordinates())) {
			 log.debug(center.getCoordinates() + " Is Invalid:");
			 return;
		}

		children = new HashMap<>();

		for (Part part : entityParts) {
			Tile tile = textureToTile(part.getPostion(), this.getPosition());
			if (tile != null) {
				children.put(tile.getCoordinates(), part.textureString);
				//Tile child = GameManager.get().getWorld().getTile(part.getPostion());
				tile.setObstructed(part.isObstructed());
			}	
		}
	}
	

	public void setup() {
		if (children != null) {
			for (HexVector childposn : children.keySet()) {
				Tile child = GameManager.get().getWorld().getTile(childposn);
				if (child != null) {
					child.setParent(this);
				}
			}
		}
	}
	

	@Override
	public void onTick(long i) {
		// Do the AI for the building in here
	}

	private Tile textureToTile(HexVector offset, HexVector center) {
		if(!WorldUtil.validColRow(offset)) {
			 log.debug(offset + " Is Invaid:"); 
				return null;
		}
		HexVector targetTile = center.add(offset);
		return GameManager.get().getWorld().getTile(targetTile);	
	}
	
	public Set<HexVector> getChildrenPositions() {
		return children.keySet();
	}

	public Texture getTexture(HexVector childpos) {
		String texture = children.get(childpos);
		return GameManager.get().getManager(TextureManager.class).getTexture(texture);
	}

	public void setChildren(Map<HexVector, String> children) {
		this.children = children;
	}
}
