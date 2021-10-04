package deco.combatevolved.worlds.biomes;

import deco.combatevolved.worlds.Tile;

import java.util.Random;

/**
 * The Ocean biome class
 *
 * Is responsible for setting and maintaining any properties relating to the
 * Ocean biome
 */
public class Ocean extends AbstractBiome {
    private Random random = new Random();

    /**
     * Ocean biome constructor
     */
    public Ocean() {
        super();
    }

    /**
     * Ocean biome constructor
     *
     * @param biomeType The BiomeType to set for this biome
     */
    public Ocean(BiomeType biomeType) {
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
        String type = "ocean_0";
        tile.setTexture(type);
    }
}
