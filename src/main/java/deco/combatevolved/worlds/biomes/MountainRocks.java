package deco.combatevolved.worlds.biomes;

import deco.combatevolved.worlds.Tile;

import java.util.Random;

/**
 * The MountainRocks biome class
 *
 * Is responsible for setting and maintaining any properties relating to the
 * MountainRocks biome
 */
public class MountainRocks extends AbstractBiome {
    private Random random = new Random();

    /**
     * MountainRocks biome constructor
     */
    public MountainRocks() {
        super();
    }

    /**
     * MountainRocks biome constructor
     *
     * @param biomeType The BiomeType to set for this biome
     */
    public MountainRocks(BiomeType biomeType) {
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
        String type = "mountainRocks_";
        type += elevation;

        tile.setTexture(type);
    }
}
