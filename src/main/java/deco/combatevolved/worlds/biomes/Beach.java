package deco.combatevolved.worlds.biomes;

import deco.combatevolved.worlds.Tile;

import java.util.Random;

/**
 * The Beach biome class
 *
 * Is responsible for setting and maintaining any properties relating to the
 * Beach biome
 */
public class Beach extends AbstractBiome {
    /**
     * Snow biome constructor
     */
    public Beach() {
        super();
    }

    /**
     * Beach biome constructor
     *
     * @param biomeType The BiomeType to set for this biome
     */
    public Beach(BiomeType biomeType) {
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
        String type = "beach_0";
        tile.setTexture(type);
    }
}
