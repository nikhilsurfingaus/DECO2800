package deco.combatevolved.worlds.biomes;

import deco.combatevolved.worlds.Tile;

import java.util.Random;

/**
 * The SubtropicalDesert biome class
 *
 * Is responsible for setting and maintaining any properties relating to the
 * SubtropicalDesert biome
 */
public class SubtropicalDesert extends AbstractBiome {

    /**
     * SubtropicalDesert biome constructor
     */
    public SubtropicalDesert() {
        super();
    }

    /**
     * SubtropicalDesert biome constructor
     *
     * @param biomeType The BiomeType to set for this biome
     */
    public SubtropicalDesert(BiomeType biomeType) {
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
