package deco.combatevolved.worlds.worldgen;

import deco.combatevolved.util.worldgen.VoronoiPoint;
import deco.combatevolved.worlds.biomes.AbstractBiome;
import deco.combatevolved.worlds.biomes.BiomeType;

/**
 * Interface class to declare methods used in world creation
 */
public interface WorldBuilderInterface {
    /**
     * Generates the heightMap used to assign biomes types
     */
    void generateWorldNoise();

    /**
     * Set's the biome type for a VoronoiPoint
     * @param voronoiPoint The VoronoiPoint to set the biome for
     */
    void setBiomeTypeForVoronoiPoint(VoronoiPoint voronoiPoint);

    /**
     * Generates the Tile objects in the world
     */
    void generateTiles();

    /**
     * Generates the biomes within the game
     */
    void generateBiomes();

    /**
     * Generates the VoronoiPoint to create the map and biomes
     */
    void generateVoronoi();

    /**
     * Get's a biome instance within the world
     * @param biomeType The type of biome to get within the world
     * @return A biome used in the world, else null if the biome does now
     * exist in the world
     */
    AbstractBiome getBiomeInWorld(BiomeType biomeType);
}
