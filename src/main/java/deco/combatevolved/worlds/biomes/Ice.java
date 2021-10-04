package deco.combatevolved.worlds.biomes;

import deco.combatevolved.worlds.Tile;

import java.util.Random;

/**
 * The Ice biome class
 *
 * Is responsible for setting and maintaining any properties relating to the
 * Ice biome
 */
public class Ice extends AbstractBiome {
    private Random random = new Random();

    /**
     * Ice biome constructor
     */
    public Ice() {
        super();
    }

    /**
     * Ice biome constructor
     *
     * @param biomeType The BiomeType to set for this biome
     */
    public Ice(BiomeType biomeType) {
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
        String type = "ice_0";
        tile.setTexture(type);
    }
}
