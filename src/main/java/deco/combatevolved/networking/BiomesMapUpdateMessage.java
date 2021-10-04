package deco.combatevolved.networking;

import deco.combatevolved.worlds.Tile;
import deco.combatevolved.worlds.biomes.BiomeType;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class BiomesMapUpdateMessage {
    private ConcurrentMap<BiomeType, CopyOnWriteArrayList<Tile>> biomesMap;

    /**
     * Gets the biomes map.
     * @return the biomes map
     */
    public ConcurrentMap<BiomeType, CopyOnWriteArrayList<Tile>> getBiomesMap() {
        return biomesMap;
    }

    /**
     * Sets the biomes map to a new biomes map.
     * @param biomesMap new biomes map
     */
    public void setBiomesMap(ConcurrentMap<BiomeType, CopyOnWriteArrayList<Tile>> biomesMap) {
        this.biomesMap = biomesMap;
    }
}
