package deco.combatevolved.worlds;

import deco.combatevolved.entities.AbstractEntity;
import deco.combatevolved.entities.dynamicentities.PlayerPeon;
import deco.combatevolved.entities.items.resources.Items;
import deco.combatevolved.entities.staticentities.ControlTowers;
import deco.combatevolved.entities.staticentities.CraftingTable;
import deco.combatevolved.entities.staticentities.ItemEntity;
import deco.combatevolved.entities.staticentities.Rock;
import deco.combatevolved.entities.staticentities.defensivetowers.SimpleTower;
import deco.combatevolved.managers.GameManager;
import deco.combatevolved.managers.NetworkManager;
import deco.combatevolved.managers.SoundManager;
import deco.combatevolved.networking.InventoryUpdateMessage;
import deco.combatevolved.worlds.biomes.BiomeType;
import deco.combatevolved.worlds.worldgen.WorldBuilder;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The main file to generate the world
 */
public class CombatEvolvedWorld extends AbstractWorld {
	private final Logger logger = LoggerFactory.getLogger(CombatEvolvedWorld.class);
    private Random random = new Random();
    private boolean notGenerated = true;
    public static final int MAX_RARITY = 5;
    public static final int MAX_ITEM_SPAWN = 16;
    private Items items = new Items();
    private WorldBuilder worldBuilder;

    /**
     * GameWorld constructor
     */
    public CombatEvolvedWorld() {
        super();
    }


    /**
     * Main method to generate the game world
     */
    @Override
    protected void generateWorld() {
        this.worldBuilder = new WorldBuilder(this.worldGenParamBag);
        this.worldBuilder.generateWorldNoise();
        this.worldBuilder.generateBiomes();
        this.worldBuilder.generateTiles();
        

    }

    private void addCraftingTable(float col, float row) {
        Tile t = GameManager.get().getWorld().getTile(col, row);
        CraftingTable table = new CraftingTable(t, true);
        entities.add(table);
    }

    private void createBuildings() {
        int noTowerPerBiome = 2;
        for (int j = 0; j < noTowerPerBiome; j++) {
        	if (j % 2 == 0) {
                if (!getBiomeTileListFromMap(BiomeType.SHRUBLAND).isEmpty()) {
                    Tile plainsTile = getTileNoNeighbourObstruct(getBiomeTileListFromMap(BiomeType.SHRUBLAND));
                    if (plainsTile != null) {
                        entities.add(new ControlTowers(plainsTile,true, "PcomTower1"));
                    }
                }
                if (!getBiomeTileListFromMap(BiomeType.TEMPERATE_DESERT).isEmpty()) {
                    Tile desertTile = getTileNoNeighbourObstruct(getBiomeTileListFromMap(BiomeType.TEMPERATE_DESERT));
                    if (desertTile != null) {
                        entities.add(new ControlTowers(desertTile,true, "DcomTower1"));
                    }
                }
                if (!getBiomeTileListFromMap(BiomeType.SUBTROPICAL_DESERT).isEmpty()) {
                    Tile desertTile = getTileNoNeighbourObstruct(getBiomeTileListFromMap(BiomeType.TEMPERATE_DESERT));
                    if (desertTile != null) {
                        entities.add(new ControlTowers(desertTile,true, "DcomTower1"));
                    }
                }
                if (!getBiomeTileListFromMap(BiomeType.SNOW).isEmpty()) {
                    Tile snowTile = getTileNoNeighbourObstruct(getBiomeTileListFromMap(BiomeType.SNOW));
                    if (snowTile != null) {
                        entities.add(new ControlTowers(snowTile,true, "ScomTower1"));
                    }
                }
                if (!getBiomeTileListFromMap(BiomeType.TEMPERATE_RAINFOREST).isEmpty()) {
                    Tile rainforestTile = getTileNoNeighbourObstruct(getBiomeTileListFromMap(BiomeType.TEMPERATE_RAINFOREST));
                    if (rainforestTile != null) {
                        entities.add(new ControlTowers(rainforestTile,true, "RFcomTower1"));
                    }
                }
                if (!getBiomeTileListFromMap(BiomeType.TROPICAL_RAINFOREST).isEmpty()) {
                    Tile rainforestTile = getTileNoNeighbourObstruct(getBiomeTileListFromMap(BiomeType.TEMPERATE_RAINFOREST));
                    if (rainforestTile != null) {
                        entities.add(new ControlTowers(rainforestTile,true, "RFcomTower1"));
                    }
                }
        	}else if (j % 2 == 1) {
        		if (!getBiomeTileListFromMap(BiomeType.SHRUBLAND).isEmpty()) {
                    Tile plainsTile = getTileNoNeighbourObstruct(getBiomeTileListFromMap(BiomeType.SHRUBLAND));
                    if (plainsTile != null) {
                        entities.add(new ControlTowers(plainsTile,true, "PcomTower2"));
                    }
                }
                if (!getBiomeTileListFromMap(BiomeType.TEMPERATE_DESERT).isEmpty()) {
                    Tile desertTile = getTileNoNeighbourObstruct(getBiomeTileListFromMap(BiomeType.TEMPERATE_DESERT));
                    if (desertTile != null) {
                        entities.add(new ControlTowers(desertTile,true, "DcomTower2"));
                    }
                }
                if (!getBiomeTileListFromMap(BiomeType.SUBTROPICAL_DESERT).isEmpty()) {
                    Tile desertTile = getTileNoNeighbourObstruct(getBiomeTileListFromMap(BiomeType.TEMPERATE_DESERT));
                    if (desertTile != null) {
                        entities.add(new ControlTowers(desertTile,true, "DcomTower2"));
                    }
                }
                if (!getBiomeTileListFromMap(BiomeType.SNOW).isEmpty()) {
                    Tile snowTile = getTileNoNeighbourObstruct(getBiomeTileListFromMap(BiomeType.SNOW));
                    if (snowTile != null) {
                        entities.add(new ControlTowers(snowTile,true, "ScomTower2"));
                    }
                }
                if (!getBiomeTileListFromMap(BiomeType.TEMPERATE_RAINFOREST).isEmpty()) {
                    Tile rainforestTile = getTileNoNeighbourObstruct(getBiomeTileListFromMap(BiomeType.TEMPERATE_RAINFOREST));
                    if (rainforestTile != null) {
                        entities.add(new ControlTowers(rainforestTile,true, "RFcomTower2"));
                    }
                }
                if (!getBiomeTileListFromMap(BiomeType.TROPICAL_RAINFOREST).isEmpty()) {
                    Tile rainforestTile = getTileNoNeighbourObstruct(getBiomeTileListFromMap(BiomeType.TEMPERATE_RAINFOREST));
                    if (rainforestTile != null) {
                        entities.add(new ControlTowers(rainforestTile,true, "RFcomTower2"));
                    }
                }
        	}
        }
    }

    /**
     * Generate rocks in Grassland Biome
     */
    private void createGrasslandEnvironmentRocks() {
        int tileCount =
                GameManager.get().getWorld().getBiomeTileListFromMap(BiomeType.GRASSLAND).size();

        if (tileCount > 0){
            // Generate some rocks to mine later
            for (int i = 0; i < tileCount / 20; i++) { //200
                Tile t = GameManager.get().getWorld().
                        getBiomeTileListFromMap(BiomeType.GRASSLAND).get(random.nextInt(tileCount));
                if (t != null && !t.isObstructed() && noEntityOnTile(t)) {
                    int elevation = random.nextInt(4);
                    String texture = "environment-grassland_";
                    texture += elevation;
                    entities.add(new Rock(t, texture, true));
                }
            }
        }
    }

    private void createDesertEnvironmentRocks() {
        int sdTileCount = GameManager.get().getWorld().getBiomeTileListFromMap(BiomeType.SUBTROPICAL_DESERT).size();
        int tdTileCount = GameManager.get().getWorld().getBiomeTileListFromMap(BiomeType.TEMPERATE_DESERT).size();

        if (sdTileCount > 0){
            // Generate some rocks to mine later
            for (int i = 0; i < sdTileCount / 50; i++) {
                Tile t = GameManager.get().getWorld().getBiomeTileListFromMap(BiomeType.TEMPERATE_DESERT).get(random.nextInt(sdTileCount));
                entities.add(new Rock(t,
                        generateTextureName(t,
                        "environment-desert_", 4),
                        true));
            }
        } else if (tdTileCount > 0) {
            // Generate some rocks to mine later
            for (int i = 0; i < tdTileCount / 50; i++) { //200
                Tile t = GameManager.get().getWorld().getBiomeTileListFromMap(BiomeType.TEMPERATE_DESERT).get(random.nextInt(sdTileCount));
                entities.add(new Rock(t,
                        generateTextureName(t,
                                "environment-desert_", 4),
                        true));
            }
        }
    }

    private void createDesertEnvironmentPlants() {
        int sdTileCount = GameManager.get().getWorld().getBiomeTileListFromMap(BiomeType.SUBTROPICAL_DESERT).size();
        int tdTileCount = GameManager.get().getWorld().getBiomeTileListFromMap(BiomeType.TEMPERATE_DESERT).size();

        if (sdTileCount > 0){
            // Generate some rocks to mine later
            for (int i = 0; i < sdTileCount / 50; i++) {
                Tile t = GameManager.get().getWorld().getBiomeTileListFromMap(BiomeType.TEMPERATE_DESERT).get(random.nextInt(sdTileCount));
                entities.add(new Rock(t,
                        generateTextureName(t,
                                "environment-desert-pant_", 2),
                        true));
            }
        } else if (tdTileCount > 0) {
            // Generate some rocks to mine later
            for (int i = 0; i < tdTileCount / 50; i++) { //200
                Tile t = GameManager.get().getWorld().getBiomeTileListFromMap(BiomeType.TEMPERATE_DESERT).get(random.nextInt(sdTileCount));
                entities.add(new Rock(t,
                        generateTextureName(t,
                                "environment-desert-plant_", 2),
                        true));
            }
        }
    }

    private void createMountainRocksEnvironmentRocks() {
        int tileCount = GameManager.get().getWorld().getBiomeTileListFromMap(BiomeType.MOUNTAIN_ROCKS).size();

        if (tileCount > 0) {
            // Generate some rocks to mine later
            for (int i = 0; i < tileCount / 50; i++) {
                Tile t = GameManager.get().getWorld()
                        .getBiomeTileListFromMap(BiomeType.TEMPERATE_DESERT)
                        .get(random.nextInt(tileCount));
                entities.add(new Rock(t, "environment-mountainrocks_0", true));
            }
        }
    }

    private void createRainforestEnvironmentTrees() {
        int tropTileCount = GameManager.get().getWorld().getBiomeTileListFromMap(BiomeType.TROPICAL_RAINFOREST).size();
        int tempTileCount = GameManager.get().getWorld().getBiomeTileListFromMap(BiomeType.TEMPERATE_RAINFOREST).size();

        if (tropTileCount > 0){
            // Generate some rocks to mine later
            for (int i = 0; i < tropTileCount / 50; i++) {
                Tile t = GameManager.get().getWorld()
                        .getBiomeTileListFromMap(BiomeType.TROPICAL_RAINFOREST)
                        .get(random.nextInt(tropTileCount));
                entities.add(new Rock(t,
                        generateTextureName(t,
                                "environment-rainforest-tree_", 2),
                        true));
            }
        } else if (tempTileCount > 0) {
            // Generate some rocks to mine later
            for (int i = 0; i < tempTileCount / 50; i++) { //200
                Tile t = GameManager.get().getWorld()
                        .getBiomeTileListFromMap(BiomeType.TEMPERATE_RAINFOREST)
                        .get(random.nextInt(tropTileCount));
                entities.add(new Rock(t,
                        generateTextureName(t,
                                "environment-rainforest-tree_", 2),
                        true));
            }
        }
    }

    /**
     * Private helper method to generate a texture name for any static asset
     * @param tile The tile to generate the texture on
     * @param textureName The texture name
     * @param randomBounds The upper limit for the random number generator
     * @return A string with the the texture name and random number appended
     * to form the full name of the texture
     */
    private String generateTextureName(Tile tile, String textureName,
                                       int randomBounds) {
        String texture = null;
        if (tile != null && !tile.isObstructed() && noEntityOnTile(tile)) {
            int elevation = random.nextInt(randomBounds);
            texture = textureName + elevation;
        }

        return texture;
    }

    /**
     * Check whether there is entity on the given tile or not.
     * @param t the tile to check
     * @return return true if there is entity on the given tile, false otherwise
     */
    private boolean noEntityOnTile(Tile t) {
    	for (AbstractEntity e: entities) {
    		if (t.getCoordinates().isCloseEnoughToBeTheSame(e.getPosition())) {
    			return false;
    		}
    	}
    	return true;
    }
    
    /**
     * A function used to find a random tile within a specific biome with
     * all neighbours without obstructions
     * @return a random tile within a biome which does not have any obstructions in
     * 	       its immediate neighbors or null if no tiles with criteria above.
     */
    public Tile getTileNoNeighbourObstruct(List<Tile> biomeTileList) {
        if (biomeTileList == null) {
            throw new NullPointerException("This list cannot be null");
        }
        int count = 0;
        int biomeSize = biomeTileList.size();
        while (count++ < biomeSize) {
            Tile t = biomeTileList.get(random.nextInt(biomeSize));
            if(t == null || t.getNeighbours() == null) {
                throw new NullPointerException("Tile and neighbours cannot be null");
            }

            if (t.isObstructed() || (t.hasParent() && t.getParent() instanceof ItemEntity) ||
            		!noEntityOnTile(t)) {
                continue;
            }

            Map<Integer, Tile> neighbours = t.getNeighbours();
            for (Tile tile: neighbours.values()) {
                if (!(tile.isObstructed())) {
                    return t;
                }
            }
        }
        return null;
    }

    /**
     * Generates items for spawning onto the map
     * will halve the number of items spawned for each successive rarity.
     * Will spawn 1 of these items
     */
    private void generateItems() {
        for (BiomeType biome : BiomeType.values()) {
            int spawnAmount = MAX_ITEM_SPAWN;
            for (int rarity = MAX_RARITY; rarity > 0; rarity--) {
                for (int i = 0; i < spawnAmount; i++) {
                    Tile tile = getTileNoNeighbourObstruct(getBiomeTileListFromMap(biome));
                    if (tile != null) {
                        addEntity(new ItemEntity(tile, items.getRandomSpawnableItem(rarity),
                                ThreadLocalRandom.current().nextInt(1, 7)));
                    }
                }
                spawnAmount /= 2;
            }
        }
    }

    /**
     * Returns current instances of items on the map
     *
     * @return list of items currently on the map
     */
    public List<ItemEntity> getItemsOnMap() {
        return entities.stream()
                .filter(item -> item instanceof ItemEntity)
                .map(item -> (ItemEntity) item)
                .collect(Collectors.toList());
    }

    /**
     * Checks whether the player is located on the same tile as an item. If they are,
     * it is removed from the map and placed in their inventory.
     *
     */
    private void checkItemLocation() {
        PlayerPeon player = (PlayerPeon) GameManager.get().getWorld().getEntityById(GameManager.get().getPlayerEntityID());

        float[] position = player.getPosTile(player.getCol(), player.getRow(), 0);

        Tile tile = getTile(position[0], position[1]);
        try {
            if (tile.hasParent() && tile.getParent() instanceof ItemEntity) {
                ItemEntity entity = (ItemEntity) tile.getParent();
                GameManager.get().getManager(NetworkManager.class).sendInventoryUpdateMessage(entity, InventoryUpdateMessage.UpdateType.I_REMOVE);

                if (entity.getInventory().transfer(player.getInventory()) && entity.getInventory().stackCount() == 0) {
                    removeEntity(entity);
                    GameManager.get().getManager(SoundManager.class).playSound("Sound Effect/EFFECT_grabItem_01_Inactive.mp3");
                    tile.setParent(null);
                }
            }
        } catch (Exception e) {
            //
        }
    }

    /**
     * Moves each entity to align with the progression of the game in terms of the time
     */
    @Override
    public void onTick(long i) {
        super.onTick(i);

        for (AbstractEntity e : this.getEntities()) {
            e.onTick(0);
        }

        if (notGenerated) {
            createGrasslandEnvironmentRocks();
            createDesertEnvironmentRocks();
            createDesertEnvironmentPlants();
            createMountainRocksEnvironmentRocks();
            createRainforestEnvironmentTrees();
            createBuildings();
            addCraftingTable(-1, -3.5f);
            generateItems();

            notGenerated = false;
        }
        checkItemLocation();
    }

    public WorldBuilder getWorldBuilder() {
        return this.worldBuilder;
    }

    @Deprecated
    public WorldBuilder getSeedBiomeMap() { return this.worldBuilder; }
}

