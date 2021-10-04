package deco.combatevolved.worlds.biomes;

import deco.combatevolved.worlds.Tile;

import java.util.Random;

/**
 * The Shrubland biome class
 *
 * Is responsible for setting and maintaining any properties relating to the
 * Shrubland biome
 */
public class Shrubland extends AbstractBiome {
    private Random random = new Random();

    /**
     * Shrubland biome constructor
     */
    public Shrubland() {
        super();
    }

    /**
     * Shrubland biome constructor
     *
     * @param biomeType The BiomeType to set for this biome
     */
    public Shrubland(BiomeType biomeType) {
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
        String type = "shrubland_";
        type += elevation;

        tile.setTexture(type);
    }
}
