package deco.combatevolved.worlds;

import deco.combatevolved.worlds.worldgen.WorldBuilder;

/**
 * Server world acts as the husk of an actual world, controlled by a server connection.
 *
 * Clients will not call onTick()
 */
public class ServerWorld extends AbstractWorld {

    /**
     * ServerWorld constructor
     */
    public ServerWorld() {
        super();
    }

    /**
     * Main method to generate the game world
     */
    @Override
    protected void generateWorld() {
        WorldBuilder worldBuilder = new WorldBuilder(this.worldGenParamBag);
        worldBuilder.generateWorldNoise();
        worldBuilder.generateBiomes();
        worldBuilder.generateTiles();
    }

    /**
     * Do not tick entities in this onTick method, entities will be ticked by the server.
     */
    @Override
    public void onTick(long i) {
        // TODO: Lerping etc
    }
}
