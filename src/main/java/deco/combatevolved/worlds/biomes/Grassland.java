package deco.combatevolved.worlds.biomes;

import deco.combatevolved.worlds.Tile;

import java.util.Random;

/**
 * The Grassland biome class
 *
 * Is responsible for setting and maintaining any properties relating to the
 * Grassland biome
 */
public class Grassland extends AbstractBiome {

    /**
     * Grassland biome constructor
     */
    public Grassland() {
        super();
    }

    /**
     * Grassland biome constructor
     *
     * @param biomeType The BiomeType to set for this biome
     */
    public Grassland(BiomeType biomeType) {
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
        String type = "grassland_";
        type += elevation;

        tile.setTexture(type);
    }
}
