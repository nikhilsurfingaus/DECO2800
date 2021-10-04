package deco.combatevolved.worlds.biomes;

import deco.combatevolved.worlds.Tile;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * AbstractBiome is the abstract class for all biome types
 *
 * It provides universal methods and properties for all biome types
 */
public abstract class AbstractBiome {
	// Variable to store the type of biome
	private BiomeType biomeType;

	//A list of the tiles in this biome. (New and easier way to access tiles.)
	private List<Tile> tileList = new CopyOnWriteArrayList<>();

	/**
	 * Empty AbstractBiome instance. Biome is OCEAN by default.
	 */
	public AbstractBiome() {
		this.biomeType = BiomeType.OCEAN;
	}

	/**
	 * Creates an AbstractBiome class with a specified biome, and a temperature
	 * randomly selected between the given values.
	 * 
	 * @param biomeType
	 *            The type of biome to generate
	 */
	public AbstractBiome(BiomeType biomeType) {
		this.biomeType = biomeType;
	}

	/**
	 * Get's the list of tile in the biome
	 * @return The list of tiles in the biome
	 */
	public List<Tile> getTileList() {
		return tileList;
	}

	/**
	 * Add's a Tile to the biome
	 * @param tile The list of tile to set in this biome
	 */
	public void addTileToBiome(Tile tile) {
		this.tileList.add(tile);
	}

	/**
	 * Set's the texture type for the biome when constructing tiles for this
	 * particular biome
	 * 
	 * @param tile
	 *            The Tile to set the texture for
	 */
	public abstract void setBiomeTexture(Tile tile);

	/**
	 * Get's the type of biome used for the instantiating class
	 * 
	 * @return The biome type for this class
	 */
	public BiomeType getBiomeType() {
		return biomeType;
	}

	/**
	 * Set the type for the biome
	 * 
	 * @param type
	 *            The type of biome to make it
	 */
	// TODO: Add code to update textures for tiles after biome change
	public void setBiomeType(BiomeType type) {
		this.biomeType = type;
	}
	
	@Override
	public String toString() {
		return this.biomeType.asLowerCase();
	}
}
