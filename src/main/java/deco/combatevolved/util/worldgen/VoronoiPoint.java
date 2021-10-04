package deco.combatevolved.util.worldgen;

import deco.combatevolved.worlds.biomes.BiomeType;

/**
 * Data structure for each Voronoi point
 */
public class VoronoiPoint {
    private short x;
    private short y;
    private float elevation;
    private float moisture; // Distance from water
    private BiomeType biome;
    private VoronoiPoint[] neighbours;

    /**
     * Default Constructor, sets temperature
     * @param x Coordinate of Voronoi point
     * @param y Coordinate of Voronoi point
     */
    VoronoiPoint(short x, short y) {
        this.x = x;
        this.y = y;
    }

    public short getX() {
        return x;
    }

    public void setX(short x) {
        this.x = x;
    }

    public short getY() {
        return y;
    }

    public void setY(short y) {
        this.y = y;
    }

    public float getElevation() {
        return elevation;
    }

    public void setElevation(float elevation) {
        this.elevation = elevation;
    }

    public float getMoisture() {
        return moisture;
    }

    public void setMoisture(float moisture) {
        this.moisture = moisture;
    }

    public BiomeType getBiome() {
        return biome;
    }

    public void setBiome(BiomeType biome) {
        this.biome = biome;
    }

    public VoronoiPoint[] getNeighbours() {
        return neighbours;
    }

    public void setNeighbours(VoronoiPoint[] neighbours) {
        this.neighbours = neighbours;
    }

    @Override
    public String toString() {
        return "VoronoiPoint - X: " + this.x + ", Y: " + this.y + ", Elevation: " + this.elevation + ", Moisture: " + this.moisture;
    }
}
