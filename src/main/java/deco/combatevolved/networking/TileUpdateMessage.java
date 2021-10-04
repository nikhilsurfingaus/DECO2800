package deco.combatevolved.networking;

import deco.combatevolved.worlds.Tile;
import deco.combatevolved.worlds.biomes.BiomeType;

public class TileUpdateMessage {
    private Tile tile;
    private BiomeType biomeType;

    /**
     * Gets the tile to update.
     * @return the tile to update
     */
    public Tile getTile() {
        return tile;
    }

    /**
     * Sets the tile to update in the message.
     * @param tile tile to update
     */
    public void setTile(Tile tile) {
        this.tile = tile;
    }

    /**
     * Gets the biome type attached to the tile to update.
     * @return biome type of the tile to update
     */
    public BiomeType getBiomeType() {
        return biomeType;
    }

    /**
     * Sets the biome type of the message for the tile to update.
     * @param biomeType biome type of the tile to update
     */
    public void setBiomeType(BiomeType biomeType) {
        this.biomeType = biomeType;
    }
}
