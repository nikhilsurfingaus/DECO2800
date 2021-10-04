package deco.combatevolved.entities;

import java.util.Objects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.google.gson.annotations.Expose;

import deco.combatevolved.managers.CollisionManager;
import deco.combatevolved.managers.GameManager;
import deco.combatevolved.managers.NetworkManager;
import deco.combatevolved.renderers.Renderable;
import deco.combatevolved.util.HexVector;

/**
 * A AbstractEntity is an item that can exist in both 3D and 2D worlds
 * AbstractEntities are rendered by Render2D and Render3D An item that does not
 * need to be rendered should not be a WorldEntity
 */
public abstract class AbstractEntity extends Sprite implements Comparable<AbstractEntity>, Renderable {
		
	private static int nextID = 0;

	public static void resetID() {
		nextID = 0;
	}

	private static int getNextID() {
		return nextID++;
	}
	@Expose
	private String texture = "error_box";


	protected HexVector position;

	protected float rotation;
	
	private int height;

	private float colRenderLength;

	private float rowRenderLength;
	


	@Expose
	private int entityID;

	/** Whether an entity should trigger a collision when */
	protected boolean collidable = true;
	
	@Expose
	private int renderOrder = 0; 
	
	/**
	 * Constructor for an abstract entity
	 * @param col the col position on the world
	 * @param row the row position on the world
	 * @param renderOrder the height position on the world
     */

	public AbstractEntity(float col, float row, int renderOrder) {
		this(col, row, renderOrder, 1f, 1f);
		this.renderOrder = renderOrder;
		this.rotation = 0;

		entityID = AbstractEntity.getNextID();
	}

	public AbstractEntity() {
		this.position = new HexVector();
		this.rotation = 0;
		this.colRenderLength = 1f;
		this.rowRenderLength = 1f;

		entityID = AbstractEntity.getNextID();
	}


	/**
	 * Constructor for an abstract entity
	 * @param col the col position on the world
	 * @param row the row position on the world
	 * @param height the height position on the world
	 * @param colRenderLength the rendered length in col direction
	 * @param rowRenderLength the rendered length in the row direction
     */
	public AbstractEntity(float col, float row, int height, float colRenderLength, float rowRenderLength) {
		this.position = new HexVector(col, row);
		this.rotation = 0;
		this.height = height;
		this.colRenderLength = colRenderLength;
		this.rowRenderLength = rowRenderLength;
		this.entityID = AbstractEntity.getNextID();
	}

	/**
	 * Get the column position of this AbstractWorld Entity
	 */
	public float getCol() {
		return position.getCol();
	}

	/**
	 * Get the row position of this AbstractWorld Entity
	 */
	public float getRow() {
		return position.getRow();
	}

	/**
	 * Get the Z position of this AbstractWorld Entity
	 * 
	 * @return The Z position
	 */
	public int getZ() {
		return height;
	}


	/**
	 * Get the counter clockwise rotation of this AbstractWorld Entity
	 *
	 * @return The counter clockwise rotation
	 */
	public float getRotation() {
		return rotation;
	}

	/**
	 * Sets the col coordinate for the entity
     */
	public void setCol(float col) {
		this.position.setCol(col);
	}

	/**
	 * Sets the row coordinate for the entity
	 */
	public void setRow(float row) {
		this.position.setRow(row);
	}

	/**
	 * Sets the height coordinate for the entity
	 */
	public void setZ(int z) {
		this.height = z;
	}

	/**
	 * sets the position of the entity in the world
	 * @param col the x coordinate for the entity
	 * @param row the y coordinate for the entity
     * @param height the z coordinate for the entity
     */
	public void setPosition(float col, float row, int height) {
		setCol(col);
		setRow(row);
		setZ(height);
	}

	public void setPosition(HexVector position) {
		this.position = position;
	}


	/**
	 * sets the rotation of the entity in the world
	 * @param rotation the counter clockwise rotation for the entity
	 */
	public void setRotation(float rotation) {
		this.rotation = rotation;
	}

	public float getColRenderWidth() {
		return colRenderLength;
	}

	public float getRowRenderWidth() {
		return rowRenderLength;
	}
	
	public void setRenderOrder(int newLevel) {
		this.renderOrder = newLevel;
	}
	
	public int getRenderOrder() {
		return renderOrder;
	} 
	
	@Override 
	public int compareTo(AbstractEntity otherEntity) {
		return this.renderOrder - otherEntity.getRenderOrder();
	}

	/**
	 * Tests to see if the item collides with another entity in the world
	 * @param entity the entity to test collision with
	 * @return true if they collide, false if they do not collide
     */
	public boolean collidesWith(AbstractEntity entity) {
		return GameManager.getManagerFromInstance(CollisionManager.class).collidesWith(this, entity);
	}
	
	public boolean isCollidable() {
		return this.collidable;
	}

	@Override
	public float getColRenderLength() {
		return this.colRenderLength;
	}

	@Override
	public float getRowRenderLength() {
		return this.rowRenderLength;
	}

	/**
	 * Gives the string for the texture of this entity. This does not mean the
	 * texture is currently registered
	 * 
	 * @return texture string
	 */
	public String getTextureString() {
		return texture;
	}

	/**
	 * Sets the texture string for this entity. Check the texture is registered with
	 * the TextureRegister
	 * 
	 * @param texture
	 *            String texture id
	 */
	public void setTexture(String texture) {
		this.texture = texture;
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		AbstractEntity that = (AbstractEntity) o;
		return height == that.height &&
				Float.compare(that.colRenderLength, colRenderLength) == 0 &&
				Float.compare(that.rowRenderLength, rowRenderLength) == 0 &&
				entityID == that.entityID &&
				collidable == that.collidable &&
				Objects.equals(texture, that.texture) &&
				Objects.equals(position, that.position);
	}

	@Override
	public int hashCode() {
		int result = position != null ? position.hashCode() : 0;
		result = 31 * result + (texture != null ? texture.hashCode() : 0);
		return result;
	}

	/**
	 * Gets the distance from an abstract entity
	 * @param e the abstract entity
	 * @return the distance as a float
     */
	public float distance(AbstractEntity e) {
		if (e == null) {
			// System.out.println("Who the fuck wrote this"); - Idk Luke, you probably should use a logger tho
			return -1;
		}
//		System.out.println(e.position);
		return this.position.distance(e.position);
	}
	

	public HexVector getPosition() {
		return position;
	}

	public abstract void onTick(long i);

	/**
	 * Get objectID (If applicable)
	 *
	 * @return Name of object
	 */
	public String getObjectName() {return this.getClass().getName();}
	
	
	public int getEntityID() {
		return entityID;
	}

	public void setEntityID(int id) {
		this.entityID = id;
	}

	public void setEntityID() {
		setEntityID(AbstractEntity.getNextID());
	}

	public void dispose() {
		GameManager.get().getManager(NetworkManager.class).deleteEntity(this);
		GameManager.get().getWorld().removeEntity(this);
	}
	
}


