package deco.combatevolved.worlds.biomes;

import deco.combatevolved.worlds.Tile;

import java.util.Random;

/**
 * The TropicalRainforest biome class
 *
 * Is responsible for setting and maintaining any properties relating to the
 * TropicalRainforest biome
 */
public class TropicalRainforest extends AbstractBiome {

    /**
     * TropicalRainforest biome constructor
     */
    public TropicalRainforest() {
        super();
    }

    /**
     * TropicalRainforest biome constructor
     *
     * @param biomeType The BiomeType to set for this biome
     */
    public TropicalRainforest(BiomeType biomeType) {
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
        String type = "tropicalRainforest_0";
        tile.setTexture(type);
    }
}
