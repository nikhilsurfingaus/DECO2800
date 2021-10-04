package deco.combatevolved.entities.items;

import deco.combatevolved.entities.items.consumableitems.Consumable;

public class Item {
	 private static IllegalArgumentException rarityException =
		        new IllegalArgumentException("Rarity cannot be less than 1 or greater than 5");
	
	// Name of item
	protected String name;
	// Rarity of item
	protected int rarity;
	// ID of item
	private String id;
	// the texture/sprite of the item
	private String texture;
	// Item description
	private String description;

	/**
	 * Class constructor
	 *
	 * @param name name of the item
	 * @param rarity rarity of the item
	 * @param texture name of the texture
	 * @throws IllegalArgumentException if rarity is less than 1
	 */
	public Item(String name, int rarity, String id, String texture) {
		if (rarity < 1 || rarity > 5) {
			throw rarityException;
		}
		
		this.name = name;
		this.rarity = rarity;
		this.id = id;
		this.texture = texture;
		this.description = "";
	}
	
	/**
	 * Class constructor
	 *
	 * @param name name of the item
	 * @param rarity rarity of the item
	 * @param texture name of the texture
	 * @param description short description of the item
	 * @throws IllegalArgumentException if rarity is less than 1
	 */
	public Item(String name, int rarity, String id, String texture, String description) {
		if (rarity < 1 || rarity > 5) {
			throw rarityException;
		}
		
		this.name = name;
		this.rarity = rarity;
		this.id = id;
		this.texture = texture;
		this.description = description;
	}
	
	/**
	 * Class constructor
	 *
	 * @param name name of the item
	 * @param rarity rarity of the item
	 * @throws IllegalArgumentException if rarity is less than 1
	 */
	public Item(String name, int rarity) {
		if (rarity < 1 || rarity > 5) {
			throw rarityException;
		}

		this.name = name;
		this.rarity = rarity;
		this.description = "";
	}
	
	/**
	 * Get the name of the item
	 * 
	 * @return name of the item
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Get the rarity of the item
	 * 
	 * @return rarity of the item
	 */
	public int getRarity() {
		return this.rarity;
	}

	@Override
	public int hashCode() {
		return 1 + this.name.hashCode() + this.rarity;
	}

	@Override
	public boolean equals(Object item) {
		return (item != null 
			&& item instanceof Item
			&& this.name == ((Item) item).getName() 
			&& this.rarity == ((Item) item).getRarity()
			&& this.hashCode() == item.hashCode());
	}

	/**
	 * Gets the Id of the item
	 * @return the id of the item
	 */
	public String getId() {
		return id;
	}

	/**
	 * Returns the texture of the item
	 * @return The texture of the item
	 */
	public String getTexture() {
		return texture;
	}

	public String getGlowTexture() {
		return texture + "-glow";
	}
	
	/**
	 * Returns the description of the item
	 * @return the description of the item
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * set the item's description
	 */
	protected void setDescription(String description) {
		this.description = description;
	}

	public boolean isConsumable() {
		return this instanceof Consumable;
	}
}
