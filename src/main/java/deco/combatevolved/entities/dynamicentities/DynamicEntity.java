package deco.combatevolved.entities.dynamicentities;

import com.google.gson.annotations.Expose;

import deco.combatevolved.entities.AbstractEntity;
import deco.combatevolved.managers.CollisionManager;
import deco.combatevolved.managers.GameManager;
import deco.combatevolved.util.HexVector;
import deco.combatevolved.worlds.Tile;

/**
 * A DymanicEntity is an entity that moves and interacts within the world. For
 * example, the player, or a duck.
 */
public abstract class DynamicEntity extends AbstractEntity{
	@Expose
	protected float speed;
	private HexVector nextCoords = null;

	public DynamicEntity(float col, float row, int renderOrder, float speed) {
		super(col, row, renderOrder);

		this.speed = speed;
	}

	public DynamicEntity() {
		super();
	}

	public void setNextCoords(HexVector coords) {this.nextCoords = coords;}

	public HexVector getNextCoords() {return this.nextCoords;}

	public void moveTowards(HexVector destination) {
//		if (!CollisionManager.get().willCollide(this, destination)) {
			position.moveToward(destination, speed);
//		} else {
//			moveTowardsIfCollisionDetected(destination);
//		}
	}
	
	/** 
	 * Move to an alternative position given that there will be collision 
	 * at the destination.
	 * @param dest the position that the entity is moving to
	 */
	private void moveTowardsIfCollisionDetected(HexVector dest) {
		Tile destTile = GameManager.get().getWorld().getTile(dest, 1f);
		float closestDistance = 9999f;
		HexVector closestDest = new HexVector();
		
		if (destTile != null) {
			for (int i = 0; i < 6; i++) {
				Tile neighbourTile = destTile.getNeighbour(i);
				if (neighbourTile != null) {
					HexVector newDest = new HexVector(neighbourTile.getCol(), neighbourTile.getRow());
					if (!CollisionManager.get().willCollide(this, newDest)) {
						float distance = position.distance(newDest);
						if (distance < closestDistance) {
							closestDistance = distance;
							closestDest = newDest;
						}
					}
				}
			}
		}
		position.moveToward(closestDest, speed);
	}
	
	public float getSpeed() {
		return speed;
	}

	public void setSpeed( float speed) {
		this.speed = speed;
	}
}
