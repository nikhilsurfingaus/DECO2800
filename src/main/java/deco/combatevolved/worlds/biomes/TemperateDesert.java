package deco.combatevolved.worlds.biomes;

import deco.combatevolved.worlds.Tile;

import java.util.Random;

/**
 * The TemperateDesert biome class
 *
 * Is responsible for setting and maintaining any properties relating to the
 * TemperateDesert biome
 */
public class TemperateDesert extends AbstractBiome {

    /**
     * TemperateDesert biome constructor
     */
    public TemperateDesert() {
        super();
    }

    /**
     * TemperateDesert biome constructor
     *
     * @param biomeType The BiomeType to set for this biome
     */
    public TemperateDesert(BiomeType biomeType) {
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
        String type = "temperateDesert_";
        type += elevation;

        tile.setTexture(type);
    }
}
