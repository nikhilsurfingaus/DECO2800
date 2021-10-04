package deco.combatevolved.worlds.biomes;

import deco.combatevolved.worlds.Tile;

import java.util.Random;

/**
 * The Lake biome class
 *
 * Is responsible for setting and maintaining any properties relating to the
 * Lake biome
 */
public class Lake extends AbstractBiome {
    private Random random = new Random();

    /**
     * Lake biome constructor
     */
    public Lake() {
        super();
    }

    /**
     * Lake biome constructor
     *
     * @param biomeType The BiomeType to set for this biome
     */
    public Lake(BiomeType biomeType) {
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
        int elevation = random.nextInt(2);
        String type = "rainforest_";
        type += elevation;

        tile.setTexture(type);
    }
}
