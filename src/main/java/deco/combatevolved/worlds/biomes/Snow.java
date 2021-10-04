package deco.combatevolved.worlds.biomes;

import deco.combatevolved.worlds.Tile;

import java.util.Random;

/**
 * The Snow biome class
 *
 * Is responsible for setting and maintaining any properties relating to the
 * Snow biome
 */
public class Snow extends AbstractBiome {

    /**
     * Snow biome constructor
     */
    public Snow() {
        super();
    }

    /**
     * Snow biome constructor
     *
     * @param biomeType The BiomeType to set for this biome
     */
    public Snow(BiomeType biomeType) {
        super(biomeType);
    }

    /**
     * Set's the texture type for the biome when constructing tiles for this
     * particular biome
     *
     * @param tile The Tile to set the texture for
     */
    @Override
    public void setBiomeTexture(Tile tile) {
        Random random = new Random();
        int elevation = random.nextInt(2);
        String type = "snow_";
        type += elevation;
        tile.setTexture(type);
    }
}
