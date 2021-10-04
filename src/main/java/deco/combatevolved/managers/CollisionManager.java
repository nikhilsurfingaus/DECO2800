package deco.combatevolved.managers;

import deco.combatevolved.entities.AbstractEntity;
import deco.combatevolved.util.HexVector;

/**
 * Manages the collision between entities.	
 * 
 */
public class CollisionManager extends AbstractManager {
	public CollisionManager() {
		// Constructor
	}
	
	/**
	 * Retrieve the CollisionManger from the current GameManager instance.
	 * @return the CollisionManager
	 */
	public static CollisionManager get() {
		return GameManager.getManagerFromInstance(CollisionManager.class);
	}
	
	/**
	 * Check if an entity will collide with anything when moving to a new position.
	 * @param entity the entity that will move
	 * @param to the position the entity is moving to
	 * @return return true if the entity will collide, false otherwise
	 */
	public boolean willCollide(AbstractEntity entity, HexVector to) {
		
		/* check if the entity is collidable */
		if (!entity.isCollidable()) {
			// entity cannot collide with other entities
			return false;
		}
		
		for (AbstractEntity potentialObstacle : GameManager.get().getWorld().getEntities()) {
			if (entity != potentialObstacle && 
					collidesWith(entity, to, potentialObstacle)) {
				// will collide
				return true;
			}
		}
		
		// no collision
		return false;
	}
	
	/**
	 * Check if two entities will collide with other when entity1 is at a position.
	 * @param entity1 the entity to check collision
	 * @param position the position entity1 is at
	 * @param entity2 the entity to check collision
	 * @return return true if there will be collision, false otherwise
	 */
	public boolean collidesWith(AbstractEntity entity1, HexVector position, 
			AbstractEntity entity2) {
		if (!entity1.isCollidable() || !entity2.isCollidable()) {
			// entity cannot collide with other entities
			return false;
		} else {
			return position.isCloseEnoughToBeTheSame(entity2.getPosition(), 0.5f);
		}
	}
	
	/**
     * Check if two entities will collide with other.
     * @param entity1 the entity to check collision
     * @param entity2 the entity to check collision
     * @return return true if there will be collision, false otherwise
     */
    public boolean collidesWith(AbstractEntity entity1, AbstractEntity entity2) {
        return collidesWith(entity1, entity1.getPosition(), entity2);
    }
}
